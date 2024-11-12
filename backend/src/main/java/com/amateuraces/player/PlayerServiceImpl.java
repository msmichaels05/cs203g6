package com.amateuraces.player;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
public class PlayerServiceImpl implements PlayerService {

    private PlayerRepository playerRepository;

    public PlayerServiceImpl(PlayerRepository playerRepository){
        this.playerRepository = playerRepository;
    }

    @Override
    public List<Player> listPlayers() {
        return playerRepository.findAll();
    }

    @Override
    public Player getPlayer(Long id){
        return playerRepository.findById(id).orElse(null);
    }

    @Transactional
    @Override
    public Player addPlayer(Player player) {
        Optional<Player> samePhoneNumber = playerRepository.findByPhoneNumber(player.getPhoneNumber());
        if (samePhoneNumber.isPresent()) {
            return null;
        }
        return playerRepository.save(player);
    }

    @Override
    public Player updatePlayer(Long id, Player newPlayerInfo) {
        return playerRepository.findById(id).map(player -> {
            player.setName(newPlayerInfo.getName());
            player.setGender(newPlayerInfo.getGender());
            player.setAge(newPlayerInfo.getAge());
            player.setElo(newPlayerInfo.getElo());
            return playerRepository.save(player);
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
    if (!playerRepository.existsById(id)) {
        throw new PlayerNotFoundException(id);
    }
    
    // If the player exists, delete them
    playerRepository.deleteById(id);
    }

    @Override
    public Player findByUserId(Long userId) {
        return playerRepository.findByUserId(userId);
    }
}