package pl.diamondleague.app.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.diamondleague.app.domain.GameTeamTestSamples.*;
import static pl.diamondleague.app.domain.PlayerGameTestSamples.*;
import static pl.diamondleague.app.domain.PlayerTestSamples.*;

import org.junit.jupiter.api.Test;
import pl.diamondleague.app.web.rest.TestUtil;

class PlayerGameTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PlayerGame.class);
        PlayerGame playerGame1 = getPlayerGameSample1();
        PlayerGame playerGame2 = new PlayerGame();
        assertThat(playerGame1).isNotEqualTo(playerGame2);

        playerGame2.setId(playerGame1.getId());
        assertThat(playerGame1).isEqualTo(playerGame2);

        playerGame2 = getPlayerGameSample2();
        assertThat(playerGame1).isNotEqualTo(playerGame2);
    }

    @Test
    void playerTest() {
        PlayerGame playerGame = getPlayerGameRandomSampleGenerator();
        Player playerBack = getPlayerRandomSampleGenerator();

        playerGame.setPlayer(playerBack);
        assertThat(playerGame.getPlayer()).isEqualTo(playerBack);

        playerGame.player(null);
        assertThat(playerGame.getPlayer()).isNull();
    }

    @Test
    void gameTeamTest() {
        PlayerGame playerGame = getPlayerGameRandomSampleGenerator();
        GameTeam gameTeamBack = getGameTeamRandomSampleGenerator();

        playerGame.setGameTeam(gameTeamBack);
        assertThat(playerGame.getGameTeam()).isEqualTo(gameTeamBack);

        playerGame.gameTeam(null);
        assertThat(playerGame.getGameTeam()).isNull();
    }
}
