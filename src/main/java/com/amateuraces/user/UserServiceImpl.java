package com.amateuraces.user;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.amateuraces.dtos.RegisterUserDto;
import com.amateuraces.player.Player;
import com.amateuraces.player.PlayerRepository;
import com.amateuraces.role.*;
import com.amateuraces.tournament.Tournament;
import com.amateuraces.tournament.TournamentRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class UserServiceImpl implements UserService {
/*This implementation is meant for business logic,which could be added later*Currently,it does not have much in terms of the business logic yet*/
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private TournamentRepository tournamentRepository;

    @Autowired 
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // public UserServiceImpl(UserRepository userRepository,PlayerRepository playerRepository){
    //     this.userRepository = userRepository;
    //     this.playerRepository = playerRepository;
    // }

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    //Tokenisation things

    public List<User> allUsers() {
        List<User> users = new ArrayList<>();

        userRepository.findAll().forEach(users::add);

        return users;
    }

    public User createAdministrator(RegisterUserDto input) {
        Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.ADMIN);

        if (optionalRole.isEmpty()) {
            return null;
        }

        User user = new User();
        user.setUsername(input.getUsername());
        user.setEmail(input.getEmail());
        user.setPassword(passwordEncoder.encode(input.getPassword()));
        user.setRole(optionalRole.get());

        return userRepository.save(user);
    }

    //-------------------------------------------

    @Override
    public List<User> listUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUser(Long id){
        return userRepository.findById(id).orElse(null);
    }   

    @Override
    public User addUser(User user) {
        Optional<User> sameUsernames = userRepository.findByUsername(user.getUsername());
        Optional<User> sameEmail = userRepository.findByEmail(user.getEmail());
        if (sameUsernames.isPresent() && sameEmail.isPresent()){
            throw new ExistingUserException("Username and Email has been used");
        }if (sameUsernames.isPresent()) {
            throw new ExistingUserException("Username has been used");
        } if(sameEmail.isPresent()){
            throw new ExistingUserException("Email has been used");
        }
        return userRepository.save(user);
    }

    @Override
    public User updateUser(Long id, User newUserInfo) {
        return userRepository.findById(id).map(user -> {user.setPassword(newUserInfo.getPassword());
            return userRepository.save(user);
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
        User user = userRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("User not found"));

        // Get the associated Player
        Player player = user.getPlayer();
        
        if (player != null) {
            // Remove player associations from tournaments
            for (Tournament tournament : player.getTournaments()) {
                tournament.removePlayer(player); // Remove player from each tournament
                tournamentRepository.save(tournament); // Save changes to the tournament
            }

            playerRepository.delete(player); //  delete the player entirely
        }

        // Now delete the user
        userRepository.delete(user); 
    }

    // public void deleteUser(Long id) {
    //     if (!userRepository.existsById(id)) {
    //         throw new UserNotFoundException(id);
    //     }
    //     userRepository.deleteById(id);
    // }

     @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
