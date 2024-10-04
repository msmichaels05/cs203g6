package com.amateuraces.tournament;

import com.amateuraces.player.Player;
import com.amateuraces.match.Match;

import java.util.List;
import java.util.Optional;

public interface TournamentRepository {
    List<Tournament> findAll();
    Optional<Tournament> findById(Long id);
    Tournament save(Tournament tournament);
    void addPlayerToTournament(Long tournamentId, Long playerId);
    List<Player> getPlayersInTournament(Long tournamentId);
    List<Match> performRandomDraw(Long tournamentId);
}
