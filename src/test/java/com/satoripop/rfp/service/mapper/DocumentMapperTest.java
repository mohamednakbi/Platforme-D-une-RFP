package com.satoripop.rfp.service.mapper;

import static com.satoripop.rfp.domain.DocumentAsserts.*;
import static com.satoripop.rfp.domain.DocumentTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DocumentMapperTest {

    private DocumentMapper documentMapper;

    @BeforeEach
    void setUp() {
        documentMapper = new DocumentMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getDocumentSample1();
        var actual = documentMapper.toEntity(documentMapper.toDto(expected));
        assertDocumentAllPropertiesEquals(expected, actual);
    }
}
