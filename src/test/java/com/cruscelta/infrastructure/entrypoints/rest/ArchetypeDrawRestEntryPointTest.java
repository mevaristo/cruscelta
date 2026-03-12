package com.cruscelta.infrastructure.entrypoints.rest;

import com.cruscelta.domain.entity.ArchetypeCard;
import com.cruscelta.domain.port.inbound.ArchetypeDrawUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ArchetypeDrawRestEntryPoint.class)
@DisplayName("ArchetypeDrawRestEntryPoint")
class ArchetypeDrawRestEntryPointTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ArchetypeDrawUseCase archetypeDrawUseCase;

    @Nested
    @DisplayName("GET /archetype-draw/{quantity}")
    class DrawEndpoint {

        @Test
        @DisplayName("should return 200 and drawn cards when quantity is valid")
        void shouldReturn200AndDrawnCards() throws Exception {
            var cards = List.of(
                    ArchetypeCard.builder().id("The Fool").build(),
                    ArchetypeCard.builder().id("The Magician").build()
            );
            when(archetypeDrawUseCase.draw(2)).thenReturn(cards);

            mockMvc.perform(get("/archetype-draw/2"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(2)))
                    .andExpect(jsonPath("$[0].id").value("The Fool"))
                    .andExpect(jsonPath("$[1].id").value("The Magician"));
        }

        @Test
        @DisplayName("should return 200 and empty list when quantity is out of bounds")
        void shouldReturn200AndEmptyListForOutOfBoundsQuantity() throws Exception {
            when(archetypeDrawUseCase.draw(0)).thenReturn(List.of());

            mockMvc.perform(get("/archetype-draw/0"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(0)));
        }

        @Test
        @DisplayName("should delegate to use case with correct quantity")
        void shouldDelegateToUseCaseWithCorrectQuantity() throws Exception {
            when(archetypeDrawUseCase.draw(5)).thenReturn(List.of());

            mockMvc.perform(get("/archetype-draw/5"));

            verify(archetypeDrawUseCase).draw(5);
        }

        @Test
        @DisplayName("should return 400 for non-numeric quantity")
        void shouldReturn400ForNonNumericQuantity() throws Exception {
            mockMvc.perform(get("/archetype-draw/abc"))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("GET /archetype-draw/view-deck")
    class ViewDeckEndpoint {

        @Test
        @DisplayName("should return 200 and the full deck")
        void shouldReturn200AndFullDeck() throws Exception {
            var deck = List.of(
                    ArchetypeCard.builder().id("The Fool").build(),
                    ArchetypeCard.builder().id("The Magician").build(),
                    ArchetypeCard.builder().id("Ace of Cups").build()
            );
            when(archetypeDrawUseCase.viewDeck()).thenReturn(deck);

            mockMvc.perform(get("/archetype-draw/view-deck"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(3)))
                    .andExpect(jsonPath("$[0].id").value("The Fool"));
        }

        @Test
        @DisplayName("should return 200 and empty list when deck is empty")
        void shouldReturn200AndEmptyListWhenDeckIsEmpty() throws Exception {
            when(archetypeDrawUseCase.viewDeck()).thenReturn(List.of());

            mockMvc.perform(get("/archetype-draw/view-deck"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(0)));
        }

        @Test
        @DisplayName("should delegate to use case")
        void shouldDelegateToUseCase() throws Exception {
            when(archetypeDrawUseCase.viewDeck()).thenReturn(List.of());

            mockMvc.perform(get("/archetype-draw/view-deck"));

            verify(archetypeDrawUseCase).viewDeck();
        }
    }
}

