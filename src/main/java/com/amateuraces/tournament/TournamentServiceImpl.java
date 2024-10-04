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
        return tournamentRepository.findAll();
    }

    @Override
    public Tournament createTournament(Tournament tournament) {
        return tournamentRepository.save(tournament);
    }

    @Override
    public Tournament addPlayerToTournament(Long tournamentId, Long playerId) {
        Optional<Tournament> tournamentOpt = tournamentRepository.findById(tournamentId);
        Optional<Player> playerOpt = playerRepository.findById(playerId);

        if (tournamentOpt.isPresent() && playerOpt.isPresent()) {
            Tournament tournament = tournamentOpt.get();
            Player player = playerOpt.get();

            tournament.addPlayer(player); // Add the player without checking the requirements
            return tournamentRepository.save(tournament); // Persist the changes
        }
        throw new IllegalArgumentException("Tournament or player not found");
    }

    @Override
    public Tournament setRegistrationPeriod(Long tournamentId, String startDate, String endDate) {
        Optional<Tournament> tournamentOpt = tournamentRepository.findById(tournamentId);

        if (tournamentOpt.isPresent()) {
            Tournament tournament = tournamentOpt.get();
            tournament.setRegistrationStartDate(startDate);
            tournament.setRegistrationEndDate(endDate);

            // Registration status will be updated automatically by the Tournament class
            return tournamentRepository.save(tournament);
        }
        throw new IllegalArgumentException("Tournament not found");
    }

    @Override
    public List<Player> getPlayersInTournament(Long tournamentId) {
        Optional<Tournament> tournamentOpt = tournamentRepository.findById(tournamentId);

        if (tournamentOpt.isPresent()) {
            return tournamentOpt.get().getPlayers();
        }
        throw new IllegalArgumentException("Tournament not found");
    }

    @Override
    public List<Match> performRandomDraw(Long tournamentId) {
        Optional<Tournament> tournamentOpt = tournamentRepository.findById(tournamentId);

        if (tournamentOpt.isPresent()) {
            Tournament tournament = tournamentOpt.get();
            // Logic for performing random draw goes here
            return new ArrayList<>(); // Return matches after drawing
        }
        throw new IllegalArgumentException("Tournament not found");
    }

    @Override
    public Tournament updateTournamentStatus(Long tournamentId, String status) {
        Optional<Tournament> tournamentOpt = tournamentRepository.findById(tournamentId);

        if (tournamentOpt.isPresent()) {
            Tournament tournament = tournamentOpt.get();
            tournament.setStatus(status);
            return tournamentRepository.save(tournament);
        }
        throw new IllegalArgumentException("Tournament not found");
    }

    @Override
    public Tournament recordMatchResult(Long tournamentId, Long matchId, String result) {
        Optional<Tournament> tournamentOpt = tournamentRepository.findById(tournamentId);

        if (tournamentOpt.isPresent()) {
            Tournament tournament = tournamentOpt.get();
            // Logic to find the match and update the result
            return tournamentRepository.save(tournament);
        }
        throw new IllegalArgumentException("Tournament not found");
    }

    @Override
    public boolean validateRegistrationPeriod(Long tournamentId) {
        Optional<Tournament> tournamentOpt = tournamentRepository.findById(tournamentId);

        if (tournamentOpt.isPresent()) {
            Tournament tournament = tournamentOpt.get();
            return tournament.checkRegistrationPeriod();
        }
        throw new IllegalArgumentException("Tournament not found");
    }
}
