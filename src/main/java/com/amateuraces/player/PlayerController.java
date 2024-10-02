package com.amateuraces.player;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amateuraces.book.BookService;


@RestController
public class PlayerController {
    @RequestMapping("/player")

    // to be used
    // private PlayerService playerService;
    // private TournamentService tournamentService;

    // Constructor Injection

    private PlayerService playerService;

    public PlayerController(PlayerService ps) {
        playerService = ps;
    }

    @GetMapping("/dashboard")
    public String viewDashboard(Model model) {
        // Return player dashboard view
        return "";
    }

    @GetMapping("/tournaments")
    public String viewTournaments(Model model) {
        // List available tournaments
        return "";
    }

    @PostMapping("/tournaments/register")
    public String registerForTournament(@RequestParam String tournamentId) {
        // Register player for tournament
        return "";
    }

    @GetMapping("/matches")
    public String viewMatchSchedule(Model model) {
        // Show match schedule
        return "THIS IS TEST";
    }

    @GetMapping("/profile")
    public String viewProfile(Model model) {
        // Show player profile
        return "";
    }

    // @PostMapping("/profile")
    // public String updateProfile(@ModelAttribute PlayerProfileDTO profileDTO) {
    //     // Update player profile
    // }

    @GetMapping("/notifications")
    public String viewNotifications(Model model) {
        // Show notifications
        return "";
    }
}