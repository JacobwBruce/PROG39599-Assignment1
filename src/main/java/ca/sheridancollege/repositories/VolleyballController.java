package ca.sheridancollege.repositories;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import ca.sheridancollege.beans.Player;

@Controller
public class VolleyballController {

    @GetMapping("/")
    public String goHome() {
        return "index.html";
    }

    @GetMapping("/players")
    public String goPlayers() {
        // get all players from database
        // add players to model attribute
        return "players.html";
    }

    @GetMapping("/addPlayer")
    public String goAddPlayer(Model model) {
        model.addAttribute("player", new Player());
        return "addPlayer.html";
    }

    @PostMapping("/addPlayer")
    public String addPlayer(@ModelAttribute Player player) {

        // Save player to database

        return "redirect:/players";
    }

}
