package com.cruscelta.infrastructure.config;

import com.cruscelta.domain.entity.CorpusIngestionRecord;
import com.cruscelta.domain.port.outbound.CorpusIngestionRecordPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.security.MessageDigest;
import java.time.Instant;
import java.util.HexFormat;
import java.util.List;

/**
 * Loads all corpora files from the {@code corpora/} classpath directory into the vector store
 * at application startup.
 *
 * <p>Idempotency is guaranteed by computing a SHA-256 hash of each file and storing it in
 * a PostgreSQL {@code corpus_ingestion_record} table. Files are only re-ingested when their
 * content changes (different hash).
 *
 * <p>Chunking strategy:
 * <ul>
 *   <li>Token-based chunk size (~800 tokens) via {@link TokenTextSplitter}</li>
 *   <li>Each chunk inherits a {@code source} metadata key (filename) for traceability</li>
 * </ul>
 */
@Configuration
public class KnowledgeBaseIngestionConfig {

    private static final Logger log = LoggerFactory.getLogger(KnowledgeBaseIngestionConfig.class);
    private static final String CORPORA_PATTERN = "classpath:corpora/*";

    @Bean
    ApplicationRunner knowledgeBaseIngestionRunner(VectorStore vectorStore,
                                                   CorpusIngestionRecordPort ingestionRecordPort) {
        return args -> {
            var resolver = new PathMatchingResourcePatternResolver();
            Resource[] resources = resolver.getResources(CORPORA_PATTERN);

            if (resources.length == 0) {
                log.info("No files found in corpora/ directory — skipping ingestion.");
                return;
            }

            var splitter = TokenTextSplitter.builder()
                    .withChunkSize(800)
                    .withMinChunkSizeChars(200)
                    .withMinChunkLengthToEmbed(50)
                    .withMaxNumChunks(500)
                    .withKeepSeparator(true)
                    .build();

            for (Resource resource : resources) {
                String filename = resource.getFilename();
                if (filename == null) {
                    log.warn("Skipping resource with null filename: {}", resource);
                    continue;
                }

                String hash = computeSha256(resource);

                if (ingestionRecordPort.existsByFilenameAndHash(filename, hash)) {
                    log.info("Corpus '{}' already ingested with same hash — skipping.", filename);
                    continue;
                }

                log.info("Ingesting corpus '{}'...", filename);

                // Delete old records for this filename (handles file-content changes)
                ingestionRecordPort.deleteByFilename(filename);

                // Read the file as a raw Document
                var reader = new TikaDocumentReader(resource);
                List<Document> rawDocuments = reader.get();

                // Tag each document with source metadata
                rawDocuments.forEach(doc -> doc.getMetadata().put("source", filename));

                // Split into chunks
                List<Document> chunks = splitter.apply(rawDocuments);
                log.info("Split '{}' into {} chunks.", filename, chunks.size());

                // Add chunks to vector store
                vectorStore.add(chunks);

                // Record the ingestion
                ingestionRecordPort.save(CorpusIngestionRecord.builder()
                        .filename(filename)
                        .hash(hash)
                        .ingestedAt(Instant.now())
                        .build());

                log.info("Corpus '{}' ingested successfully ({} chunks).", filename, chunks.size());
            }
        };
    }

    /**
     * Computes the SHA-256 hash of the given resource's content.
     */
    private String computeSha256(Resource resource) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(resource.getContentAsByteArray());
            return HexFormat.of().formatHex(hashBytes);
        } catch (Exception e) {
            throw new RuntimeException("Failed to compute SHA-256 for resource: " + resource.getFilename(), e);
        }
    }
}
