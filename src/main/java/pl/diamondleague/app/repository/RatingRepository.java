package pl.diamondleague.app.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import pl.diamondleague.app.domain.Rating;

/**
 * Spring Data JPA repository for the Rating entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {}
