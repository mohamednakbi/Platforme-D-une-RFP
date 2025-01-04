package com.satoripop.rfp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.satoripop.rfp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CVDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CVDTO.class);
        CVDTO cVDTO1 = new CVDTO();
        cVDTO1.setId(1L);
        CVDTO cVDTO2 = new CVDTO();
        assertThat(cVDTO1).isNotEqualTo(cVDTO2);
        cVDTO2.setId(cVDTO1.getId());
        assertThat(cVDTO1).isEqualTo(cVDTO2);
        cVDTO2.setId(2L);
        assertThat(cVDTO1).isNotEqualTo(cVDTO2);
        cVDTO1.setId(null);
        assertThat(cVDTO1).isNotEqualTo(cVDTO2);
    }
}
