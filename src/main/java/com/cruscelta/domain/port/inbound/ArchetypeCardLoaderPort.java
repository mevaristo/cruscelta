package com.cruscelta.domain.port.inbound;

import com.cruscelta.domain.entity.ArchetypeCard;

import java.util.List;

public interface ArchetypeCardLoaderPort {
    List<ArchetypeCard> load();
}
