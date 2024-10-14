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
    private PlayerRepository playerRepository;

    @InjectMocks
    private PlayerServiceImpl playerService;
    
    
    @Test
    void addPlayer_NewName_ReturnSavedPlayer(){
        // arrange ***
        Player player = new Player("This is a New player");
        // mock the "findbytitle" operation
        when(playerRepository.findByName(any(String.class))).thenReturn(player);
        // mock the "save" operation 
        when(playerRepository.save(any(Player.class))).thenReturn(player);

        // act ***
        Player savedPlayer = playerService.addPlayer(player);
        
        // assert ***
        assertNotNull(savedPlayer);
        verify(playerRepository).findByName(player.getName());
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
    void addPlayer_SameName_ReturnNull(){
        // your code here
        Player player = new Player("The Same Title Exists");
        List<Player> sameNames = new ArrayList<Player>();
        sameNames.add(new Player("The Same Name Exists"));
        when(playerRepository.findByName(player.getName())).thenReturn(player);
        Player savedPlayer = playerService.addPlayer(player);
        assertNull(savedPlayer);
        verify(playerRepository).findByName(player.getName());
    }

    @Test
    void updatePlayer_NotFound_ReturnNull() {
        Player player = new Player("Updated Name of Player");
        Long playerId = 10L;
        when(playerRepository.findById(playerId)).thenReturn(Optional.empty());
        
        Player updatedPlayer = playerService.updatePlayer(playerId, player);
        
        assertNull(updatedPlayer);
        verify(playerRepository).findById(playerId);
    }

}