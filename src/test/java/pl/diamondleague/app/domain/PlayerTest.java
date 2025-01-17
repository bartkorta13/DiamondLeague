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
    void favouriteClubTest() {
        Player player = getPlayerRandomSampleGenerator();
        Club clubBack = getClubRandomSampleGenerator();

        player.setFavouriteClub(clubBack);
        assertThat(player.getFavouriteClub()).isEqualTo(clubBack);

        player.favouriteClub(null);
        assertThat(player.getFavouriteClub()).isNull();
    }

    @Test
    void ratingsTest() {
        Player player = getPlayerRandomSampleGenerator();
        Rating ratingBack = getRatingRandomSampleGenerator();

        player.addRatings(ratingBack);
        assertThat(player.getRatings()).containsOnly(ratingBack);
        assertThat(ratingBack.getPlayer()).isEqualTo(player);

        player.removeRatings(ratingBack);
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
    void gamesTest() {
        Player player = getPlayerRandomSampleGenerator();
        PlayerGame playerGameBack = getPlayerGameRandomSampleGenerator();

        player.addGames(playerGameBack);
        assertThat(player.getGames()).containsOnly(playerGameBack);
        assertThat(playerGameBack.getPlayer()).isEqualTo(player);

        player.removeGames(playerGameBack);
        assertThat(player.getGames()).doesNotContain(playerGameBack);
        assertThat(playerGameBack.getPlayer()).isNull();

        player.games(new HashSet<>(Set.of(playerGameBack)));
        assertThat(player.getGames()).containsOnly(playerGameBack);
        assertThat(playerGameBack.getPlayer()).isEqualTo(player);

        player.setGames(new HashSet<>());
        assertThat(player.getGames()).doesNotContain(playerGameBack);
        assertThat(playerGameBack.getPlayer()).isNull();
    }

    @Test
    void teamsTest() {
        Player player = getPlayerRandomSampleGenerator();
        GameTeam gameTeamBack = getGameTeamRandomSampleGenerator();

        player.addTeams(gameTeamBack);
        assertThat(player.getTeams()).containsOnly(gameTeamBack);
        assertThat(gameTeamBack.getCaptain()).isEqualTo(player);

        player.removeTeams(gameTeamBack);
        assertThat(player.getTeams()).doesNotContain(gameTeamBack);
        assertThat(gameTeamBack.getCaptain()).isNull();

        player.teams(new HashSet<>(Set.of(gameTeamBack)));
        assertThat(player.getTeams()).containsOnly(gameTeamBack);
        assertThat(gameTeamBack.getCaptain()).isEqualTo(player);

        player.setTeams(new HashSet<>());
        assertThat(player.getTeams()).doesNotContain(gameTeamBack);
        assertThat(gameTeamBack.getCaptain()).isNull();
    }
}
