package sk.tuke.gamestudio.gameservice.controller;

import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sk.tuke.gamestudio.gameservice.game.CheckersField;
import sk.tuke.gamestudio.gameservice.game.GameState;
import sk.tuke.gamestudio.gameservice.game.figures.Tile;

import java.util.*;

@Controller
@RequestMapping("/checkers")
public class GameController {

    private final CheckersField field = new CheckersField();
    private final List<String> movesLog = new ArrayList<>();

    @GetMapping
    public String checkers(Model model) {
        List<String> reversedLog = new ArrayList<>(movesLog);
        Collections.reverse(reversedLog);

        model.addAttribute("htmlField", getHtmlField());
        model.addAttribute("whitePlayerScore", field.getScoreWhite());
        model.addAttribute("blackPlayerScore", field.getScoreBlack());
        model.addAttribute("isWhiteTurn", field.isWhiteTurn());
        model.addAttribute("gameOver", field.getGameState() != GameState.PLAYING);
        return "checkers";
    }

    @PostMapping("/move")
    public String makeMove(@RequestParam int fromRow,
                           @RequestParam int fromCol,
                           @RequestParam int toRow,
                           @RequestParam int toCol) {
        field.move(fromRow, fromCol, toRow, toCol);
        return "redirect:/checkers";
    }

    @GetMapping("/new")
    public String newGame() {
        field.startNewGame();
        return "redirect:/checkers";
    }

    private void recordMove(int fromRow, int fromCol, int toRow, int toCol) {
        String player = field.isWhiteTurn() ? "Black" : "White";
        boolean captured = field.isLastCaptured();
        boolean becameKing = field.isLastBecameKing();

        StringBuilder logEntry = new StringBuilder();
        logEntry.append(String.format("%s: (%d,%d) → (%d,%d)", player, fromRow, fromCol, toRow, toCol));

        if (captured) logEntry.append(" [Capture]");
        if (becameKing) logEntry.append(" [Kinged]");

        movesLog.add(logEntry.toString());
    }

    private String getHtmlField() {
        StringBuilder sb = new StringBuilder();
        sb.append("<table class='checkers-field'>\n");
        for (int row = 0; row < 8; row++) {
            sb.append("<tr>\n");
            for (int col = 0; col < 8; col++) {
                Tile tile = field.getField()[row][col];
                sb.append("<td>");
                if (!tile.isEmpty()) {
                    String color = tile.isWhite() ? "white" : "black";
                    String type = tile.isKing() ? "king" : "checker";
                    sb.append(String.format("<div class='%s %s'></div>", type, color));
                }
                sb.append("</td>\n");
            }
            sb.append("</tr>\n");
        }
        sb.append("</table>\n");
        return sb.toString();
    }
}