package com.amateuraces.tournament;
import java.util.List;

import com.amateuraces.match.Match;
import com.amateuraces.player.Player;

public interface TournamentService {

    Tournament addTournament(Tournament tournament);

    /**
     * Create a new tournament.
     * @param tournament The tournament to be created.
     * @return The created tournament.
     */
    Tournament createTournament(Tournament tournament);


        /**
     * Change method's signature: do not return a value for delete operation
     * @param id
     */
    void deleteTournament(Long id);


        /**
     * Return the updated player
     * 
     * @param id
     * @param player
     * @return
     */
    Tournament updateTournament(Long id, Tournament tournament);

    /**
     * Set the registration period for a tournament.
     * @param tournamentId The ID of the tournament.
     * @param startDate The start date of the registration period.
     * @param endDate The end date of the registration period.
     * @return The updated tournament.
     */
    // Tournament setRegistrationPeriod(Long tournamentId, String startDate, String endDate);

    /**
     * Add a player to the tournament.
     * @param tournamentId The ID of the tournament.
     * @param playerId The ID of the player to be added.
     * @return The updated tournament with the player added.
     */
    Tournament addPlayerToTournament(Long tournamentId, Long playerId);

    /**
     * Get the list of players registered for the tournament.
     * @param tournamentId The ID of the tournament.
     * @return List of players in the tournament.
     */
    List<Player> getPlayersInTournament(Long tournamentId);

      /**
     * Get all tournaments.
     * @return List of tournaments.
     */
    List<Tournament> listTournaments();

    /**
     * Perform a randomized draw for the tournament.
     * This will create match pairings randomly.
     * @param tournamentId The ID of the tournament.
     * @return List of match pairings after the draw.
     */
    List<Match> performRandomDraw(Long tournamentId);

    /**
     * Update the status of the tournament (e.g., in-progress, completed).
     * @param tournamentId The ID of the tournament.
     * @param status The new status of the tournament.
     * @return The updated tournament.
     */
    // Tournament updateTournamentStatus(Long tournamentId, String status);

    /**
     * Record the result of a match.
     * @param tournamentId The ID of the tournament.
     * @param matchId The ID of the match.
     * @param result The result of the match (e.g., playerA wins, playerB wins).
     * @return The updated tournament with match results.
     */
    Tournament recordMatchResult(Long tournamentId, Long matchId, String result);

    /**
     * Validate if the registration period is valid.
     * @param tournamentId The ID of the tournament.
     * @return True if registration is open, false otherwise.
     */
    // boolean validateRegistrationPeriod(Long tournamentId);

    /**
     * Change method's signature: do not return a value for delete operation
     * @param tournamentId The ID of the tournament.
     */
    void deleteTournament(Long tournamentId);
}
