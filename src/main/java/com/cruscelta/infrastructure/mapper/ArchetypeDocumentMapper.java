package com.cruscelta.infrastructure.mapper;

import com.cruscelta.domain.entity.ArchetypeCardDocument;
import org.springframework.ai.document.Document;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ArchetypeDocumentMapper implements Mapper<ArchetypeCardDocument, Document> {
    private static final String DEFAULT_CARD_NAME = "Unidentified";

    @Override
    public ArchetypeCardDocument toTarget(Document source) {
        var cardName = Optional
                .ofNullable(source.getMetadata().get(MetadataKey.CARD_NAME))
                .map(Object::toString)
                .orElse(DEFAULT_CARD_NAME);

        return ArchetypeCardDocument
                .builder()
                .cardName(cardName)
                .cardDescription(source.getFormattedContent())
                .build();
    }

    @Override
    public Document toSource(ArchetypeCardDocument target) {
        return Document.builder().metadata(MetadataKey.CARD_NAME, target.cardName())
                .text(target.cardDescription())
                .build();
    }

    private static class MetadataKey {
        private static final String CARD_NAME = "cardName";
    }
}
