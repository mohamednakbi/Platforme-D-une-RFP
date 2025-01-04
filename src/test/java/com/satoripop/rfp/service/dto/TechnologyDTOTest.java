package com.satoripop.rfp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.satoripop.rfp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TechnologyDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TechnologyDTO.class);
        TechnologyDTO technologyDTO1 = new TechnologyDTO();
        technologyDTO1.setId(1L);
        TechnologyDTO technologyDTO2 = new TechnologyDTO();
        assertThat(technologyDTO1).isNotEqualTo(technologyDTO2);
        technologyDTO2.setId(technologyDTO1.getId());
        assertThat(technologyDTO1).isEqualTo(technologyDTO2);
        technologyDTO2.setId(2L);
        assertThat(technologyDTO1).isNotEqualTo(technologyDTO2);
        technologyDTO1.setId(null);
        assertThat(technologyDTO1).isNotEqualTo(technologyDTO2);
    }
}
