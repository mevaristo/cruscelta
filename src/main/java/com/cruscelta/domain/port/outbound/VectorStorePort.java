package com.cruscelta.domain.port.outbound;

import com.cruscelta.domain.entity.ArchetypeCardDocument;

import java.util.List;

public interface VectorStorePort {
    void save(List<ArchetypeCardDocument> documents);
    void save(ArchetypeCardDocument document);
}
