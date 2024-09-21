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
}
