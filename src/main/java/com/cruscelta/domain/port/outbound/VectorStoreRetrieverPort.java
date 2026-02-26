package com.cruscelta.domain.port.outbound;

import com.cruscelta.domain.entity.ArchetypeCardDocument;

import java.util.List;

public interface VectorStoreRetrieverPort {
    ArchetypeCardDocument queryCard(String queryText);
    List<ArchetypeCardDocument> queryKCards(String queryText, int k);
}
