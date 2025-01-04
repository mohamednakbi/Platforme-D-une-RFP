package com.satoripop.rfp.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class DocumentAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertDocumentAllPropertiesEquals(Document expected, Document actual) {
        assertDocumentAutoGeneratedPropertiesEquals(expected, actual);
        assertDocumentAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertDocumentAllUpdatablePropertiesEquals(Document expected, Document actual) {
        assertDocumentUpdatableFieldsEquals(expected, actual);
        assertDocumentUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertDocumentAutoGeneratedPropertiesEquals(Document expected, Document actual) {
        assertThat(expected)
            .as("Verify Document auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertDocumentUpdatableFieldsEquals(Document expected, Document actual) {
        assertThat(expected)
            .as("Verify Document relevant properties")
            .satisfies(e -> assertThat(e.getTitle()).as("check title").isEqualTo(actual.getTitle()))
            .satisfies(e -> assertThat(e.getContent()).as("check content").isEqualTo(actual.getContent()))
            .satisfies(e -> assertThat(e.getDocumentType()).as("check documentType").isEqualTo(actual.getDocumentType()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertDocumentUpdatableRelationshipsEquals(Document expected, Document actual) {
        assertThat(expected)
            .as("Verify Document relationships")
            .satisfies(e -> assertThat(e.getUserConfig()).as("check userConfig").isEqualTo(actual.getUserConfig()));
    }
}
