package pl.diamondleague.app.service.mapper;

import static pl.diamondleague.app.domain.ClubAsserts.*;
import static pl.diamondleague.app.domain.ClubTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ClubMapperTest {

    private ClubMapper clubMapper;

    @BeforeEach
    void setUp() {
        clubMapper = new ClubMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getClubSample1();
        var actual = clubMapper.toEntity(clubMapper.toDto(expected));
        assertClubAllPropertiesEquals(expected, actual);
    }
}
