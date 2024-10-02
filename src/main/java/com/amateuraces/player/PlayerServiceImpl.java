package com.amateuraces.player;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

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
        Optional<Player> b = players.findById(id);
        if (b.isPresent())
            return b.get();
        else
            return null;
        
        
    }

    @Override
    public Player addPlayer(Player player) {
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
    }
}