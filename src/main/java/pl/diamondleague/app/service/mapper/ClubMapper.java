package pl.diamondleague.app.service.mapper;

import org.mapstruct.*;
import pl.diamondleague.app.domain.Club;
import pl.diamondleague.app.service.dto.ClubDTO;

/**
 * Mapper for the entity {@link Club} and its DTO {@link ClubDTO}.
 */
@Mapper(componentModel = "spring")
public interface ClubMapper extends EntityMapper<ClubDTO, Club> {}
