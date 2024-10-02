package com.amateuraces.book;

import java.util.List;
import java.util.Optional;

/**
 * Data access object - work with the persistent database
 */
public interface BookRepository {
    Long save(Book book);
    int update(Book book);
    int deleteById(Long id);
    List<Book> findAll();

    // Using Optional - the return value of this method may contain a null value
    Optional<Book> findById(Long id);
 
}