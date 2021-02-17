package ca.sheridancollege.repositories;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

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

    @PostMapping("/addPlayer")
    public String addPlayer() {
        return "players.html";
    }

}
