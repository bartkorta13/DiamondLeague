package pl.diamondleague.app.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import pl.diamondleague.app.domain.Player;

/**
 * Spring Data JPA repository for the Player entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {}
