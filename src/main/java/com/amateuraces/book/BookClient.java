package com.amateuraces.book;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class BookClient {
    
    private RestTemplate template;
    
    BookClient(RestTemplateBuilder builder) {
        this.template = builder.build();
    }
    
    /**
     * Get a book with given id
     * 
     * @param URI
     * @param id
     * @return
     */
    public Book getBook(final String URI, final Long id) {
        final Book book = template.getForObject(URI + "/" + id, Book.class);
        return book;
    }

    /**
     * Add a new book
     * 
     * @param URI
     * @param newBook
     * @return
     */
    public Book addBook(final String URI, final Book newBook) {
        final Book returned = template.postForObject(URI, newBook, Book.class);
        return returned;
    }
    
}