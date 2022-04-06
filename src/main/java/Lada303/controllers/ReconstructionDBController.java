package Lada303.controllers;

import Lada303.dao.*;
import Lada303.models.Gameplay;
import Lada303.models.players.Gamer;
import Lada303.services.reconstruction.ReconstructionGameplayImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("/gameplay/reconstruction/db")
@Controller("/gameplay/reconstruction/db")
@ControllerAdvice
public class ReconstructionDBController {

    private final ReconstructionGameplayImp reconstructionGame;
    private final GameplayDAO gameplayDAO;
    private final PlayerDAO playerDAO;
    private final MapDAO mapDAO;
    private final StepDAO stepDAO;
    private final WinnerDAO winnerDAO;

    @Autowired
    public ReconstructionDBController(ReconstructionGameplayImp reconstructionGame,
                                      GameplayDAO gameplayDAO,
                                      PlayerDAO playerDAO,
                                      MapDAO mapDAO,
                                      StepDAO stepDAO,
                                      WinnerDAO winnerDAO) {
        this.reconstructionGame = reconstructionGame;
        this.gameplayDAO = gameplayDAO;
        this.playerDAO = playerDAO;
        this.mapDAO = mapDAO;
        this.stepDAO = stepDAO;
        this.winnerDAO = winnerDAO;
    }

    @GetMapping()
    public String fromDB(Model model) {
        List<Gameplay> listGameplay = gameplayDAO.getAllGameplay();
        model.addAttribute("listGameplay", listGameplay);
        return "gameplay/reconstruction/listGameplay";
    }
    @GetMapping("/{id}")
    public String takeGameplayFromId(@PathVariable("id") int id_gameplay, Model model) {
        List<Object> gameplay = new ArrayList<>();
        gameplay.add(playerDAO.getPlayerByItId(id_gameplay, 1));
        gameplay.add(playerDAO.getPlayerByItId(id_gameplay, 2));
        gameplay.add(mapDAO.getMapSize(id_gameplay));
        gameplay.addAll(stepDAO.getAllStepGameplay(id_gameplay));
        int winner = winnerDAO.getWinner(id_gameplay);
        String name;
        if (winner != 0) {
            Gamer player = playerDAO.getPlayerByItId(id_gameplay, winner);
            gameplay.add(player);
            name = player.getName();
        } else {
            name = "DRAW!";
        }
        List<String> gameMapAsString;
        gameMapAsString = reconstructionGame.reconstruction(gameplay);
        model.addAttribute("lines", gameMapAsString);
        model.addAttribute("msg", "Success : " + name);
        model.addAttribute("text", "Select from list gameplay press --> ");
        model.addAttribute("href", "/gameplay/reconstruction/db");
        model.addAttribute("submit", "list gameplay");
        return "gameplay/reconstruction/successPage";
    }
}
