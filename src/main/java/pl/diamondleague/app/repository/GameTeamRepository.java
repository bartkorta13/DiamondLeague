package pl.diamondleague.app.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.diamondleague.app.domain.GameTeam;

/**
 * Spring Data JPA repository for the GameTeam entity.
 */
@Repository
public interface GameTeamRepository extends JpaRepository<GameTeam, Long> {
    default Optional<GameTeam> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<GameTeam> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<GameTeam> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select gameTeam from GameTeam gameTeam left join fetch gameTeam.captain",
        countQuery = "select count(gameTeam) from GameTeam gameTeam"
    )
    Page<GameTeam> findAllWithToOneRelationships(Pageable pageable);

    @Query("select gameTeam from GameTeam gameTeam left join fetch gameTeam.captain")
    List<GameTeam> findAllWithToOneRelationships();

    @Query("select gameTeam from GameTeam gameTeam left join fetch gameTeam.captain where gameTeam.id =:id")
    Optional<GameTeam> findOneWithToOneRelationships(@Param("id") Long id);
}
