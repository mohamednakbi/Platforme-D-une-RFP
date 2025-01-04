package com.satoripop.rfp.service.mapper;

import static com.satoripop.rfp.domain.ContextAsserts.*;
import static com.satoripop.rfp.domain.ContextTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ContextMapperTest {

    private ContextMapper contextMapper;

    @BeforeEach
    void setUp() {
        contextMapper = new ContextMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getContextSample1();
        var actual = contextMapper.toEntity(contextMapper.toDto(expected));
        assertContextAllPropertiesEquals(expected, actual);
    }
}
