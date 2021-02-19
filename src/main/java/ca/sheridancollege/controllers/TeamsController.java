package ca.sheridancollege.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class TeamsController {

    @GetMapping("/organizeTeams")
    public String organizeTeams(Model model) {
        // Organize teams here

        // make sure 20 players are registered

        // each team will have a min of 5 and max of 8 players

        // teams need to be evenly balanced

        // atleast 2 males & 2 females on each team
        return "redirect:/teams";
    }

    @GetMapping("/teamRoster/{id}")
    public String teamRoster(@PathVariable int id) {
        // get team by id and all of its players
        return "teamroster.html";
    }

    @PostMapping("/trade")
    public String tradePlayers() {
        // commence the trade here
        return "redirect:/teams";
    }

}
