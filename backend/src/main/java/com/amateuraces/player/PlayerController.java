package com.amateuraces.player;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.amateuraces.user.ExistingUserException;
import com.amateuraces.user.User;
import com.amateuraces.user.UserNotFoundException;
import com.amateuraces.user.UserRepository;

import jakarta.validation.Valid;

@Controller
public class PlayerController {
    private PlayerRepository playerRepository;
    private UserRepository userRepository;

    public PlayerController(PlayerRepository playerRepository, UserRepository userRepository) {
        this.playerRepository = playerRepository;
        this.userRepository = userRepository;
    }
    
    @ResponseBody
    @GetMapping("/players")
    public List<Player> listPlayers() {
        return playerRepository.findAll();
    }

    /**
     * List all players in the system
     * 
     * @return list of all players
     */
    @ResponseBody
    @GetMapping("/users/{userId}/players")
    public Player getPlayerByUserId(@PathVariable(value = "userId") Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new PlayerNotFoundException(userId);
        }
        return playerRepository.findByUserId(userId);
    }

    @ResponseBody
    @PostMapping("/users/{userId}/players")
    public Player addPlayer(@PathVariable(value = "userId") Long userId, @Valid @RequestBody Player player) {
        // Retrieve the user and check for existence
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new PlayerNotFoundException(userId));
    
        player.setUser(user);
    
        // Check for duplicates based on user association
        if (playerRepository.findOptionalByUserId(userId).isPresent()) {
                throw new ExistingUserException("Player already exists for this user");
        }
        // Check for duplicates by phone number or name
        if (playerRepository.findByPhoneNumber(player.getPhoneNumber()).isPresent() || 
            playerRepository.findByName(player.getName()).isPresent()) {
            throw new ExistingUserException("Player with this phone number or name already exists");
        }
    
        // Save and return the new player
        return playerRepository.save(player);
    }
    

    @ResponseBody
    @PutMapping("/users/{userId}/players/{playerId}")
    public Player updatePlayer(@PathVariable(value = "userId") Long userId,
            @PathVariable(value = "playerId") Long playerId,
            @Valid @RequestBody Player newPlayer) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }

        return playerRepository.findByIdAndUserId(playerId, userId).map(player -> {
            player.setName(newPlayer.getName()); // Update the name
            player.setPhoneNumber(newPlayer.getPhoneNumber()); // Update the phone number
            player.setAge(newPlayer.getAge()); // Update the age
            player.setGender(newPlayer.getGender()); // Update the gender
            player.setElo(newPlayer.getElo()); // Update the ELO

            return playerRepository.save(player);
        }).orElseThrow(() -> new PlayerNotFoundException(playerId));
    }

    // @ResponseBody
    // @PostMapping("/users/{userId}/players")
    // public Player addPlayer(@PathVariable(value = "userId") Long userId, @Valid
    // @RequestBody Player player) {
    // return users.findById(userId).map(user -> {
    // player.setUser(user);
    // return players.save(player);
    // }).orElseThrow(() -> new PlayerNotFoundException(userId));
    // }

    // @ResponseBody
    // @PostMapping("/users/{userId}/players/{playerId}/tournaments")
    // public Tournament registerForTournament(@PathVariable(value = "userId") Long
    // userId,
    // @PathVariable(value = "playerId") Long playerId,
    // @Valid @RequestBody Tournament tournamentId) {
    // return users.findById(userId).map(user -> {
    // return players.findById(playerId).map(player -> {

    // })
    // })
    // return "";
    // }

    // @ResponseBody
    // @PostMapping("/users/{userId}/players/{playerId}/tournaments")
    // public Tournament registerForTournament(@PathVariable(value = "userId") Long
    // userId,
    // @PathVariable(value = "playerId") Long playerId,
    // @Valid @RequestBody Tournament tournament) {
    // return
    // return "";
    // }

    /**
     * Search for player with the given id
     * If there is no player with the given "id", throw a PlayerNotFoundException
     * 
     * @param id
     * @return player with the given id
     */
    // @GetMapping("/players/{id}")
    // public Player getPlayer(@PathVariable Long id){
    // Player player = players.getPlayer(id);

    // // Need to handle "player not found" error using proper HTTP status code
    // // In this case it should be HTTP 404
    // if(player == null) throw new PlayerNotFoundException(id);
    // return playerService.getPlayer(id);

    // }

    // /**
    // * Add a new player with POST request to "/players"
    // * Note the use of @RequestBody
    // * @param player
    // * @return list of all players
    // */
    // @ResponseStatus(HttpStatus.CREATED)
    // @PostMapping("/players")
    // public Player addPlayer(@RequestBody Player player){
    // return playerService.addPlayer(player);
    // }

    // /**
    // * If there is no player with the given "id", throw a PlayerNotFoundException
    // * @param id
    // * @param newPlayerInfo
    // * @return the updated, or newly added book
    // */
    // @PutMapping("/players/{id}")
    // public Player updatePlayer(@PathVariable Long id, @Valid @RequestBody Player
    // newPlayerInfo){
    // Player player = playerService.updatePlayer(id, newPlayerInfo);
    // if(player == null) throw new PlayerNotFoundException(id);

    // return player;
    // }

    // /**
    // * Remove a player with the DELETE request to "/players/{id}"
    // * If there is no player with the given "id", throw a PlayerNotFoundException
    // * @param id
    // */
    // @DeleteMapping("/players/{id}")
    // public void deletePlayer(@PathVariable Long id){
    // try{
    // playerService.deletePlayer(id);
    // }catch(EmptyResultDataAccessException e) {
    // throw new PlayerNotFoundException(id);
    // }
    // }

    // to be used
    // private PlayerService playerService;
    // private TournamentService tournamentService;

    // Constructor Injection

    // @GetMapping("/dashboard")
    // public String viewDashboard(Model model) {
    // // Return player dashboard view
    // return "";
    // }

    // @GetMapping("/tournaments")
    // public String viewTournaments(Model model) {
    // // List available tournaments
    // return "";
    // }

    // @GetMapping("/matches")
    // public String viewMatchSchedule(Model model) {
    // // Show match schedule
    // return "THIS IS TESYT";
    // }

    // @GetMapping("/profile")
    // public String viewProfile(Model model) {
    // // Show player profile
    // return "";
    // }

    // // @PostMapping("/profile")
    // // public String updateProfile(@ModelAttribute PlayerProfileDTO profileDTO) {
    // // // Update player profile
    // // }

    // @GetMapping("/notifications")
    // public String viewNotifications(Model model) {
    // // Show notifications
    // return "";
    // }
}