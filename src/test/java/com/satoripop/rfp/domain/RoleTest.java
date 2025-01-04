package com.satoripop.rfp.domain;

import static com.satoripop.rfp.domain.RoleTestSamples.*;
import static com.satoripop.rfp.domain.UserConfigTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.satoripop.rfp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class RoleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Role.class);
        Role role1 = getRoleSample1();
        Role role2 = new Role();
        assertThat(role1).isNotEqualTo(role2);

        role2.setId(role1.getId());
        assertThat(role1).isEqualTo(role2);

        role2 = getRoleSample2();
        assertThat(role1).isNotEqualTo(role2);
    }

    @Test
    void userConfigTest() throws Exception {
        Role role = getRoleRandomSampleGenerator();
        UserConfig userConfigBack = getUserConfigRandomSampleGenerator();

        role.addUserConfig(userConfigBack);
        assertThat(role.getUserConfigs()).containsOnly(userConfigBack);
        assertThat(userConfigBack.getRole()).isEqualTo(role);

        role.removeUserConfig(userConfigBack);
        assertThat(role.getUserConfigs()).doesNotContain(userConfigBack);
        assertThat(userConfigBack.getRole()).isNull();

        role.userConfigs(new HashSet<>(Set.of(userConfigBack)));
        assertThat(role.getUserConfigs()).containsOnly(userConfigBack);
        assertThat(userConfigBack.getRole()).isEqualTo(role);

        role.setUserConfigs(new HashSet<>());
        assertThat(role.getUserConfigs()).doesNotContain(userConfigBack);
        assertThat(userConfigBack.getRole()).isNull();
    }
}
