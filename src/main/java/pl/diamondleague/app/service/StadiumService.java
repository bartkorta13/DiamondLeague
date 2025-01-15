package pl.diamondleague.app.service;

import java.util.List;
import java.util.Optional;
import pl.diamondleague.app.service.dto.StadiumDTO;

/**
 * Service Interface for managing {@link pl.diamondleague.app.domain.Stadium}.
 */
public interface StadiumService {
    /**
     * Save a stadium.
     *
     * @param stadiumDTO the entity to save.
     * @return the persisted entity.
     */
    StadiumDTO save(StadiumDTO stadiumDTO);

    /**
     * Updates a stadium.
     *
     * @param stadiumDTO the entity to update.
     * @return the persisted entity.
     */
    StadiumDTO update(StadiumDTO stadiumDTO);

    /**
     * Partially updates a stadium.
     *
     * @param stadiumDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<StadiumDTO> partialUpdate(StadiumDTO stadiumDTO);

    /**
     * Get all the stadiums.
     *
     * @return the list of entities.
     */
    List<StadiumDTO> findAll();

    /**
     * Get the "id" stadium.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<StadiumDTO> findOne(Long id);

    /**
     * Delete the "id" stadium.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
