package com.satoripop.rfp.domain;

import static com.satoripop.rfp.domain.ContextTestSamples.*;
import static com.satoripop.rfp.domain.UserConfigTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.satoripop.rfp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ContextTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Context.class);
        Context context1 = getContextSample1();
        Context context2 = new Context();
        assertThat(context1).isNotEqualTo(context2);

        context2.setId(context1.getId());
        assertThat(context1).isEqualTo(context2);

        context2 = getContextSample2();
        assertThat(context1).isNotEqualTo(context2);
    }

    @Test
    void userConfigTest() throws Exception {
        Context context = getContextRandomSampleGenerator();
        UserConfig userConfigBack = getUserConfigRandomSampleGenerator();

        context.setUserConfig(userConfigBack);
        assertThat(context.getUserConfig()).isEqualTo(userConfigBack);

        context.userConfig(null);
        assertThat(context.getUserConfig()).isNull();
    }
}
