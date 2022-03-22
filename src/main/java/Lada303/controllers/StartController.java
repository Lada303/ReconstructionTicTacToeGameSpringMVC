package Lada303.controllers;

import Lada303.reconstructionApp.ReconstructionGame;
import Lada303.reconstructionApp.parsers.JacksonParser;
import Lada303.reconstructionApp.parsers.StaXParser;
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

@RequestMapping("/reconstruction")
@Controller("/start")
public class StartController {

    private final ReconstructionGame reconstructionGame;
    private final JacksonParser jacksonParser;
    private final StaXParser staXParser;
    private String name;
    private List<String> game;

    @Autowired
    public StartController(ReconstructionGame reconstructionGame, JacksonParser jacksonParser, StaXParser staXParser) {
        this.reconstructionGame = reconstructionGame;
        this.jacksonParser = jacksonParser;
        this.staXParser = staXParser;
    }

    @GetMapping("/start")
    public String startPage() {
        return "reconstruction/start";
    }

    @GetMapping("/uploadForm")
    public String uploadForm(@RequestParam(value = "msg", required = false) String msg, Model model) {
        model.addAttribute("msg", Objects.requireNonNullElse(msg, "Please, Choose a .json or .xml file to upload : "));
        return "reconstruction/uploadForm";
    }

    @PostMapping("/uploadFile")
    public String uploadFile(@RequestParam("file") MultipartFile file, Model model) {// имена параметров (тут - "file") - из формы JSP.
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                name = file.getOriginalFilename();
                System.out.println(name);
                //указать путь куда сервер будет закачивать файлы
                File dir = new File("C:\\server\\");
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File uploadedFile = new File(dir.getAbsolutePath() + File.separator + name);

                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(uploadedFile));
                stream.write(bytes);
                stream.flush();
                stream.close();
                if (name.contains("xml")) {
                    game = reconstructionGame.reconstruction(staXParser.readConfig(String.valueOf(uploadedFile)));
                } else {
                    game = reconstructionGame.reconstruction(jacksonParser.readConfig(String.valueOf(uploadedFile)));
                }

                return "redirect:/reconstruction/successPage";
            } catch (Exception e) {
                model.addAttribute("msg", "You failed to upload. Exs.: " + e.getMessage());
                return "redirect:/reconstruction/uploadForm";
            }
        } else {
            model.addAttribute("msg", "You failed to upload " + name + " because the file was empty.");
            return "redirect:/reconstruction/uploadForm";
        }
    }

    @GetMapping("/successPage")
    public String success (Model model) {
        model.addAttribute("msg", "Success : " + name);
        model.addAttribute("lines", game);
        return "reconstruction/successPage";
    }

    @GetMapping("/exit")
    @ResponseBody
    public String exit() {
        return "Good Bye!!!";
    }
}
