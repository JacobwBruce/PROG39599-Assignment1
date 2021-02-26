package ca.sheridancollege.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ca.sheridancollege.beans.Player;
import ca.sheridancollege.beans.ToastNotifcation;
import ca.sheridancollege.repositories.PlayerRepository;
import ca.sheridancollege.repositories.TeamsRepository;

@Controller
public class NavigationController {

    @Autowired
    private PlayerRepository playerRepo;

    @Autowired
    private TeamsRepository teamRepo;

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
    public String goAddPlayer(Model model, RedirectAttributes redirectModel) {
        if (playerRepo.count() >= 64) {
            redirectModel.addFlashAttribute("toast", new ToastNotifcation("Cannot add more than 64 players", "danger"));
            return "redirect:/players";
        }
        model.addAttribute("player", new Player());
        return "addPlayer.html";
    }

    @GetMapping("/teams")
    public String goTeams(Model model) {
        model.addAttribute("teams", teamRepo.findAll());
        return "teams.html";
    }

    @GetMapping("/trade")
    public String goTrade(Model model) {
        model.addAttribute("teams", teamRepo.findAll());
        return "trade.html";
    }

}
