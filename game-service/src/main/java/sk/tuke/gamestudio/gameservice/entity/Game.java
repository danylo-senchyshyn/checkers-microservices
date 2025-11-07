package sk.tuke.gamestudio.gameservice.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "games")
@Getter @Setter @NoArgsConstructor @ToString @EqualsAndHashCode
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String winnerNickname;

    @Column(nullable = false)
    private String loserNickname;

    @Column(nullable = false, updatable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Europe/Bratislava")
    private LocalDateTime playedOn;

    public Game (String winnerNickname, String loserNickname) {
        this.winnerNickname = winnerNickname;
        this.loserNickname = loserNickname;
        this.playedOn = LocalDateTime.now();
    }
}
