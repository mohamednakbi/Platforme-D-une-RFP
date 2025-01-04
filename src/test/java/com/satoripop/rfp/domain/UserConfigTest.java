package com.satoripop.rfp.domain;

import static com.satoripop.rfp.domain.CVTestSamples.*;
import static com.satoripop.rfp.domain.ContextTestSamples.*;
import static com.satoripop.rfp.domain.DocumentTestSamples.*;
import static com.satoripop.rfp.domain.ReferenceTestSamples.*;
import static com.satoripop.rfp.domain.RoleTestSamples.*;
import static com.satoripop.rfp.domain.TechnologyTestSamples.*;
import static com.satoripop.rfp.domain.UserConfigTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.satoripop.rfp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class UserConfigTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserConfig.class);
        UserConfig userConfig1 = getUserConfigSample1();
        UserConfig userConfig2 = new UserConfig();
        assertThat(userConfig1).isNotEqualTo(userConfig2);

        userConfig2.setId(userConfig1.getId());
        assertThat(userConfig1).isEqualTo(userConfig2);

        userConfig2 = getUserConfigSample2();
        assertThat(userConfig1).isNotEqualTo(userConfig2);
    }

    @Test
    void roleTest() throws Exception {
        UserConfig userConfig = getUserConfigRandomSampleGenerator();
        Role roleBack = getRoleRandomSampleGenerator();

        userConfig.setRole(roleBack);
        assertThat(userConfig.getRole()).isEqualTo(roleBack);

        userConfig.role(null);
        assertThat(userConfig.getRole()).isNull();
    }

    @Test
    void contextTest() throws Exception {
        UserConfig userConfig = getUserConfigRandomSampleGenerator();
        Context contextBack = getContextRandomSampleGenerator();

        userConfig.addContext(contextBack);
        assertThat(userConfig.getContexts()).containsOnly(contextBack);
        assertThat(contextBack.getUserConfig()).isEqualTo(userConfig);

        userConfig.removeContext(contextBack);
        assertThat(userConfig.getContexts()).doesNotContain(contextBack);
        assertThat(contextBack.getUserConfig()).isNull();

        userConfig.contexts(new HashSet<>(Set.of(contextBack)));
        assertThat(userConfig.getContexts()).containsOnly(contextBack);
        assertThat(contextBack.getUserConfig()).isEqualTo(userConfig);

        userConfig.setContexts(new HashSet<>());
        assertThat(userConfig.getContexts()).doesNotContain(contextBack);
        assertThat(contextBack.getUserConfig()).isNull();
    }

    @Test
    void documentTest() throws Exception {
        UserConfig userConfig = getUserConfigRandomSampleGenerator();
        Document documentBack = getDocumentRandomSampleGenerator();

        userConfig.addDocument(documentBack);
        assertThat(userConfig.getDocuments()).containsOnly(documentBack);
        assertThat(documentBack.getUserConfig()).isEqualTo(userConfig);

        userConfig.removeDocument(documentBack);
        assertThat(userConfig.getDocuments()).doesNotContain(documentBack);
        assertThat(documentBack.getUserConfig()).isNull();

        userConfig.documents(new HashSet<>(Set.of(documentBack)));
        assertThat(userConfig.getDocuments()).containsOnly(documentBack);
        assertThat(documentBack.getUserConfig()).isEqualTo(userConfig);

        userConfig.setDocuments(new HashSet<>());
        assertThat(userConfig.getDocuments()).doesNotContain(documentBack);
        assertThat(documentBack.getUserConfig()).isNull();
    }

    @Test
    void referenceTest() throws Exception {
        UserConfig userConfig = getUserConfigRandomSampleGenerator();
        Reference referenceBack = getReferenceRandomSampleGenerator();

        userConfig.addReference(referenceBack);
        assertThat(userConfig.getReferences()).containsOnly(referenceBack);
        assertThat(referenceBack.getUserConfig()).isEqualTo(userConfig);

        userConfig.removeReference(referenceBack);
        assertThat(userConfig.getReferences()).doesNotContain(referenceBack);
        assertThat(referenceBack.getUserConfig()).isNull();

        userConfig.references(new HashSet<>(Set.of(referenceBack)));
        assertThat(userConfig.getReferences()).containsOnly(referenceBack);
        assertThat(referenceBack.getUserConfig()).isEqualTo(userConfig);

        userConfig.setReferences(new HashSet<>());
        assertThat(userConfig.getReferences()).doesNotContain(referenceBack);
        assertThat(referenceBack.getUserConfig()).isNull();
    }

    @Test
    void technologysTest() throws Exception {
        UserConfig userConfig = getUserConfigRandomSampleGenerator();
        Technology technologyBack = getTechnologyRandomSampleGenerator();

        userConfig.addTechnologys(technologyBack);
        assertThat(userConfig.getTechnologies()).containsOnly(technologyBack);

        userConfig.removeTechnologys(technologyBack);
        assertThat(userConfig.getTechnologies()).doesNotContain(technologyBack);

        userConfig.technologies(new HashSet<>(Set.of(technologyBack)));
        assertThat(userConfig.getTechnologies()).containsOnly(technologyBack);

        userConfig.setTechnologies(new HashSet<>());
        assertThat(userConfig.getTechnologies()).doesNotContain(technologyBack);
    }

    @Test
    void cVTest() throws Exception {
        UserConfig userConfig = getUserConfigRandomSampleGenerator();
        CV cVBack = getCVRandomSampleGenerator();

        userConfig.setCV(cVBack);
        assertThat(userConfig.getCV()).isEqualTo(cVBack);
        assertThat(cVBack.getUserConfig()).isEqualTo(userConfig);

        userConfig.cV(null);
        assertThat(userConfig.getCV()).isNull();
        assertThat(cVBack.getUserConfig()).isNull();
    }
}
