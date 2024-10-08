package com.amateuraces.tournament;

import com.amateuraces.player.Player;
import com.amateuraces.match.Match;
import com.amateuraces.player.PlayerRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TournamentServiceImpl implements TournamentService {

    private final TournamentRepository tournamentRepository;
    private final PlayerRepository playerRepository;

    public TournamentServiceImpl(TournamentRepository tournamentRepository, PlayerRepository playerRepository) {
        this.tournamentRepository = tournamentRepository;
        this.playerRepository = playerRepository;
    }

    @Override
    public List<Tournament> getAllTournaments() {
        // This uses the JPA method findAll() provided by JpaRepository
        return tournamentRepository.findAll();
    }

    @Override
    public Tournament createTournament(Tournament tournament) {
        // Validate tournament before saving
        if (tournament.getName() == null || tournament.getName().isEmpty()) {
            throw new IllegalArgumentException("Tournament name cannot be empty");
        }
        return tournamentRepository.save(tournament);
    }

    @Override
    public Tournament addPlayerToTournament(Long tournamentId, Long playerId) {
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new IllegalArgumentException("Tournament not found"));
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new IllegalArgumentException("Player not found"));

        tournament.addPlayer(player); // Add the player
        return tournamentRepository.save(tournament); // Persist the changes
    }

    @Override
    public Tournament setRegistrationPeriod(Long tournamentId, String startDate, String endDate) {
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new IllegalArgumentException("Tournament not found"));

        tournament.setRegistrationStartDate(startDate);
        tournament.setRegistrationEndDate(endDate);
        
        // Automatically update registration status in the Tournament class
        return tournamentRepository.save(tournament);
    }

    @Override
    public List<Player> getPlayersInTournament(Long tournamentId) {
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new IllegalArgumentException("Tournament not found"));

        return tournament.getPlayers(); // Directly return the players
    }

    @Override
    public List<Match> performRandomDraw(Long tournamentId) {
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new IllegalArgumentException("Tournament not found"));

        // Logic for performing random draw goes here
        return new ArrayList<>(); // Return matches after drawing
    }

    @Override
    public Tournament updateTournamentStatus(Long tournamentId, String status) {
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new IllegalArgumentException("Tournament not found"));

        tournament.setStatus(status);
        return tournamentRepository.save(tournament);
    }

    @Override
    public Tournament recordMatchResult(Long tournamentId, Long matchId, String result) {
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new IllegalArgumentException("Tournament not found"));

        // Logic to find the match and update the result
        return tournamentRepository.save(tournament);
    }

    @Override
    public boolean validateRegistrationPeriod(Long tournamentId) {
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new IllegalArgumentException("Tournament not found"));

        return tournament.checkRegistrationPeriod();
    }
}
