package pl.diamondleague.app.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import pl.diamondleague.app.domain.Club;

/**
 * Spring Data JPA repository for the Club entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ClubRepository extends JpaRepository<Club, Long> {}
