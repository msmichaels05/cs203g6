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

import com.amateuraces.match.Match;
import com.amateuraces.match.MatchRepository;
import com.amateuraces.match.MatchServiceImpl;
import com.amateuraces.player.Player;
import com.amateuraces.player.PlayerRepository;
import com.amateuraces.player.PlayerServiceImpl;
import com.amateuraces.tournament.Tournament;

@ExtendWith(MockitoExtension.class)
public class MatchServiceTest {

    // @Mock
    // private MatchRepository matchRepository;

    // @Mock
    // private PlayerRepository players;

    // @InjectMocks
    // private MatchServiceImpl matchService;

    // @InjectMocks
    // private PlayerServiceImpl playerService;

    // @BeforeEach
    // void setUp() {
    //     MockitoAnnotations.openMocks(this);
    // }

    // @Test
    // void addMatch_ReturnSavedMatch() {
    //     Match match = new Match(new Tournament("test tournament"), new Player("adam"), new Player("bob"));
    //     when(matchRepository.save(any(Match.class))).thenReturn(match);
    //     Match savedMatch = matchService.addMatch(match);

    //     assertNotNull(savedMatch);
    //     assertEquals(match.getTournament(), savedMatch.getTournament());
    //     assertEquals(match.getPlayer1(), savedMatch.getPlayer1());
    //     assertEquals(match.getPlayer2(), savedMatch.getPlayer2());

    //     verify(matchRepository).save(match);
    // }

    // @Test
    // public void updateMatchWinner_ValidId_ReturnUpdatedMatch() {
    //     Match existingMatch = new Match(new Tournament("test tournament"), new Player("adam"), new Player("bob"));

    //     Match newMatchInfo = existingMatch;
    //     newMatchInfo.setWinner(existingMatch.getPlayer1());

    //     when(matchRepository.findById(1L)).thenReturn(Optional.of(existingMatch));
    //     when(matchRepository.save(any(Match.class))).thenReturn(existingMatch);

    //     Match updatedMatch = matchService.updateMatch(1L, newMatchInfo);

    //     assertNotNull(updatedMatch);
    //     assertEquals(newMatchInfo.getWinner(), updatedMatch.getWinner());

    //     verify(matchRepository).save(existingMatch);
    // }

    // @Test
    // void updateMatch_NotFound_ReturnNull(){
    //     Match match = new Match(new Tournament("test tournament"), new Player("adam"), new Player("bob"));
    //     Long matchID = 10L;
    //     when(matchRepository.findById(matchID)).thenReturn(Optional.empty());
        
    //     Match updatedMatch = matchService.updateMatch(matchID, match);
        
    //     assertNull(updatedMatch);
    //     verify(matchRepository).findById(matchID);
    // }
}