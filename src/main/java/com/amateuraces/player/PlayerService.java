package com.amateuraces.player;

import java.util.List;

public interface PlayerService {
    /**
     * Retrieve all players.
     * @return List of players.
     */
    List<Player> listPlayers();

    /**
     * Retrieve a player by ID.
     * @param id the player's ID
     * @return the player
     */
    Player getPlayer(Long id);

    /**
     * Return the newly added player.
     * @param player the player to add
     * @return the added player
     */
    Player addPlayer(Player player);

    /**
     * Return the updated player.
     * @param id the player ID
     * @param player the updated player details
     * @return the updated player
     */
    Player updatePlayer(Long id, Player player);

    /**
     * Return status of the delete.
     * If it's 1: the player has been removed.
     * If it's 0: the player does not exist.
     * @param id the player ID
     * @return status of the delete operation
     */
    int deletePlayer(Long id);
}
