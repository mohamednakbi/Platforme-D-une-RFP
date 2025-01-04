package com.satoripop.rfp.service.mapper;

import static com.satoripop.rfp.domain.ReferenceAsserts.*;
import static com.satoripop.rfp.domain.ReferenceTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReferenceMapperTest {

    private ReferenceMapper referenceMapper;

    @BeforeEach
    void setUp() {
        referenceMapper = new ReferenceMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getReferenceSample1();
        var actual = referenceMapper.toEntity(referenceMapper.toDto(expected));
        assertReferenceAllPropertiesEquals(expected, actual);
    }
}
