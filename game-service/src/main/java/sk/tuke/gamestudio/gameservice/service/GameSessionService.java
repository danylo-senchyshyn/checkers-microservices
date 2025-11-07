package sk.tuke.gamestudio.gameservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sk.tuke.gamestudio.gameservice.dto.MoveDTO;
import sk.tuke.gamestudio.gameservice.dto.PlayerDTO;
import sk.tuke.gamestudio.gameservice.entity.Game;
import sk.tuke.gamestudio.gameservice.game.CheckersField;
import sk.tuke.gamestudio.gameservice.game.figures.Tile;
import sk.tuke.gamestudio.gameservice.repository.GameRepository;
import sk.tuke.gamestudio.gameservice.dto.GameStateDTO;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class GameSessionService {

    private final GameRepository gameRepository;

    // single in-memory session
    private CheckersField currentField;
    private PlayerDTO whitePlayer;
    private PlayerDTO blackPlayer;
    private final AtomicInteger gameIdCounter = new AtomicInteger(1);
    private Integer currentGameId = null;
    private boolean finished = false;

    // start a new game and return its id
    public synchronized int startNewGame(String white, String black, String whiteAvatarUrl, String blackAvatarUrl) {
        this.currentField = new CheckersField();
        this.whitePlayer = new PlayerDTO(white, whiteAvatarUrl);
        this.blackPlayer = new PlayerDTO(black, blackAvatarUrl);
        this.currentGameId = gameIdCounter.getAndIncrement();
        return currentGameId;
    }

    public synchronized GameStateDTO getState() {
        if (currentField == null) return null;
        return mapToDto(currentGameId, currentField);
    }

    public synchronized List<int[]> getPossibleMoves(int row, int col) {
        if (currentField == null) return Collections.emptyList();
        return currentField.getPossibleMoves(row, col);
    }

    public synchronized boolean makeMove(int fromRow, int fromCol, int toRow, int toCol) {
        if (currentField == null) {
            System.err.println("makeMove called but no current game!");
            return false;
        }
        boolean ok = currentField.move(fromRow, fromCol, toRow, toCol);
        if (ok && isFinished(currentField)) {
            finishAndSaveIfNeeded();
        }
        return ok;
    }

    public synchronized void resign(String resigningPlayer) {
        if (currentField == null) return;
        // determine winner and loser
        String winner, loser;
        if (resigningPlayer != null && resigningPlayer.equals(whitePlayer.getNickname())) {
            winner = blackPlayer.getNickname();
            loser = whitePlayer.getNickname();
        } else {
            winner = whitePlayer.getNickname();
            loser = blackPlayer.getNickname();
        }
        saveResult(winner, loser);
        // clear current game
        currentField = null;
        currentGameId = null;
    }

    // helper to determine finished state from CheckersField
    private boolean isFinished(CheckersField field) {
        return field.getGameState() != null &&
                (field.getGameState().name().contains("WON") ||
                        field.getGameState().name().equals("DRAW")
                );
    }

    // save winner/loser into DB if finished
    private void finishAndSaveIfNeeded() {
        if (currentField == null) return;
        if (!isFinished(currentField)) return;

        String winner, loser;
        switch (currentField.getGameState()) {
            case WHITE_WON:
                winner = whitePlayer.getNickname();
                loser = blackPlayer.getNickname();
                break;
            case BLACK_WON:
                winner = blackPlayer.getNickname();
                loser = whitePlayer.getNickname();
                break;
            default:
                currentField = null;
                currentGameId = null;
                return;
        }
        saveResult(winner, loser);
        finished = true;
    }

    private void saveResult(String winner, String loser) {
        Game g = new Game(winner, loser);
        gameRepository.save(g);
    }

    // mapping CheckersField -> GameStateDTO
    private GameStateDTO mapToDto(int gid, CheckersField field) {
        int size = CheckersField.SIZE;
        String[][] board = new String[size][size];
        Tile[][] tiles = field.getField();

        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                board[r][c] = tiles[r][c].getState().name(); // e.g. WHITE, BLACK_KING, EMPTY_BLACK
            }
        }

        List<MoveDTO> log = field.getMovesLog();

        return new GameStateDTO(
                gid,
                board,
                field.isWhiteTurn() ? "WHITE" : "BLACK",
                field.getScoreWhite(),
                field.getScoreBlack(),
                field.getGameState() == null ? "PLAYING" : field.getGameState().name(),
                field.isLastCaptured(),
                field.isLastBecameKing(),
                whitePlayer,
                blackPlayer,
                log
        );
    }
}