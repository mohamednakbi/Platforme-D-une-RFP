package com.satoripop.rfp.domain;

import static com.satoripop.rfp.domain.CVTestSamples.*;
import static com.satoripop.rfp.domain.UserConfigTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.satoripop.rfp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CVTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CV.class);
        CV cV1 = getCVSample1();
        CV cV2 = new CV();
        assertThat(cV1).isNotEqualTo(cV2);

        cV2.setId(cV1.getId());
        assertThat(cV1).isEqualTo(cV2);

        cV2 = getCVSample2();
        assertThat(cV1).isNotEqualTo(cV2);
    }

    @Test
    void userConfigTest() throws Exception {
        CV cV = getCVRandomSampleGenerator();
        UserConfig userConfigBack = getUserConfigRandomSampleGenerator();

        cV.setUserConfig(userConfigBack);
        assertThat(cV.getUserConfig()).isEqualTo(userConfigBack);

        cV.userConfig(null);
        assertThat(cV.getUserConfig()).isNull();
    }
}
