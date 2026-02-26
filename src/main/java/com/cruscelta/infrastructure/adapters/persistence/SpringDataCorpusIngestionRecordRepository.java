package com.cruscelta.infrastructure.adapters.persistence;

import com.cruscelta.domain.entity.CorpusIngestionRecord;
import org.springframework.data.jpa.repository.JpaRepository;

interface SpringDataCorpusIngestionRecordRepository extends JpaRepository<CorpusIngestionRecord, Long> {
    boolean existsByFilenameAndHash(String filename, String hash);
    void deleteByFilename(String filename);
}

