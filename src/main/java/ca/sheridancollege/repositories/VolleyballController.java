package ca.sheridancollege.repositories;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class VolleyballController {

    @GetMapping("/")
    public String goHome() {
        return "index.html";
    }

    @GetMapping("/players")
    public String goPlayers() {
        return "players.html";
    }

    @GetMapping("/addPlayer")
    public String goAddPlayer() {
        return "addPlayer.html";
    }

}
