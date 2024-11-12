package com.amateuraces;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.amateuraces.user.User;
import com.amateuraces.user.UserRepository;


@SpringBootApplication
public class AmateurAces {

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(AmateurAces.class, args);

        // JPA user repository init
        UserRepository users = ctx.getBean(UserRepository.class);
        BCryptPasswordEncoder encoder = ctx.getBean(BCryptPasswordEncoder.class);
        System.out.println("[Add user]: " + users.save(
            new User("admin@helpdesk.com","admin", encoder.encode("goodpassword"), "ROLE_ADMIN")).getUsername());
    }
}