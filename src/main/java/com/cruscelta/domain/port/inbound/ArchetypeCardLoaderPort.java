package com.cruscelta.domain.port.inbound;

import com.cruscelta.domain.entity.ArchetypeCardDocument;

import java.util.List;

public interface ArchetypeCardLoaderPort {
    List<ArchetypeCardDocument> load();
}
