package pl.diamondleague.app.service;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.diamondleague.app.service.dto.RatingDTO;

/**
 * Service Interface for managing {@link pl.diamondleague.app.domain.Rating}.
 */
public interface RatingService {
    /**
     * Save a rating.
     *
     * @param ratingDTO the entity to save.
     * @return the persisted entity.
     */
    RatingDTO save(RatingDTO ratingDTO);

    /**
     * Updates a rating.
     *
     * @param ratingDTO the entity to update.
     * @return the persisted entity.
     */
    RatingDTO update(RatingDTO ratingDTO);

    /**
     * Partially updates a rating.
     *
     * @param ratingDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<RatingDTO> partialUpdate(RatingDTO ratingDTO);

    /**
     * Get all the ratings.
     *
     * @return the list of entities.
     */
    List<RatingDTO> findAll();

    /**
     * Get all the ratings with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<RatingDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" rating.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<RatingDTO> findOne(Long id);

    /**
     * Delete the "id" rating.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
