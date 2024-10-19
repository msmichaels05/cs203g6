package com.amateuraces;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
    private MatchRepository matches;

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
    public void recordMatchResult_ValidMatchAndPlayers_ReturnUpdatedMatch() {
        // Arrange
        Player winner = new Player("Winner", "Male", 25, "1234567890", 10, 8);
        winner.setId(1L); // Set ID for winner player
        Player loser = new Player("Loser", "Male", 24, "0987654321", 10, 5);
        loser.setId(2L); // Set ID for loser player

        // Create a match and set the IDs
        Match match = new Match();
        match.setId(1L);
        match.setPlayer1(winner);
        match.setPlayer2(loser);

        // Mocking repository behavior
        when(matches.findById(1L)).thenReturn(Optional.of(match)); // Ensure match is returned
        when(players.findById(1L)).thenReturn(Optional.of(winner));
        when(players.findById(2L)).thenReturn(Optional.of(loser));

        // Mock saving behavior
        when(matches.save(any(Match.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(players.save(any(Player.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Match updatedMatch = matchService.recordMatchResult(1L, 1L, 2L, "6-0, 6-0");

        // Assert
        assertNotNull(updatedMatch);
        assertEquals(winner, updatedMatch.getWinner());
        assertEquals("6-0, 6-0", updatedMatch.getScore());

        // Verify that player saves and match save were called
        verify(players).save(winner);
        verify(players).save(loser);
        verify(matches).save(match);
    }

    @Test
    public void recordMatchResult_OnlyFindMatch() {
        // Arrange
        Match match = new Match();
        match.setId(1L);
        when(matches.findById(1L)).thenReturn(Optional.of(match));

        // Act
        Match foundMatch = matches.findById(1L).orElseThrow(() -> new MatchNotFoundException(1L));

        // Assert
        assertNotNull(foundMatch);
        assertEquals(1L, foundMatch.getId());
    }
}