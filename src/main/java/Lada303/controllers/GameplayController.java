package Lada303.controllers;

import Lada303.dao.*;
import Lada303.exсeptions.GameDataException;
import Lada303.services.game.Game;
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

    private final Game game;
    private final GameplayDAO gameplayDAO;
    private final PlayerDAO playerDAO;
    private final MapDAO mapDAO;
    private final StepDAO stepDAO;
    private final WinnerDAO winnerDAO;

    @Autowired
    public GameplayController(Game game,
                              GameplayDAO gameplayDAO,
                              PlayerDAO playerDAO,
                              MapDAO mapDAO,
                              StepDAO stepDAO,
                              WinnerDAO winnerDAO) {
        this.game = game;
        this.gameplayDAO = gameplayDAO;
        this.playerDAO = playerDAO;
        this.mapDAO = mapDAO;
        this.stepDAO = stepDAO;
        this.winnerDAO = winnerDAO;
    }

    @GetMapping()
    public String modeGame(@RequestParam("mode") String mode,
                           @RequestParam("type") String type,
                           Model model) {
        game.setMode(mode);
        game.setTypeFile(type);
        model.addAttribute("name2", mode.equals("AI") ? "AI" : "");
        return "gameplay/game/playerForm";
    }

    @PostMapping("/player")
    public String registrationPlayer(@RequestParam("name1") String name1,
                                     @RequestParam("name2") String name2,
                                     Model model) {
        game.setGamer1(new HumanGamer(1, name1.isEmpty() ? "Player1" : name1, Dots.X));
        if (game.getMode().equals("AI")) {
            game.setGamer2(new AIGamer(2,"AI", Dots.O));
        } else {
            game.setGamer2(new HumanGamer(2, name2.isEmpty() ? "Player2" : name2, Dots.O));
        }
        game.getGameManager().setGame(game);
        game.setName_gameplay(game.getGamer1().getName() + "Vs" + game.getGamer2().getName());

        gameplayDAO.addNewGameplay(game.getName_gameplay());
        game.setId_gameplay(gameplayDAO.getLastGameplayId());
        playerDAO.addNewGameplay(game.getId_gameplay(), game.getGamer1());
        playerDAO.addNewGameplay(game.getId_gameplay(), game.getGamer2());
        model.addAttribute("msg", "Set the size of the field and the number of dots to win");
        return "gameplay/game/mapForm";
    }

    @GetMapping("/player")
    public String newRound(Model model) {
        gameplayDAO.addNewGameplay(game.getName_gameplay());
        game.setId_gameplay(gameplayDAO.getLastGameplayId());
        playerDAO.addNewGameplay(game.getId_gameplay(), game.getGamer1());
        playerDAO.addNewGameplay(game.getId_gameplay(), game.getGamer2());
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
        game.setMap(mapX, mapY);
        mapDAO.addNewMap(game.getId_gameplay(), game.getMap().getSize());
        game.setDots_to_win(dots_to_win);
        game.startNewRound();
        model.addAttribute("msg", "Start game");
        model.addAttribute("playerName", game.getGamer1().getName());
        model.addAttribute("playerDot", game.getGamer1().getDots());
        model.addAttribute("dots_to_win", game.getDots_to_win());
        model.addAttribute("lines", game.getMap().mapAsString());
        return "gameplay/game/stepForm";
    }

    @PostMapping("/step")
    public String doingStep(@RequestParam(value = "X", required = false) int stepX,
                            @RequestParam(value = "Y", required = false) int stepY,
                            Model model) {
        if (game.isPlayerDidStep(stepX - 1, stepY - 1)) {

            stepDAO.addNewStep(game.getId_gameplay(), game.getGameManager().getLastStep());

            if (game.isTheEndOfRound()) {

                winnerDAO.addNewWinner(game.getId_gameplay(), game.getGameManager().getLastWinner_id());
                playerDAO.incrementScore(game.getId_gameplay(), game.getGameManager().getLastWinner_id());

                model.addAttribute("msg", "Winner -> " + game.getGameManager().getLastWinner());
                model.addAttribute("lines", game.getMap().mapAsString());
                return "gameplay/game/endRoundPage";
            }
            model.addAttribute("msg", "Playing game");
        } else {
            model.addAttribute("msg", "Wrong step. Try again");
        }

        model.addAttribute("playerName", game.getGameManager().getWhoseMove() == 1 ?
            game.getGamer1().getName() : game.getGamer2().getName());
        model.addAttribute("playerDot", game.getGameManager().getWhoseMove() == 1 ?
                game.getGamer1().getDots() : game.getGamer2().getDots());
        model.addAttribute("dots_to_win", game.getDots_to_win());
        model.addAttribute("lines", game.getMap().mapAsString());
        return "gameplay/game/stepForm";
    }
}
