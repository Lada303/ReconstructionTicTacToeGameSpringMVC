package Lada303.controllers;

import Lada303.exсeptions.GameDataException;
import Lada303.services.gameplay.Competition;
import Lada303.utils.enums.Dots;
import Lada303.models.players.AIGamer;
import Lada303.models.players.HumanGamer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/gameplay/game")
@Controller("/gameplay/game")
@ControllerAdvice
public class GameController {

    private final Competition competition;

    @Autowired
    public GameController(Competition competition) {
        this.competition = competition;
    }

    @GetMapping()
    public String modeGame(@RequestParam("mode") String mode,
                           @RequestParam("type") String type,
                           Model model) {
        competition.setMode(mode);
        competition.setTypeFile(type);
        model.addAttribute("name2", mode.equals("AI") ? "AI" : "");
        return "gameplay/game/playerForm";
    }

    @PostMapping("/player")
    public String registrationPlayer(@RequestParam("name1") String name1,
                                     @RequestParam("name2") String name2) {
        competition.setGamer1(new HumanGamer(1, name1.isEmpty() ? "Player1" : name1, Dots.X));
        if (competition.getMode().equals("AI")) {
            competition.setGamer2(new AIGamer(2,"AI", Dots.O));
        } else {
            competition.setGamer2(new HumanGamer(2, name2.isEmpty() ? "Player2" : name2, Dots.O));
        }
        competition.getJudge().setCompetition(competition);
        return "gameplay/game/mapForm";
    }

    @GetMapping("/player")
    public String startNewRound(Model model) {
        model.addAttribute("msg", "Set the size of the field and the number of dots to win");
        return "gameplay/game/mapForm";
    }

    @GetMapping("/map")
    public String startNewRound(@RequestParam("X") int mapX,
                                @RequestParam("Y") int mapY,
                                @RequestParam("dots_to_win") int dots_to_win,
                                Model model) {
        try {
            if (Math.min(mapX, mapY) < dots_to_win) {
                throw new GameDataException("the number of Dots_to_win cannot be more than the minimum side of the field\n");
            }
            if (Math.min(mapX, mapY) - 1 > dots_to_win) {
                throw new GameDataException("the number of Dots_to_win cannot be less than the minimum side of the field by more than 1\n");
            }
        } catch (GameDataException e) {
            model.addAttribute("msg", e.getMessage());
            return "/gameplay/game/mapForm";
        }

        competition.setMap(mapX, mapY);
        competition.setDots_to_win(dots_to_win);
        competition.startNewRound();
        model.addAttribute("msg", "Start game");
        model.addAttribute("playerName", competition.getGamer1().getName());
        model.addAttribute("playerDot", competition.getGamer1().getDots());
        model.addAttribute("dots_to_win", competition.getDots_to_win());
        model.addAttribute("lines", competition.getMap().mapAsString());
        return "gameplay/game/stepForm";
    }

    @PostMapping("/step")
    public String doingStep(@RequestParam(value = "X", required = false) int stepX,
                            @RequestParam(value = "Y", required = false) int stepY,
                            Model model) {
        if (competition.isPlayerDidStep(stepX - 1, stepY - 1)) {
            if (competition.isTheEndOfRound()) {
                model.addAttribute("msg", "Winner -> " + competition.getJudge().getLastWinner());
                model.addAttribute("lines", competition.getMap().mapAsString());
                return "gameplay/game/endRoundPage";
            }
            model.addAttribute("msg", "Playing game");
        } else {
            model.addAttribute("msg", "Wrong step. Try again");
        }

        model.addAttribute("playerName", competition.getJudge().getWhoseMove() == 1 ?
            competition.getGamer1().getName() : competition.getGamer2().getName());
        model.addAttribute("playerDot", competition.getJudge().getWhoseMove() == 1 ?
                competition.getGamer1().getDots() : competition.getGamer2().getDots());
        model.addAttribute("dots_to_win", competition.getDots_to_win());
        model.addAttribute("lines", competition.getMap().mapAsString());
        return "gameplay/game/stepForm";
    }
}
