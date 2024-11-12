package com.amateuraces.match;

import java.util.List;

public interface MatchService {
    List<Match> listMatches();

    Match getMatch(Long id);

    /**
     * Return the newly added match
     */
    Match addMatch(Match match);

    /**
     * Change method's signature: do not return a value for delete operation
     * @param id
     */
    void deleteMatch(Long id);

    /**
     * Updates match results, moves the winner to the next round(if there is), and update players' ELOs
     * @param id
     */
    Match updateMatch(Long matchId, Match updatedMatchInfo, Long tournamentId);

    void promoteWinnerToNextMatch(Match match, Long tournamentId);
}