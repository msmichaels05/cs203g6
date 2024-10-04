package com.amateuraces.tournament;

import com.amateuraces.player.Player;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TournamentServiceImpl implements TournamentService {

    private final TournamentRepository tournamentRepository;  // Repository for managing Tournament data
    private final List<Tournament> tournaments = new ArrayList<>(); // Sample in-memory storage for tournaments

    public TournamentServiceImpl(TournamentRepository tournamentRepository) {
        this.tournamentRepository = tournamentRepository;
        // Sample data for testing purposes
        tournaments.add(new Tournament(1L, "Tournament 1", "2024-10-01 to 2024-10-10", new ArrayList<>(), "ELO:1500"));
        tournaments.add(new Tournament(2L, "Tournament 2", "2024-11-01 to 2024-11-10", new ArrayList<>(), "Age:18+"));
    }

    @Override
    public List<Tournament> getAllTournaments() {
        return tournaments; // Return the list of tournaments
    }

    @Override
    public Tournament createTournament(Tournament tournament) {
        tournaments.add(tournament); // Add the new tournament to the list
        return tournament;
    }

    @Override
    public Tournament addPlayerToTournament(Long tournamentId, Long playerId) {
        Optional<Tournament> tournamentOpt = tournaments.stream()
                .filter(tournament -> tournament.getId().equals(tournamentId))
                .findFirst();

        if (tournamentOpt.isPresent()) {
            Tournament tournament = tournamentOpt.get();
            // Assuming you have a method to find a player by ID
            // Player player = playerService.getPlayer(playerId); // You need to implement player retrieval

            // Check if the player meets the requirements and add them
            // if (tournament.meetsRequirements(player)) {
            //     tournament.addPlayer(player);
            //     return tournament;
            // }
            // Handle the case where the player does not meet requirements
            return null; // or throw an exception
        }
        return null; // or throw an exception if the tournament doesn't exist
    }

    @Override
    public Tournament setRegistrationPeriod(Long tournamentId, String startDate, String endDate) {
        Optional<Tournament> tournamentOpt = tournaments.stream()
                .filter(tournament -> tournament.getId().equals(tournamentId))
                .findFirst();

        if (tournamentOpt.isPresent()) {
            Tournament tournament = tournamentOpt.get();
            tournament.setRegistrationPeriod(startDate + " to " + endDate);
            return tournament;
        }
        return null; // or throw an exception if the tournament doesn't exist
    }

    @Override
    public List<Player> getPlayersInTournament(Long tournamentId) {
        Optional<Tournament> tournamentOpt = tournaments.stream()
                .filter(tournament -> tournament.getId().equals(tournamentId))
                .findFirst();

        return tournamentOpt.map(Tournament::getPlayers).orElse(new ArrayList<>());
    }

    @Override
    public List<Match> performRandomDraw(Long tournamentId) {
        // Logic for performing a random draw based on tournament rules
        return new ArrayList<>(); // Return a list of matches after drawing
    }
}
