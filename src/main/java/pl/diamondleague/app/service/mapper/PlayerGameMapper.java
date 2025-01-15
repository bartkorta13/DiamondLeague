package pl.diamondleague.app.service.mapper;

import org.mapstruct.*;
import pl.diamondleague.app.domain.GameTeam;
import pl.diamondleague.app.domain.Player;
import pl.diamondleague.app.domain.PlayerGame;
import pl.diamondleague.app.service.dto.GameTeamDTO;
import pl.diamondleague.app.service.dto.PlayerDTO;
import pl.diamondleague.app.service.dto.PlayerGameDTO;

/**
 * Mapper for the entity {@link PlayerGame} and its DTO {@link PlayerGameDTO}.
 */
@Mapper(componentModel = "spring")
public interface PlayerGameMapper extends EntityMapper<PlayerGameDTO, PlayerGame> {
    @Mapping(target = "player", source = "player", qualifiedByName = "playerId")
    @Mapping(target = "gameTeam", source = "gameTeam", qualifiedByName = "gameTeamId")
    PlayerGameDTO toDto(PlayerGame s);

    @Named("playerId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PlayerDTO toDtoPlayerId(Player player);

    @Named("gameTeamId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    GameTeamDTO toDtoGameTeamId(GameTeam gameTeam);
}
