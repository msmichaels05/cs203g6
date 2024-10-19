package com.amateuraces;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.amateuraces.match.*;
import com.amateuraces.player.*;
import com.amateuraces.tournament.*;

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
    public void recordMatchResult_ValidMatchAndPlayers_UpdatesAndSaves() {
        // Arrange
        Player winner = new Player("Winner", "Male", 25, "1234567890", 10, 8);
        winner.setId(1L); // Ensure the winner ID is 1L

        Player loser = new Player("Loser", "Male", 24, "0987654321", 10, 5);
        loser.setId(2L); // Ensure the loser ID is 2L

        // Create a match and set the ID to 1L
        Match match = new Match();
        match.setId(1L); // Ensure the match ID is 1L
        match.setPlayer1(winner);
        match.setPlayer2(loser);

        // Mock repository behavior for finding the match and players
        when(incompleteMatches.findById(1L)).thenReturn(Optional.of(match)); // Ensure match is returned
        when(players.findById(1L)).thenReturn(Optional.of(winner)); // Mock the winner retrieval
        when(players.findById(2L)).thenReturn(Optional.of(loser)); // Mock the loser retrieval

        // Mock saving behavior for match and players
        when(incompleteMatches.save(any(Match.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(players.save(any(Player.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Match updatedMatch = matchService.recordMatchResult(1L, 1L, 2L, "6-0, 6-0");

        // Assert
        assertNotNull(updatedMatch); // Ensure match is not null
        assertEquals(winner, updatedMatch.getWinner()); // Ensure the winner is set correctly
        assertEquals("6-0, 6-0", updatedMatch.getScore()); // Ensure the score is set correctly

        // Verify that players and match were saved
        verify(players).save(winner);
        verify(players).save(loser);
        verify(completedMatches).save(updatedMatch);
    }

    // @Test
    // public void recordMatchResult_OnlyFindMatch() {
    // // Arrange
    // Match match = new Match();
    // match.setId(1L);
    // when(incompleteMatches.findById(1L)).thenReturn(Optional.of(match));

    // // Act
    // Match foundMatch = incompleteMatches.findById(1L).orElseThrow(() -> new
    // MatchNotFoundException(1L));

    // // Assert
    // assertNotNull(foundMatch);
    // assertEquals(1L, foundMatch.getId());
    // }
}