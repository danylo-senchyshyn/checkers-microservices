package sk.tuke.gamestudio.scoreservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class ScoreDTO {
    private String nickname;
    private int points;
}
