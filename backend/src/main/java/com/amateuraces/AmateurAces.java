package com.amateuraces;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

<<<<<<< HEAD
import com.amateuraces.highlight.Highlight;
import com.amateuraces.highlight.HighlightRepository;
=======
>>>>>>> 9a18cd91b4908f622a3c6165399a34e45d7d628a
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