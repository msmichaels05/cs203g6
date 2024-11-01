package com.amateuraces;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
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

import com.amateuraces.highlight.Highlight;
import com.amateuraces.highlight.HighlightRepository;
import com.amateuraces.highlight.HighlightServiceImpl;
import com.amateuraces.match.MatchServiceImpl;
import com.amateuraces.player.Player;
import com.amateuraces.player.PlayerRepository;
import com.amateuraces.player.PlayerServiceImpl;
import com.amateuraces.tournament.TournamentRepository;
import com.amateuraces.tournament.TournamentServiceImpl;

@ExtendWith(MockitoExtension.class)
public class HighlightServiceTest {

    @Mock
    private HighlightRepository highlightRepository;

    @Mock
    private PlayerRepository players;

    @Mock
    private TournamentRepository tournaments;

    @InjectMocks
    private MatchServiceImpl matchService;

    @InjectMocks
    private PlayerServiceImpl playerService;

    @InjectMocks
    private TournamentServiceImpl tournamentService;

    @InjectMocks
    private HighlightServiceImpl highlightService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addHighlight_ReturnSavedHighlight() {
        Highlight highlight = new Highlight(new Player("adam"), new Player("bob"), "test tournament");
        when(highlightRepository.save(any(Highlight.class))).thenReturn(highlight);
        Highlight savedHighlight = highlightService.addHighlight(highlight);

        assertNotNull(savedHighlight);
        assertEquals(highlight.getTournamentOfTheMonth(), savedHighlight.getTournamentOfTheMonth());
        assertEquals(highlight.getPlayerOfTheMonth(), savedHighlight.getPlayerOfTheMonth());
        assertEquals(highlight.getMostImprovedPlayer(), savedHighlight.getMostImprovedPlayer());

        verify(highlightRepository).save(highlight);
    }

    @Test
    public void updateHighlightTournament_ValidId_ReturnUpdatedHighlight() {
        Highlight existingHighlight = new Highlight(new Player("adam"), new Player("bob"), "test tournament");

        Highlight newHighlightInfo = existingHighlight;
        newHighlightInfo.setTournamentOfTheMonth(existingHighlight.getTournamentOfTheMonth());

        when(highlightRepository.findById(1L)).thenReturn(Optional.of(existingHighlight));
        when(highlightRepository.save(any(Highlight.class))).thenReturn(existingHighlight);

        Highlight updatedHighlight = highlightService.updateHighlight(1L, newHighlightInfo);

        assertNotNull(updatedHighlight);
        assertEquals(newHighlightInfo.getTournamentOfTheMonth(), updatedHighlight.getTournamentOfTheMonth());

        verify(highlightRepository).save(existingHighlight);
    }

    @Test
    void updateHighlight_NotFound_ReturnNull(){
        Highlight highlight = new Highlight(new Player("adam"), new Player("bob"), "test tournament");
        Long highlightID = 10L;
        when(highlightRepository.findById(highlightID)).thenReturn(Optional.empty());
        
        Highlight updatedHighlight = highlightService.updateHighlight(highlightID, highlight);
        
        assertNull(updatedHighlight);
        verify(highlightRepository).findById(highlightID);
    }
}