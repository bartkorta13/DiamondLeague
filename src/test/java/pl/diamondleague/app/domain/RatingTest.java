package pl.diamondleague.app.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.diamondleague.app.domain.PlayerTestSamples.*;
import static pl.diamondleague.app.domain.RatingTestSamples.*;

import org.junit.jupiter.api.Test;
import pl.diamondleague.app.web.rest.TestUtil;

class RatingTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Rating.class);
        Rating rating1 = getRatingSample1();
        Rating rating2 = new Rating();
        assertThat(rating1).isNotEqualTo(rating2);

        rating2.setId(rating1.getId());
        assertThat(rating1).isEqualTo(rating2);

        rating2 = getRatingSample2();
        assertThat(rating1).isNotEqualTo(rating2);
    }

    @Test
    void playerTest() {
        Rating rating = getRatingRandomSampleGenerator();
        Player playerBack = getPlayerRandomSampleGenerator();

        rating.setPlayer(playerBack);
        assertThat(rating.getPlayer()).isEqualTo(playerBack);

        rating.player(null);
        assertThat(rating.getPlayer()).isNull();
    }
}
