package com.amateuraces.player;
<<<<<<< HEAD
=======

>>>>>>> a84a5a3c7149033df22ad0b28304fe880ce57d68
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
<<<<<<< HEAD
     * Return status of the delete
     * If it's 1: the player has been removed
     * If it's 0: the player does not exist
     * 
     * @param id
     * @return
     */
    int deletePlayer(Long id);
=======
     * Change method's signature: do not return a value for delete operation
     * @param id
     */
    void deletePlayer(Long id);
>>>>>>> a84a5a3c7149033df22ad0b28304fe880ce57d68
}