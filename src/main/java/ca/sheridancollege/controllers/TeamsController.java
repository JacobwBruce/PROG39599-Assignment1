package ca.sheridancollege.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ca.sheridancollege.beans.Player;
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

        // make sure 20 players are registered
        if (playerRepo.count() < 20) {
            redirectModel.addFlashAttribute("toast",
                    new ToastNotifcation("Cannot organize teams if there are less than 20 players", "danger"));
            return "redirect:/teams";
        }

        List<Team> teams = (List<Team>) teamRepo.findAll();
        List<Player> players = (List<Player>) playerRepo.findAll();

        // find ideal number of teams
        List<Integer> numOfTeams = new ArrayList<>();
        for (int i = 5; i <= 8; i++) {
            int temp = players.size() / i;
            if (temp >= 5) {
                System.out.println("Potential number of teams: " + i);
                numOfTeams.add(i);
            }
        }

        Random rand = new Random();
        // target number of teams
        int targetSize = numOfTeams.get(rand.nextInt(numOfTeams.size()));

        int numOfPlayersEachTeam = players.size() / targetSize;

        // overflow is the players 'leftover' that needed to be added
        int overflow = 0;

        // add 2 males and 2 females to teams
        for (int i = 0; i < targetSize; i++) {

            int temp = numOfPlayersEachTeam * i;
            for (int j = temp; j < (temp + numOfPlayersEachTeam); j++) {
                // assign a player to a team
                if (j == temp)
                    teams.get(i).setCaptain(players.get(j).getName());
                teams.get(i).getPlayers().add(players.get(j));
                players.get(j).setTeam(teams.get(i));

                overflow = j;
            }
        }

        // add remaining players randomly
        if (overflow != 0) {
            int i = 0;
            for (int j = overflow + 1; j < players.size(); j++) {
                teams.get(i).getPlayers().add(players.get(j));
                players.get(j).setTeam(teams.get(i));
                i++;
            }
        }
        //
        // save back to db
        teamRepo.saveAll(teams);
        playerRepo.saveAll(players);

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
