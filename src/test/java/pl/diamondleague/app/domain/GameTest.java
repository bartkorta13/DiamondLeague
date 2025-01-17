package pl.diamondleague.app.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.diamondleague.app.domain.GameTeamTestSamples.*;
import static pl.diamondleague.app.domain.GameTestSamples.*;
import static pl.diamondleague.app.domain.StadiumTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import pl.diamondleague.app.web.rest.TestUtil;

class GameTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Game.class);
        Game game1 = getGameSample1();
        Game game2 = new Game();
        assertThat(game1).isNotEqualTo(game2);

        game2.setId(game1.getId());
        assertThat(game1).isEqualTo(game2);

        game2 = getGameSample2();
        assertThat(game1).isNotEqualTo(game2);
    }

    @Test
    void stadiumTest() {
        Game game = getGameRandomSampleGenerator();
        Stadium stadiumBack = getStadiumRandomSampleGenerator();

        game.setStadium(stadiumBack);
        assertThat(game.getStadium()).isEqualTo(stadiumBack);

        game.stadium(null);
        assertThat(game.getStadium()).isNull();
    }

    @Test
    void gameTeamsTest() {
        Game game = getGameRandomSampleGenerator();
        GameTeam gameTeamBack = getGameTeamRandomSampleGenerator();

        game.addGameTeams(gameTeamBack);
        assertThat(game.getGameTeams()).containsOnly(gameTeamBack);
        assertThat(gameTeamBack.getGame()).isEqualTo(game);

        game.removeGameTeams(gameTeamBack);
        assertThat(game.getGameTeams()).doesNotContain(gameTeamBack);
        assertThat(gameTeamBack.getGame()).isNull();

        game.gameTeams(new HashSet<>(Set.of(gameTeamBack)));
        assertThat(game.getGameTeams()).containsOnly(gameTeamBack);
        assertThat(gameTeamBack.getGame()).isEqualTo(game);

        game.setGameTeams(new HashSet<>());
        assertThat(game.getGameTeams()).doesNotContain(gameTeamBack);
        assertThat(gameTeamBack.getGame()).isNull();
    }
}
