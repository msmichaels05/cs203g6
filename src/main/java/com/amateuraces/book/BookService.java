package com.amateuraces.book;

import java.util.List;


public interface BookService {
    List<Book> listBooks();
    Book getBook(Long id);

    /**
     * Return the newly added book
     */
    Book addBook(Book book);

    /**
     * Return the updated book
     * @param id
     * @param book
     * @return
     */
    Book updateBook(Long id, Book book);

    /**
     * Return status of the delete
     * If it's 1: the book has been removed
     * If it's 0: the book does not exist
     * @param id
     * @return 
     */
    int deleteBook(Long id);
}