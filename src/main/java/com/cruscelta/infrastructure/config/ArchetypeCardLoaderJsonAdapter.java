package com.cruscelta.infrastructure.config;

import com.cruscelta.domain.port.inbound.ArchetypeCardLoaderPort;
import com.cruscelta.domain.entity.ArchetypeCard;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class ArchetypeCardLoaderJsonAdapter implements ArchetypeCardLoaderPort {
    private final List<ArchetypeCard> cards;
    private static final String [] CARDS_FILES = {
            "/archetypes/cups_archetypes.json",
            "/archetypes/wands_archetypes.json",
            "/archetypes/swords_archetypes.json",
            "/archetypes/pentacles_archetypes.json",
            "/archetypes/major_archetypes.json"
    };

    ArchetypeCardLoaderJsonAdapter() throws IOException {
        var mapper = new ObjectMapper();
        var loadedCards = new ArrayList<ArchetypeCard>();

        for (String file : CARDS_FILES) {
            try (var is = getClass().getResourceAsStream(file)) {
                loadedCards.addAll(mapper.readValue(is, new TypeReference<List<ArchetypeCard>>() {}));
            }
        }

        this.cards = List.copyOf(loadedCards);
    }

    @Override
    public List<ArchetypeCard> load() {
        return this.cards;
    }
}
