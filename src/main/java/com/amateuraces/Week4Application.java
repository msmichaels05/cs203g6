package com.amateuraces;

import java.util.Arrays;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import com.amateuraces.player.Player;
import com.amateuraces.admin.Admin;
import com.amateuraces.player.PlayerRepository;
import com.amateuraces.admin.AdminRepository;

@SpringBootApplication
public class Week4Application {

    public static void main(String[] args) {
        
        ApplicationContext ctx = SpringApplication.run(Week4Application.class, args);

        // Acquire the various instances we need
        JdbcTemplate template = ctx.getBean(JdbcTemplate.class);
        PlayerRepository playerRepo = ctx.getBean(PlayerRepository.class);
        AdminRepository adminRepo = ctx.getBean(AdminRepository.class);  // Corrected: added AdminRepository

        // Initialize the H2 database for players
        // Comment or remove this code if you want the DB to persist data
        template.execute("DROP TABLE IF EXISTS players");
        template.execute("CREATE TABLE players(" +
                "id SERIAL PRIMARY KEY, name VARCHAR(255))"); // Change field names as necessary

        List<Player> listPlayers = Arrays.asList(
            new Player(1L, "name1"), // Adjust the constructor based on your Player class
            new Player(2L, "name2")  // Adjust the constructor based on your Player class
        );

        listPlayers.forEach(player -> {
            playerRepo.save(player);
        });

        // Initialize the H2 database for admins
        template.execute("DROP TABLE IF EXISTS admins");
        template.execute("CREATE TABLE admins(" +
                "id SERIAL PRIMARY KEY, name VARCHAR(255), email VARCHAR(255), password VARCHAR(255))"); // Adjust field names as necessary

        List<Admin> listAdmins = Arrays.asList(
            new Admin(1L, "admin1", "admin1@example.com", "password1"), // Adjust the constructor based on your Admin class
            new Admin(2L, "admin2", "admin2@example.com", "password2")  // Adjust the constructor based on your Admin class
        );

        listAdmins.forEach(admin -> {
            adminRepo.save(admin);
        });
    }
}
