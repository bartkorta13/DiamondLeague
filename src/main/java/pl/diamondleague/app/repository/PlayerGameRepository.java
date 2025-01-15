package pl.diamondleague.app.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import pl.diamondleague.app.domain.PlayerGame;

/**
 * Spring Data JPA repository for the PlayerGame entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PlayerGameRepository extends JpaRepository<PlayerGame, Long> {}
