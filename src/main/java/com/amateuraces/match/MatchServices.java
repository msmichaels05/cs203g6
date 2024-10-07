package com.amateuraces.match;

import java.time.LocalDateTime;
import java.util.List;

import com.amateuraces.player.Player;
import com.amateuraces.tournament.Tournament;

public class MatchServices {
    public interface MatchService {

        /**
         * Add a new match to the system.
         * 
         * @param match The match to be added.
         * @return The added match.
         */
        Match addMatch(Match match);

        /**
         * Register a player for a specific match.
         * 
         * @param matchId  The ID of the match.
         * @param playerId The ID of the player to register.
         */
        void registerPlayerForMatch(Long matchId, Long playerId);

        /**
         * List all matches.
         * 
         * @return List of all matches.
         */
        List<Match> listMatches();

        /**
         * Get the match schedule for a specific tournament.
         * 
         * @param tournamentId The ID of the tournament.
         * @return List of matches for the tournament.
         */
        List<Match> getTournamentMatchSchedule(Long tournamentId);

        /**
         * Record the result of a match.
         * 
         * @param matchId The ID of the match.
         * @param winner  The player who won the match.
         */
        void recordMatchResult(Long matchId, Player winner);

        /**
         * Reschedule a match to a new date and time.
         * 
         * @param matchId     The ID of the match.
         * @param newDateTime The new date and time for the match.
         */
        void rescheduleMatch(Long matchId, LocalDateTime newDateTime);

        /**
         * Cancel a match.
         * 
         * @param matchId The ID of the match to be canceled.
         */
        void cancelMatch(Long matchId);
    }
}