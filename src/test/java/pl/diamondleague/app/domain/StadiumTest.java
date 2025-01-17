package pl.diamondleague.app.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.diamondleague.app.domain.GameTestSamples.*;
import static pl.diamondleague.app.domain.StadiumTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import pl.diamondleague.app.web.rest.TestUtil;

class StadiumTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Stadium.class);
        Stadium stadium1 = getStadiumSample1();
        Stadium stadium2 = new Stadium();
        assertThat(stadium1).isNotEqualTo(stadium2);

        stadium2.setId(stadium1.getId());
        assertThat(stadium1).isEqualTo(stadium2);

        stadium2 = getStadiumSample2();
        assertThat(stadium1).isNotEqualTo(stadium2);
    }

    @Test
    void gamesTest() {
        Stadium stadium = getStadiumRandomSampleGenerator();
        Game gameBack = getGameRandomSampleGenerator();

        stadium.addGames(gameBack);
        assertThat(stadium.getGames()).containsOnly(gameBack);
        assertThat(gameBack.getStadium()).isEqualTo(stadium);

        stadium.removeGames(gameBack);
        assertThat(stadium.getGames()).doesNotContain(gameBack);
        assertThat(gameBack.getStadium()).isNull();

        stadium.games(new HashSet<>(Set.of(gameBack)));
        assertThat(stadium.getGames()).containsOnly(gameBack);
        assertThat(gameBack.getStadium()).isEqualTo(stadium);

        stadium.setGames(new HashSet<>());
        assertThat(stadium.getGames()).doesNotContain(gameBack);
        assertThat(gameBack.getStadium()).isNull();
    }
}
