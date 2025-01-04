package com.satoripop.rfp.domain;

import static com.satoripop.rfp.domain.DocumentTestSamples.*;
import static com.satoripop.rfp.domain.UserConfigTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.satoripop.rfp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DocumentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Document.class);
        Document document1 = getDocumentSample1();
        Document document2 = new Document();
        assertThat(document1).isNotEqualTo(document2);

        document2.setId(document1.getId());
        assertThat(document1).isEqualTo(document2);

        document2 = getDocumentSample2();
        assertThat(document1).isNotEqualTo(document2);
    }

    @Test
    void userConfigTest() throws Exception {
        Document document = getDocumentRandomSampleGenerator();
        UserConfig userConfigBack = getUserConfigRandomSampleGenerator();

        document.setUserConfig(userConfigBack);
        assertThat(document.getUserConfig()).isEqualTo(userConfigBack);

        document.userConfig(null);
        assertThat(document.getUserConfig()).isNull();
    }
}
