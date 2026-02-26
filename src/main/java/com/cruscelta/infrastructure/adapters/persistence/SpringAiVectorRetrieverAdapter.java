package com.cruscelta.infrastructure.adapters.persistence;

import com.cruscelta.domain.entity.ArchetypeCardDocument;
import com.cruscelta.domain.port.outbound.VectorStoreRetrieverPort;
import com.cruscelta.infrastructure.mapper.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStoreRetriever;
import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SpringAiVectorRetrieverAdapter implements VectorStoreRetrieverPort {
    private final VectorStoreRetriever vectorStoreRetriever;
    private final Mapper<ArchetypeCardDocument, Document> archetypeCardDocumentDocumentMapper;

    @Override
    public ArchetypeCardDocument queryCard(String queryText) {
        Filter.Expression expression = new Filter.Expression(
                Filter.ExpressionType.EQ,
                new Filter.Key("name"),
                new Filter.Value(queryText));

        var searchRequest = SearchRequest.builder()
                .filterExpression(expression)
                .similarityThreshold(0.9)
                .build();

        return archetypeCardDocumentDocumentMapper
                .toTarget(vectorStoreRetriever
                        .similaritySearch(searchRequest)
                        .getFirst());
    }

    @Override
    public List<ArchetypeCardDocument> queryKCards(String queryText, int k) {
        Filter.Expression expression = new Filter.Expression(
                Filter.ExpressionType.EQ,
                new Filter.Key("name"),
                new Filter.Value(queryText));

        var searchRequest = SearchRequest.builder()
                .filterExpression(expression)
                .similarityThreshold(0.8)
                .topK(k)
                .build();

        return vectorStoreRetriever
                .similaritySearch(searchRequest)
                .stream()
                .map(archetypeCardDocumentDocumentMapper::toTarget)
                .toList();
    }
}
