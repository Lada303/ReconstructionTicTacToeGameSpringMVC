package Lada303.controllers;

import Lada303.dao.*;
import Lada303.exсeptions.UploadFileException;
import Lada303.models.Gameplay;
import Lada303.models.players.Gamer;
import Lada303.services.reconstruction.ReconstructionGame;
import Lada303.utils.parsers.readers.JacksonParser;
import Lada303.utils.parsers.readers.StaXParser;
import Lada303.utils.ServerPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RequestMapping("/gameplay/reconstruction")
@Controller("/gameplay/reconstruction")
@ControllerAdvice
public class ReconstructionController {

    private static final File dir_upload = new File(ServerPath.SERVER_DIR_UPLOAD);

    private final ReconstructionGame reconstructionGame;
    private final JacksonParser jacksonParser;
    private final StaXParser staXParser;
    private final GameplayDAO gameplayDAO;
    private final PlayerDAO playerDAO;
    private final MapDAO mapDAO;
    private final StepDAO stepDAO;
    private final WinnerDAO winnerDAO;

    @Autowired
    public ReconstructionController(ReconstructionGame reconstructionGame,
                                    JacksonParser jacksonParser,
                                    StaXParser staXParser,
                                    GameplayDAO gameplayDAO,
                                    PlayerDAO playerDAO,
                                    MapDAO mapDAO,
                                    StepDAO stepDAO,
                                    WinnerDAO winnerDAO) {
        this.reconstructionGame = reconstructionGame;
        this.jacksonParser = jacksonParser;
        this.staXParser = staXParser;
        this.gameplayDAO = gameplayDAO;
        this.playerDAO = playerDAO;
        this.mapDAO = mapDAO;
        this.stepDAO = stepDAO;
        this.winnerDAO = winnerDAO;
    }

    @GetMapping("/uploadForm")
    public String uploadForm(@RequestParam(value = "msg", required = false) String msg, Model model) {
        model.addAttribute("msg", Objects.requireNonNullElse(msg, "Please, Choose a .json or .xml file to upload : "));
        return "gameplay/reconstruction/uploadForm";
    }

    @PostMapping()
    public String uploadFile(@RequestParam("file") MultipartFile file, Model model) {
        File uploadedFile;
        String name = "";
        try {
            if (file == null || file.isEmpty()) {
                throw new UploadFileException("uploadFile is null or empty");
            }
            if (file.getContentType() == null) {
                throw new UploadFileException("type of file is null.");
            }
            if (!file.getContentType().contains("xml") && !file.getContentType().contains("json")) {
                throw new UploadFileException("the file not *.json or *.xml.");
            }

            byte[] bytes = file.getBytes();
            name = file.getOriginalFilename();
            if (name == null) {
                throw new UploadFileException("uploadFile is null or empty");

            }
            uploadedFile = new File(dir_upload.getAbsolutePath() + File.separator + name);
            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(uploadedFile));
            stream.write(bytes);
            stream.flush();
            stream.close();
        } catch (UploadFileException | IOException e) {
            model.addAttribute("msg", "You failed to upload file "+ name + " -> " + e.getMessage());
            return "redirect:/gameplay/reconstruction";
        }

        List<String> game;
        if (name.contains("xml")) {
            game = reconstructionGame.reconstruction(staXParser.readConfig(String.valueOf(uploadedFile)));
        } else {
            game = reconstructionGame.reconstruction(jacksonParser.readConfig(String.valueOf(uploadedFile)));
        }
        model.addAttribute("lines", game);
        model.addAttribute("msg", "Success : " + name);
        return "/gameplay/reconstruction/successPage";
    }

    @GetMapping("/db")
    public String fromDB(Model model) {
        List<Gameplay> listGameplay = gameplayDAO.getAllGameplay();
        model.addAttribute("listGameplay", listGameplay);
        return "gameplay/reconstruction/listGameplay";
    }
    @GetMapping("/db/{id}")
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
        System.out.println(gameplay);
        List<String> gameMapAsString;
        gameMapAsString = reconstructionGame.reconstruction(gameplay);
        model.addAttribute("lines", gameMapAsString);
        model.addAttribute("msg", "Success : " + name);
        return "gameplay/reconstruction/successPage";
    }


}
