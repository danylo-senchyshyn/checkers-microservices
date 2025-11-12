package sk.tuke.gamestudio.ratingservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.tuke.gamestudio.ratingservice.dto.RatingRequest;
import sk.tuke.gamestudio.ratingservice.entity.Rating;
import sk.tuke.gamestudio.ratingservice.service.RatingService;

import java.util.List;

@RestController
@RequestMapping("/rating")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class RatingController {

    private final RatingService ratingService;

    @PostMapping
    public ResponseEntity<String> addRating(@RequestBody RatingRequest request) {
        ratingService.addOrUpdateRating(request.getPlayer(), request.getRating());
        return ResponseEntity.ok("Rating saved");
    }

    @GetMapping("/all")
    public ResponseEntity<List<Rating>> getAllRatings() {
        List<Rating> ratings = ratingService.getAllRatings();
        return ResponseEntity.ok(ratings);
    }

    @GetMapping("/average")
    public ResponseEntity<Double> getAverageRating() {
        double avg = ratingService.getAverageRating();
        return ResponseEntity.ok(avg);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getRatingCount() {
        long count = ratingService.getRatingCount();
        return ResponseEntity.ok(count);
    }

    @DeleteMapping
    public ResponseEntity<Void> resetRatings() {
        ratingService.reset();
        return ResponseEntity.noContent().build();
    }
}