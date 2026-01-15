package com.cruscelta.domain;

import lombok.Builder;
import lombok.Getter;

import java.util.HashMap;

@Getter
@Builder
public class NarrativeArchetype {
    private String id;
    private Integer ponderIndex;
    private HashMap<Integer, String> ponderedMeaning;
}
