package sk.tuke.gamestudio.gameservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.tuke.gamestudio.gameservice.dto.*;
import sk.tuke.gamestudio.gameservice.service.GameSessionService;

import java.util.List;

@RestController
@RequestMapping("/game")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class GameController {

    private final GameSessionService sessionService;

    // 1) Start a new game
    @PostMapping("/start")
    public ResponseEntity<GameStateDTO> startGame(@RequestBody StartGameRequest req) {
        if (req.getWhitePlayer() == null || req.getBlackPlayer() == null) {
            return ResponseEntity.badRequest().body(null);
        }

        sessionService.startNewGame(
                req.getWhitePlayer(),
                req.getBlackPlayer(),
                req.getWhiteAvatarUrl(),
                req.getBlackAvatarUrl()
        );

        GameStateDTO state = sessionService.getState();

        return ResponseEntity.ok(state);
    }

    // 2) Get game state
    @GetMapping
    public ResponseEntity<GameStateDTO> getState() {
        GameStateDTO state = sessionService.getState();
        if (state == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(state);
    }

    // 3) Get possible moves for a cell
    @GetMapping("/moves")
    public ResponseEntity<PossibleMovesDTO> getPossibleMoves(
            @RequestParam int row,
            @RequestParam int col) {

        List<int[]> moves = sessionService.getPossibleMoves(row, col);
        PossibleMovesDTO dto = new PossibleMovesDTO(0, row, col, moves);

        return ResponseEntity.ok(dto);
    }

    // 4) Make a moveмв
    @PostMapping("/move")
    public ResponseEntity<?> makeMove(@RequestBody MoveRequest req) {
        boolean ok = sessionService.makeMove(req.getFromRow(), req.getFromCol(), req.getToRow(), req.getToCol());

        if (!ok) {
            return ResponseEntity.badRequest().body("Invalid move");
        }

        return ResponseEntity.ok(sessionService.getState());
    }

    // 5) Resign
    @PostMapping("/resign")
    public ResponseEntity<String> resign(@RequestParam String player) {
        sessionService.resign(player);
        return ResponseEntity.ok("Resigned and result saved.");
    }
}