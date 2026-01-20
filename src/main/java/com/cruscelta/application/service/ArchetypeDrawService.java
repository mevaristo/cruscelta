package com.cruscelta.application.service;

import com.cruscelta.domain.port.inbound.ArchetypeCardLoaderPort;
import com.cruscelta.domain.port.inbound.ArchetypeDrawUseCase;
import com.cruscelta.domain.model.ArchetypeCard;
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

    @Override
    public List<ArchetypeCard> draw(int quantity) {
        var deck = shuffle();
        
        if (quantity > 0 && quantity <= deck.size()) {
            return deck.subList(0, quantity);
        } else {
            log.warn("Requested quantity {} is out of bounds. Deck size is {}.", quantity, deck.size());

            return List.of();
        }
    }

    @Override
    public List<ArchetypeCard> viewDeck() {
        return List.copyOf(archetypeCardLoader.load());
    }

    private List<ArchetypeCard> shuffle() {
        var archetypeCards = new ArrayList<>(List.copyOf(archetypeCardLoader.load()));
        
        log.debug("List before shuffle: {}", archetypeCards);

        Collections.shuffle(archetypeCards, new SecureRandom());

        log.debug("List after shuffle: {}", archetypeCards);
        return archetypeCards;
    }

}
