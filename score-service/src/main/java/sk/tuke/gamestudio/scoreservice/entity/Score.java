package sk.tuke.gamestudio.scoreservice.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@NamedQueries({
        @NamedQuery(name = "Score.getTopScores", query = "SELECT s FROM Score s WHERE s.game = :game ORDER BY s.points DESC"),
        @NamedQuery(name = "Score.reset", query = "DELETE FROM Score")
})
public class Score implements Serializable {
    @Id
    @GeneratedValue
    private int ident;

    private String game;
    private String player;
    private int points;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Europe/Bratislava")
    private Date playedOn;
    private String avatar;

    public Score(String game, String player, int points, Date playedOn, String avatar) {
        this.game = game;
        this.player = player;
        this.points = points;
        this.playedOn = playedOn;
        this.avatar = avatar;
    }
}
