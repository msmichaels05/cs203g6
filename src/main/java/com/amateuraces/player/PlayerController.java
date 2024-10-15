package com.amateuraces.player;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.amateuraces.tournament.Tournament;
import com.amateuraces.user.*;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

@Controller
public class PlayerController {
    private PlayerRepository players;
    private UserRepository users;

    public PlayerController(PlayerRepository players, UserRepository users) {
        this.players = players;
        this.users = users;
    }

    // Display Player Registration Form
    @ResponseBody
    @GetMapping("/player/register")
    public String showPlayerRegistrationForm(@RequestParam("userId") Long userId, Model model) {
        // Fetch the user from the UserRepository by userId
        User user = users.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        // Ensure the User entity is managed by the persistence context
        user = users.saveAndFlush(user);

        // Create a new Player object and set the user
        Player player = new Player();
        player.setUser(user);  // Link player to the user

        // Add the player object to the model to bind with the form
        model.addAttribute("player", player);
        return "player_register";  // This should map to the player_register.html template
    }

    // Handle Player Registration Form Submission
    @ResponseBody
    @PostMapping("/player/register")
    public String registerPlayer(@Valid @ModelAttribute Player player, Model model) {
        // Ensure that the player has a linked user before saving
        if (player.getUser() == null) {
            model.addAttribute("error", "User must be linked before registering a player.");
            return "player_register";
        }

        // Ensure the User entity is managed by the persistence context
        User managedUser = users.findById(player.getUser().getId())
                .orElseThrow(() -> new UserNotFoundException(player.getUser().getId()));

        player.setUser(managedUser);  // Attach the managed user to the player

        // Save the player to the repository
        players.save(player);
        return "redirect:/home";  // Redirect to home page after successful player registration
    }

    @ResponseBody
    @GetMapping("/players")
    public List<Player> getAllPlayers() {
        return players.findAll();
    }

    /**
     * List all players in the system
     * 
     * @return list of all players
     */
    @ResponseBody
    @GetMapping("/users/{userId}/players")
    public Player getPlayerByUserId(@PathVariable(value = "userId") Long userId) {
        if (!users.existsById(userId)) {
            throw new PlayerNotFoundException(userId);
        }
        return players.findByUserId(userId);
    }
    @ResponseBody
    @PostMapping("/users/{userId}/players")
    public Player addPlayer(@PathVariable(value = "userId") Long userId, @Valid @RequestBody Player player) {
        return users.findById(userId).map(user -> {
            player.setUser(user);
            return players.save(player);
        }).orElseThrow(() -> new PlayerNotFoundException(userId));
    }
    @ResponseBody
    @PutMapping("/users/{userId}/players/{playerId}")
    public Player updatePlayer(@PathVariable(value = "userId") Long userId,
            @PathVariable(value = "playerId") Long playerId,
            @Valid @RequestBody Player newPlayer) {
        if (!users.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }

        return players.findByIdAndUserId(playerId, userId).map(player -> {
            player.setName(newPlayer.getName()); // Update the name
            player.setPhoneNumber(newPlayer.getPhoneNumber()); // Update the phone number
            player.setAge(newPlayer.getAge()); // Update the age
            player.setGender(newPlayer.getGender()); // Update the gender
            player.setElo(newPlayer.getElo()); // Update the ELO

            return players.save(player);
        }).orElseThrow(() -> new PlayerNotFoundException(playerId));
    }


    // @ResponseBody
    // @PostMapping("/users/{userId}/players")
    // public Player addPlayer(@PathVariable(value = "userId") Long userId, @Valid @RequestBody Player player) {
    //     return users.findById(userId).map(user -> {
    //         player.setUser(user);
    //         return players.save(player);
    //     }).orElseThrow(() -> new PlayerNotFoundException(userId));
    // }

    // @ResponseBody
    // @PostMapping("/users/{userId}/players/{playerId}/tournaments")
    // public Tournament registerForTournament(@PathVariable(value = "userId") Long userId,
    //         @PathVariable(value = "playerId") Long playerId,
    //         @Valid @RequestBody Tournament tournamentId) {
    //         return users.findById(userId).map(user -> {
    //             return players.findById(playerId).map(player -> {

    //             })
    //         })
    //     return "";
    // }


    // @ResponseBody
    // @PostMapping("/users/{userId}/players/{playerId}/tournaments")
    // public Tournament registerForTournament(@PathVariable(value = "userId") Long userId,
    //         @PathVariable(value = "playerId") Long playerId,
    //         @Valid @RequestBody Tournament tournament) {
    //         return 
    //     return "";
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