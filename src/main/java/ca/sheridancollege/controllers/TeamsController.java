package ca.sheridancollege.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ca.sheridancollege.beans.Team;
import ca.sheridancollege.beans.ToastNotifcation;
import ca.sheridancollege.repositories.PlayerRepository;
import ca.sheridancollege.repositories.TeamsRepository;

@Controller
public class TeamsController {

    @Autowired
    private TeamsRepository teamRepo;

    @Autowired
    private PlayerRepository playerRepo;

    @GetMapping("/organizeTeams")
    public String organizeTeams(RedirectAttributes redirectModel) {
        // Organize teams here

        // make sure 20 players are registered
        if (playerRepo.count() < 20) {
            redirectModel.addFlashAttribute("toast",
                    new ToastNotifcation("Cannot organize teams if there are less than 20 players", "danger"));
            return "redirect:/teams";
        }

        // each team will have a min of 5 and max of 8 players

        // teams need to be evenly balanced

        // atleast 2 males & 2 females on each team
        return "redirect:/teams";
    }

    @GetMapping("/teamRoster/{id}")
    public String teamRoster(@PathVariable int id, Model model, RedirectAttributes redirectModel) {

        Optional<Team> team = teamRepo.findById(id);

        if (team.isPresent()) {
            model.addAttribute("team", team.get());
            return "teamroster.html";
        }
        redirectModel.addFlashAttribute("toast", new ToastNotifcation("Cannot find team with id: " + id, "danger"));
        return "redirect:/teams";
    }

    @PostMapping("/trade")
    public String tradePlayers() {
        // commence the trade here
        return "redirect:/teams";
    }

}
