package pl.diamondleague.app.service.mapper;

import org.mapstruct.*;
import pl.diamondleague.app.domain.Game;
import pl.diamondleague.app.domain.Stadium;
import pl.diamondleague.app.service.dto.GameDTO;
import pl.diamondleague.app.service.dto.StadiumDTO;

/**
 * Mapper for the entity {@link Game} and its DTO {@link GameDTO}.
 */
@Mapper(componentModel = "spring")
public interface GameMapper extends EntityMapper<GameDTO, Game> {
    @Mapping(target = "stadium", source = "stadium", qualifiedByName = "stadiumName")
    GameDTO toDto(Game s);

    @Named("stadiumName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    StadiumDTO toDtoStadiumName(Stadium stadium);
}
