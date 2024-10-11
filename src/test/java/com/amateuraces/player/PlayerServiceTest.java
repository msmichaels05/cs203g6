package com.amateuraces.player;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PlayerServiceTest {
    
    @Mock
    private PlayerRepository players;

    @InjectMocks
    private PlayerServiceImpl playerService;
    
    
    @Test
    void addPlayer_SameName_ReturnNull(){
    // your code here
    Player player = new Player("The Same Name Exists");
    List<Player> samePlayer = new ArrayList<Player>();
    samePlayer.add(new Player("The Same Name Exists"));
    when(players.findByName(player.getName())).thenReturn(samePlayer);
    Player savedPlayer = playerService.addPlayer(player);
    assertNull(savedPlayer);
    verify(players).findByName(player.getName());
    }
    
//     /**
//      * 
//      * TODO: Activity 1 (Week 6)
//      * Write a test case: when adding a new book but the title already exists
//      * The test case should pass if BookServiceImpl.addBook(book)
//      * returns null (can't add book), otherwise it will fail.
//      * Remember to include suitable "verify" operations
//      * 
//      */
//     @Test
//     void addBook_SameTitle_ReturnNull(){
//         // your code here
//         Book book = new Book("The same title exists");
        
//         List<Book> sameTitles = new ArrayList<Book>();
//         sameTitles.add(new Book("The same title exists"));
//         when(books.findByTitle(book.getTitle())).thenReturn(sameTitles);
//         Book savedBook = bookService.addBook(book);
//         assertNull(savedBook);
//         verify(books).findByTitle(book.getTitle());                
//     }

//     @Test
//     void updateBook_NotFound_ReturnNull(){
//         Book book = new Book("Updated Title of Book");
//         Long bookId = 10L;
//         when(books.findById(bookId)).thenReturn(Optional.empty());
        
//         Book updatedBook = bookService.updateBook(bookId, book);
        
//         assertNull(updatedBook);
//         verify(books).findById(bookId);
    }
