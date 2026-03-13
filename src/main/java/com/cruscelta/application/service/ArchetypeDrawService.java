package com.cruscelta.application.service;

import com.cruscelta.domain.entity.ArchetypeCardDocument;
import com.cruscelta.domain.port.inbound.ArchetypeCardLoaderPort;
import com.cruscelta.domain.port.inbound.ArchetypeDrawUseCase;
import com.cruscelta.domain.port.outbound.VectorStoreRetrieverPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArchetypeDrawService implements ArchetypeDrawUseCase {
    private final ArchetypeCardLoaderPort archetypeCardLoader;
    private final VectorStoreRetrieverPort vectorStoreRetriever;

    @Override
    public List<ArchetypeCardDocument> draw(int quantity) throws IndexOutOfBoundsException {
        var deck = shuffle();
        
        if (quantity > 0 && quantity <= deck.size()) {
            return deck.subList(0, quantity);
        } else {
            log.error("Requested quantity {} is out of bounds. Deck size is {}.", quantity, deck.size());

            throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public List<ArchetypeCardDocument> viewDeck() {
        return List.copyOf(archetypeCardLoader.load());
    }

    @Override
    public List<ArchetypeCardDocument> drawEnriched(int quantity) {
        var cards = this.draw(quantity);

        return cards
                .stream()
                .map(card -> vectorStoreRetriever.queryCard(card.cardName()))
                .toList();
    }

    private List<ArchetypeCardDocument> shuffle() {
        var archetypeCards = new ArrayList<>(List.copyOf(archetypeCardLoader.load()));

        Collections.shuffle(archetypeCards, new SecureRandom());

        return archetypeCards;
    }

}
