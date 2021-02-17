package ca.sheridancollege.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

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

    @GetMapping("/editPlayer")
    public String goEditPlayer() {
        return "";
    }

    @PostMapping("/editPlayer")
    public String editPlayer() {
        return "";
    }

    @GetMapping("/deletePlayer")
    public String goDeletePlayer() {
        return "";
    }
}
