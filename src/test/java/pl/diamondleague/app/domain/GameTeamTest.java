package pl.diamondleague.app.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.diamondleague.app.domain.GameTeamTestSamples.*;
import static pl.diamondleague.app.domain.GameTestSamples.*;
import static pl.diamondleague.app.domain.PlayerTestSamples.*;

import org.junit.jupiter.api.Test;
import pl.diamondleague.app.web.rest.TestUtil;

class GameTeamTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(GameTeam.class);
        GameTeam gameTeam1 = getGameTeamSample1();
        GameTeam gameTeam2 = new GameTeam();
        assertThat(gameTeam1).isNotEqualTo(gameTeam2);

        gameTeam2.setId(gameTeam1.getId());
        assertThat(gameTeam1).isEqualTo(gameTeam2);

        gameTeam2 = getGameTeamSample2();
        assertThat(gameTeam1).isNotEqualTo(gameTeam2);
    }

    @Test
    void captainTest() {
        GameTeam gameTeam = getGameTeamRandomSampleGenerator();
        Player playerBack = getPlayerRandomSampleGenerator();

        gameTeam.setCaptain(playerBack);
        assertThat(gameTeam.getCaptain()).isEqualTo(playerBack);

        gameTeam.captain(null);
        assertThat(gameTeam.getCaptain()).isNull();
    }

    @Test
    void gameTest() {
        GameTeam gameTeam = getGameTeamRandomSampleGenerator();
        Game gameBack = getGameRandomSampleGenerator();

        gameTeam.setGame(gameBack);
        assertThat(gameTeam.getGame()).isEqualTo(gameBack);

        gameTeam.game(null);
        assertThat(gameTeam.getGame()).isNull();
    }
}
