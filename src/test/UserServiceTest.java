package csd.week6.book;

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

import com.amateuraces.player.PlayerRepository;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    
    @Mock
    private PlayerRepository playerRepository;

    @InjectMocks
    private PlayerServiceImpl playerService;
    
    
    @Test
    void addPlayer_NewName_ReturnSavedPlayer(){
        // arrange ***
        Player player = new Player("This is a New player");
        // mock the "findbytitle" operation
        when(Players.findByTitle(any(String.class))).thenReturn(new ArrayList<Player>());
        // mock the "save" operation 
        when(playerRepository.save(any(Player.class))).thenReturn(player);

        // act ***
        Player savedPlayer = playerService.addBook(player);
        
        // assert ***
        assertNotNull(savedPlayer);
        verify(playerRepository).findByName(Player.getName());
        verify(playerRepository).save(player);
    }
    /**
     * TODO: Activity 1 (Week 6)
     * Write a test case: when adding a new book but the title already exists
     * The test case should pass if BookServiceImpl.addBook(book)
     * returns null (can't add book), otherwise it will fail.
     * Remember to include suitable "verify" operations
     * 
     */
    @Test
    void addBook_SameTitle_ReturnNull(){
        // your code here
        
    }

    @Test
    void updateBook_NotFound_ReturnNull(){
        Book book = new Book("Updated Title of Book");
        Long bookId = 10L;
        when(books.findById(bookId)).thenReturn(Optional.empty());
        
        Book updatedBook = bookService.updateBook(bookId, book);
        
        assertNull(updatedBook);
        verify(books).findById(bookId);
    }

}