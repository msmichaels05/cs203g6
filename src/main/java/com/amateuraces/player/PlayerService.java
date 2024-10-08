package com.amateuraces.player;

<<<<<<< HEAD
public interface PlayerService {
    List<Book> listBooks();
    String Name(Long id);
    String addBook(Book book);
    String updateBook(Long id, Book book);
=======
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
>>>>>>> 3e3de983188d74e9d50551f17476ca4dfd004347

    /**
     * Change method's signature: do not return a value for delete operation
     * @param id
     */
<<<<<<< HEAD
    void deleteBook(Long id);
}
=======
    void deletePlayer(Long id);
}
>>>>>>> 3e3de983188d74e9d50551f17476ca4dfd004347
