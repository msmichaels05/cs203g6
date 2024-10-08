package com.amateuraces.player;

import java.util.List;

import jakarta.validation.Valid;


import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.amateuraces.book.BookService;


@RestController
public class PlayerController {
    private PlayerService playerService;
    
    public PlayerController(PlayerService ps){
        this.playerService = ps ; 
    }

        /**
     * List all players in the system
     * @return list of all players
     */
     @GetMapping("/players")
        public List<Player> getPlayers(){
            return playerService.listPlayers();
        }

    /**
     * Search for player with the given id
     * If there is no player with the given "id", throw a PlayerNotFoundException
     * @param id
     * @return player with the given id
     */
    @GetMapping("/players/{id}")
    public Player getPlayer(@PathVariable Long id){
        Player player = playerService.getPlayer(id);

        // Need to handle "player not found" error using proper HTTP status code
        // In this case it should be HTTP 404
        if(player == null) throw new PlayerNotFoundException(id);
        return playerService.getPlayer(id);

    }

 /**
     * Add a new player with POST request to "/players"
     * Note the use of @RequestBody
     * @param player
     * @return list of all players
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/players")
    public Player addPlayer(@RequestBody Player player){
        return playerService.addPlayer(player);
    }

        /**
     * If there is no player with the given "id", throw a PlayerNotFoundException
     * @param id
     * @param newPlayerInfo
     * @return the updated, or newly added book
     */
    @PutMapping("/players/{id}")
    public Player updatPlayer(@PathVariable Long id, @Valid @RequestBody Player newPlayerInfo){
        Player player = playerService.updatePlayer(id, newPlayerInfo);
        if(player == null) throw new PlayerNotFoundException(id);
        
        return player;
    }

        /**
     * Remove a player with the DELETE request to "/players/{id}"
     * If there is no player with the given "id", throw a PlayerNotFoundException
     * @param id
     */
    @DeleteMapping("/players/{id}")
    public void deletePlayer(@PathVariable Long id){
        try{
            playerService.deletePlayer(id);
         }catch(EmptyResultDataAccessException e) {
            throw new PlayerNotFoundException(id);
         }
    }


    // to be used
    // private PlayerService playerService;
    // private TournamentService tournamentService;

    // Constructor Injection

<<<<<<< HEAD
    private PlayerService playerService;

    public PlayerController(PlayerService ps) {
        playerService = ps;
    }

    @GetMapping("/dashboard")
    public String viewDashboard(Model model) {
        // Return player dashboard view
        return "";
    }

    @GetMapping("/tournaments")
    public String viewTournaments(Model model) {
        // List available tournaments
        return "";
    }

    @PostMapping("/tournaments/register")
    public String registerForTournament(@RequestParam String tournamentId) {
        // Register player for tournament
        return "";
    }

    @GetMapping("/matches")
    public String viewMatchSchedule(Model model) {
        // Show match schedule
        return "THIS IS TEST";
    }

    @GetMapping("/profile")
    public String viewProfile(Model model) {
        // Show player profile
        return "";
    }

    // @PostMapping("/profile")
    // public String updateProfile(@ModelAttribute PlayerProfileDTO profileDTO) {
    //     // Update player profile
=======
    // @GetMapping("/dashboard")
    // public String viewDashboard(Model model) {
    //     // Return player dashboard view
    //     return "";
>>>>>>> 3e3de983188d74e9d50551f17476ca4dfd004347
    // }

    // @GetMapping("/tournaments")
    // public String viewTournaments(Model model) {
    //     // List available tournaments
    //     return "";
    // }

    // @PostMapping("/tournaments/register")
    // public String registerForTournament(@RequestParam String tournamentId) {
    //     // Register player for tournament
    //     return "";
    // }

    // @GetMapping("/matches")
    // public String viewMatchSchedule(Model model) {
    //     // Show match schedule
    //     return "THIS IS TESYT";
    // }

    // @GetMapping("/profile")
    // public String viewProfile(Model model) {
    //     // Show player profile
    //     return "";
    // }

    // // @PostMapping("/profile")
    // // public String updateProfile(@ModelAttribute PlayerProfileDTO profileDTO) {
    // //     // Update player profile
    // // }

    // @GetMapping("/notifications")
    // public String viewNotifications(Model model) {
    //     // Show notifications
    //     return "";
    // }
}