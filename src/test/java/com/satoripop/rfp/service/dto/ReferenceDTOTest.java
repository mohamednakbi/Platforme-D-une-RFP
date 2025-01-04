package com.satoripop.rfp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.satoripop.rfp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReferenceDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReferenceDTO.class);
        ReferenceDTO referenceDTO1 = new ReferenceDTO();
        referenceDTO1.setId(1L);
        ReferenceDTO referenceDTO2 = new ReferenceDTO();
        assertThat(referenceDTO1).isNotEqualTo(referenceDTO2);
        referenceDTO2.setId(referenceDTO1.getId());
        assertThat(referenceDTO1).isEqualTo(referenceDTO2);
        referenceDTO2.setId(2L);
        assertThat(referenceDTO1).isNotEqualTo(referenceDTO2);
        referenceDTO1.setId(null);
        assertThat(referenceDTO1).isNotEqualTo(referenceDTO2);
    }
}
