package com.amateuraces.player;

import java.util.List;
<<<<<<< HEAD
import java.util.Optional;
=======
>>>>>>> a84a5a3c7149033df22ad0b28304fe880ce57d68

import org.springframework.stereotype.Service;

/*This implementation is meant for business logic,which could be added later*Currently,it does not have much in terms of the business logic yet*/

@Service
public class PlayerServiceImpl implements PlayerService {

<<<<<<< HEAD
    private PlayerRepository players;

    public PlayerServiceImpl(PlayerRepository players){

        this.players = players;
     
=======
    private final PlayerRepository players;

    public PlayerServiceImpl(PlayerRepository players){
        this.players = players;
>>>>>>> a84a5a3c7149033df22ad0b28304fe880ce57d68
    }

    @Override
    public List<Player> listPlayers() {
        return players.findAll();
    }

    @Override
    public Player getPlayer(Long id){
<<<<<<< HEAD
        Optional<Player> b = players.findById(id);
        if (b.isPresent())
            return b.get();
        else
            return null;
        
        
=======
        return players.findById(id).orElse(null);
>>>>>>> a84a5a3c7149033df22ad0b28304fe880ce57d68
    }

    @Override
    public Player addPlayer(Player player) {
<<<<<<< HEAD
        player.setId(players.save(player));
        return player;
    }

    /*TODO:Activity 1-

    Implement the
    update method*
    This method should return
    the updated Player,or null if
    the given
    id is
    not found**/

    @Override
    public Player updatePlayer(Long id, Player newPlayerInfo) {
   
        Optional<Player> existingPlayerOptional = players.findById(id);
    
        Player player = newPlayerInfo;
        player.setId(id);
        return players.update(player)>0 ? player : null;
    }

    @Override
    public int deletePlayer(Long id){
        return players.deleteById(id);
=======
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
>>>>>>> a84a5a3c7149033df22ad0b28304fe880ce57d68
    }
}