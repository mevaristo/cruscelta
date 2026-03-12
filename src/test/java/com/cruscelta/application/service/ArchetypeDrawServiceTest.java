package com.cruscelta.application.service;

import com.cruscelta.domain.entity.ArchetypeCard;
import com.cruscelta.domain.port.inbound.ArchetypeCardLoaderPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ArchetypeDrawService")
class ArchetypeDrawServiceTest {

    @Mock
    private ArchetypeCardLoaderPort archetypeCardLoader;

    @InjectMocks
    private ArchetypeDrawService archetypeDrawService;

    private List<ArchetypeCard> sampleDeck;

    @BeforeEach
    void setUp() {
        sampleDeck = List.of(
                ArchetypeCard.builder().id("Ace of Cups").build(),
                ArchetypeCard.builder().id("Two of Cups").build(),
                ArchetypeCard.builder().id("Three of Cups").build(),
                ArchetypeCard.builder().id("The Fool").build(),
                ArchetypeCard.builder().id("The Magician").build()
        );
    }

    @Nested
    @DisplayName("draw()")
    class Draw {

        @Test
        @DisplayName("should return the exact number of cards requested")
        void shouldReturnExactNumberOfCards() {
            when(archetypeCardLoader.load()).thenReturn(sampleDeck);

            List<ArchetypeCard> result = archetypeDrawService.draw(3);

            assertThat(result).hasSize(3);
        }

        @Test
        @DisplayName("should return one card when quantity is 1")
        void shouldReturnOneCard() {
            when(archetypeCardLoader.load()).thenReturn(sampleDeck);

            List<ArchetypeCard> result = archetypeDrawService.draw(1);

            assertThat(result).hasSize(1);
        }

        @Test
        @DisplayName("should return entire deck when quantity equals deck size")
        void shouldReturnEntireDeckWhenQuantityEqualsDeckSize() {
            when(archetypeCardLoader.load()).thenReturn(sampleDeck);

            List<ArchetypeCard> result = archetypeDrawService.draw(sampleDeck.size());

            assertThat(result).hasSize(sampleDeck.size());
        }

        @Test
        @DisplayName("should return only cards from the original deck")
        void shouldReturnOnlyCardsFromOriginalDeck() {
            when(archetypeCardLoader.load()).thenReturn(sampleDeck);

            List<ArchetypeCard> result = archetypeDrawService.draw(3);

            assertThat(result).allSatisfy(card ->
                    assertThat(sampleDeck).contains(card));
        }

        @Test
        @DisplayName("should return no duplicate cards")
        void shouldReturnNoDuplicateCards() {
            when(archetypeCardLoader.load()).thenReturn(sampleDeck);

            List<ArchetypeCard> result = archetypeDrawService.draw(5);

            assertThat(result).doesNotHaveDuplicates();
        }

        @Test
        @DisplayName("should return empty list when quantity is zero")
        void shouldReturnEmptyListWhenQuantityIsZero() {
            List<ArchetypeCard> result = archetypeDrawService.draw(0);

            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("should return empty list when quantity is negative")
        void shouldReturnEmptyListWhenQuantityIsNegative() {
            List<ArchetypeCard> result = archetypeDrawService.draw(-1);

            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("should return empty list when quantity exceeds deck size")
        void shouldReturnEmptyListWhenQuantityExceedsDeckSize() {
            when(archetypeCardLoader.load()).thenReturn(sampleDeck);

            List<ArchetypeCard> result = archetypeDrawService.draw(100);

            assertThat(result).isEmpty();
        }

        @ParameterizedTest
        @ValueSource(ints = {-5, -1, 0})
        @DisplayName("should return empty list for non-positive quantities")
        void shouldReturnEmptyListForNonPositiveQuantities(int quantity) {
            List<ArchetypeCard> result = archetypeDrawService.draw(quantity);

            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("should shuffle the deck producing different orderings across runs")
        void shouldShuffleDeck() {
            when(archetypeCardLoader.load()).thenReturn(sampleDeck);

            boolean foundDifferentOrder = false;
            List<ArchetypeCard> firstDraw = archetypeDrawService.draw(5);

            // Run multiple times — at least one should differ in order
            for (int i = 0; i < 20; i++) {
                List<ArchetypeCard> nextDraw = archetypeDrawService.draw(5);
                if (!firstDraw.equals(nextDraw)) {
                    foundDifferentOrder = true;
                    break;
                }
            }

            assertThat(foundDifferentOrder)
                    .as("Expected at least one shuffle to produce a different ordering")
                    .isTrue();
        }

        @Test
        @DisplayName("should invoke the card loader")
        void shouldInvokeCardLoader() {
            when(archetypeCardLoader.load()).thenReturn(sampleDeck);

            archetypeDrawService.draw(2);

            verify(archetypeCardLoader, atLeastOnce()).load();
        }
    }

    @Nested
    @DisplayName("viewDeck()")
    class ViewDeck {

        @Test
        @DisplayName("should return all cards in the deck")
        void shouldReturnAllCards() {
            when(archetypeCardLoader.load()).thenReturn(sampleDeck);

            List<ArchetypeCard> result = archetypeDrawService.viewDeck();

            assertThat(result).hasSize(sampleDeck.size());
            assertThat(result).containsExactlyInAnyOrderElementsOf(sampleDeck);
        }

        @Test
        @DisplayName("should return an immutable copy of the deck")
        void shouldReturnImmutableCopy() {
            when(archetypeCardLoader.load()).thenReturn(sampleDeck);

            List<ArchetypeCard> result = archetypeDrawService.viewDeck();

            assertThat(result).isUnmodifiable();
        }

        @Test
        @DisplayName("should return empty list when deck is empty")
        void shouldReturnEmptyListWhenDeckIsEmpty() {
            when(archetypeCardLoader.load()).thenReturn(List.of());

            List<ArchetypeCard> result = archetypeDrawService.viewDeck();

            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("should delegate to the card loader port")
        void shouldDelegateToCardLoaderPort() {
            when(archetypeCardLoader.load()).thenReturn(sampleDeck);

            archetypeDrawService.viewDeck();

            verify(archetypeCardLoader).load();
        }
    }
}

