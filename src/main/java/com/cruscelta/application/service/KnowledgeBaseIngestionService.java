package com.cruscelta.application.service;

import com.cruscelta.domain.port.outbound.VectorStorePort;
import lombok.RequiredArgsConstructor;

/**
 * Class responsible for receiving raw data, processing it, and then saving it to the vector store, checking for
 * redundancy and optimizing resources.
 */
@RequiredArgsConstructor
public class KnowledgeBaseIngestionService {
    private final VectorStorePort vectorStore;
}
