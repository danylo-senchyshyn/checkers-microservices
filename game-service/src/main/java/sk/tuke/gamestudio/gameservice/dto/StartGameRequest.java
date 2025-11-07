package sk.tuke.gamestudio.gameservice.dto;

import lombok.Data;

@Data
public class StartGameRequest {
    private String whitePlayer;
    private String blackPlayer;
    private String whiteAvatarUrl;
    private String blackAvatarUrl;
}