package sk.tuke.gamestudio.gameservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sk.tuke.gamestudio.gameservice.entity.Game;
import sk.tuke.gamestudio.gameservice.repository.GameRepository;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class GameService {

    private final GameRepository gameRepository;

    public Game saveGameResult(String winnerNickname, String loserNickname) {
        Game game = new Game(winnerNickname, loserNickname);
        return gameRepository.save(game);
    }

    public List<Game> getAllGames() {
        return gameRepository.findAllByOrderByPlayedOnDesc();
    }

    public List<Game> getGamesByPlayer(String nickname) {
        return gameRepository.findByWinnerNicknameOrLoserNicknameOrderByPlayedOnDesc(nickname, nickname);
    }

    public void reset() {
        gameRepository.deleteAll();
    }
}