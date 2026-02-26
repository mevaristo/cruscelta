package com.cruscelta.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "corpus_ingestion_record")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CorpusIngestionRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String filename;

    @Column(nullable = false)
    private String hash;

    @Column(nullable = false)
    private Instant ingestedAt;
}

