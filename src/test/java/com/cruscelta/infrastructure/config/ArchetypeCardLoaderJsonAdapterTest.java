package com.cruscelta.infrastructure.config;

import com.cruscelta.domain.entity.ArchetypeCardDocument;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ArchetypeCardLoaderJsonAdapter")
class ArchetypeCardLoaderJsonAdapterTest {

    @Test
    @DisplayName("should load cards from all JSON resource files")
    void shouldLoadCardsFromAllJsonFiles() throws IOException {
        var loader = new ArchetypeCardLoaderJsonAdapter();

        var cards = loader.load();

        assertThat(cards).isNotEmpty();
    }

    @Test
    @DisplayName("should load the expected total number of cards (78 tarot cards)")
    void shouldLoadExpectedTotalNumberOfCards() throws IOException {
        var loader = new ArchetypeCardLoaderJsonAdapter();

        var cards = loader.load();

        // Standard tarot deck: 22 major arcana + 14x4 minor arcana suits = 78
        assertThat(cards).hasSize(78);
    }

    @Test
    @DisplayName("should load cards with non-null ids")
    void shouldLoadCardsWithNonNullIds() throws IOException {
        var loader = new ArchetypeCardLoaderJsonAdapter();

        var cards = loader.load();

        assertThat(cards).allSatisfy(card ->
                assertThat(card.cardName()).isNotNull().isNotBlank());
    }

    @Test
    @DisplayName("should contain known major arcana cards")
    void shouldContainKnownMajorArcanaCards() throws IOException {
        var loader = new ArchetypeCardLoaderJsonAdapter();

        var cards = loader.load();
        List<String> cardIds = cards.stream().map(ArchetypeCardDocument::cardName).toList();

        assertThat(cardIds).contains("The Fool", "The Magician", "The High Priestess");
    }

    @Test
    @DisplayName("should contain known cups suit cards")
    void shouldContainKnownCupsSuitCards() throws IOException {
        var loader = new ArchetypeCardLoaderJsonAdapter();

        var cards = loader.load();
        List<String> cardIds = cards.stream().map(ArchetypeCardDocument::cardName).toList();

        assertThat(cardIds).contains("Ace of Cups", "Two of Cups", "King of Cups");
    }

    @Test
    @DisplayName("should return an immutable list")
    void shouldReturnImmutableList() throws IOException {
        var loader = new ArchetypeCardLoaderJsonAdapter();

        var cards = loader.load();

        assertThat(cards).isUnmodifiable();
    }

    @Test
    @DisplayName("should return the same list instance on subsequent calls")
    void shouldReturnSameListOnSubsequentCalls() throws IOException {
        var loader = new ArchetypeCardLoaderJsonAdapter();

        var firstCall = loader.load();
        var secondCall = loader.load();

        assertThat(firstCall).isSameAs(secondCall);
    }

    @Test
    @DisplayName("should not contain duplicate card ids")
    void shouldNotContainDuplicateCardIds() throws IOException {
        var loader = new ArchetypeCardLoaderJsonAdapter();

        var cards = loader.load();
        List<String> cardIds = cards.stream().map(ArchetypeCardDocument::cardName).toList();

        assertThat(cardIds).doesNotHaveDuplicates();
    }
}

