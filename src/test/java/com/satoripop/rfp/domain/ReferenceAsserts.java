package com.satoripop.rfp.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class ReferenceAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertReferenceAllPropertiesEquals(Reference expected, Reference actual) {
        assertReferenceAutoGeneratedPropertiesEquals(expected, actual);
        assertReferenceAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertReferenceAllUpdatablePropertiesEquals(Reference expected, Reference actual) {
        assertReferenceUpdatableFieldsEquals(expected, actual);
        assertReferenceUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertReferenceAutoGeneratedPropertiesEquals(Reference expected, Reference actual) {
        assertThat(expected)
            .as("Verify Reference auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertReferenceUpdatableFieldsEquals(Reference expected, Reference actual) {
        assertThat(expected)
            .as("Verify Reference relevant properties")
            .satisfies(e -> assertThat(e.getTitle()).as("check title").isEqualTo(actual.getTitle()))
            .satisfies(e -> assertThat(e.getContent()).as("check content").isEqualTo(actual.getContent()))
            .satisfies(e -> assertThat(e.getLastmodified()).as("check lastmodified").isEqualTo(actual.getLastmodified()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertReferenceUpdatableRelationshipsEquals(Reference expected, Reference actual) {
        assertThat(expected)
            .as("Verify Reference relationships")
            .satisfies(e -> assertThat(e.getUserConfig()).as("check userConfig").isEqualTo(actual.getUserConfig()));
    }
}
