package com.amateuraces;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.amateuraces.match.MatchRepository;
import com.amateuraces.match.MatchServiceImpl;
import com.amateuraces.player.PlayerRepository;
import com.amateuraces.player.PlayerServiceImpl;
import com.amateuraces.tournament.ExistingTournamentException;
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

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addTournament_NewName_ReturnSavedTournament() {
        Tournament tournament = new Tournament("New Tournament");
        when(tournamentRepository.save(any(Tournament.class))).thenReturn(tournament);
        Tournament savedTournament = tournamentService.addTournament(tournament);

        assertNotNull(savedTournament);
        assertEquals(tournament.getName(), savedTournament.getName());
        assertEquals(tournament.getELOrequirement(), savedTournament.getELOrequirement());

        verify(tournamentRepository).save(tournament);
    }

    @Test
    void addTournament_SameName_ReturnException(){
        Tournament tournament = new Tournament("New Tournament");
        Optional<Tournament> sameName = Optional.of(new Tournament("New Tournament"));
        
        when(tournamentRepository.findByName(tournament.getName())).thenReturn(sameName);
        assertThrows(ExistingTournamentException.class, () -> {
            tournamentService.addTournament(tournament);
        });
        verify(tournamentRepository).findByName(tournament.getName());
        verify(tournamentRepository, never()).save(any(Tournament.class));
    }

    @Test
    public void updateTournamentName_ValidId_ReturnUpdatedTournament() {
        Tournament existingTournament = new Tournament();
        existingTournament.setId(1L);
        existingTournament.setName("Old Tournament");
        existingTournament.setELOrequirement(1000);

        Tournament newTournamentInfo = new Tournament();
        newTournamentInfo.setName("Updated Tournament");
        newTournamentInfo.setELOrequirement(1200);

        when(tournamentRepository.findById(1L)).thenReturn(Optional.of(existingTournament));
        when(tournamentRepository.save(any(Tournament.class))).thenReturn(existingTournament);

        Tournament updatedTournament = tournamentService.updateTournament(1L, newTournamentInfo);

        assertNotNull(updatedTournament);
        assertEquals("Updated Tournament", updatedTournament.getName());
        assertEquals(1200, updatedTournament.getELOrequirement());

        verify(tournamentRepository).save(existingTournament);
    }

    @Test
    public void updateTournamentELORequirement_ValidId_ReturnUpdatedTournament() {
        Tournament existingTournament = new Tournament();
        existingTournament.setId(1L);
        existingTournament.setName("Tournament");
        existingTournament.setELOrequirement(1000);

        Tournament newTournamentInfo = new Tournament();
        newTournamentInfo.setName("Tournament");
        newTournamentInfo.setELOrequirement(1200);

        when(tournamentRepository.findById(1L)).thenReturn(Optional.of(existingTournament));
        when(tournamentRepository.save(any(Tournament.class))).thenReturn(existingTournament);

        Tournament updatedTournament = tournamentService.updateTournament(1L, newTournamentInfo);

        assertNotNull(updatedTournament);
        assertEquals(1200, updatedTournament.getELOrequirement());

        verify(tournamentRepository).save(existingTournament);
    }

    @Test
    public void updateTournamentLocation_ValidId_ReturnUpdatedTournament() {
        Tournament existingTournament = new Tournament();
        existingTournament.setId(1L);
        existingTournament.setLocation("USA");

        Tournament newTournamentInfo = new Tournament();
        newTournamentInfo.setLocation("France");

        when(tournamentRepository.findById(1L)).thenReturn(Optional.of(existingTournament));
        when(tournamentRepository.save(any(Tournament.class))).thenReturn(existingTournament);

        Tournament updatedTournament = tournamentService.updateTournament(1L, newTournamentInfo);

        assertNotNull(updatedTournament);
        assertEquals("France", updatedTournament.getLocation());

        verify(tournamentRepository).save(existingTournament);
    }

    @Test
    public void updateTournamentMaxPlayers_ValidId_ReturnUpdatedTournament() {
        Tournament existingTournament = new Tournament();
        existingTournament.setId(1L);
        existingTournament.setMaxPlayers(16);

        Tournament newTournamentInfo = new Tournament();
        newTournamentInfo.setMaxPlayers(32);

        when(tournamentRepository.findById(1L)).thenReturn(Optional.of(existingTournament));
        when(tournamentRepository.save(any(Tournament.class))).thenReturn(existingTournament);

        Tournament updatedTournament = tournamentService.updateTournament(1L, newTournamentInfo);

        assertNotNull(updatedTournament);
        assertEquals(32, updatedTournament.getMaxPlayers());

        verify(tournamentRepository).save(existingTournament);
    }

    @Test
    public void updateTournamentDescription_ValidId_ReturnUpdatedTournament() {
        Tournament existingTournament = new Tournament();
        existingTournament.setId(1L);
        existingTournament.setDescription("testing123");

        Tournament newTournamentInfo = new Tournament();
        newTournamentInfo.setDescription("nomoretesting");

        when(tournamentRepository.findById(1L)).thenReturn(Optional.of(existingTournament));
        when(tournamentRepository.save(any(Tournament.class))).thenReturn(existingTournament);

        Tournament updatedTournament = tournamentService.updateTournament(1L, newTournamentInfo);

        assertNotNull(updatedTournament);
        assertEquals("nomoretesting", updatedTournament.getDescription());

        verify(tournamentRepository).save(existingTournament);
    }

    @Test
    void updateTournament_NotFound_ReturnNull(){
        Tournament tournament = new Tournament("Updated Name of Tournament");
        Long tournamentID = 10L;
        when(tournamentRepository.findById(tournamentID)).thenReturn(Optional.empty());
        
        Tournament updatedTournament = tournamentService.updateTournament(tournamentID, tournament);
        
        assertNull(updatedTournament);
        verify(tournamentRepository).findById(tournamentID);
    }

    // @Test
    // void addPlayerToTournament_ValidTournamentAndPlayer_ReturnPlayerList() {
    //     Player player1 = new Player("player 1");
    //     Player player2 = new Player("player 2");
    //     Tournament tournament = new Tournament("Tournament 1", "USA");

    //     when(tournamentRepository.findById(1L)).thenReturn(Optional.of(tournament));
    //     when(players.findById(1L)).thenReturn(Optional.of(player1));
    //     when(players.findById(2L)).thenReturn(Optional.of(player2));

    //     tournamentService.addPlayerToTournament(1L, 1L); // Add player 1
    //     tournamentService.addPlayerToTournament(1L, 2L); // Add player 2
    //     Set<Player> playersInTournament = tournament.getPlayers(); // Get players in tournament

    //     assertNotNull(playersInTournament);
    //     assertEquals(2, playersInTournament.size());
    //     assertTrue(playersInTournament.contains(player1));
    //     assertTrue(playersInTournament.contains(player2));

    //     verify(tournamentRepository, times(2)).findById(1L);
    //     verify(players).findById(1L);
    //     verify(players).findById(2L);
    // }
    //     @Test
