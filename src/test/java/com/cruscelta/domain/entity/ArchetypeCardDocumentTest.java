package com.cruscelta.domain.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ArchetypeCardDocument")
class ArchetypeCardDocumentTest {

    @Test
    @DisplayName("should create document using builder")
    void shouldCreateDocumentUsingBuilder() {
        var doc = ArchetypeCardDocument.builder()
                .cardName("The Fool")
                .cardDescription("Represents new beginnings.")
                .build();

        assertThat(doc.cardName()).isEqualTo("The Fool");
        assertThat(doc.cardDescription()).isEqualTo("Represents new beginnings.");
    }

    @Test
    @DisplayName("should support equality based on record fields")
    void shouldSupportEqualityBasedOnRecordFields() {
        var doc1 = ArchetypeCardDocument.builder()
                .cardName("The Fool")
                .cardDescription("Desc")
                .build();
        var doc2 = ArchetypeCardDocument.builder()
                .cardName("The Fool")
                .cardDescription("Desc")
                .build();

        assertThat(doc1).isEqualTo(doc2);
    }

    @Test
    @DisplayName("should not be equal when fields differ")
    void shouldNotBeEqualWhenFieldsDiffer() {
        var doc1 = ArchetypeCardDocument.builder()
                .cardName("The Fool")
                .cardDescription("Desc A")
                .build();
        var doc2 = ArchetypeCardDocument.builder()
                .cardName("The Magician")
                .cardDescription("Desc B")
                .build();

        assertThat(doc1).isNotEqualTo(doc2);
    }

    @Test
    @DisplayName("should produce consistent hashCode for equal records")
    void shouldProduceConsistentHashCode() {
        var doc1 = ArchetypeCardDocument.builder()
                .cardName("The Star")
                .cardDescription("Hope")
                .build();
        var doc2 = ArchetypeCardDocument.builder()
                .cardName("The Star")
                .cardDescription("Hope")
                .build();

        assertThat(doc1.hashCode()).isEqualTo(doc2.hashCode());
    }

    @Test
    @DisplayName("should have meaningful toString")
    void shouldHaveMeaningfulToString() {
        var doc = ArchetypeCardDocument.builder()
                .cardName("The Moon")
                .cardDescription("Illusion")
                .build();

        assertThat(doc.toString()).contains("The Moon", "Illusion");
    }
}

