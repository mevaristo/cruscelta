package com.cruscelta.infrastructure.adapters.persistence;

import com.cruscelta.domain.entity.ArchetypeCardDocument;
import com.cruscelta.infrastructure.mapper.Mapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStoreRetriever;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("SpringAiVectorRetrieverAdapter")
class SpringAiVectorRetrieverAdapterTest {

    @Mock
    private VectorStoreRetriever vectorStoreRetriever;

    @Mock
    private Mapper<ArchetypeCardDocument, Document> mapper;

    @InjectMocks
    private SpringAiVectorRetrieverAdapter adapter;

    @Nested
    @DisplayName("queryCard()")
    class QueryCard {

        @Test
        @DisplayName("should return mapped archetype card document from vector store")
        void shouldReturnMappedArchetypeCardDocument() {
            var springDoc = Document.builder()
                    .text("The Fool description")
                    .metadata(Map.of("name", "The Fool"))
                    .build();
            var expected = ArchetypeCardDocument.builder()
                    .cardName("The Fool")
                    .cardDescription("The Fool description")
                    .build();

            when(vectorStoreRetriever.similaritySearch(any(SearchRequest.class)))
                    .thenReturn(List.of(springDoc));
            when(mapper.toTarget(springDoc)).thenReturn(expected);

            ArchetypeCardDocument result = adapter.queryCard("The Fool");

            assertThat(result).isEqualTo(expected);
            assertThat(result.cardName()).isEqualTo("The Fool");
        }

        @Test
        @DisplayName("should delegate similarity search to vector store retriever")
        void shouldDelegateSimilaritySearchToRetriever() {
            var springDoc = Document.builder().text("text").build();
            var cardDoc = ArchetypeCardDocument.builder()
                    .cardName("test").cardDescription("text").build();

            when(vectorStoreRetriever.similaritySearch(any(SearchRequest.class)))
                    .thenReturn(List.of(springDoc));
            when(mapper.toTarget(any())).thenReturn(cardDoc);

            adapter.queryCard("test query");

            verify(vectorStoreRetriever).similaritySearch(any(SearchRequest.class));
        }
    }

    @Nested
    @DisplayName("queryKCards()")
    class QueryKCards {

        @Test
        @DisplayName("should return mapped list of archetype card documents")
        void shouldReturnMappedListOfArchetypeCardDocuments() {
            var doc1 = Document.builder().text("Desc 1").metadata(Map.of("name", "Card1")).build();
            var doc2 = Document.builder().text("Desc 2").metadata(Map.of("name", "Card2")).build();

            var card1 = ArchetypeCardDocument.builder()
                    .cardName("Card1").cardDescription("Desc 1").build();
            var card2 = ArchetypeCardDocument.builder()
                    .cardName("Card2").cardDescription("Desc 2").build();

            when(vectorStoreRetriever.similaritySearch(any(SearchRequest.class)))
                    .thenReturn(List.of(doc1, doc2));
            when(mapper.toTarget(doc1)).thenReturn(card1);
            when(mapper.toTarget(doc2)).thenReturn(card2);

            List<ArchetypeCardDocument> result = adapter.queryKCards("test", 2);

            assertThat(result).hasSize(2);
            assertThat(result).containsExactly(card1, card2);
        }

        @Test
        @DisplayName("should return empty list when vector store returns no results")
        void shouldReturnEmptyListWhenNoResults() {
            when(vectorStoreRetriever.similaritySearch(any(SearchRequest.class)))
                    .thenReturn(List.of());

            List<ArchetypeCardDocument> result = adapter.queryKCards("nonexistent", 5);

            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("should delegate to vector store retriever")
        void shouldDelegateToRetriever() {
            when(vectorStoreRetriever.similaritySearch(any(SearchRequest.class)))
                    .thenReturn(List.of());

            adapter.queryKCards("query", 3);

            verify(vectorStoreRetriever).similaritySearch(any(SearchRequest.class));
        }
    }
}

