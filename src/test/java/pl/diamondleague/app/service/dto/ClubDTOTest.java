package pl.diamondleague.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import pl.diamondleague.app.web.rest.TestUtil;

class ClubDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ClubDTO.class);
        ClubDTO clubDTO1 = new ClubDTO();
        clubDTO1.setId(1L);
        ClubDTO clubDTO2 = new ClubDTO();
        assertThat(clubDTO1).isNotEqualTo(clubDTO2);
        clubDTO2.setId(clubDTO1.getId());
        assertThat(clubDTO1).isEqualTo(clubDTO2);
        clubDTO2.setId(2L);
        assertThat(clubDTO1).isNotEqualTo(clubDTO2);
        clubDTO1.setId(null);
        assertThat(clubDTO1).isNotEqualTo(clubDTO2);
    }
}
