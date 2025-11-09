package sk.tuke.gamestudio.scoreservice.controller;

import lombok.AllArgsConstructor;
import sk.tuke.gamestudio.scoreservice.dto.ScoreDTO;
import sk.tuke.gamestudio.scoreservice.dto.TopPlayerDTO;
import sk.tuke.gamestudio.scoreservice.entity.Score;
import sk.tuke.gamestudio.scoreservice.service.ScoreService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("/score")
@CrossOrigin(origins = "http://localhost:5173")
public class ScoreController {

    private final ScoreService scoreService;

    /**
     * Сохранить очки победителя
     */
    @PostMapping
    public Score addScore(@RequestBody ScoreDTO scoreDTO) {
        return scoreService.addScore(scoreDTO.getNickname(), scoreDTO.getPoints());
    }

    /**
     * Получить все победы игрока
     */
    @GetMapping("/{nickname}")
    public List<Score> getScores(@PathVariable String nickname) {
        return scoreService.getScoresForPlayer(nickname);
    }

    /**
     * Получить топ игроков по сумме очков
     */
    @GetMapping("/top")
    public List<TopPlayerDTO> getTopPlayers() {
        return scoreService.getTopPlayers().stream()
                .map(row -> new TopPlayerDTO(
                        (String) row[0], ((Number) row[1]).intValue()
                ))
                .collect(Collectors.toList());
    }
}