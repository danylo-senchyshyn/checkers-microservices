package sk.tuke.gamestudio.gameservice.dto;

import lombok.Data;

@Data
public class MoveRequest {
    private int fromRow;
    private int fromCol;
    private int toRow;
    private int toCol;
}