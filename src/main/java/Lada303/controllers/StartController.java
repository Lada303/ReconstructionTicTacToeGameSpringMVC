package Lada303.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/gameplay")
@Controller("/gameplay")
public class StartController {

    @GetMapping()
    public String startPage() {
        return "gameplay/start";
    }
}
