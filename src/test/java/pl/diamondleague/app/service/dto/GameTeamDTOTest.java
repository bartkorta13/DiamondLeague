package pl.diamondleague.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import pl.diamondleague.app.web.rest.TestUtil;

class GameTeamDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(GameTeamDTO.class);
        GameTeamDTO gameTeamDTO1 = new GameTeamDTO();
        gameTeamDTO1.setId(1L);
        GameTeamDTO gameTeamDTO2 = new GameTeamDTO();
        assertThat(gameTeamDTO1).isNotEqualTo(gameTeamDTO2);
        gameTeamDTO2.setId(gameTeamDTO1.getId());
        assertThat(gameTeamDTO1).isEqualTo(gameTeamDTO2);
        gameTeamDTO2.setId(2L);
        assertThat(gameTeamDTO1).isNotEqualTo(gameTeamDTO2);
        gameTeamDTO1.setId(null);
        assertThat(gameTeamDTO1).isNotEqualTo(gameTeamDTO2);
    }
}
