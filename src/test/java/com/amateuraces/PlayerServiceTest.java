package com.amateuraces;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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
        player.setPhoneNumber(12345678L);
        when(players.save(any(Player.class))).thenReturn(player);
        Player savedPlayer = playerService.addPlayer(player);
        
        assertNotNull(savedPlayer);
        assertEquals(player.getPhoneNumber(),savedPlayer.getPhoneNumber());
        verify(players).save(player);
    }

    @Test
    void addPlayer_NewEmail_ReturnSavedPlayer(){
        Player player = new Player("test123@gmail.com");
        when(players.save(any(Player.class))).thenReturn(player);
        Player savedPlayer = playerService.addPlayer(player);
        
        assertNotNull(savedPlayer);
        assertEquals(player.getEmail(),savedPlayer.getEmail());
        verify(players).save(player);
    }

    
    @Test
    void addPlayer_SamePhoneNumber_ReturnNull(){
        Player player = new Player();
        player.setPhoneNumber(10293848L);
        Player samePlayerNumber = new Player();
        samePlayerNumber.setPhoneNumber(10293848L);


        Optional<Player> samePhoneNumber = Optional.of(samePlayerNumber);
        when(players.findByPhoneNumber(player.getPhoneNumber())).thenReturn(samePhoneNumber);
        Player savedPlayer = playerService.addPlayer(player);
        assertNull(savedPlayer);
        
        verify(players).findByPhoneNumber(player.getPhoneNumber());
        verify(players, never()).save(any(Player.class));
    }

    @Test
    void addPlayer_SameEmail_ReturnNull(){
        Player player = new Player();
        player.setEmail("test123@gmail.com");
        Player samePlayerEmail = new Player();
        samePlayerEmail.setEmail("test123@gmail.com");

        Optional<Player> sameEmail = Optional.of(samePlayerEmail);

        when(players.findByEmail(player.getEmail())).thenReturn(sameEmail);
        Player savedPlayer = playerService.addPlayer(player);
        assertNull(savedPlayer);

        verify(players).findByEmail(player.getEmail());
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