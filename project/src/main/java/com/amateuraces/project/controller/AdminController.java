package com.amateuraces.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amateuraces.project.services.PlayerService;

@RestController
@RequestMapping("/admin")
public class AdminController {

    // to be used
    // private TournamentService tournamentService;
    // private ReportingService reportingService;

    @Autowired
    private PlayerService playerService;

    // Endpoint for admin to update player ELO and stats
    @PutMapping("/update-player-stats")
    public String updatePlayerStats(
            @RequestParam String playerId, 
            @RequestParam int opponentElo, 
            @RequestParam boolean hasWon) {
        try {
            playerService.updateEloAndRecord(playerId, opponentElo, hasWon);
            return "Player stats updated successfully";
        } catch (RuntimeException e) {
            return "Error: " + e.getMessage();
        }
    }

    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {
        // Return admin dashboard view
        return "";
    }

    @GetMapping("/tournaments/create")
    public String createTournamentPage(Model model) {
        // Return tournament creation form
        return "";
    }

    // @PostMapping("/tournaments/create")
    // public String createTournament(@ModelAttribute TournamentDTO tournamentDTO) {
    //     // Handle tournament creation
    // }

    @GetMapping("/tournaments/{id}/edit")
    public String updateTournamentPage(@PathVariable String id, Model model) {
        // Return tournament edit form
        return "";
    }

    // @PostMapping("/tournaments/{id}/edit")
    // public String updateTournament(@ModelAttribute TournamentDTO tournamentDTO) {
    //     // Handle tournament update
    // }

    @PostMapping("/tournaments/{id}/delete")
    public String deleteTournament(@PathVariable String id) {
        // Handle tournament deletion
        return "";
    }

    @GetMapping("/reports")
    public String viewReports(Model model) {
        // Show reports
        return "";
    }
}