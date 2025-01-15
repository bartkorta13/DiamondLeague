package pl.diamondleague.app.service.mapper;

import static pl.diamondleague.app.domain.PlayerAsserts.*;
import static pl.diamondleague.app.domain.PlayerTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PlayerMapperTest {

    private PlayerMapper playerMapper;

    @BeforeEach
    void setUp() {
        playerMapper = new PlayerMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPlayerSample1();
        var actual = playerMapper.toEntity(playerMapper.toDto(expected));
        assertPlayerAllPropertiesEquals(expected, actual);
    }
}
