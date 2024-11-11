package com.amateuraces.tournament;
import java.util.List;
import java.util.Set;

import com.amateuraces.match.Match;
import com.amateuraces.player.Player;

public interface TournamentService {

     /**
     * Get all tournaments.
     * @return List of tournaments.
     */
    List<Tournament> listTournaments();

    Tournament getTournament(Long id);

    Tournament addTournament(Tournament tournament);

    void deleteTournament(Long id);

    void joinTournament(Long id);

    Tournament updateTournament(Long id, Tournament tournament);

    // /**
    //  * Get the list of players registered for the tournament.
    //  * @param tournamentId The ID of the tournament.
    //  * @return List of players in the tournament.
    //  */
    Set<Player> getPlayersInTournament(Long tournamentId);

    void removePlayerFromTournament(Long tournamentId, Long playerId);

    // List<Match> createMatchesForTournament(Long tournamentId);

    // List<Match> initialiseDraw(Long tournamentId);

    // List<Match> updateNextRound(Long tournamentId);

    List<Match> generateMatches(Long tournamentId);
}
