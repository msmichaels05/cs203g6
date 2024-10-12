package com.amateuraces.tournament;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.amateuraces.player.Player;
import com.amateuraces.player.PlayerRepository;
import com.amateuraces.player.PlayerServiceImpl;
import com.amateuraces.tournament.Tournament;
import com.amateuraces.tournament.TournamentRepository;
import com.amateuraces.tournament.TournamentServiceImpl;

@ExtendWith(MockitoExtension.class)
public class TournamentServiceTest {

    @Mock
    private TournamentRepository tournamentRepository;
    private PlayerRepository players;

    @InjectMocks
    private TournamentServiceImpl tournamentService;
    private PlayerServiceImpl playerService;

    @Test
    void createTournament_NewName_ReturnSavedTounament() {

        Tournament tournament = new Tournament("Tournament 1", 10L);
        when(tournamentRepository.save(any(Tournament.class))).thenReturn(tournament);
        Tournament savedTournament = tournamentService.createTournament(tournament);

        // assert ***
        assertNotNull(savedTournament);
        assertEquals(tournament.getName(),savedTournament.getName());
        verify(tournamentRepository).save(tournament);
    }

    @Test
    void addPlayerToTournament_ValidTournamentAndPlayer_AddsPlayer() {
        // Setup test data
        Tournament tournament = new Tournament("Tournament 1", 10L);
        tournament.setId(1L);
        
        Player player = new Player();
        player.setId(1L);
        player.setName("New Player");

        // Mock findById for both tournament and player
        when(tournamentRepository.findById(1L)).thenReturn(Optional.of(tournament));
        when(players.findById(1L)).thenReturn(Optional.of(player));

        // Call the service method
        Tournament updatedTournament = tournamentService.addPlayerToTournament(1L, 1L);

        // Assert that the player was added
        assertNotNull(updatedTournament);
        assertEquals(1, updatedTournament.getPlayers().size());
        assertEquals(player.getName(), updatedTournament.getPlayers().get(0).getName());

        // Verify that the tournament was saved with the added player
        verify(tournamentRepository).save(tournament);
    }

    @Test
    void getPlayersInTournament_ValidTournamentId_ReturnPlayerList() {
        // Arrange
        Player player1 = new Player("player 1", "Male", 25, "player1@example.com", "password", "12345678", 10, 5);
        Player player2 = new Player("player 2", "Male", 23, "player2@example.com", "password", "12345678", 12, 8);
        List<Player> playerList = Arrays.asList(player1, player2);

        Tournament tournament = new Tournament("Tournament 1", 10L);
        tournament.addPlayer(player1);
        tournament.addPlayer(player2);

        when(tournamentRepository.findById(10L)).thenReturn(Optional.of(tournament));

        // Act
        List<Player> players = tournamentService.getPlayersInTournament(1L);

        // Assert
        assertNotNull(players);
        assertEquals(2, players.size());
        assertEquals("player 1", players.get(0).getName());
        assertEquals("player 2", players.get(1).getName());
    }

    // @Test
    // void addBook_SameTitle_ReturnNull() {
    //     // your code here
    //     Book book = new Book("The Same Title Exists");
    //     List<Book> sameTitles = new ArrayList<Book>();
    //     sameTitles.add(new Book("The Same Title Exists"));
    //     when(books.findByTitle(book.getTitle())).thenReturn(sameTitles);
    //     Book savedBook = bookService.addBook(book);
    //     assertNull(savedBook);
    //     verify(books).findByTitle(book.getTitle());
    // }
}