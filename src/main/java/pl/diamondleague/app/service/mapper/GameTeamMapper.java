package pl.diamondleague.app.service.mapper;

import org.mapstruct.*;
import pl.diamondleague.app.domain.Game;
import pl.diamondleague.app.domain.GameTeam;
import pl.diamondleague.app.domain.Player;
import pl.diamondleague.app.service.dto.GameDTO;
import pl.diamondleague.app.service.dto.GameTeamDTO;
import pl.diamondleague.app.service.dto.PlayerDTO;

/**
 * Mapper for the entity {@link GameTeam} and its DTO {@link GameTeamDTO}.
 */
@Mapper(componentModel = "spring")
public interface GameTeamMapper extends EntityMapper<GameTeamDTO, GameTeam> {
    @Mapping(target = "captain", source = "captain", qualifiedByName = "playerNickname")
    @Mapping(target = "game", source = "game", qualifiedByName = "gameId")
    GameTeamDTO toDto(GameTeam s);

    @Named("playerNickname")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nickname", source = "nickname")
    PlayerDTO toDtoPlayerNickname(Player player);

    @Named("gameId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    GameDTO toDtoGameId(Game game);
}
