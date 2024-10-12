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
     * Return the updated match
     * 
     * @param id
     * @param match
     * @return
     */
    Match updateMatch(Long id, Match match);

    /**
     * Change method's signature: do not return a value for delete operation
     * @param id
     */
    void deleteMatch(Long id);

    /**
     * Declares the winner of the match.
     *
     * @param matchId
     * @param winnerId
     * @return
     */
    Match declareWinner(Long matchId, Long winnerId);

    /**
     * Records the match result, updating both players' statistics.
     *
     * @param matchId 
     * @param winnerId
     * @param loserId 
     * @param score 
     * @return
     */
    Match recordMatchResult(Long matchId, Long winnerId, Long loserId, String score);
}