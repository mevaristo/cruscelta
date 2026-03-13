package com.cruscelta.infrastructure.entrypoints.rest;

import com.cruscelta.domain.entity.ArchetypeCardDocument;
import com.cruscelta.domain.port.inbound.ArchetypeDrawUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/archetype-draw")
@RequiredArgsConstructor
public class ArchetypeDrawRestEntryPoint {
    private final ArchetypeDrawUseCase archetypeDrawUseCase;

    @GetMapping("/draw")
    public List<ArchetypeCardDocument> draw(@RequestParam int quantity, @RequestParam(defaultValue = "false") boolean enriched) {
        if (enriched) {
            return archetypeDrawUseCase.drawEnriched(quantity);
        }

        return archetypeDrawUseCase.draw(quantity);
    }

    @GetMapping("/{quantity}")
    public List<ArchetypeCardDocument> drawEnriched(@PathVariable int quantity) {
        return archetypeDrawUseCase.draw(quantity);
    }

    @GetMapping("/view-deck")
    public List<ArchetypeCardDocument> viewDeck() {
        return archetypeDrawUseCase.viewDeck();
    }
}
