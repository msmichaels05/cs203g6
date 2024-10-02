package com.amateuraces.player;
import java.util.List;

public interface PlayerService {
    List<Player> listPlayers();

    Player getPlayer(Long id);

    /**
     * Return the newly added player
     */
    Player addPlayer(Player player);

    /**
     * Return the updated player
     * 
     * @param id
     * @param player
     * @return
     */
    Player updatePlayer(Long id, Player player);

    /**
     * Return status of the delete
     * If it's 1: the player has been removed
     * If it's 0: the player does not exist
     * 
     * @param id
     * @return
     */
    int deletePlayer(Long id);
}