package com.amateuraces;

import java.util.Arrays;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import com.amateuraces.player.Player;
import com.amateuraces.player.PlayerRepository;

@SpringBootApplication
public class Week4Application {

    public static void main(String[] args) {
        
        ApplicationContext ctx = SpringApplication.run(Week4Application.class, args);

        // Acquire the various instances we need
        JdbcTemplate template = ctx.getBean(JdbcTemplate.class);
        PlayerRepository repo = ctx.getBean(PlayerRepository.class);
        
        // Initialize the H2 database
        // Comment or remove this code if you want the DB to persist data
        template.execute("DROP TABLE players IF EXISTS");
        template.execute("CREATE TABLE players(" +
                "id SERIAL PRIMARY KEY, name VARCHAR(255))"); // Change field names as necessary

        List<Player> listPlayers = Arrays.asList(
            new Player(1L, "name1"), // Adjust the constructor based on your Player class
            new Player(2L, "name2")  // Adjust the constructor based on your Player class
        );

        listPlayers.forEach(player -> {
            repo.save(player);
        });
    }
}

