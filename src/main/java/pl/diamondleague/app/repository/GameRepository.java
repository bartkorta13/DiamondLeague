package pl.diamondleague.app.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import pl.diamondleague.app.domain.Game;

/**
 * Spring Data JPA repository for the Game entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GameRepository extends JpaRepository<Game, Long> {}
