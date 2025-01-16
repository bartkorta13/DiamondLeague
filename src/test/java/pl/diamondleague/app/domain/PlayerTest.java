package pl.diamondleague.app.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.diamondleague.app.domain.ClubTestSamples.*;
import static pl.diamondleague.app.domain.PlayerTestSamples.*;

import org.junit.jupiter.api.Test;
import pl.diamondleague.app.web.rest.TestUtil;

class PlayerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Player.class);
        Player player1 = getPlayerSample1();
        Player player2 = new Player();
        assertThat(player1).isNotEqualTo(player2);

        player2.setId(player1.getId());
        assertThat(player1).isEqualTo(player2);

        player2 = getPlayerSample2();
        assertThat(player1).isNotEqualTo(player2);
    }

    @Test
    void favouriteClubTest() {
        Player player = getPlayerRandomSampleGenerator();
        Club clubBack = getClubRandomSampleGenerator();

        player.setFavouriteClub(clubBack);
        assertThat(player.getFavouriteClub()).isEqualTo(clubBack);

        player.favouriteClub(null);
        assertThat(player.getFavouriteClub()).isNull();
    }
}
