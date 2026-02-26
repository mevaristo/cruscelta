package com.cruscelta.domain.port.outbound;

import com.cruscelta.domain.entity.CorpusIngestionRecord;

public interface CorpusIngestionRecordPort {
    boolean existsByFilenameAndHash(String filename, String hash);
    void save(CorpusIngestionRecord record);
    void deleteByFilename(String filename);
}

