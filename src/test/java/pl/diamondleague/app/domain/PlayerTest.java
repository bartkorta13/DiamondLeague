package pl.diamondleague.app.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.diamondleague.app.domain.ClubTestSamples.*;
import static pl.diamondleague.app.domain.GameTeamTestSamples.*;
import static pl.diamondleague.app.domain.PlayerGameTestSamples.*;
import static pl.diamondleague.app.domain.PlayerTestSamples.*;
import static pl.diamondleague.app.domain.RatingTestSamples.*;

import java.util.HashSet;
import java.util.Set;
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
    void ratingTest() {
        Player player = getPlayerRandomSampleGenerator();
        Rating ratingBack = getRatingRandomSampleGenerator();

        player.addRating(ratingBack);
        assertThat(player.getRatings()).containsOnly(ratingBack);
        assertThat(ratingBack.getPlayer()).isEqualTo(player);

        player.removeRating(ratingBack);
        assertThat(player.getRatings()).doesNotContain(ratingBack);
        assertThat(ratingBack.getPlayer()).isNull();

        player.ratings(new HashSet<>(Set.of(ratingBack)));
        assertThat(player.getRatings()).containsOnly(ratingBack);
        assertThat(ratingBack.getPlayer()).isEqualTo(player);

        player.setRatings(new HashSet<>());
        assertThat(player.getRatings()).doesNotContain(ratingBack);
        assertThat(ratingBack.getPlayer()).isNull();
    }

    @Test
    void playerGameTest() {
        Player player = getPlayerRandomSampleGenerator();
        PlayerGame playerGameBack = getPlayerGameRandomSampleGenerator();

        player.addPlayerGame(playerGameBack);
        assertThat(player.getPlayerGames()).containsOnly(playerGameBack);
        assertThat(playerGameBack.getPlayer()).isEqualTo(player);

        player.removePlayerGame(playerGameBack);
        assertThat(player.getPlayerGames()).doesNotContain(playerGameBack);
        assertThat(playerGameBack.getPlayer()).isNull();

        player.playerGames(new HashSet<>(Set.of(playerGameBack)));
        assertThat(player.getPlayerGames()).containsOnly(playerGameBack);
        assertThat(playerGameBack.getPlayer()).isEqualTo(player);

        player.setPlayerGames(new HashSet<>());
        assertThat(player.getPlayerGames()).doesNotContain(playerGameBack);
        assertThat(playerGameBack.getPlayer()).isNull();
    }

    @Test
    void gameTeamTest() {
        Player player = getPlayerRandomSampleGenerator();
        GameTeam gameTeamBack = getGameTeamRandomSampleGenerator();

        player.addGameTeam(gameTeamBack);
        assertThat(player.getGameTeams()).containsOnly(gameTeamBack);
        assertThat(gameTeamBack.getCaptain()).isEqualTo(player);

        player.removeGameTeam(gameTeamBack);
        assertThat(player.getGameTeams()).doesNotContain(gameTeamBack);
        assertThat(gameTeamBack.getCaptain()).isNull();

        player.gameTeams(new HashSet<>(Set.of(gameTeamBack)));
        assertThat(player.getGameTeams()).containsOnly(gameTeamBack);
        assertThat(gameTeamBack.getCaptain()).isEqualTo(player);

        player.setGameTeams(new HashSet<>());
        assertThat(player.getGameTeams()).doesNotContain(gameTeamBack);
        assertThat(gameTeamBack.getCaptain()).isNull();
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
