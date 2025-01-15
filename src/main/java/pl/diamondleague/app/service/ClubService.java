package pl.diamondleague.app.service;

import java.util.List;
import java.util.Optional;
import pl.diamondleague.app.service.dto.ClubDTO;

/**
 * Service Interface for managing {@link pl.diamondleague.app.domain.Club}.
 */
public interface ClubService {
    /**
     * Save a club.
     *
     * @param clubDTO the entity to save.
     * @return the persisted entity.
     */
    ClubDTO save(ClubDTO clubDTO);

    /**
     * Updates a club.
     *
     * @param clubDTO the entity to update.
     * @return the persisted entity.
     */
    ClubDTO update(ClubDTO clubDTO);

    /**
     * Partially updates a club.
     *
     * @param clubDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ClubDTO> partialUpdate(ClubDTO clubDTO);

    /**
     * Get all the clubs.
     *
     * @return the list of entities.
     */
    List<ClubDTO> findAll();

    /**
     * Get the "id" club.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ClubDTO> findOne(Long id);

    /**
     * Delete the "id" club.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
