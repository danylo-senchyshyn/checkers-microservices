package sk.tuke.gamestudio.ratingservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RatingRequest {
    private String player;
    private int rating;
}
