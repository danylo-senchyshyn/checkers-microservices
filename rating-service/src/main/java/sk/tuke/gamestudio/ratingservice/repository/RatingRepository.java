package sk.tuke.gamestudio.ratingservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sk.tuke.gamestudio.ratingservice.entity.Rating;

import java.util.Locale;
import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    Optional<Rating> findByPlayer(String player);
}
