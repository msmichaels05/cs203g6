package com.amateuraces.player;

import java.util.List;
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
     * Remove a book with the given id
     * Spring Data JPA does not return a value for delete operation
     * Cascading: removing a book will also remove all its associated reviews
     */
    @Override
    public void deletePlayer(Long id){
         players.deleteById(id);
    }
}