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
        model.addAttribute("numOfPlayers", playerRepo.count());
        return "players.html";
    }

    @GetMapping("/addPlayer")
    public String goAddPlayer(Model model) {
        if (playerRepo.count() >= 64) {
            return "redirect:/players";
        }
        model.addAttribute("player", new Player());
        return "addPlayer.html";
    }

    @GetMapping("/teams")
    public String goTeams(Model model) {
        String[] teams = new String[] { "Team 1", "Team 2", "Team 3", "Team 4", "Team 5", "Team 6", "Team 7",
                "Team 8" };
        model.addAttribute("teams", teams);
        return "teams.html";
    }

    @GetMapping("/trade")
    public String goTrade(Model model) {
        // will need to get all teams and all players!!
        String[] teams = new String[] { "Team 1", "Team 2", "Team 3", "Team 4", "Team 5", "Team 6", "Team 7",
                "Team 8" };
        model.addAttribute("teams", teams);
        return "trade.html";
    }

}
