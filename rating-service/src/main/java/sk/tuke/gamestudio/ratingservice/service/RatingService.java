package sk.tuke.gamestudio.ratingservice.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sk.tuke.gamestudio.ratingservice.entity.Rating;
import sk.tuke.gamestudio.ratingservice.repository.RatingRepository;

import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class RatingService {

    private final RatingRepository ratingRepository;

    public Rating addOrUpdateRating(String player, int ratingValue) {
        return ratingRepository.findByPlayer(player)
                .map(existingRating -> {
                    existingRating.setRating(ratingValue);
                    return ratingRepository.save(existingRating);
                })
                .orElseGet(() -> ratingRepository.save(new Rating(player, ratingValue)));
    }

    public List<Rating> getAllRatings() {
        return ratingRepository.findAll();
    }

    public double getAverageRating() {
        return ratingRepository.findAll().stream()
                .mapToInt(Rating::getRating)
                .average()
                .orElse(0.0);
    }

    public long getRatingCount() {
        return ratingRepository.count();
    }

    public void reset() {
        ratingRepository.deleteAll();
    }
}