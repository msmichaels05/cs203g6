package com.amateuraces.player;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.amateuraces.tournament.Tournament;
import com.amateuraces.user.*;

import jakarta.persistence.EntityNotFoundException;

/*This implementation is meant for business logic,which could be added later*Currently,it does not have much in terms of the business logic yet*/

@Service
public class PlayerServiceImpl implements PlayerService {

    private PlayerRepository players;

    public PlayerServiceImpl(PlayerRepository players){
        this.players = players;
    }

    @Override
    public List<Player> listPlayers() {
        return players.findAll();
    }

    @Override
    public Player getPlayer(Long id){
        return players.findById(id).orElse(null);
    }

    @Override
    public Player addPlayer(Player player) {
        if (players.findByPhoneNumber(player.getPhoneNumber()).isPresent() ||
            players.findByName(player.getName()).isPresent()) {
            return null;
        }
        return players.save(player);
    }
    
    @Override
    public Player updatePlayer(Long id, Player newPlayerInfo) {
        return players.findById(id).map(player -> {player.setName(newPlayerInfo.getName());
            return players.save(player);
        }).orElse(null);
    }

    /**
     * Remove a player with the given id
     * Spring Data JPA does not return a value for delete operation
     * Cascading: removing a player will also remove all its associated reviews
     */
    @Override
    public void deletePlayer(Long id){
    // Check if the player exists before attempting to delete
    if (!players.existsById(id)) {
        throw new PlayerNotFoundException(id);
    }
    
    // If the player exists, delete them
    players.deleteById(id);
    }

    @Override
    public Player findByUserId(Long userId) {
        return players.findByUserId(userId);
    }
}