//     void getPlayersInTournament_ValidTournamentId_ReturnPlayerList() {
//         // Arrange
//         Player player1 = new Player("player 1");
//         Player player2 = new Player("player 2");
//         List<Player> playerList = Arrays.asList(player1, player2);

//         Tournament tournament = new Tournament("Tournament 1", 1);
//         tournament.addPlayer(player1);
//         tournament.addPlayer(player2);

//         when(tournamentRepository.findById(1L)).thenReturn(Optional.of(tournament));

//         // Act
//         List<Player> players = tournamentService.getPlayersInTournament(1L);

//         // Assert
//         assertNotNull(players);
//         assertEquals(2, players.size());
//         assertEquals("player 1", players.get(0).getName());
//         assertEquals("player 2", players.get(1).getName());
//     }

//     @Test
//     void recordMatchResult_ValidMatch_ReturnUpdatedTournament() {
//         Tournament tournament = new Tournament("Test Tournament", 1);
//         Player player1 = new Player("player 1");
//         Player player2 = new Player("player 2");
//         Match match = new Match(tournament, player1, player2);
    
//         // Ensure the match and tournament are found
//         when(tournamentRepository.findById(any(Long.class))).thenReturn(Optional.of(tournament));
//         when(matches.findById(any(Long.class))).thenReturn(Optional.of(match));
    
//         // Mock saving the match and tournament
//         when(matches.save(any(Match.class))).thenReturn(match);
//         when(tournamentRepository.save(any(Tournament.class))).thenReturn(tournament);
    
//         Tournament updatedTournament = tournamentService.recordMatchResult(10L, 1L, "Player 1");
    
//         assertNotNull(updatedTournament);
//         assertEquals(player1, match.getWinner());
    
//         verify(matches).save(match);
//         verify(tournamentRepository).save(tournament);
//     }

