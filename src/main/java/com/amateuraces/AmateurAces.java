package com.amateuraces;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.amateuraces.highlight.Highlight;
import com.amateuraces.highlight.HighlightRepository;
import com.amateuraces.match.MatchRepository;
import com.amateuraces.player.Player;
import com.amateuraces.player.PlayerRepository;
import com.amateuraces.tournament.Tournament;
import com.amateuraces.tournament.TournamentRepository;
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
            new User("admin", encoder.encode("goodpassword"), "ROLE_ADMIN")).getUsername());
        System.out.println("[Add user]: " + users.save(
            new User("betatester" , encoder.encode("betatester"), "ROLE_USER")).getUsername());

        // JPA Highlight repository initialization
        HighlightRepository highlights = ctx.getBean(HighlightRepository.class);
        
        System.out.println("[Add highlight]: " + highlights.save(new Highlight("TESTING")));

        //JPA Match repository initialization
        MatchRepository match = ctx.getBean(MatchRepository.class);

        //JPA Tournament repository initialization
        TournamentRepository tournament = ctx.getBean(TournamentRepository.class);
        
    }
    
}

