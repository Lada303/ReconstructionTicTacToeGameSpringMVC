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
    private final Gameplay gameplay;
    private final GameplayDAO gameplayDAO;
    private final PlayerDAO playerDAO;
    private final StepDAO stepDAO;
    private final WinnerDAO winnerDAO;

    @Autowired
    public GameplayController(GameplayService gameplayService,
                              Gameplay gameplay,
                              GameplayDAO gameplayDAO,
                              PlayerDAO playerDAO,
                              StepDAO stepDAO,
                              WinnerDAO winnerDAO) {
        this.gameplayService = gameplayService;
        this.gameplay = gameplay;
        this.gameplayDAO = gameplayDAO;
        this.playerDAO = playerDAO;
        this.stepDAO = stepDAO;
        this.winnerDAO = winnerDAO;
    }

    @GetMapping()
    public String modeGame(@RequestParam("mode") String mode,
                           @RequestParam("type") String type,
                           Model model) {
        gameplay.setMode(mode);
        gameplay.setTypeWriter(type);
        gameplayService.setStartData(gameplay);
        model.addAttribute("name2", mode.equals("AI") ? "AI" : "");
        return "gameplay/game/playerForm";
    }

    @PostMapping("/player")
    public String registrationPlayer(@RequestParam("name1") String name1,
                                     @RequestParam("name2") String name2,
                                     Model model) {
        gameplay.setGamer1(new HumanGamer(1, (name1.isEmpty() ? "Player1" : name1), Dots.X));
        if (gameplay.getMode().equals("AI")) {
            gameplay.setGamer2(new AIGamer(2, "AI", Dots.O));
        }
        else {
            gameplay.setGamer2(new HumanGamer(2, (name2.isEmpty() ? "Player2" : name2), Dots.O));
        }
        gameplay.setName(gameplay.getGamer1().getName() + "Vs" + gameplay.getGamer2().getName());

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
        gameplay.clearListStep();

        gameplayDAO.addNewGameplay(gameplay);
        gameplay.setId(gameplayDAO.getLastGameplayId());
        playerDAO.addNewGameplay(gameplay.getId(), gameplay.getGamer1());
        playerDAO.addNewGameplay(gameplay.getId(), gameplay.getGamer2());

        gameplayService.startNewRound();

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
            stepDAO.addNewStep(gameplay.getId(), gameplay.getLastStep());
            if (gameplayService.isTheEndOfRound()) {
                int index_Winner = gameplayService.getLastWinner_id();
                winnerDAO.addNewWinner(gameplay.getId(), index_Winner);
                String name_winner = index_Winner == 1 ? gameplay.getGamer1().getName() :
                        (index_Winner == 2 ? gameplay.getGamer2().getName() : "Draw!");
                model.addAttribute("msg", "Winner -> " + name_winner);
                model.addAttribute("lines", gameplay.getMap().mapAsString());
                return "gameplay/game/endRoundPage";
            }
            model.addAttribute("msg", "Playing game");
        } else {
            model.addAttribute("msg", "Wrong step. Try again");
        }

        int index_last_player = gameplay.getLastStep().getPlayerId();
        model.addAttribute("playerName", index_last_player == 1 ?
                gameplay.getGamer2().getName() : gameplay.getGamer1().getName());
        model.addAttribute("playerDot", index_last_player == 1 ?
                gameplay.getGamer2().getDots() : gameplay.getGamer1().getDots());
        model.addAttribute("dots_to_win", gameplay.getDots_to_win());
        model.addAttribute("lines", gameplay.getMap().mapAsString());
        return "gameplay/game/stepForm";
    }
}
