package pl.diamondleague.app.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import pl.diamondleague.app.domain.Stadium;

/**
 * Spring Data JPA repository for the Stadium entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StadiumRepository extends JpaRepository<Stadium, Long> {}
