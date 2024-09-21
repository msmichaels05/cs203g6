package com.amateuraces.project.controller;


import com.amateuraces.project.app.Player;
import com.amateuraces.project.service.PlayerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegistrationController {

    @Autowired
    private PlayerService playerService;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("player", new Player());
        return "registration";  // Points to registration.html
    }

    @PostMapping("/register")
    public String registerPlayer(@ModelAttribute Player player) {
        playerService.savePlayer(player);  // Save player to the database
        return "redirect:/registrationSuccess";  // Redirect to a success page
    }

    @GetMapping("/registrationSuccess")
    public String registrationSuccess() {
        return "registrationSuccess";  // Points to registrationSuccess.html
    }
}
