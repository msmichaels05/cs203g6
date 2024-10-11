package com.amateuraces.player;

import java.util.List;

import com.amateuraces.tournament.Tournament;

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

    Player registerForTournament(Long playerId, Tournament tournament);
}
    /**
     * Return status of the delete.
     * If it's 1: the player has been removed.
     * If it's 0: the player does not exist.
     * @param id the player ID
     * @return status of the delete operation
     */
    int deletePlayer(Long id);
}
