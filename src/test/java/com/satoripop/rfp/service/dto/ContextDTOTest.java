package com.satoripop.rfp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.satoripop.rfp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ContextDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContextDTO.class);
        ContextDTO contextDTO1 = new ContextDTO();
        contextDTO1.setId(1L);
        ContextDTO contextDTO2 = new ContextDTO();
        assertThat(contextDTO1).isNotEqualTo(contextDTO2);
        contextDTO2.setId(contextDTO1.getId());
        assertThat(contextDTO1).isEqualTo(contextDTO2);
        contextDTO2.setId(2L);
        assertThat(contextDTO1).isNotEqualTo(contextDTO2);
        contextDTO1.setId(null);
        assertThat(contextDTO1).isNotEqualTo(contextDTO2);
    }
}
