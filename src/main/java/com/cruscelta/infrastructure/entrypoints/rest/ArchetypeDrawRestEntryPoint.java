package com.cruscelta.infrastructure.entrypoints.rest;

import com.cruscelta.domain.port.inbound.ArchetypeDrawUseCase;
import com.cruscelta.domain.entity.ArchetypeCard;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/archetype-draw")
@RequiredArgsConstructor
public class ArchetypeDrawRestEntryPoint {
    private final ArchetypeDrawUseCase archetypeDrawUseCase;

    @GetMapping("/{quantity}")
    public List<ArchetypeCard> draw(@PathVariable int quantity) {
        return archetypeDrawUseCase.draw(quantity);
    }

    @GetMapping("/view-deck")
    public List<ArchetypeCard> viewDeck() {
        return archetypeDrawUseCase.viewDeck();
    }
}
