package com.amateuraces.project.service;

import com.amateuraces.project.app.Player;
import com.amateuraces.project.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlayerService {

    @Autowired
    private PlayerRepository playerRepository;
    
    // to view DB - http://localhost:8080/h2-console
    public Player savePlayer(Player player) {
        return playerRepository.save(player);  // Save player to the database
    }
    public void updateEloAndRecord(Long playerId, int opponentElo, boolean hasWon) {
        // Find player by ID
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new RuntimeException("Player not found"));

        // Update player's ELO and win/loss record
        player.updateElo(opponentElo, hasWon);
        player.updateWinsAndLosses(hasWon);

        // Save updated player back to the repository
        playerRepository.save(player);
    }

}