//     @Test
//     void performRandomDraw_TournamentWithPlayers_ReturnMatches() {
//         Tournament tournament = new Tournament("Test Tournament", 1);

//         Player player1 = new Player("Player 1");
//         Player player2 = new Player("Player 2");
//         Player player3 = new Player("Player 3");
//         Player player4 = new Player("Player 4");

//         tournament.addPlayer(player1);
//         tournament.addPlayer(player2);
//         tournament.addPlayer(player3);
//         tournament.addPlayer(player4);

//         // Mock finding the tournament
//         when(tournamentRepository.findById(1L)).thenReturn(Optional.of(tournament));

//         // Mock the matches returned from the repository
//         List<Match> mockMatches = Arrays.asList(
//                 new Match(tournament, player1, player2),
//                 new Match(tournament, player3, player4));

//         // Mock the saveAll call to return the created matches
//         when(matches.saveAll(anyList())).thenReturn(mockMatches);

//         // Perform the random draw
//         List<Match> returnedMatches = tournamentService.performRandomDraw(1L);

//         // Assert that the correct number of matches were returned
//         assertEquals(2, returnedMatches.size());
//         verify(matches).saveAll(anyList());
//     }

//     // @Test
//     // void deleteTournament_TournamentExists_DeletesSuccessfully() {
//     //     Long tournamentId = 1L;
//     //     Tournament tournament = new Tournament("Test Tournament", tournamentId);

//     //     when(tournamentRepository.findById(tournamentId)).thenReturn(Optional.of(tournament));

//     //     tournamentService.deleteTournament(tournamentId);

//     //     verify(tournamentRepository, times(1)).deleteById(tournamentId);
//     // }

//     @Test
//     void deleteTournament_TournamentNotFound_ThrowsException() {
//         Long tournamentId = 2L;
//         when(tournamentRepository.findById(tournamentId)).thenReturn(Optional.empty());

//         Exception exception = assertThrows(IllegalArgumentException.class, () -> {
//             tournamentService.deleteTournament(tournamentId);
//         });

//         assertEquals("Tournament not found", exception.getMessage());

//         verify(tournamentRepository, never()).deleteById(any(Long.class));
//     }

//     @Test
//     void recordMatchResult_ValidMatch_ReturnUpdatedTournament() {
//         Tournament tournament = new Tournament("Test Tournament", 1);
//         Player player1 = new Player("player 1");
//         Player player2 = new Player("player 2");
//         Match match = new Match(tournament, player1, player2);
    
//         // Ensure the match and tournament are found
//         when(tournamentRepository.findById(any(Long.class))).thenReturn(Optional.of(tournament));
//         when(matches.findById(any(Long.class))).thenReturn(Optional.of(match));
    
//         // Mock saving the match and tournament
//         when(matches.save(any(Match.class))).thenReturn(match);
//         when(tournamentRepository.save(any(Tournament.class))).thenReturn(tournament);
    
//         Tournament updatedTournament = tournamentService.recordMatchResult(10L, 1L, "Player 1");
    
//         assertNotNull(updatedTournament);
//         assertEquals(player1, match.getWinner());
    
//         verify(matches).save(match);
//         verify(tournamentRepository).save(tournament);
//     }

//     @Test
//     void performRandomDraw_TournamentWithPlayers_ReturnMatches() {
//         Tournament tournament = new Tournament("Test Tournament", 1);

//         Player player1 = new Player("Player 1");
//         Player player2 = new Player("Player 2");
//         Player player3 = new Player("Player 3");
//         Player player4 = new Player("Player 4");

//         tournament.addPlayer(player1);
//         tournament.addPlayer(player2);
//         tournament.addPlayer(player3);
//         tournament.addPlayer(player4);

//         // Mock finding the tournament
//         when(tournamentRepository.findById(1L)).thenReturn(Optional.of(tournament));

//         // Mock the matches returned from the repository
//         List<Match> mockMatches = Arrays.asList(
//                 new Match(tournament, player1, player2),
//                 new Match(tournament, player3, player4));

//         // Mock the saveAll call to return the created matches
//         when(matches.saveAll(anyList())).thenReturn(mockMatches);

//         // Perform the random draw
//         List<Match> returnedMatches = tournamentService.performRandomDraw(1L);

//         // Assert that the correct number of matches were returned
//         assertEquals(2, returnedMatches.size());
//         verify(matches).saveAll(anyList());
//     }

}