package pl.diamondleague.app.service.mapper;

import static pl.diamondleague.app.domain.StadiumAsserts.*;
import static pl.diamondleague.app.domain.StadiumTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StadiumMapperTest {

    private StadiumMapper stadiumMapper;

    @BeforeEach
    void setUp() {
        stadiumMapper = new StadiumMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getStadiumSample1();
        var actual = stadiumMapper.toEntity(stadiumMapper.toDto(expected));
        assertStadiumAllPropertiesEquals(expected, actual);
    }
}
