package sk.tuke.gamestudio.scoreservice.dto;

import lombok.Getter;

@Getter
public class TopPlayerDTO {
    private String nickname;
    private int totalPoints;

    public TopPlayerDTO(String nickname, int totalPoints) {
        this.nickname = nickname;
        this.totalPoints = totalPoints;
    }
}