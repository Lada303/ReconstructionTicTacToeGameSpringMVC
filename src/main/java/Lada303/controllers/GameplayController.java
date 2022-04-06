package Lada303.controllers;

import Lada303.dao.*;
import Lada303.exсeptions.GameDataException;
import Lada303.services.gameplay.GameplayService;
import Lada303.models.Gameplay;
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
public class GameplayController {

    private final GameplayService gameplayService;
    private Gameplay gameplay;
    private final GameplayDAO gameplayDAO;
    private final PlayerDAO playerDAO;
    private final StepDAO stepDAO;
    private final WinnerDAO winnerDAO;

    @Autowired
    public GameplayController(GameplayService gameplayService,
                              GameplayDAO gameplayDAO,
                              PlayerDAO playerDAO,
                              StepDAO stepDAO,
                              WinnerDAO winnerDAO) {
        this.gameplayService = gameplayService;
        this.gameplayDAO = gameplayDAO;
        this.playerDAO = playerDAO;
        this.stepDAO = stepDAO;
        this.winnerDAO = winnerDAO;
    }

    @GetMapping()
    public String modeGame(@RequestParam("mode") String mode,
                           @RequestParam("type") String type,
                           Model model) {
        gameplay = gameplayService.getGameplay();
        gameplayService.setStartData();
        gameplayService.setMode(mode);
        gameplayService.setTypeWriter(type);
        model.addAttribute("name2", mode.equals("AI") ? "AI" : "");
        return "gameplay/game/playerForm";
    }

    @PostMapping("/player")
    public String registrationPlayer(@RequestParam("name1") String name1,
                                     @RequestParam("name2") String name2,
                                     Model model) {
        gameplay.setGamer1(new HumanGamer(1, (name1.isEmpty() ? "Player1" : name1), Dots.X));
        if (gameplayService.getMode().equals("AI")) {
            gameplay.setGamer2(new AIGamer(2, "AI", Dots.O));
        }
        else {
            gameplay.setGamer2(new HumanGamer(2, (name2.isEmpty() ? "Player2" : name2), Dots.O));
        }
        gameplay.setName(gameplay.getGamer1().getName() + "Vs" + gameplay.getGamer2().getName());
        gameplayService.setWriter();

        model.addAttribute("msg", "Set the size of the field and the number of dots to win");
        return "gameplay/game/mapForm";
    }

    @GetMapping("/player")
    public String newRound(Model model) {
        model.addAttribute("msg", "Set the size of the field and the number of dots to win");
        return "gameplay/game/mapForm";
    }

    @GetMapping("/map")
    public String startNewRound(@RequestParam("X") int mapX,
                                @RequestParam("Y") int mapY,
                                @RequestParam("dots_to_win") int dots_to_win,
                                Model model) {
        try {
            if (mapX != mapY && Math.min(mapX, mapY) != dots_to_win) {
                throw new GameDataException("the number of Dots_to_win must be equals minimum side of the field\n");
            }
            if (mapX == mapY && dots_to_win < mapX - 1 && dots_to_win > mapX) {
                throw new GameDataException("the number of Dots_to_win must be equals side or side - 1 (if sideX == sideY)\n");
            }
        } catch (GameDataException e) {
            model.addAttribute("msg", e.getMessage());
            return "/gameplay/game/mapForm";
        }
        gameplay.setMap(mapX, mapY);
        gameplay.setDots_to_win(dots_to_win);

        gameplayDAO.addNewGameplay(gameplay);
        gameplay.setId(gameplayDAO.getLastGameplayId());
        playerDAO.addNewGameplay(gameplay.getId(), gameplay.getGamer1());
        playerDAO.addNewGameplay(gameplay.getId(), gameplay.getGamer2());

        gameplayService.resetCountStep();
        gameplayService.clearListStep();
        gameplayService.resetWhoseMove();

        model.addAttribute("msg", "Start game");
        model.addAttribute("playerName", gameplay.getGamer1().getName());
        model.addAttribute("playerDot", gameplay.getGamer1().getDots());
        model.addAttribute("dots_to_win", gameplay.getDots_to_win());
        model.addAttribute("lines", gameplay.getMap().mapAsString());
        return "gameplay/game/stepForm";
    }

    @PostMapping("/step")
    public String doingStep(@RequestParam(value = "X", required = false) int stepX,
                            @RequestParam(value = "Y", required = false) int stepY,
                            Model model) {
        if (gameplayService.isPlayerDidStep(stepX - 1, stepY - 1)) {
            stepDAO.addNewStep(gameplay.getId(), gameplayService.getLastStep());
            if (gameplayService.isTheEndOfRound()) {
                winnerDAO.addNewWinner(gameplay.getId(), gameplayService.getLastWinner_id());
                model.addAttribute("msg", "Winner -> " + gameplayService.getLastWinner());
                model.addAttribute("lines", gameplay.getMap().mapAsString());
                return "gameplay/game/endRoundPage";
            }
            model.addAttribute("msg", "Playing game");
        } else {
            model.addAttribute("msg", "Wrong step. Try again");
        }

        model.addAttribute("playerName", gameplayService.getWhoseMove() == 1 ?
                gameplay.getGamer1().getName() : gameplay.getGamer2().getName());
        model.addAttribute("playerDot", gameplayService.getWhoseMove() == 1 ?
                gameplay.getGamer1().getDots() : gameplay.getGamer2().getDots());
        model.addAttribute("dots_to_win", gameplay.getDots_to_win());
        model.addAttribute("lines", gameplay.getMap().mapAsString());
        return "gameplay/game/stepForm";
    }
}
