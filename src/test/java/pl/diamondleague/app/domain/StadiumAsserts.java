package pl.diamondleague.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class StadiumAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertStadiumAllPropertiesEquals(Stadium expected, Stadium actual) {
        assertStadiumAutoGeneratedPropertiesEquals(expected, actual);
        assertStadiumAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertStadiumAllUpdatablePropertiesEquals(Stadium expected, Stadium actual) {
        assertStadiumUpdatableFieldsEquals(expected, actual);
        assertStadiumUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertStadiumAutoGeneratedPropertiesEquals(Stadium expected, Stadium actual) {
        assertThat(expected)
            .as("Verify Stadium auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertStadiumUpdatableFieldsEquals(Stadium expected, Stadium actual) {
        assertThat(expected)
            .as("Verify Stadium relevant properties")
            .satisfies(e -> assertThat(e.getName()).as("check name").isEqualTo(actual.getName()))
            .satisfies(e -> assertThat(e.getImagePath()).as("check imagePath").isEqualTo(actual.getImagePath()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertStadiumUpdatableRelationshipsEquals(Stadium expected, Stadium actual) {
        // empty method
    }
}
