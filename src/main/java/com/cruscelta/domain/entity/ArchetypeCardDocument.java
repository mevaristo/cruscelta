package com.cruscelta.domain.entity;

import lombok.Builder;

@Builder
public record ArchetypeCardDocument(
    String cardName,
    String cardDescription) {}
