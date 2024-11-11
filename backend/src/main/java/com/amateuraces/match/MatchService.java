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
     * Records the match result, updating both players' statistics.
     *
     * @param matchId 
     * @param winnerId
     * @param score 
     * @return
     */
    Match recordMatchResult(Long matchId, Long winnerId, Long loserId, String score);

    /**
     * Records the match result, updating both players' statistics.
     *
     * @param matchId 
     * @param newScore
     * @return
     */
    Match updateRecordMatchScore(Long matchId, String newScore);

    /**
     * Records the match result, updating both players' statistics.
     *
     * @param matchId 
     * @param oldWinnerId
     * @param newWinnerId 
     * @param newScore 
     * @return
     */
    Match updateRecordMatchWinner(Long matchId, Long oldWinnerId, Long newWinnerId, String newScore);

    Match updateMatch(Long matchId, Match updatedMatchInfo);
}