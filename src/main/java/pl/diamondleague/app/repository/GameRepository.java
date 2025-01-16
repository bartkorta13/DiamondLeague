package pl.diamondleague.app.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.diamondleague.app.domain.Game;

/**
 * Spring Data JPA repository for the Game entity.
 */
@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
    default Optional<Game> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Game> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Game> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(value = "select game from Game game left join fetch game.stadium", countQuery = "select count(game) from Game game")
    Page<Game> findAllWithToOneRelationships(Pageable pageable);

    @Query("select game from Game game left join fetch game.stadium")
    List<Game> findAllWithToOneRelationships();

    @Query("select game from Game game left join fetch game.stadium where game.id =:id")
    Optional<Game> findOneWithToOneRelationships(@Param("id") Long id);
}
