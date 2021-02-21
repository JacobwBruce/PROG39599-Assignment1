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
import org.springframework.web.bind.annotation.RequestParam;
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

        for (Player player : playerRepo.findAll()) {
            if (player.getTeam() != null) {
                redirectModel.addFlashAttribute("toast",
                        new ToastNotifcation("Cannot organize teams they have already been organized", "danger"));
                return "redirect:/teams";
            }
        }

        List<Team> teams = (List<Team>) teamRepo.findAll();
        List<Player> malePlayers = playerRepo.findByGender("male");
        List<Player> femalePlayers = playerRepo.findByGender("female");
        List<Player> players = new ArrayList<>();
        int numOfPlayers = (int) playerRepo.count();

        // find ideal number of teams
        List<Integer> numOfTeams = new ArrayList<>();
        for (int i = 5; i <= 8; i++) {
            int temp = numOfPlayers / i;
            if (temp >= 5) {
                numOfTeams.add(i);
            }
        }

        Random rand = new Random();
        // target number of teams
        int targetSize = numOfTeams.get(rand.nextInt(numOfTeams.size()));

        int teamIndex = 0;
        while (malePlayers.size() > 0) {
            int randomMale = rand.nextInt(malePlayers.size());
            teams.get(teamIndex).getPlayers().add(malePlayers.get(randomMale));
            malePlayers.get(randomMale).setTeam(teams.get(teamIndex));
            players.add(malePlayers.get(randomMale));
            malePlayers.remove(randomMale);

            teamIndex++;
            if (teamIndex == targetSize) {
                teamIndex = 0;
            }
        }

        while (femalePlayers.size() > 0) {
            int randomFemale = rand.nextInt(femalePlayers.size());
            teams.get(teamIndex).getPlayers().add(femalePlayers.get(randomFemale));
            femalePlayers.get(randomFemale).setTeam(teams.get(teamIndex));
            players.add(femalePlayers.get(randomFemale));
            femalePlayers.remove(randomFemale);

            teamIndex++;
            if (teamIndex == targetSize) {
                teamIndex = 0;
            }
        }

        // assign captains
        for (Team team : teams) {
            if (team.getPlayers().size() > 0) {
                team.setCaptain(team.getPlayers().get(rand.nextInt(team.getPlayers().size())).getName());
            }
        }

        teamRepo.saveAll(teams);
        playerRepo.saveAll(players);

        return "redirect:/teams";
    }

    @GetMapping("/teamRoster/{id}")
    public String teamRoster(@PathVariable int id, Model model, RedirectAttributes redirectModel) {

        Optional<Team> team = teamRepo.findById(id);

        if (!team.isPresent()) {
            redirectModel.addFlashAttribute("toast", new ToastNotifcation("Cannot find team with id: " + id, "danger"));
            return "redirect:/teams";
        }

        model.addAttribute("team", team.get());
        return "teamroster.html";
    }

    @PostMapping("/trade")
    public String tradePlayers(@RequestParam String playerOneSelect, @RequestParam String playerTwoSelect,
            @RequestParam String teamOneSelect, @RequestParam String teamTwoSelect, @RequestParam String action,
            RedirectAttributes redirectModel) {

        // make sure the user selected a player to move/trade
        if (playerOneSelect.equals("") || teamOneSelect.equals("Select a team")) {
            redirectModel.addFlashAttribute("toast", new ToastNotifcation("Please select a player to move", "danger"));
            return "redirect:/trade";
        }

        int teamIndex = 0;
        if (action.equals("swap")) {
            teamIndex = 1;
        }

        String destinationTeam = teamTwoSelect.split(",")[teamIndex];
        if (destinationTeam.equals("Select a team")) {
            redirectModel.addFlashAttribute("toast",
                    new ToastNotifcation(
                            "Please select a destination for: "
                                    + playerRepo.findById(Integer.parseInt(playerOneSelect)).get().getName(),
                            "danger"));
            return "redirect:/trade";
        }

        if (action.equals("move")) {
            // move the player to the destination team

        } else {
            // make sure that player 2 is selected
            System.out.println(playerTwoSelect);
            if (playerTwoSelect.equals("")) {
                redirectModel.addFlashAttribute("toast",
                        new ToastNotifcation(
                                "Please select a player to trade for for: "
                                        + playerRepo.findById(Integer.parseInt(playerOneSelect)).get().getName(),
                                "danger"));
                return "redirect:/trade";
            }
            // swap players

        }

        return "redirect:/teams";
    }

}
