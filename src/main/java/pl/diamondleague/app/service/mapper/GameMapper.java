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
    @Mapping(target = "stadium", source = "stadium", qualifiedByName = "stadiumId")
    GameDTO toDto(Game s);

    @Named("stadiumId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    StadiumDTO toDtoStadiumId(Stadium stadium);
}
