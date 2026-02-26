package com.cruscelta.infrastructure.adapters.persistence;

import com.cruscelta.domain.entity.ArchetypeCardDocument;
import com.cruscelta.domain.port.outbound.VectorStorePort;
import com.cruscelta.infrastructure.mapper.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SpringAiVectorAdapter implements VectorStorePort {
    private final VectorStore vectorStore;
    private final Mapper<ArchetypeCardDocument, Document> archetypeCardDocumentDocumentMapper;

    @Override
    public void save(List<ArchetypeCardDocument> archetypeCardDocuments) {
        var documents = archetypeCardDocuments
                .stream()
                .map(archetypeCardDocumentDocumentMapper::toSource)
                .toList();

        vectorStore.add(documents);
    }

    @Override
    public void save(ArchetypeCardDocument document) {
        var documents = List.of(archetypeCardDocumentDocumentMapper.toSource(document));

        vectorStore.add(documents);
    }
}
