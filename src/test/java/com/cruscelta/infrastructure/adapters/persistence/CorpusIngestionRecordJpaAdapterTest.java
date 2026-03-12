package com.cruscelta.infrastructure.adapters.persistence;

import com.cruscelta.domain.entity.CorpusIngestionRecord;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("CorpusIngestionRecordJpaAdapter")
class CorpusIngestionRecordJpaAdapterTest {

    @Mock
    private SpringDataCorpusIngestionRecordRepository repository;

    @InjectMocks
    private CorpusIngestionRecordJpaAdapter adapter;

    @Test
    @DisplayName("existsByFilenameAndHash should delegate to repository and return true when exists")
    void existsByFilenameAndHashShouldReturnTrueWhenExists() {
        when(repository.existsByFilenameAndHash("file.md", "abc123")).thenReturn(true);

        boolean result = adapter.existsByFilenameAndHash("file.md", "abc123");

        assertThat(result).isTrue();
        verify(repository).existsByFilenameAndHash("file.md", "abc123");
    }

    @Test
    @DisplayName("existsByFilenameAndHash should delegate to repository and return false when not exists")
    void existsByFilenameAndHashShouldReturnFalseWhenNotExists() {
        when(repository.existsByFilenameAndHash("file.md", "abc123")).thenReturn(false);

        boolean result = adapter.existsByFilenameAndHash("file.md", "abc123");

        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("save should delegate to repository")
    void saveShouldDelegateToRepository() {
        var record = CorpusIngestionRecord.builder()
                .filename("corpus.md")
                .hash("sha256hash")
                .ingestedAt(Instant.now())
                .build();

        adapter.save(record);

        verify(repository).save(record);
    }

    @Test
    @DisplayName("deleteByFilename should delegate to repository")
    void deleteByFilenameShouldDelegateToRepository() {
        adapter.deleteByFilename("old_corpus.md");

        verify(repository).deleteByFilename("old_corpus.md");
    }
}

