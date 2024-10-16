package com.amateuraces.user;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amateuraces.player.Player;
import com.amateuraces.player.PlayerRepository;
import com.amateuraces.tournament.Tournament;
import com.amateuraces.tournament.TournamentRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class UserServiceImpl implements UserService {
/*This implementation is meant for business logic,which could be added later*Currently,it does not have much in terms of the business logic yet*/
    @Autowired
    private UserRepository users;
    @Autowired
    private PlayerRepository players;
    @Autowired
    private TournamentRepository tournamentRepository;

    // public UserServiceImpl(UserRepository users,PlayerRepository players){
    //     this.users = users;
    //     this.players = players;
    // }

    @Override
    public List<User> listUsers() {
        return users.findAll();
    }

    @Override
    public User getUser(Long id){
        return users.findById(id).orElse(null);
    }   

    @Override
    public User addUser(User user) {
        Optional<User> sameUsernames = users.findByUsername(user.getUsername());
        Optional<User> sameEmail = users.findByEmail(user.getEmail());
        if (sameUsernames.isPresent() || sameEmail.isPresent()) {
            return null; // or throw an exception
        }
        return users.save(user);
    }

    @Override
    public User updateUser(Long id, User newUserInfo) {
        return users.findById(id).map(user -> {user.setPassword(newUserInfo.getPassword());
            return users.save(user);
        }).orElse(null);
    }

    /**
     * Remove a player with the given id
     * Spring Data JPA does not return a value for delete operation
     * Cascading: removing a player will also remove all its associated reviews
     */
    @Transactional
    @Override
    public void deleteUser(Long id){
        User user = users.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("User not found"));

        // Get the associated Player
        Player player = user.getPlayer();
        
        if (player != null) {
            // Remove player associations from tournaments
            for (Tournament tournament : player.getTournaments()) {
                tournament.removePlayer(player); // Remove player from each tournament
                tournamentRepository.save(tournament); // Save changes to the tournament
            }

            players.delete(player); //  delete the player entirely
        }

        // Now delete the user
        users.delete(user); 
    }

     @Override
    public Optional<User> findByUsername(String username) {
        return users.findByUsername(username);
    }
}
