package com.satoripop.rfp.domain;

import static com.satoripop.rfp.domain.ReferenceTestSamples.*;
import static com.satoripop.rfp.domain.UserConfigTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.satoripop.rfp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReferenceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Reference.class);
        Reference reference1 = getReferenceSample1();
        Reference reference2 = new Reference();
        assertThat(reference1).isNotEqualTo(reference2);

        reference2.setId(reference1.getId());
        assertThat(reference1).isEqualTo(reference2);

        reference2 = getReferenceSample2();
        assertThat(reference1).isNotEqualTo(reference2);
    }

    @Test
    void userConfigTest() throws Exception {
        Reference reference = getReferenceRandomSampleGenerator();
        UserConfig userConfigBack = getUserConfigRandomSampleGenerator();

        reference.setUserConfig(userConfigBack);
        assertThat(reference.getUserConfig()).isEqualTo(userConfigBack);

        reference.userConfig(null);
        assertThat(reference.getUserConfig()).isNull();
    }
}
