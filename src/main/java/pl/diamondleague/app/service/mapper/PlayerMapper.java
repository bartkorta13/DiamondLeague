package pl.diamondleague.app.service.mapper;

import org.mapstruct.*;
import pl.diamondleague.app.domain.Club;
import pl.diamondleague.app.domain.Player;
import pl.diamondleague.app.service.dto.ClubDTO;
import pl.diamondleague.app.service.dto.PlayerDTO;

/**
 * Mapper for the entity {@link Player} and its DTO {@link PlayerDTO}.
 */
@Mapper(componentModel = "spring")
public interface PlayerMapper extends EntityMapper<PlayerDTO, Player> {
    @Mapping(target = "favouriteClub", source = "favouriteClub", qualifiedByName = "clubId")
    PlayerDTO toDto(Player s);

    @Named("clubId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ClubDTO toDtoClubId(Club club);
}
