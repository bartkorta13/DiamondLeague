package pl.diamondleague.app.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.diamondleague.app.domain.Rating;

/**
 * Spring Data JPA repository for the Rating entity.
 */
@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    default Optional<Rating> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Rating> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Rating> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(value = "select rating from Rating rating left join fetch rating.player", countQuery = "select count(rating) from Rating rating")
    Page<Rating> findAllWithToOneRelationships(Pageable pageable);

    @Query("select rating from Rating rating left join fetch rating.player")
    List<Rating> findAllWithToOneRelationships();

    @Query("select rating from Rating rating left join fetch rating.player where rating.id =:id")
    Optional<Rating> findOneWithToOneRelationships(@Param("id") Long id);
}
