package ca.sheridancollege.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
public class PlayerController {

    @Autowired
    private PlayerRepository playerRepo;
    @Autowired
    private TeamsRepository teamRepo;

    PlayerUtils playerUtils = new PlayerUtils();

    @PostMapping("/addPlayer")
    public String addPlayer(@ModelAttribute Player player, RedirectAttributes redirectModel) {

        if (player.getName().trim() == "" || player.getAge() == 0 || player.getEmail().trim() == ""
                || player.getPhone().trim() == "" || player.getGender() == null) {
            redirectModel.addFlashAttribute("toast", new ToastNotifcation("All fields are mandatory", "danger"));
            return "redirect:/players";
        }

        playerRepo.save(player);
        redirectModel.addFlashAttribute("toast", new ToastNotifcation("Successfully added player", "success"));
        return "redirect:/players";
    }

    @GetMapping("/editPlayer/{id}")
    public String goEditPlayer(@PathVariable int id, Model model, RedirectAttributes redirectModel) {
        Optional<Player> player = playerRepo.findById(id);
        if (!player.isPresent()) {
            redirectModel.addFlashAttribute("toast", new ToastNotifcation("Cannot find player", "danger"));
            return "redirect:/players";
        } else {
            model.addAttribute("player", player.get());
            return "editPlayer.html";
        }
    }

    @PostMapping("/editPlayer")
    public String editPlayer(@ModelAttribute Player player, RedirectAttributes redirectModel) {
        if (player.getTeam().getId() == null) {
            player.setTeam(null);
        } else {
            Team team = teamRepo.findById(player.getTeam().getId()).get();
            Player tempPlayer = playerRepo.findById(player.getId()).get();
            if (tempPlayer.getName().equals(team.getCaptain())) {
                team.setCaptain(player.getName());
            }
        }
        playerRepo.save(player);
        redirectModel.addFlashAttribute("toast", new ToastNotifcation("Successfully editted", "success"));
        return "redirect:/players";
    }

    @GetMapping("/deletePlayer/{id}")
    public String goDeletePlayer(@PathVariable int id, RedirectAttributes redirectModel) {
        Optional<Player> player = playerRepo.findById(id);

        if (!player.isPresent()) {
            redirectModel.addFlashAttribute("toast", new ToastNotifcation("Cannot find player", "danger"));
            return "redirect:/players";
        }

        // remove player from their team
        playerUtils.removePlayerFromTeam(player.get(), teamRepo);

        // delete player
        playerRepo.deleteById(id);

        redirectModel.addFlashAttribute("toast", new ToastNotifcation("Successfully deleted", "success"));
        return "redirect:/players";
    }

    @GetMapping("/search")
    public String searchPlayers(@RequestParam String query, @RequestParam String searchBy, Model model) {
        List<Player> players = new ArrayList<Player>();
        switch (searchBy) {
            case "name":
                players = playerRepo.findByName(query);
                break;
            case "age":
                try {
                    int age = Integer.parseInt(query);
                    players = playerRepo.findByAge(age);
                } catch (NumberFormatException ex) {
                    System.err.println(ex);
                }
                break;
            case "gender":
                players = playerRepo.findByGender(query);
                break;
            case "team":
                List<Team> teams = teamRepo.findByName(query);
                if (teams.size() > 0) {
                    players = playerRepo.findByTeam(teams.get(0));
                }
                break;
        }
        if (players.size() == 0) {
            model.addAttribute("toast", new ToastNotifcation("Cannot find any players from that search", "warning"));
        }
        model.addAttribute("players", players);
        return "players.html";
    }

}
