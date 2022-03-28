package Lada303.controllers;

import Lada303.services.ServerPath;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/gameplay")
@Controller("/gameplay")
public class StartController {

    static {
        new ServerPath();
    }

    @GetMapping()
    public String startPage() {
        return "gameplay/start";
    }
}
