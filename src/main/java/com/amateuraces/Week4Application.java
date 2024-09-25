package com.amateuraces;

import java.util.Arrays;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;


import com.amateuraces.book.*;

@SpringBootApplication
public class Week4Application {

	public static void main(String[] args) {
		
		ApplicationContext ctx = SpringApplication.run(Week4Application.class, args);

        // acquire the various instances we need
        JdbcTemplate template = ctx.getBean(JdbcTemplate.class);
        BookRepository repo = ctx.getBean(BookRepository.class);
        
        // initialize the H2 database
        // comment or remove this code if you want the DB to persist data
        template.execute("DROP TABLE books IF EXISTS");
        template.execute("CREATE TABLE books(" +
                "id SERIAL PRIMARY KEY, title VARCHAR(255))");

        List<Book> listBooks = Arrays.asList(
            new Book("The Big Short"),
            new Book("Gone With The Wind")
        );

        listBooks.forEach(book -> {
            repo.save(book);
        });

    }
    
}
