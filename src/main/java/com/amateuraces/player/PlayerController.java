package com.amateuraces.player;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class PlayerController {
    
    private final PlayerService playerService; // Make it final and use constructor injection
    
    // Constructor Injection
    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    /**
     * Get all players
     * @return List of players
     */
    @GetMapping("/players")
    public List<Player> viewPlayers() {
        return playerService.listPlayers(); // Assuming you have this method in PlayerService
    }

    /**
     * Add a new player with POST request to "/players"
     * @param player
     * @return the added player
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/players")
    public Player addPlayer(@RequestBody Player player) {
        return playerService.addPlayer(player);
    }
    

    // to be used
    // private PlayerService playerService;
    // private TournamentService tournamentService;

    // Constructor Injection

    @GetMapping("/players/dashboard")
    public String viewDashboard(Model model) {
        // Return player dashboard view
        return "";
    }

    @GetMapping("/players/tournaments")
    public String viewTournaments(Model model) {
        // List available tournaments
        return "";
    }

    @PostMapping("/players/tournaments/register")
    public String registerForTournament(@RequestParam String tournamentId) {
        // Register player for tournament
        return "";
    }

    @GetMapping("/players/matches")
    public String viewMatchSchedule(Model model) {
        // Show match schedule
        return "THIS IS TESYT";
    }

    @GetMapping("/players/profile")
    public String viewProfile(Model model) {
        // Show player profile
        return "";
    }

    // @PostMapping("/profile")
    // public String updateProfile(@ModelAttribute PlayerProfileDTO profileDTO) {
    //     // Update player profile
    // }

    @GetMapping("/players/notifications")
    public String viewNotifications(Model model) {
        // Show notifications
        return "";
    }
}