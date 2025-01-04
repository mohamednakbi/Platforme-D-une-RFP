package com.satoripop.rfp.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class ContextAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertContextAllPropertiesEquals(Context expected, Context actual) {
        assertContextAutoGeneratedPropertiesEquals(expected, actual);
        assertContextAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertContextAllUpdatablePropertiesEquals(Context expected, Context actual) {
        assertContextUpdatableFieldsEquals(expected, actual);
        assertContextUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertContextAutoGeneratedPropertiesEquals(Context expected, Context actual) {
        assertThat(expected)
            .as("Verify Context auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertContextUpdatableFieldsEquals(Context expected, Context actual) {
        assertThat(expected)
            .as("Verify Context relevant properties")
            .satisfies(e -> assertThat(e.getName()).as("check name").isEqualTo(actual.getName()))
            .satisfies(e -> assertThat(e.getDescription()).as("check description").isEqualTo(actual.getDescription()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertContextUpdatableRelationshipsEquals(Context expected, Context actual) {
        assertThat(expected)
            .as("Verify Context relationships")
            .satisfies(e -> assertThat(e.getUserConfig()).as("check userConfig").isEqualTo(actual.getUserConfig()));
    }
}
