package com.amateuraces.match;

import java.util.List;

import org.springframework.stereotype.Service;

import com.amateuraces.player.Player;
import com.amateuraces.player.PlayerRepository;
import com.amateuraces.tournament.Tournament;
import com.amateuraces.tournament.TournamentNotFoundException;
import com.amateuraces.tournament.TournamentRepository;

import jakarta.transaction.Transactional;


@Service
public class MatchServiceImpl implements MatchService {

    private MatchRepository matchRepository;
    private PlayerRepository playerRepository;
    private TournamentRepository tournamentRepository;

    public MatchServiceImpl(MatchRepository matchRepository, PlayerRepository playerRepository, TournamentRepository tournamentRepository) {
        this.matchRepository = matchRepository;
        this.playerRepository = playerRepository;
        this.tournamentRepository = tournamentRepository;
    }

    @Override
    public List<Match> listMatches() {
        return matchRepository.findAll();
    }

    @Override
    public Match getMatch(Long id) {
        return matchRepository.findById(id).orElse(null);
    }

    @Override
    public Match addMatch(Match match) {
        return matchRepository.save(match);
    }

    /**
     * Remove a match with the given id
     * Spring Data JPA does not return a value for delete operation
     * Cascading: removing a match will also remove all its associated reviews
     */
    @Override
    public void deleteMatch(Long id) {
        // Check if the match exists before attempting to delete
        if (!matchRepository.existsById(id)) {
            throw new MatchNotFoundException(id);
        }

        // If the match exists, delete them
        matchRepository.deleteById(id);
    }

    @Transactional
    public Match updateMatch(Long matchId, Match updatedMatchInfo, Long tournamentId) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new MatchNotFoundException(matchId));

        // Prevent update if match is already completed
        if ("Completed".equals(match.getStatus())) {
            throw new IllegalArgumentException("Match has already been completed and cannot be updated.");
        }

        // Update match details
        String score = updatedMatchInfo.getScore();
        match.setScore(score);
        match.setStatus("Completed");

        Player winner = match.getPlayer1();
        if (Integer.parseInt(score.split("-")[0]) < Integer.parseInt(score.split("-")[1])) winner = match.getPlayer2();
        match.setWinner(winner);

        // Save the updated match
        matchRepository.save(match);

        // Promote winner if match is completed
        if (match.getStatus().equals("Completed") && match.getWinner() != null) {
            updatePlayerElos(match);
            promoteWinnerToNextMatch(match, tournamentId);
        }

        return match;
    }

    @Transactional
    public void promoteWinnerToNextMatch(Match match, Long tournamentId) {

        if (!match.getStatus().equals("Completed") || match.getWinner() == null) {
            throw new IllegalArgumentException("Match is not completed or winner is not set.");
        }

        Player winner = match.getWinner();
        Match nextMatch = match.getNextMatch();

        if (nextMatch != null) {
            if (nextMatch.getPlayer1() == null) {
                nextMatch.setPlayer1(winner);
            } else if (nextMatch.getPlayer2() == null) {
                nextMatch.setPlayer2(winner);
            } else {
                throw new IllegalStateException("Next match already has both players assigned.");
            }
            matchRepository.save(nextMatch);
        } else {
            // This is the final match; update the tournament winner
            Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new TournamentNotFoundException(tournamentId));
            tournament.setChampion(winner);
            tournamentRepository.save(tournament);
        }
    }

    private void updatePlayerElos(Match match) {
        Player winner = match.getWinner();
        Player loser = match.getPlayer1();
        
        if (winner == match.getPlayer1()) loser = match.getPlayer2();

        winner.setMatchesPlayed(winner.getMatchesPlayed() + 1);
        winner.setMatchesWon(winner.getMatchesWon() + 1);
        loser.setMatchesPlayed(loser.getMatchesPlayed() + 1);

        // Calculate Elo adjustments
        int eloGain = calculateEloGain(winner, loser);
        winner.setElo(winner.getElo() + eloGain);
        loser.setElo(Math.max(loser.getElo() - eloGain, 0));

        // Save Elo adjustments
        playerRepository.save(winner);
        playerRepository.save(loser);
    }

    private int calculateEloGain(Player winner, Player loser) {
        int K = 32; // K-factor in Elo rating system
        double eloDifference = loser.getElo() - winner.getElo();
        double expectedScore = 1 / (1 + Math.pow(10, eloDifference / 400.0));

        double actualScore = 1.0; // Winner gets a score of 1
        int eloGain = (int) Math.round(K * (actualScore - expectedScore));
        return eloGain;
    }
}