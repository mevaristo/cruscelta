package com.cruscelta.domain.port.inbound;

import com.cruscelta.domain.entity.ArchetypeCard;

import java.util.List;

public interface OracleUseCase {
    String answerQuestion(String question);

    String answerQuestion(String question, List<ArchetypeCard> cards);
}
