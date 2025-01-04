package com.satoripop.rfp.service.mapper;

import static com.satoripop.rfp.domain.CVAsserts.*;
import static com.satoripop.rfp.domain.CVTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CVMapperTest {

    private CVMapper cVMapper;

    @BeforeEach
    void setUp() {
        cVMapper = new CVMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCVSample1();
        var actual = cVMapper.toEntity(cVMapper.toDto(expected));
        assertCVAllPropertiesEquals(expected, actual);
    }
}
