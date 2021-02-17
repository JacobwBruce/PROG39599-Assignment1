package ca.sheridancollege.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import ca.sheridancollege.beans.Player;
import ca.sheridancollege.repositories.PlayerRepository;

@Controller
public class NavigationController {

    @Autowired
    private PlayerRepository playerRepo;

    @GetMapping("/")
    public String goHome() {
        return "index.html";
    }

    @GetMapping("/players")
    public String goPlayers(Model model) {
        model.addAttribute("players", playerRepo.findAll());
        return "players.html";
    }

    @GetMapping("/addPlayer")
    public String goAddPlayer(Model model) {
        model.addAttribute("player", new Player());
        return "addPlayer.html";
    }

}
