package pl.diamondleague.app.service.mapper;

import org.mapstruct.*;
import pl.diamondleague.app.domain.Club;
import pl.diamondleague.app.domain.Player;
import pl.diamondleague.app.domain.User;
import pl.diamondleague.app.service.dto.ClubDTO;
import pl.diamondleague.app.service.dto.PlayerDTO;
import pl.diamondleague.app.service.dto.UserDTO;

/**
 * Mapper for the entity {@link Player} and its DTO {@link PlayerDTO}.
 */
@Mapper(componentModel = "spring")
public interface PlayerMapper extends EntityMapper<PlayerDTO, Player> {
    @Mapping(target = "appUser", source = "appUser", qualifiedByName = "userLogin")
    @Mapping(target = "favouriteClub", source = "favouriteClub", qualifiedByName = "clubName")
    PlayerDTO toDto(Player s);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);

    @Named("clubName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ClubDTO toDtoClubName(Club club);
}
