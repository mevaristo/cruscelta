package com.cruscelta.infrastructure.adapters.persistence;

import com.cruscelta.domain.entity.ArchetypeCardDocument;
import com.cruscelta.infrastructure.mapper.Mapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("SpringAiVectorAdapter")
class SpringAiVectorAdapterTest {

    @Mock
    private VectorStore vectorStore;

    @Mock
    private Mapper<ArchetypeCardDocument, Document> mapper;

    @InjectMocks
    private SpringAiVectorAdapter adapter;

    @Captor
    private ArgumentCaptor<List<Document>> documentListCaptor;

    @Nested
    @DisplayName("save(List<ArchetypeCardDocument>)")
    class SaveBatch {

        @Test
        @DisplayName("should map all documents and delegate to vector store")
        void shouldMapAllDocumentsAndDelegateToVectorStore() {
            var doc1 = ArchetypeCardDocument.builder()
                    .cardName("The Fool").cardDescription("Desc 1").build();
            var doc2 = ArchetypeCardDocument.builder()
                    .cardName("The Magician").cardDescription("Desc 2").build();

            var springDoc1 = Document.builder().text("Desc 1").build();
            var springDoc2 = Document.builder().text("Desc 2").build();

            when(mapper.toSource(doc1)).thenReturn(springDoc1);
            when(mapper.toSource(doc2)).thenReturn(springDoc2);

            adapter.save(List.of(doc1, doc2));

            verify(vectorStore).add(documentListCaptor.capture());
            assertThat(documentListCaptor.getValue()).containsExactly(springDoc1, springDoc2);
        }

        @Test
        @DisplayName("should handle empty list without error")
        void shouldHandleEmptyList() {
            adapter.save(List.of());

            verify(vectorStore).add(List.of());
        }
    }

    @Nested
    @DisplayName("save(ArchetypeCardDocument)")
    class SaveSingle {

        @Test
        @DisplayName("should map single document and delegate to vector store")
        void shouldMapSingleDocumentAndDelegateToVectorStore() {
            var cardDoc = ArchetypeCardDocument.builder()
                    .cardName("The Star").cardDescription("Hope").build();
            var springDoc = Document.builder().text("Hope").build();

            when(mapper.toSource(cardDoc)).thenReturn(springDoc);

            adapter.save(cardDoc);

            verify(vectorStore).add(documentListCaptor.capture());
            assertThat(documentListCaptor.getValue()).hasSize(1);
            assertThat(documentListCaptor.getValue().getFirst()).isEqualTo(springDoc);
        }
    }
}


