package pl.diamondleague.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import pl.diamondleague.app.web.rest.TestUtil;

class StadiumDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(StadiumDTO.class);
        StadiumDTO stadiumDTO1 = new StadiumDTO();
        stadiumDTO1.setId(1L);
        StadiumDTO stadiumDTO2 = new StadiumDTO();
        assertThat(stadiumDTO1).isNotEqualTo(stadiumDTO2);
        stadiumDTO2.setId(stadiumDTO1.getId());
        assertThat(stadiumDTO1).isEqualTo(stadiumDTO2);
        stadiumDTO2.setId(2L);
        assertThat(stadiumDTO1).isNotEqualTo(stadiumDTO2);
        stadiumDTO1.setId(null);
        assertThat(stadiumDTO1).isNotEqualTo(stadiumDTO2);
    }
}
