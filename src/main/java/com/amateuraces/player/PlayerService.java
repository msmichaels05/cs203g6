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
     * Change method's signature: do not return a value for delete operation
     * @param id
     */
    void deletePlayer(Long id);
}
