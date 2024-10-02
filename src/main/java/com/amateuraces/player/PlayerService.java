package com.amateuraces.player;

public interface PlayerService {
    List<Book> listBooks();
    String Name(Long id);
    String addBook(Book book);
    String updateBook(Long id, Book book);

    /**
     * Change method's signature: do not return a value for delete operation
     * @param id
     */
    void deleteBook(Long id);
}
