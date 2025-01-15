package pl.diamondleague.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class ClubAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertClubAllPropertiesEquals(Club expected, Club actual) {
        assertClubAutoGeneratedPropertiesEquals(expected, actual);
        assertClubAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertClubAllUpdatablePropertiesEquals(Club expected, Club actual) {
        assertClubUpdatableFieldsEquals(expected, actual);
        assertClubUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertClubAutoGeneratedPropertiesEquals(Club expected, Club actual) {
        assertThat(expected)
            .as("Verify Club auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertClubUpdatableFieldsEquals(Club expected, Club actual) {
        assertThat(expected)
            .as("Verify Club relevant properties")
            .satisfies(e -> assertThat(e.getName()).as("check name").isEqualTo(actual.getName()))
            .satisfies(e -> assertThat(e.getLogoPath()).as("check logoPath").isEqualTo(actual.getLogoPath()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertClubUpdatableRelationshipsEquals(Club expected, Club actual) {
        // empty method
    }
}