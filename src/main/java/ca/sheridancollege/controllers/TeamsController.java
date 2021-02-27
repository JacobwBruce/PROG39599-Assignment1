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
import ca.sheridancollege.utils.PlayerUtils;

@Controller
public class TeamsController {

    @Autowired
    private TeamsRepository teamRepo;

    @Autowired
    private PlayerRepository playerRepo;

    PlayerUtils playerUtils = new PlayerUtils();

    private boolean teamsOrganized = false;

    @GetMapping("/organizeTeams")
    public String organizeTeams(RedirectAttributes redirectModel) {

        // make sure 20 players are registered
        if (playerRepo.count() < 20) {
            redirectModel.addFlashAttribute("toast",
                    new ToastNotifcation("Cannot organize teams if there are less than 20 players", "danger"));
            return "redirect:/teams";
        }

        // Check if teams have already been organized
        if (teamsOrganized) {
            redirectModel.addFlashAttribute("toast",
                    new ToastNotifcation("Cannot organize teams, they have already been organized", "danger"));
            return "redirect:/teams";
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

        teamsOrganized = true;
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

        // Make the the user selects a team to move/swap to
        String destinationTeamId = teamTwoSelect.split(",")[teamIndex];
        if (destinationTeamId.equals("Select a team") || destinationTeamId.equals(teamOneSelect)) {
            redirectModel.addFlashAttribute("toast",
                    new ToastNotifcation(
                            "Please select a valid destination for: "
                                    + playerRepo.findById(Integer.parseInt(playerOneSelect)).get().getName(),
                            "danger"));
            return "redirect:/trade";
        }

        // get player and teams
        Team destinationTeam = teamRepo.findById(Integer.parseInt(destinationTeamId)).get();
        Team originalTeam = teamRepo.findById(Integer.parseInt(teamOneSelect)).get();
        Player playerOne = playerRepo.findById(Integer.parseInt(playerOneSelect)).get();

        if (action.equals("move")) {
            // do not allow the player to move if there is no space available
            // also not allowing players to join a team if the team is empty (might change)
            if (destinationTeam.getPlayers().size() == 8
                    || destinationTeam.getPlayers().size() >= originalTeam.getPlayers().size()
                    || destinationTeam.getPlayers().size() == 0) {
                redirectModel.addFlashAttribute("toast", new ToastNotifcation(
                        "Cannot trade: teams are either full or would be too unevenly matched ", "danger"));
                return "redirect:/trade";
            }

            // move the player to the destination team
            playerUtils.removePlayerFromTeam(playerOne, teamRepo);
            playerOne.setTeam(destinationTeam);
            destinationTeam.getPlayers().add(playerOne);

            playerRepo.save(playerOne);
            teamRepo.save(destinationTeam);

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
            Player playerTwo = playerRepo.findById(Integer.parseInt(playerTwoSelect)).get();

            playerUtils.removePlayerFromTeam(playerOne, teamRepo);
            playerUtils.removePlayerFromTeam(playerTwo, teamRepo);

            playerOne.setTeam(destinationTeam);
            destinationTeam.getPlayers().add(playerOne);

            playerTwo.setTeam(originalTeam);
            originalTeam.getPlayers().add(playerTwo);

            playerRepo.save(playerOne);
            teamRepo.save(destinationTeam);
            playerRepo.save(playerTwo);
            teamRepo.save(originalTeam);

        }

        return "redirect:/teams";
    }

}
