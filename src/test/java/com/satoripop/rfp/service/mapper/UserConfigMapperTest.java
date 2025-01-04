package com.satoripop.rfp.service.mapper;

import static com.satoripop.rfp.domain.UserConfigAsserts.*;
import static com.satoripop.rfp.domain.UserConfigTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserConfigMapperTest {

    private UserConfigMapper userConfigMapper;

    @BeforeEach
    void setUp() {
        userConfigMapper = new UserConfigMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getUserConfigSample1();
        var actual = userConfigMapper.toEntity(userConfigMapper.toDto(expected));
        assertUserConfigAllPropertiesEquals(expected, actual);
    }
}
