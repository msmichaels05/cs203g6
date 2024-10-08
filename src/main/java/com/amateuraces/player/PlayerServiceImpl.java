package com.amateuraces.player;

import java.util.List;
<<<<<<< HEAD
import java.util.Optional;

import org.springframework.stereotype.Service;


/**
 * This implementation is meant for business logic, which could be added later
 * Currently, it does not have much in terms of the business logic yet
 */
@Service
public class PlayerServiceImpl implements PlayerService {
   
    private PlayerRepository players;
=======
import org.springframework.stereotype.Service;

/*This implementation is meant for business logic,which could be added later*Currently,it does not have much in terms of the business logic yet*/

@Service
public class PlayerServiceImpl implements PlayerService {

    private PlayerRepository players;

>>>>>>> 3e3de983188d74e9d50551f17476ca4dfd004347
    public PlayerServiceImpl(PlayerRepository players){

        this.players = players;
     
    }

    @Override
    public List<Player> listPlayers() {
        return players.findAll();
    }

<<<<<<< HEAD
    
    @Override
    public Player getPlayer(Long id){
        Optional<Player> b = players.findById(id);
        if (b.isPresent())
            return b.get();
        else
            return null;
        
        
    }
    
  
    @Override
    public Player addPlayer(Player player) {
        Player.setId(players.save(player));
        return player;
    }
    
    /**
     * TODO: Activity 1 - Implement the update method
     * This method should return the updated Player, or null if the given id is not found
     * 
     */
    @Override
    public Player updatePlayer(Long id, Player newPlayerInfo) {
   
        Optional<Player> existingPlayerOptional = players.findById(id);
    
        Player player = newPlayerInfo;
        Player.setId(id);
        return players.update(player)>0 ? player : null;
    }

    @Override
    public int deletePlayer(Long id){
        return players.deleteById(id);
=======
    @Override
    public Player getPlayer(Long id){
        return players.findById(id).orElse(null);
        
        
    }

    @Override
    public Player addPlayer(Player player) {
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
>>>>>>> 3e3de983188d74e9d50551f17476ca4dfd004347
    }
}