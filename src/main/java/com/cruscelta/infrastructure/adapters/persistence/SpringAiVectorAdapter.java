package com.cruscelta.infrastructure.adapters.persistence;

import com.cruscelta.domain.entity.ArchetypeCardDocument;
import com.cruscelta.domain.port.outbound.VectorStorePort;
import com.cruscelta.infrastructure.mapper.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SpringAiVectorAdapter implements VectorStorePort {
    private final VectorStore vectorStore;
    private final Mapper<ArchetypeCardDocument, Document> archetypeCardDocumentDocumentMapper;

    void tmp() {
        var content = vectorStore.similaritySearch("").getFirst().getText();


    }

    @Override
    public void save(List<ArchetypeCardDocument> archetypeCardDocuments) {
        var documents = archetypeCardDocuments
                .stream()
                .map(archetypeCardDocumentDocumentMapper::toSource)
                .toList();

        vectorStore.add(documents);
    }

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
                .toTarget(vectorStore
                        .similaritySearch(searchRequest)
                        .getFirst());
    }
}
