package pl.diamondleague.app.service;

import java.util.List;
import java.util.Optional;
import pl.diamondleague.app.service.dto.PlayerGameDTO;

/**
 * Service Interface for managing {@link pl.diamondleague.app.domain.PlayerGame}.
 */
public interface PlayerGameService {
    /**
     * Save a playerGame.
     *
     * @param playerGameDTO the entity to save.
     * @return the persisted entity.
     */
    PlayerGameDTO save(PlayerGameDTO playerGameDTO);

    /**
     * Updates a playerGame.
     *
     * @param playerGameDTO the entity to update.
     * @return the persisted entity.
     */
    PlayerGameDTO update(PlayerGameDTO playerGameDTO);

    /**
     * Partially updates a playerGame.
     *
     * @param playerGameDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PlayerGameDTO> partialUpdate(PlayerGameDTO playerGameDTO);

    /**
     * Get all the playerGames.
     *
     * @return the list of entities.
     */
    List<PlayerGameDTO> findAll();

    /**
     * Get the "id" playerGame.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PlayerGameDTO> findOne(Long id);

    /**
     * Delete the "id" playerGame.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
