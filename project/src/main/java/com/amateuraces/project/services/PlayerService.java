package com.amateuraces.project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amateuraces.project.entity.Player;
import com.amateuraces.project.repository.PlayerRepository;

import jakarta.persistence.EntityNotFoundException;

// import java.util.ArrayList;
// import java.util.List;


@Service
public class PlayerService {

    @Autowired
    private PlayerRepository playerRepository;
    
    // to view DB - http://localhost:8080/h2-console
    public Player savePlayer(Player player) {
        return playerRepository.save(player);  // Save player to the database
    }

    public void updateEloAndRecord(String playerId, int opponentElo, boolean hasWon) {
        // Find player by ID
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new EntityNotFoundException("No player with that ID"));

        // Update player's ELO and win/loss record
        player.updateElo(opponentElo, hasWon);
        player.updateWinsAndLosses(hasWon);

        // Save updated player back to the repository
        playerRepository.save(player);
    }

    // possible methods?

    // // void updatePlayerProfile(PlayerProfileDTO profileDTO){}
    // List<Player> getRegisteredTournaments(String playerId){
    //     return new ArrayList<>();
    // }

    // List<Player> viewMatchSchedule(String playerId){
    //     return new ArrayList<>();
    // }

}
