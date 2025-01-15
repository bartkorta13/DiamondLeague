package pl.diamondleague.app.service.mapper;

import static pl.diamondleague.app.domain.GameTeamAsserts.*;
import static pl.diamondleague.app.domain.GameTeamTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GameTeamMapperTest {

    private GameTeamMapper gameTeamMapper;

    @BeforeEach
    void setUp() {
        gameTeamMapper = new GameTeamMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getGameTeamSample1();
        var actual = gameTeamMapper.toEntity(gameTeamMapper.toDto(expected));
        assertGameTeamAllPropertiesEquals(expected, actual);
    }
}
