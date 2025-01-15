package pl.diamondleague.app.service.mapper;

import org.mapstruct.*;
import pl.diamondleague.app.domain.Player;
import pl.diamondleague.app.domain.Rating;
import pl.diamondleague.app.service.dto.PlayerDTO;
import pl.diamondleague.app.service.dto.RatingDTO;

/**
 * Mapper for the entity {@link Rating} and its DTO {@link RatingDTO}.
 */
@Mapper(componentModel = "spring")
public interface RatingMapper extends EntityMapper<RatingDTO, Rating> {
    @Mapping(target = "player", source = "player", qualifiedByName = "playerId")
    RatingDTO toDto(Rating s);

    @Named("playerId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PlayerDTO toDtoPlayerId(Player player);
}
