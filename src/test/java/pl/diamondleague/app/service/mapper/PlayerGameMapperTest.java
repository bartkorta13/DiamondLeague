package pl.diamondleague.app.service.mapper;

import static pl.diamondleague.app.domain.PlayerGameAsserts.*;
import static pl.diamondleague.app.domain.PlayerGameTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PlayerGameMapperTest {

    private PlayerGameMapper playerGameMapper;

    @BeforeEach
    void setUp() {
        playerGameMapper = new PlayerGameMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPlayerGameSample1();
        var actual = playerGameMapper.toEntity(playerGameMapper.toDto(expected));
        assertPlayerGameAllPropertiesEquals(expected, actual);
    }
}
