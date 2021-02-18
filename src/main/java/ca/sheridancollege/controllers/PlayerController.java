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

import ca.sheridancollege.beans.Player;
import ca.sheridancollege.repositories.PlayerRepository;

@Controller
public class PlayerController {

    @Autowired
    private PlayerRepository playerRepo;

    @PostMapping("/addPlayer")
    public String addPlayer(@ModelAttribute Player player) {
        playerRepo.save(player);
        return "redirect:/players";
    }

    @GetMapping("/editPlayer/{id}")
    public String goEditPlayer(@PathVariable int id, Model model) {
        Optional<Player> player = playerRepo.findById(id);
        if (!player.isPresent()) {
            // player not found!!
            return "redirect:/players";
        } else {
            model.addAttribute("player", player.get());
            return "editPlayer.html";
        }
    }

    @PostMapping("/editPlayer")
    public String editPlayer(@ModelAttribute Player player) {
        playerRepo.save(player);
        return "redirect:/players";
    }

    @GetMapping("/deletePlayer/{id}")
    public String goDeletePlayer(@PathVariable int id) {
        playerRepo.deleteById(id);
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
        }
        model.addAttribute("players", players);
        return "players.html";
    }

}
