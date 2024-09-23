package com.amateuraces.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.amateuraces.project.service.PlayerService;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private PlayerService playerService;

    // Endpoint for admin to update player ELO and stats
    @PutMapping("/update-player-stats")
    public String updatePlayerStats(
            @RequestParam Long playerId, 
            @RequestParam int opponentElo, 
            @RequestParam boolean hasWon) {
        try {
            playerService.updateEloAndRecord(playerId, opponentElo, hasWon);
            return "Player stats updated successfully";
        } catch (RuntimeException e) {
            return "Error: " + e.getMessage();
        }
    }
}
