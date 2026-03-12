package com.cruscelta.domain.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("CorpusIngestionRecord")
class CorpusIngestionRecordTest {

    @Test
    @DisplayName("should create record using builder")
    void shouldCreateRecordUsingBuilder() {
        Instant now = Instant.now();
        var record = CorpusIngestionRecord.builder()
                .filename("corpus.md")
                .hash("abc123def456")
                .ingestedAt(now)
                .build();

        assertThat(record.getFilename()).isEqualTo("corpus.md");
        assertThat(record.getHash()).isEqualTo("abc123def456");
        assertThat(record.getIngestedAt()).isEqualTo(now);
        assertThat(record.getId()).isNull();
    }

    @Test
    @DisplayName("should create record with no-args constructor")
    void shouldCreateRecordWithNoArgsConstructor() {
        var record = new CorpusIngestionRecord();

        assertThat(record.getId()).isNull();
        assertThat(record.getFilename()).isNull();
        assertThat(record.getHash()).isNull();
        assertThat(record.getIngestedAt()).isNull();
    }

    @Test
    @DisplayName("should support setters via @Data")
    void shouldSupportSetters() {
        var record = new CorpusIngestionRecord();
        Instant now = Instant.now();

        record.setFilename("test.md");
        record.setHash("hash123");
        record.setIngestedAt(now);

        assertThat(record.getFilename()).isEqualTo("test.md");
        assertThat(record.getHash()).isEqualTo("hash123");
        assertThat(record.getIngestedAt()).isEqualTo(now);
    }

    @Test
    @DisplayName("should support equality based on all fields")
    void shouldSupportEquality() {
        Instant now = Instant.now();
        var record1 = CorpusIngestionRecord.builder()
                .id(1L).filename("file.md").hash("hash").ingestedAt(now).build();
        var record2 = CorpusIngestionRecord.builder()
                .id(1L).filename("file.md").hash("hash").ingestedAt(now).build();

        assertThat(record1).isEqualTo(record2);
    }
}

