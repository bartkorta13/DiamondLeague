package pl.diamondleague.app.service.mapper;

import org.mapstruct.*;
import pl.diamondleague.app.domain.Stadium;
import pl.diamondleague.app.service.dto.StadiumDTO;

/**
 * Mapper for the entity {@link Stadium} and its DTO {@link StadiumDTO}.
 */
@Mapper(componentModel = "spring")
public interface StadiumMapper extends EntityMapper<StadiumDTO, Stadium> {}
