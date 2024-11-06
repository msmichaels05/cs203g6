package com.amateuraces.match;

import java.util.List;

public interface MatchService {
    List<Match> listMatches();

    Match getMatch(Long id);

    /**
     * Return the newly added match
     */
    Match addMatch(Match match);

    // /**
    //  * Return the updated match
    //  * 
    //  * @param id
    //  * @param match
    //  * @return
    //  */
    // Match updateMatch(Long id, Match match);

    /**
     * Change method's signature: do not return a value for delete operation
     * @param id
     */
    void deleteMatch(Long id);
}