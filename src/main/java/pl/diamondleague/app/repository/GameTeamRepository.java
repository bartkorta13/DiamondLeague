package pl.diamondleague.app.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import pl.diamondleague.app.domain.GameTeam;

/**
 * Spring Data JPA repository for the GameTeam entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GameTeamRepository extends JpaRepository<GameTeam, Long> {}
