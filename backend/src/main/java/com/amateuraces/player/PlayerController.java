package com.amateuraces.player;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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


    /**
     * Delete a player by player ID
     * 
     * @param userId The ID of the user who owns the player
     * @param playerId The ID of the player to be deleted
     * @return A confirmation message
     */
    @ResponseBody
    @DeleteMapping("/users/{userId}/players/{playerId}")
    public String deletePlayer(@PathVariable(value = "userId") Long userId,
                               @PathVariable(value = "playerId") Long playerId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);  // Ensure user exists before deleting player
        }

        return playerRepository.findByIdAndUserId(playerId, userId).map(player -> {
            playerRepository.delete(player);  // Delete the player
            return "Player deleted successfully";  // Return confirmation message
        }).orElseThrow(() -> new PlayerNotFoundException(playerId));  // Error if player not found
    }
}