package com.amateuraces;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.amateuraces.match.Match;
import com.amateuraces.match.MatchRepositoryCompleted;
import com.amateuraces.match.MatchRepositoryIncomplete;
import com.amateuraces.match.MatchServiceImpl;
import com.amateuraces.player.Player;
import com.amateuraces.player.PlayerRepository;
import com.amateuraces.player.PlayerServiceImpl;
import com.amateuraces.tournament.TournamentRepository;
import com.amateuraces.tournament.TournamentServiceImpl;

@ExtendWith(MockitoExtension.class)
public class MatchServiceTest {

    @Mock
    private TournamentRepository tournamentRepository;

    @Mock
    private PlayerRepository players;

    @Mock
    private MatchRepositoryCompleted completedMatches;

    @Mock
    private MatchRepositoryIncomplete incompleteMatches;

    @InjectMocks
    private TournamentServiceImpl tournamentService;

    @InjectMocks
    private PlayerServiceImpl playerService;

    @InjectMocks
    private MatchServiceImpl matchService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addMatch_ShouldSaveMatchInIncompleteRepository() {
        Player player1 = new Player();
        player1.setId(10L);

        Player player2 = new Player();
        player2.setId(20L);

        Match match = new Match(player1, player2);
        match.setId(1L);

        // Mock save operation for incompleteMatches
        when(incompleteMatches.save(any(Match.class))).thenReturn(match);

        // Call the service to add the match
        Match savedMatch = matchService.addMatch(match);

        // Validate the result
        assertNotNull(savedMatch);  // Check if the saved match is not null
        assertEquals(match, savedMatch);  // Check if the returned match is the same as expected
        verify(incompleteMatches).save(match);  // Verify that the save method was called
    }

    // @Test
    // void addMatch_ShouldSaveMatchInIncompleteRepository() {
    //     // Given
    //     Player player1 = new Player();
    //     player1.setId(10L);

    //     Player player2 = new Player();
    //     player2.setId(20L);

    //     Match match = new Match(player1, player2);  // Create the match with players
    //     match.setId(1L);  // Set the match ID

    //     // When
    //     when(incompleteMatches.save(any(Match.class))).thenReturn(match);  // Mock saving

    //     // Act
    //     Match savedMatch = matchService.addMatch(match);  // Call the service to add the match

    //     // Then
    //     verify(incompleteMatches).save(match);
    //     assertNotNull(savedMatch);  // Check if the saved match is not null
    //     assertEquals(match, savedMatch);  // Check if the returned match is the same as expected
    //       // Verify that the save method was called
    // }

    // @Test
    // public void recordMatchResult_ValidMatchAndPlayers_UpdatesAndSaves() {
    //     // Arrange
    //     Player winner = new Player("Winner", "Male", 25, "1234567890", 10, 8);
    //     winner.setId(1L); // Ensure the winner ID is 1L

    //     Player loser = new Player("Loser", "Male", 24, "0987654321", 10, 5);
    //     loser.setId(2L); // Ensure the loser ID is 2L

    //     // Create a match and set the ID to 1L
    //     Match match = new Match();
    //     match.setId(1L); // Ensure the match ID is 1L
    //     match.setPlayer1(winner);
    //     match.setPlayer2(loser);

    //     // Mock repository behavior for finding the match and players
    //     when(incompleteMatches.findById(1L)).thenReturn(Optional.of(match)); // Ensure match is returned
    //     when(players.findById(1L)).thenReturn(Optional.of(winner)); // Mock the winner retrieval
    //     when(players.findById(2L)).thenReturn(Optional.of(loser)); // Mock the loser retrieval

    //     // Mock saving behavior for match and players
    //     when(incompleteMatches.save(any(Match.class))).thenAnswer(invocation -> invocation.getArgument(0));
    //     when(players.save(any(Player.class))).thenAnswer(invocation -> invocation.getArgument(0));

    //     // Act
    //     Match updatedMatch = matchService.recordMatchResult(1L, 1L, 2L, "6-0, 6-0");

    //     // Assert
    //     assertNotNull(updatedMatch); // Ensure match is not null
    //     assertEquals(winner, updatedMatch.getWinner()); // Ensure the winner is set correctly
    //     assertEquals("6-0, 6-0", updatedMatch.getScore()); // Ensure the score is set correctly

    //     // Verify that players and match were saved
    //     verify(players).save(winner);
    //     verify(players).save(loser);
    //     verify(completedMatches).save(updatedMatch);
    // }
}