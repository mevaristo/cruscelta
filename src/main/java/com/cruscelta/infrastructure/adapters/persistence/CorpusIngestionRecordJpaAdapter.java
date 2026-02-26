package com.cruscelta.infrastructure.adapters.persistence;

import com.cruscelta.domain.entity.CorpusIngestionRecord;
import com.cruscelta.domain.port.outbound.CorpusIngestionRecordPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CorpusIngestionRecordJpaAdapter implements CorpusIngestionRecordPort {

    private final SpringDataCorpusIngestionRecordRepository repository;

    @Override
    public boolean existsByFilenameAndHash(String filename, String hash) {
        return repository.existsByFilenameAndHash(filename, hash);
    }

    @Override
    public void save(CorpusIngestionRecord record) {
        repository.save(record);
    }

    @Override
    @Transactional
    public void deleteByFilename(String filename) {
        repository.deleteByFilename(filename);
    }
}

