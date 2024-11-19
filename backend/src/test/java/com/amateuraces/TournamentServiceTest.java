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
        existingTournament.setELORequirement(1000);

        Tournament newTournamentInfo = new Tournament();
        newTournamentInfo.setName("Updated Tournament");
        newTournamentInfo.setELORequirement(1200);

        when(tournamentRepository.findById(1L)).thenReturn(Optional.of(existingTournament));
        when(tournamentRepository.save(any(Tournament.class))).thenReturn(existingTournament);

        Tournament updatedTournament = tournamentService.updateTournament(1L, newTournamentInfo);

        assertNotNull(updatedTournament);
        assertEquals("Updated Tournament", updatedTournament.getName());
        assertEquals(1200, updatedTournament.getELORequirement());

        verify(tournamentRepository).save(existingTournament);
    }

    @Test
    public void updateTournamentELORequirement_ValidId_ReturnUpdatedTournament() {
        Tournament existingTournament = new Tournament();
        existingTournament.setId(1L);
        existingTournament.setName("Tournament");
        existingTournament.setELORequirement(1000);

        Tournament newTournamentInfo = new Tournament();
        newTournamentInfo.setName("Tournament");
        newTournamentInfo.setELORequirement(1200);

        when(tournamentRepository.findById(1L)).thenReturn(Optional.of(existingTournament));
        when(tournamentRepository.save(any(Tournament.class))).thenReturn(existingTournament);

        Tournament updatedTournament = tournamentService.updateTournament(1L, newTournamentInfo);

        assertNotNull(updatedTournament);
        assertEquals(1200, updatedTournament.getELORequirement());

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

}