package com.cruscelta.infrastructure.mapper;

import com.cruscelta.domain.entity.ArchetypeCardDocument;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ArchetypeDocumentMapper")
class ArchetypeDocumentMapperTest {

    private final ArchetypeDocumentMapper mapper = new ArchetypeDocumentMapper();

    @Nested
    @DisplayName("toSource()")
    class ToSource {

        @Test
        @DisplayName("should map card description to document text")
        void shouldMapCardDescriptionToDocumentText() {
            var cardDocument = ArchetypeCardDocument.builder()
                    .cardName("The Fool")
                    .cardDescription("Represents new beginnings and spontaneity.")
                    .build();

            Document result = mapper.toSource(cardDocument);

            assertThat(result.getText()).isEqualTo("Represents new beginnings and spontaneity.");
        }

        @Test
        @DisplayName("should set cardName in document metadata")
        void shouldSetCardNameInMetadata() {
            var cardDocument = ArchetypeCardDocument.builder()
                    .cardName("The Magician")
                    .cardDescription("Willpower and resourcefulness.")
                    .build();

            Document result = mapper.toSource(cardDocument);

            assertThat(result.getMetadata()).containsEntry("cardName", "The Magician");
        }

        @Test
        @DisplayName("should produce non-null document")
        void shouldProduceNonNullDocument() {
            var cardDocument = ArchetypeCardDocument.builder()
                    .cardName("Ace of Cups")
                    .cardDescription("Emotional new beginnings.")
                    .build();

            Document result = mapper.toSource(cardDocument);

            assertThat(result).isNotNull();
        }
    }

    @Nested
    @DisplayName("toTarget()")
    class ToTarget {

        @Test
        @DisplayName("should map document text to card description")
        void shouldMapDocumentTextToCardDescription() {
            var document = Document.builder()
                    .text("Represents duality and balance.")
                    .metadata(Map.of("cardName", "Two of Cups"))
                    .build();

            ArchetypeCardDocument result = mapper.toTarget(document);

            assertThat(result.cardDescription()).contains("Represents duality and balance.");
        }

        @Test
        @DisplayName("should produce non-null archetype card document")
        void shouldProduceNonNullArchetypeCardDocument() {
            var document = Document.builder()
                    .text("A test description.")
                    .metadata(Map.of("cardName", "The Tower"))
                    .build();

            ArchetypeCardDocument result = mapper.toTarget(document);

            assertThat(result).isNotNull();
        }
    }

    @Nested
    @DisplayName("roundtrip")
    class Roundtrip {

        @Test
        @DisplayName("should preserve card description through toSource -> toTarget")
        void shouldPreserveCardDescriptionThroughRoundtrip() {
            var original = ArchetypeCardDocument.builder()
                    .cardName("The Star")
                    .cardDescription("Hope and inspiration.")
                    .build();

            Document intermediate = mapper.toSource(original);
            ArchetypeCardDocument roundtripped = mapper.toTarget(intermediate);

            assertThat(roundtripped.cardDescription()).contains("Hope and inspiration.");
        }
    }
}

