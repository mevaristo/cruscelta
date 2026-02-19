package com.cruscelta.infrastructure.mapper;

import com.cruscelta.domain.entity.ArchetypeCardDocument;
import org.springframework.ai.document.Document;
import org.springframework.ai.document.MetadataMode;
import org.springframework.stereotype.Component;

@Component
public class ArchetypeDocumentMapper implements Mapper<ArchetypeCardDocument, Document> {
    @Override
    public ArchetypeCardDocument toTarget(Document source) {
        return ArchetypeCardDocument
                .builder()
                .cardName(source.getFormattedContent(MetadataMode.valueOf(MetadataKey.CARD_NAME)))
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
