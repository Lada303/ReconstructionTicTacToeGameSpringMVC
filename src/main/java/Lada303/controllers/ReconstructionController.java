package Lada303.controllers;

import Lada303.exeptions.UploadFileException;
import Lada303.services.reconstruction.ReconstructionGame;
import Lada303.services.parsers.readers.JacksonParser;
import Lada303.services.parsers.readers.StaXParser;
import Lada303.utils.ServerPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
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
    public String uploadFile(@RequestParam("file") MultipartFile file, Model model) {
        File uploadedFile;
        String name = "";
        try {
            if (file == null || file.isEmpty()) {
                throw new UploadFileException("uploadFile is null or empty");
            }
            if (file.getContentType() == null) {
                throw new UploadFileException("because type of file is null.");
            }
            if (!file.getContentType().contains("xml") && !file.getContentType().contains("json")) {
                throw new UploadFileException("because the file not *.json or *.xml.");
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


}
