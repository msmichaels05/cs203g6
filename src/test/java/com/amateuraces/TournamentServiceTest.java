package com.amateuraces;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.amateuraces.match.Match;
import com.amateuraces.match.MatchRepository;
import com.amateuraces.match.MatchServiceImpl;
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

    @Mock
    private PlayerRepository players;

    @Mock
    private MatchRepository matches;

    @InjectMocks
    private TournamentServiceImpl tournamentService;

    @InjectMocks
    private PlayerServiceImpl playerService;

    @InjectMocks
    private MatchServiceImpl matchService;

    @Test
    void createTournament_NewName_ReturnSavedTounament() {

        Tournament tournament = new Tournament("Tournament 1", 1);
        when(tournamentRepository.save(any(Tournament.class))).thenReturn(tournament);
        Tournament savedTournament = tournamentService.addTournament(tournament);

        // assert ***
        assertNotNull(savedTournament);
        assertEquals(tournament.getName(), savedTournament.getName());
        verify(tournamentRepository).save(tournament);
    }

    @Test
    void addPlayerToTournament_ValidTournamentAndPlayer_AddsPlayer() {
        Tournament tournament = new Tournament("Tournament 1", 1);
        tournament.setId(1L);

        Player player = new Player();
        player.setId(1L);
        player.setName("New Player");

        when(tournamentRepository.findById(1L)).thenReturn(Optional.of(tournament));
        when(players.findById(1L)).thenReturn(Optional.of(player));

        // Mock saving the updated tournament
        when(tournamentRepository.save(any(Tournament.class))).thenReturn(tournament);

        Tournament updatedTournament = tournamentService.addPlayerToTournament(1L, 1L);

        assertNotNull(updatedTournament);
        assertEquals(1, updatedTournament.getPlayers().size());
        assertEquals(player.getName(), updatedTournament.getPlayers().get(0).getName());

        // Verify the repository was called with the updated tournament
        verify(tournamentRepository).save(tournament);
    }

    @Test
    void getPlayersInTournament_ValidTournamentId_ReturnPlayerList() {
        // Arrange
        Player player1 = new Player("player 1");
        Player player2 = new Player("player 2");
        List<Player> playerList = Arrays.asList(player1, player2);

        Tournament tournament = new Tournament("Tournament 1", 1);
        tournament.addPlayer(player1);
        tournament.addPlayer(player2);

        when(tournamentRepository.findById(1L)).thenReturn(Optional.of(tournament));

        // Act
        List<Player> players = tournamentService.getPlayersInTournament(1L);

        // Assert
        assertNotNull(players);
        assertEquals(2, players.size());
        assertEquals("player 1", players.get(0).getName());
        assertEquals("player 2", players.get(1).getName());
    }

    @Test
    void recordMatchResult_ValidMatch_ReturnUpdatedTournament() {
        Tournament tournament = new Tournament("Test Tournament", 1);
        Player player1 = new Player("player 1");
        Player player2 = new Player("player 2");
        Match match = new Match(tournament, player1, player2);
    
        // Ensure the match and tournament are found
        when(tournamentRepository.findById(any(Long.class))).thenReturn(Optional.of(tournament));
        when(matches.findById(any(Long.class))).thenReturn(Optional.of(match));
    
        // Mock saving the match and tournament
        when(matches.save(any(Match.class))).thenReturn(match);
        when(tournamentRepository.save(any(Tournament.class))).thenReturn(tournament);
    
        Tournament updatedTournament = tournamentService.recordMatchResult(10L, 1L, "Player 1");
    
        assertNotNull(updatedTournament);
        assertEquals(player1, match.getWinner());
    
        verify(matches).save(match);
        verify(tournamentRepository).save(tournament);
    }

    @Test
    void performRandomDraw_TournamentWithPlayers_ReturnMatches() {
        Tournament tournament = new Tournament("Test Tournament", 1);

        Player player1 = new Player("Player 1");
        Player player2 = new Player("Player 2");
        Player player3 = new Player("Player 3");
        Player player4 = new Player("Player 4");

        tournament.addPlayer(player1);
        tournament.addPlayer(player2);
        tournament.addPlayer(player3);
        tournament.addPlayer(player4);

        // Mock finding the tournament
        when(tournamentRepository.findById(1L)).thenReturn(Optional.of(tournament));

        // Mock the matches returned from the repository
        List<Match> mockMatches = Arrays.asList(
                new Match(tournament, player1, player2),
                new Match(tournament, player3, player4));

        // Mock the saveAll call to return the created matches
        when(matches.saveAll(anyList())).thenReturn(mockMatches);

        // Perform the random draw
        List<Match> returnedMatches = tournamentService.performRandomDraw(1L);

        // Assert that the correct number of matches were returned
        assertEquals(2, returnedMatches.size());
        verify(matches).saveAll(anyList());
    }

    // @Test
    // void deleteTournament_TournamentExists_DeletesSuccessfully() {
    //     Long tournamentId = 1L;
    //     Tournament tournament = new Tournament("Test Tournament", tournamentId);

    //     when(tournamentRepository.findById(tournamentId)).thenReturn(Optional.of(tournament));

    //     tournamentService.deleteTournament(tournamentId);

    //     verify(tournamentRepository, times(1)).deleteById(tournamentId);
    // }

    @Test
    void deleteTournament_TournamentNotFound_ThrowsException() {
        Long tournamentId = 2L;
        when(tournamentRepository.findById(tournamentId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            tournamentService.deleteTournament(tournamentId);
        });

        assertEquals("Tournament not found", exception.getMessage());

        verify(tournamentRepository, never()).deleteById(any(Long.class));
    }


    // @Test
    // void addBook_SameTitle_ReturnNull() {
    // // your code here
    // Book book = new Book("The Same Title Exists");
    // List<Book> sameTitles = new ArrayList<Book>();
    // sameTitles.add(new Book("The Same Title Exists"));
    // when(books.findByTitle(book.getTitle())).thenReturn(sameTitles);
    // Book savedBook = bookService.addBook(book);
    // assertNull(savedBook);
    // verify(books).findByTitle(book.getTitle());
    // }
}