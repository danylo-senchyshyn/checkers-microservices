package sk.tuke.gamestudio.ratingservice.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Entity
@NamedQuery(name = "Rating.getRating", query = "SELECT r FROM Rating r WHERE r.game = :game AND r.player = :player")
@NamedQuery(name = "Rating.getAverageRating", query = "SELECT AVG(r.rating) FROM Rating r WHERE r.game = :game")
@NamedQuery(name = "Rating.reset", query = "DELETE FROM Rating")
public class Rating implements Serializable {
    @Id
    @GeneratedValue
    private int ident;

    private String game;
    private String player;
    private int rating;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="Europe/Bratislava")
    private Date ratedOn;
    private  String avatar;

    public Rating(String game, String player, int rating, Date ratedOn, String avatar) {
        this.game = game;
        this.player = player;
        this.rating = rating;
        this.ratedOn = ratedOn;
        this.avatar = avatar;
    }
}

