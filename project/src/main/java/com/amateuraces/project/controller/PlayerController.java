package com.amateuraces.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/player")
public class PlayerController {

    // to be used
    // private PlayerService playerService;
    // private TournamentService tournamentService;

    // Constructor Injection

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
        return "";
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