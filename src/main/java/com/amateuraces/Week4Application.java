package com.amateuraces;

import java.util.Arrays;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import com.amateuraces.highlight.Highlight;
import com.amateuraces.highlight.HighlightRepository;


@SpringBootApplication
public class Week4Application {

    public static void main(String[] args) {
        
        ApplicationContext ctx = SpringApplication.run(Week4Application.class, args);

        // Acquire the various instances we need
        JdbcTemplate template = ctx.getBean(JdbcTemplate.class);
        HighlightRepository repo = ctx.getBean(HighlightRepository.class);
        
        // Initialize the H2 database
        // Comment or remove this code if you want the DB to persist data
        template.execute("DROP TABLE IF EXISTS highlights");
        template.execute("CREATE TABLE highlights (" +
                         "\"year\" BIGINT NOT NULL, " +
                         "\"month\" BIGINT NOT NULL, " +
                         "PRIMARY KEY (\"year\", \"month\"))");

        List<Highlight> listHighlights = Arrays.asList(
            new Highlight(2024L, 10L), 
            new Highlight(2024L, 11L)
        );

        // listHighlights.forEach(repo::save);

        listHighlights.forEach(highlight -> {
            repo.save(highlight);
        });
    }
}