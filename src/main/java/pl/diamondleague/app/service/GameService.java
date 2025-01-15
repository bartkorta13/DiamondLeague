package pl.diamondleague.app.service;

import java.util.List;
import java.util.Optional;
import pl.diamondleague.app.service.dto.GameDTO;

/**
 * Service Interface for managing {@link pl.diamondleague.app.domain.Game}.
 */
public interface GameService {
    /**
     * Save a game.
     *
     * @param gameDTO the entity to save.
     * @return the persisted entity.
     */
    GameDTO save(GameDTO gameDTO);

    /**
     * Updates a game.
     *
     * @param gameDTO the entity to update.
     * @return the persisted entity.
     */
    GameDTO update(GameDTO gameDTO);

    /**
     * Partially updates a game.
     *
     * @param gameDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<GameDTO> partialUpdate(GameDTO gameDTO);

    /**
     * Get all the games.
     *
     * @return the list of entities.
     */
    List<GameDTO> findAll();

    /**
     * Get the "id" game.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<GameDTO> findOne(Long id);

    /**
     * Delete the "id" game.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
