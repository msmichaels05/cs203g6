package com.amateuraces;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.amateuraces.player.Player;
import com.amateuraces.player.PlayerRepository;
import com.amateuraces.player.PlayerServiceImpl;

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
    void addPlayer_NewPhoneNumber_ReturnSavedPlayer(){
        
        Player player = new Player();
        player.setPhoneNumber("12345678");
        when(players.save(any(Player.class))).thenReturn(player);
        Player savedPlayer = playerService.addPlayer(player);
        
        assertNotNull(savedPlayer);
        assertEquals(player.getPhoneNumber(),savedPlayer.getPhoneNumber());
        verify(players).save(player);
    }

    
    @Test
    void addPlayer_SamePhoneNumber_ReturnNull(){
        Player player = new Player();
        player.setPhoneNumber("10293848");
        Player samePlayerNumber = new Player();
        samePlayerNumber.setPhoneNumber("10293848");


        Optional<Player> samePhoneNumber = Optional.of(samePlayerNumber);
        when(players.findByPhoneNumber(player.getPhoneNumber())).thenReturn(samePhoneNumber);
        Player savedPlayer = playerService.addPlayer(player);
        assertNull(savedPlayer);
        
        verify(players).findByPhoneNumber(player.getPhoneNumber());
        verify(players, never()).save(any(Player.class));
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
}