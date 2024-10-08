package com.amateuraces;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.amateuraces.player.*;
import com.amateuraces.client.RestTemplateClient;
import com.amateuraces.user.User;
import com.amateuraces.user.UserRepository;
import com.amateuraces.admin.*;
import com.amateuraces.tournament.Tournament;
import com.amateuraces.tournament.TournamentRepository; // Import your TournamentRepository

@SpringBootApplication
public class AmateurAces {

    public static void main(String[] args) {
        
        ApplicationContext ctx = SpringApplication.run(AmateurAces.class, args);

        // JPA player repository init
        PlayerRepository players = ctx.getBean(PlayerRepository.class);
        System.out.println("[Add player]: " + players.save(new Player("Tim")));
        System.out.println("[Add player]: " + players.save(new Player("Gone With The Wind")));

        // JPA user repository init
        UserRepository users = ctx.getBean(UserRepository.class);
        BCryptPasswordEncoder encoder = ctx.getBean(BCryptPasswordEncoder.class);
        System.out.println("[Add user]: " + users.save(
            new User("admin", encoder.encode("goodpassword"), "ROLE_ADMIN")).getUsername());
        System.out.println("[Add user]: " + users.save(
            new User("betatester" , encoder.encode("betatester"), "ROLE_USER")).getUsername());
            
        // JPA admin repository init
        AdminRepository admins = ctx.getBean(AdminRepository.class);
        // Assuming a default password is to be provided
        System.out.println("[Add admin]: " + admins.save(new Admin(null, "John Doe", "john.doe@example.com", "defaultPassword")).getName());
        System.out.println("[Add admin]: " + admins.save(new Admin(null, "Jane Smith", "jane.smith@example.com", "defaultPassword")).getName());

        // JPA tournament repository init
        TournamentRepository tournaments = ctx.getBean(TournamentRepository.class);
        
        // Creating players list 
        List<Player> playersList = new ArrayList<>();
        playersList.add(new Player("Player 1")); // Add players as needed
        playersList.add(new Player("Player 2")); // Add players as needed

        // Initialize tournaments with the appropriate parameters
        System.out.println("[Add tournament]: " + tournaments.save(new Tournament(null, "Spring Championship", "2024-04-01", "2024-04-30", playersList, "Must be a registered player", "Scheduled")).getName());
        System.out.println("[Add tournament]: " + tournaments.save(new Tournament(null, "Summer Series", "2024-06-01", "2024-06-30", new ArrayList<>(), "Open to all", "Scheduled")).getName());

        // Test the RestTemplate client with authentication
        /**
         * TODO: Activity 3 (after class)
         * Uncomment the following code and test the changes
         * Here we use our own Rest client to test the service
         * Authentication info has been added in the RestTemplateClient.java
        //  */
        
        RestTemplateClient client = ctx.getBean(RestTemplateClient.class);
        System.out.println("[Add player]: " + client.addPlayer("http://localhost:8080/players", new Player("Spring in Actions")).getName());

        // Get the 1st player, obtain a HTTP response and print out the name of the player
        System.out.println("[Get player]: " + client.getPlayerEntity("http://localhost:8080/players", 1L).getBody().getName());
        
    }
}
