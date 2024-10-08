package com.amateuraces;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.amateuraces.player.Player;
import com.amateuraces.player.PlayerRepository;
import com.amateuraces.client.RestTemplateClient;
import com.amateuraces.user.User;
import com.amateuraces.user.UserRepository;

@SpringBootApplication
public class Week4Application {

    public static void main(String[] args) {
        
        ApplicationContext ctx = SpringApplication.run(Week4Application.class, args);

        // JPA Player repository initialization
        PlayerRepository playerRepository = ctx.getBean(PlayerRepository.class);
        System.out.println("[Add player]: " + playerRepository.save(new Player("Tim")).getName());
        System.out.println("[Add player]: " + playerRepository.save(new Player("Gone With The Wind")).getName());

        // JPA User repository initialization
        UserRepository userRepository = ctx.getBean(UserRepository.class);
        BCryptPasswordEncoder encoder = ctx.getBean(BCryptPasswordEncoder.class);
        System.out.println("[Add user]: " + userRepository.save(
            new User("admin", encoder.encode("goodpassword"), "ROLE_ADMIN")).getUsername());
        System.out.println("[Add user]: " + userRepository.save(
            new User("betatester", encoder.encode("betatester"), "ROLE_USER")).getUsername());

        // Test the RestTemplate client with authentication
        RestTemplateClient client = ctx.getBean(RestTemplateClient.class);
        System.out.println("[Add player via RestTemplate]: " + client.addPlayer(
            "http://localhost:8080/players", new Player("Spring in Actions")).getName());

        // Get the first player via RestTemplate and print out the name
        System.out.println("[Get player via RestTemplate]: " + client.getPlayerEntity(
            "http://localhost:8080/players", 1L).getBody().getName());
    }
}
