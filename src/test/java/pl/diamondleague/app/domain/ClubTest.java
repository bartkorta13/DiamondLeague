package pl.diamondleague.app.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.diamondleague.app.domain.ClubTestSamples.*;
import static pl.diamondleague.app.domain.PlayerTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import pl.diamondleague.app.web.rest.TestUtil;

class ClubTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Club.class);
        Club club1 = getClubSample1();
        Club club2 = new Club();
        assertThat(club1).isNotEqualTo(club2);

        club2.setId(club1.getId());
        assertThat(club1).isEqualTo(club2);

        club2 = getClubSample2();
        assertThat(club1).isNotEqualTo(club2);
    }

    @Test
    void playersTest() {
        Club club = getClubRandomSampleGenerator();
        Player playerBack = getPlayerRandomSampleGenerator();

        club.addPlayers(playerBack);
        assertThat(club.getPlayers()).containsOnly(playerBack);
        assertThat(playerBack.getFavouriteClub()).isEqualTo(club);

        club.removePlayers(playerBack);
        assertThat(club.getPlayers()).doesNotContain(playerBack);
        assertThat(playerBack.getFavouriteClub()).isNull();

        club.players(new HashSet<>(Set.of(playerBack)));
        assertThat(club.getPlayers()).containsOnly(playerBack);
        assertThat(playerBack.getFavouriteClub()).isEqualTo(club);

        club.setPlayers(new HashSet<>());
        assertThat(club.getPlayers()).doesNotContain(playerBack);
        assertThat(playerBack.getFavouriteClub()).isNull();
    }
}
