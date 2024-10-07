package com.amateuraces;

import java.util.Arrays;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.amateuraces.player.*;
import com.amateuraces.client.RestTemplateClient;
import com.amateuraces.user.User;
import com.amateuraces.user.UserRepository;


@SpringBootApplication
public class Week4Application {

    public static void main(String[] args) {
        
        ApplicationContext ctx = SpringApplication.run(Week4Application.class, args);

        // JPA book repository init
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
            
        
        // // Test the RestTemplate client with authentication
        /**
         * TODO: Activity 3 (after class)
         * Uncomment the following code and test the changes
         * Here we use our own Rest client to test the service
         * Authentication info has been added int the RestTemplateClient.java
        //  */
        
        RestTemplateClient client = ctx.getBean(RestTemplateClient.class);
        System.out.println("[Add player]: " + client.addPlayer("http://localhost:8080/players", new Player("Spring in Actions")).getName());

        // Get the 1st book, obtain a HTTP response and print out the title of the book
        System.out.println("[Get player]: " + client.getPlayerEntity("http://localhost:8080/players", 1L).getBody().getName());
        
    }
    
}

