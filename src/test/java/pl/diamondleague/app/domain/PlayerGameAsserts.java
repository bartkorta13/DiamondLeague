package pl.diamondleague.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class PlayerGameAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPlayerGameAllPropertiesEquals(PlayerGame expected, PlayerGame actual) {
        assertPlayerGameAutoGeneratedPropertiesEquals(expected, actual);
        assertPlayerGameAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPlayerGameAllUpdatablePropertiesEquals(PlayerGame expected, PlayerGame actual) {
        assertPlayerGameUpdatableFieldsEquals(expected, actual);
        assertPlayerGameUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPlayerGameAutoGeneratedPropertiesEquals(PlayerGame expected, PlayerGame actual) {
        assertThat(expected)
            .as("Verify PlayerGame auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPlayerGameUpdatableFieldsEquals(PlayerGame expected, PlayerGame actual) {
        assertThat(expected)
            .as("Verify PlayerGame relevant properties")
            .satisfies(e -> assertThat(e.getGoals()).as("check goals").isEqualTo(actual.getGoals()))
            .satisfies(e -> assertThat(e.getAssists()).as("check assists").isEqualTo(actual.getAssists()))
            .satisfies(e -> assertThat(e.getAttackScore()).as("check attackScore").isEqualTo(actual.getAttackScore()))
            .satisfies(e -> assertThat(e.getDefenseScore()).as("check defenseScore").isEqualTo(actual.getDefenseScore()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPlayerGameUpdatableRelationshipsEquals(PlayerGame expected, PlayerGame actual) {
        assertThat(expected)
            .as("Verify PlayerGame relationships")
            .satisfies(e -> assertThat(e.getPlayer()).as("check player").isEqualTo(actual.getPlayer()))
            .satisfies(e -> assertThat(e.getGameTeam()).as("check gameTeam").isEqualTo(actual.getGameTeam()));
    }
}
