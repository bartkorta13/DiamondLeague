package pl.diamondleague.app.service;

import java.util.List;
import java.util.Optional;
import pl.diamondleague.app.service.dto.GameTeamDTO;

/**
 * Service Interface for managing {@link pl.diamondleague.app.domain.GameTeam}.
 */
public interface GameTeamService {
    /**
     * Save a gameTeam.
     *
     * @param gameTeamDTO the entity to save.
     * @return the persisted entity.
     */
    GameTeamDTO save(GameTeamDTO gameTeamDTO);

    /**
     * Updates a gameTeam.
     *
     * @param gameTeamDTO the entity to update.
     * @return the persisted entity.
     */
    GameTeamDTO update(GameTeamDTO gameTeamDTO);

    /**
     * Partially updates a gameTeam.
     *
     * @param gameTeamDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<GameTeamDTO> partialUpdate(GameTeamDTO gameTeamDTO);

    /**
     * Get all the gameTeams.
     *
     * @return the list of entities.
     */
    List<GameTeamDTO> findAll();

    /**
     * Get the "id" gameTeam.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<GameTeamDTO> findOne(Long id);

    /**
     * Delete the "id" gameTeam.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
