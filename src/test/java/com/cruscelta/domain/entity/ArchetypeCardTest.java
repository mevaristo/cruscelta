package com.cruscelta.domain.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ArchetypeCard")
class ArchetypeCardTest {

    @Test
    @DisplayName("should create card using builder with all fields")
    void shouldCreateCardUsingBuilder() {
        var card = ArchetypeCard.builder()
                .id("The Fool")
                .name("The Fool")
                .build();

        assertThat(card.getId()).isEqualTo("The Fool");
        assertThat(card.getName()).isEqualTo("The Fool");
    }

    @Test
    @DisplayName("should create card with no-args constructor")
    void shouldCreateCardWithNoArgsConstructor() {
        var card = new ArchetypeCard();

        assertThat(card.getId()).isNull();
        assertThat(card.getName()).isNull();
    }

    @Test
    @DisplayName("should create card with all-args constructor")
    void shouldCreateCardWithAllArgsConstructor() {
        var card = new ArchetypeCard("ace-cups", "Ace of Cups");

        assertThat(card.getId()).isEqualTo("ace-cups");
        assertThat(card.getName()).isEqualTo("Ace of Cups");
    }
}

