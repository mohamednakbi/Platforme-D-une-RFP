package com.satoripop.rfp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.satoripop.rfp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserConfigDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserConfigDTO.class);
        UserConfigDTO userConfigDTO1 = new UserConfigDTO();
        userConfigDTO1.setId(1L);
        UserConfigDTO userConfigDTO2 = new UserConfigDTO();
        assertThat(userConfigDTO1).isNotEqualTo(userConfigDTO2);
        userConfigDTO2.setId(userConfigDTO1.getId());
        assertThat(userConfigDTO1).isEqualTo(userConfigDTO2);
        userConfigDTO2.setId(2L);
        assertThat(userConfigDTO1).isNotEqualTo(userConfigDTO2);
        userConfigDTO1.setId(null);
        assertThat(userConfigDTO1).isNotEqualTo(userConfigDTO2);
    }
}
