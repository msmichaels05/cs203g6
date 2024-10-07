package com.amateuraces.match;

import com.amateuraces.player.Player;
import com.amateuraces.match.Match;
import com.amateuraces.tournament.tournament;

import java.time.LocalDateTime;
import java.util.List;

public interface MatchRepository {

    Match save(Match match);

    Match findById(Long matchId);

    List<Match> findAll();

    Match update(Match match);

    void delete(Long matchId);

    List<Match> findByTournamentId(Long tournamentId);

    void recordMatchResult(Long matchId, Long winnerId); // New method for recording results

    void rescheduleMatch(Long matchId, LocalDateTime newDateTime); // New method for rescheduling matches

    void cancelMatch(Long matchId); // New method for cancelling matches
}
