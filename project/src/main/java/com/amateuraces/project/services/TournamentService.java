package com.amateuraces.project.services;

import java.util.List;

import com.amateuraces.project.entity.Tournament;

public interface TournamentService {
    // void createTournament(TournamentDTO tournamentDTO);
    // void updateTournament(TournamentDTO tournamentDTO);
    void deleteTournament(String tournamentId);
    List<Tournament> getAllTournaments();
    Tournament getTournamentById(String tournamentId);
    void registerPlayer(String tournamentId, String playerId);
    void organizeMatches(String tournamentId);
}