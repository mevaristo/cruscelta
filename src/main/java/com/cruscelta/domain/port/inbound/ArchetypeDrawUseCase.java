package com.cruscelta.domain.port.inbound;

import com.cruscelta.domain.model.ArchetypeCard;

import java.util.List;

public interface ArchetypeDrawUseCase {
    /**
     * Draw a specified number of archetype cards randomly, in a straight spread.
     * @param quantity Number of archetype cards to draw
     * @return List of randomly drawn archetype cards
     */
    List<ArchetypeCard> draw(int quantity);

    /**
     * View the entire deck of archetype cards.
     * @return List of all archetype cards in the deck.
     */
    List<ArchetypeCard> viewDeck();
}
