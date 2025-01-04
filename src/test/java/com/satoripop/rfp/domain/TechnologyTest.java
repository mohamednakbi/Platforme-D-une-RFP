package com.satoripop.rfp.domain;

import static com.satoripop.rfp.domain.TechnologyTestSamples.*;
import static com.satoripop.rfp.domain.UserConfigTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.satoripop.rfp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class TechnologyTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Technology.class);
        Technology technology1 = getTechnologySample1();
        Technology technology2 = new Technology();
        assertThat(technology1).isNotEqualTo(technology2);

        technology2.setId(technology1.getId());
        assertThat(technology1).isEqualTo(technology2);

        technology2 = getTechnologySample2();
        assertThat(technology1).isNotEqualTo(technology2);
    }

    @Test
    void userConfigsTest() throws Exception {
        Technology technology = getTechnologyRandomSampleGenerator();
        UserConfig userConfigBack = getUserConfigRandomSampleGenerator();

        technology.addUserConfigs(userConfigBack);
        assertThat(technology.getUserConfigs()).containsOnly(userConfigBack);

        technology.removeUserConfigs(userConfigBack);
        assertThat(technology.getUserConfigs()).doesNotContain(userConfigBack);

        technology.userConfigs(new HashSet<>(Set.of(userConfigBack)));
        assertThat(technology.getUserConfigs()).containsOnly(userConfigBack);

        technology.setUserConfigs(new HashSet<>());
        assertThat(technology.getUserConfigs()).doesNotContain(userConfigBack);
    }
}
