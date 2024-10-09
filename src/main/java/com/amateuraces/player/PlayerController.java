package com.amateuraces.player;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// import com.amateuraces.book.Book;
// import com.amateuraces.book.BookNotFoundException;

import java.util.List;

@RestController
@RequestMapping("/players")
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    // Get all players
    @GetMapping("/players")
    public List<Player> listPlayers() {
        return playerService.listPlayers();
    }

    // Get a player by ID
    @GetMapping("/players/{id}")
    public Player getPlayer(@PathVariable Long id) {  //or should i return ResponseEntity<Player> type instead?
        Player player = playerService.getPlayer(id);
        // Need to handle "book not found" error using proper HTTP status code
        // In this case it should be HTTP 404
        if(player == null) throw new PlayerNotFoundException(id);
        return playerService.getPlayer(id);
    }

    // Add a new player
    @PostMapping
    public ResponseEntity<Player> addPlayer(@RequestBody Player player) {
        Player newPlayer = playerService.addPlayer(player);
        return new ResponseEntity<>(newPlayer, HttpStatus.CREATED);
    }

    // Update an existing player
    @PutMapping("/books/{id}")
    public Player updatePlayer(@PathVariable Long id, @RequestBody Player player) { //Return ResponseEntity<Player> type instead?
        Player updatedPlayer = playerService.updatePlayer(id, player);
        if (player == null) throw new PlayerNotFoundException(id);

        return updatedPlayer;
    }

    // Delete a player by ID
    @DeleteMapping("/players/{id}")
    public void deletePlayer(@PathVariable Long id) {
        if (playerService.deletePlayer(id) == 0) throw new PlayerNotFoundException(id);
    }

}
