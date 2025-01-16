package pl.diamondleague.app.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.diamondleague.app.domain.PlayerGame;

/**
 * Spring Data JPA repository for the PlayerGame entity.
 */
@Repository
public interface PlayerGameRepository extends JpaRepository<PlayerGame, Long> {
    default Optional<PlayerGame> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<PlayerGame> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<PlayerGame> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select playerGame from PlayerGame playerGame left join fetch playerGame.player",
        countQuery = "select count(playerGame) from PlayerGame playerGame"
    )
    Page<PlayerGame> findAllWithToOneRelationships(Pageable pageable);

    @Query("select playerGame from PlayerGame playerGame left join fetch playerGame.player")
    List<PlayerGame> findAllWithToOneRelationships();

    @Query("select playerGame from PlayerGame playerGame left join fetch playerGame.player where playerGame.id =:id")
    Optional<PlayerGame> findOneWithToOneRelationships(@Param("id") Long id);
}
