package com.cruscelta.domain.port.inbound;

import com.cruscelta.domain.model.ArchetypeCard;

import java.util.List;

public interface ArchetypeCardLoader {
    List<ArchetypeCard> load();
}
