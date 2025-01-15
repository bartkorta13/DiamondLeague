package pl.diamondleague.app.service;

import java.util.List;
import java.util.Optional;
import pl.diamondleague.app.service.dto.PlayerDTO;

/**
 * Service Interface for managing {@link pl.diamondleague.app.domain.Player}.
 */
public interface PlayerService {
    /**
     * Save a player.
     *
     * @param playerDTO the entity to save.
     * @return the persisted entity.
     */
    PlayerDTO save(PlayerDTO playerDTO);

    /**
     * Updates a player.
     *
     * @param playerDTO the entity to update.
     * @return the persisted entity.
     */
    PlayerDTO update(PlayerDTO playerDTO);

    /**
     * Partially updates a player.
     *
     * @param playerDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PlayerDTO> partialUpdate(PlayerDTO playerDTO);

    /**
     * Get all the players.
     *
     * @return the list of entities.
     */
    List<PlayerDTO> findAll();

    /**
     * Get the "id" player.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PlayerDTO> findOne(Long id);

    /**
     * Delete the "id" player.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
