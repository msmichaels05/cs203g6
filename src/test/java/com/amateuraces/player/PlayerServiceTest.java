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
    void addPlayer_NewName_ReturnSavedPlayer(){
        
        Player player = new Player("This is a New Player");
        when(players.save(any(Player.class))).thenReturn(player);
        Player savedPlayer = playerService.addPlayer(player);
        
        assertNotNull(savedPlayer);
        assertEquals(player.getName(),savedPlayer.getName());
        verify(players).save(player);
    }

    @Test
    void updatePlayer_NotFound_ReturnNull(){
        Player player = new Player("Updated Name of Player");
        Long playerID = 10L;
        when(players.findById(playerID)).thenReturn(Optional.empty());
        
        Player updatedPlayer = playerService.updatePlayer(playerID, player);
        
        assertNull(updatedPlayer);
        verify(players).findById(playerID);
    }

    // @Test
    // void addPlayer_SameName_ReturnNull(){
    // // your code here
    // Player player = new Player("The Same Name Exists");
    // List<Player> samePlayer = new ArrayList<Player>();
    // samePlayer.add(new Player("The Same Name Exists"));
    // when(players.findByName(player.getName())).thenReturn(samePlayer);
    // Player savedPlayer = playerService.addPlayer(player);
    // assertNull(savedPlayer);
    // verify(players).findByName(player.getName());
    // }
    
    }