package Lada303.controllers;

import Lada303.models.reconstruction.ReconstructionGame;
import Lada303.models.parsers.readers.JacksonParser;
import Lada303.models.parsers.readers.StaXParser;
import Lada303.services.ServerPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Objects;

@RequestMapping("/gameplay/reconstruction")
@Controller("/gameplay/reconstruction")
public class ReconstructionController {

    private static final File dir_upload = new File(ServerPath.SERVER_DIR_UPLOAD);

    private final ReconstructionGame reconstructionGame;
    private final JacksonParser jacksonParser;
    private final StaXParser staXParser;
    private String name;
    private List<String> game;

    @Autowired
    public ReconstructionController(ReconstructionGame reconstructionGame, JacksonParser jacksonParser, StaXParser staXParser) {
        this.reconstructionGame = reconstructionGame;
        this.jacksonParser = jacksonParser;
        this.staXParser = staXParser;
    }

    @GetMapping()
    public String uploadForm(@RequestParam(value = "msg", required = false) String msg, Model model) {
        model.addAttribute("msg", Objects.requireNonNullElse(msg, "Please, Choose a .json or .xml file to upload : "));
        return "gameplay/reconstruction/uploadForm";
    }

    @PostMapping()
    public String uploadFile(@RequestParam("file") MultipartFile file, Model model) {// имена параметров (тут - "file") - из формы JSP.
        File uploadedFile;
        if (file.isEmpty()) {
            model.addAttribute("msg", "You failed to upload " + name + " because the file was empty.");
            return "redirect:/gameplay/reconstruction";
        }
        if (!file.getContentType().contains("xml") && !file.getContentType().contains("json")) {
            model.addAttribute("msg", "You failed to upload " + name + " because the file not .json or .xml.");
            return "redirect:/gameplay/reconstruction";
        }
        try {
            byte[] bytes = file.getBytes();
            name = file.getOriginalFilename();
            uploadedFile = new File(dir_upload.getAbsolutePath() + File.separator + name);
            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(uploadedFile));
            stream.write(bytes);
            stream.flush();
            stream.close();
        } catch (Exception e) {
            model.addAttribute("msg", "You failed to upload. Exs.: " + e.getMessage());
            return "redirect:/gameplay/reconstruction";
        }

        if (name.contains("xml")) {
            game = reconstructionGame.reconstruction(staXParser.readConfig(String.valueOf(uploadedFile)));
        } else {
            game = reconstructionGame.reconstruction(jacksonParser.readConfig(String.valueOf(uploadedFile)));
        }
        return "redirect:/gameplay/reconstruction/successPage";
    }

    @GetMapping("/successPage")
    public String success (Model model) {
        model.addAttribute("msg", "Success : " + name);
        model.addAttribute("lines", game);
        return "gameplay/reconstruction/successPage";
    }

}
