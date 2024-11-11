package com.amateuraces.match;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.LongAdder;

import org.springframework.stereotype.Service;

import com.amateuraces.player.Player;
import com.amateuraces.player.PlayerNotFoundException;
import com.amateuraces.player.PlayerRepository;
import com.amateuraces.tournament.Tournament;
import com.amateuraces.tournament.TournamentRepository;

import jakarta.transaction.Transactional;

/*This implementation is meant for business logic,which could be added later*Currently,it does not have much in terms of the business logic yet*/

@Service
public class MatchServiceImpl implements MatchService {

    private MatchRepository matchRepository;
    private PlayerRepository playerRepository;
    private TournamentRepository tournamentRepository;

    public MatchServiceImpl(MatchRepository matchRepository, PlayerRepository playerRepository) {
        this.matchRepository = matchRepository;
        this.playerRepository = playerRepository;
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

    // @Override
    // public Match updateMatch(Long id, Match newMatchInfo) {
    // return matchRepository.findById(id).map(match -> {
    // match.setWinner(newMatchInfo.getWinner());
    // return matchRepository.save(match);
    // }).orElse(null);
    // }

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

    @Override
    public Match recordMatchResult(Long matchId, Long winnerId, Long loserId, String score) {
        // Check if match exsits
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new MatchNotFoundException(matchId));

        // Check if either player exists
        Player winner = playerRepository.findById(winnerId)
                .orElseThrow(() -> new PlayerNotFoundException(winnerId));

        Player loser = playerRepository.findById(loserId)
                .orElseThrow(() -> new PlayerNotFoundException(loserId));

        // If either player is part of the match
        if (!match.isPlayerInvolved(winner)) {
            throw new PlayerNotPartOfMatchException(winnerId, matchId);
        }

        if (!match.isPlayerInvolved(loser)) {
            throw new PlayerNotPartOfMatchException(loserId, matchId);
        }

        // Update the match result
        match.setMatchResult(winner, score);

        // Update both players' statistics
        winner.updateWinsAndLosses(true);
        loser.updateWinsAndLosses(false);

        // Update ELO scores (optional, depends on your system)
        winner.updateElo(loser.getElo(), true);
        loser.updateElo(winner.getElo(), false);

        // Store elo change in case if need to reverse elo
        match.setElo(winner.changedElo(loser.getElo(), true));

        // save match into repo
        return matchRepository.save(match);
    }

    // public static double expectedProbability(double rating1, double rating2) {
    // return 1.0 / (1 + Math.pow(10, (rating2 - rating1) / 400.0));
    // }

    // public static void updateElo(Player winner, Player loser) {

    // int kFactor = 32;
    // double eloWinner = winner.getElo();
    // double eloLoser = loser.getElo();

    // double prob1 = expectedProbability(winner, loser);
    // double prob2 = expectedProbability(loser, winner);

    // winner.setElo(winner + kFactor * (1 - prob1));
    // loser.setElo(loser + kFactor * (1 - prob2));
    // playerRepository.save(winner);
    // playerRepository.save(loser);
    // }

    @Override
    public Match updateRecordMatchScore(Long matchId, String newScore) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new MatchNotFoundException(matchId));

        // Set new score for match
        match.setScore(newScore);

        // Save updated match
        return matchRepository.save(match);
    }

    @Override
    public Match updateRecordMatchWinner(Long matchId, Long oldWinnerId, Long newWinnerId, String newScore) {
        // Fetch the match and check if it exist
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new MatchNotFoundException(matchId));

        // Fetch the players and check it either player exist
        Player oldWinner = playerRepository.findById(oldWinnerId)
                .orElseThrow(() -> new PlayerNotFoundException(oldWinnerId));
        Player newWinner = playerRepository.findById(newWinnerId)
                .orElseThrow(() -> new PlayerNotFoundException(newWinnerId));

        // If either player part of the match
        if (!match.isPlayerInvolved(oldWinner)) {
            throw new PlayerNotPartOfMatchException(oldWinnerId, matchId);
        }

        if (!match.isPlayerInvolved(newWinner)) {
            throw new PlayerNotPartOfMatchException(newWinnerId, matchId);
        }

        // Update the match result with new winner and loser
        match.setMatchResult(newWinner, newScore);

        // Revert ELO
        // oldWinner.revertElo(match, true);
        // newWinner.revertElo(match, false);
        newWinner.updateElo(oldWinner.getElo(), true);
        oldWinner.updateElo(newWinner.getElo(), false);

        // Revert wins and losses
        oldWinner.revertWinsAndLosses(true); // Revert old winner's win
        newWinner.revertWinsAndLosses(false); // Revert old loser's loss
        newWinner.updateWinsAndLosses(true);
        oldWinner.updateWinsAndLosses(false);

        return matchRepository.save(match);
    }

    // @Override
    // public Player updatePlayer(Long id, Player newPlayerInfo) {
    // return playerRepository.findById(id).map(player -> {
    // player.setName(newPlayerInfo.getName());
    // player.setGender(newPlayerInfo.getGender());
    // player.setAge(newPlayerInfo.getAge());
    // player.setElo(newPlayerInfo.getElo());
    // return playerRepository.save(player);
    // }).orElse(null);
    // }

    // @Transactional
    // public Match updateMatch(Long matchId, Match newMatchInfo) {
    // // Retrieve the match by matchId
    // Match match = matchRepository.findById(matchId)
    // .orElseThrow(() -> new MatchNotFoundException(matchId));

    // // Update basic match details
    // match.setScore(newMatchInfo.getScore());

    // // Handle Elo updates and promotion if the match is over after the update
    // if (match.isCompleted()) {
    // updatePlayerElos(match);
    // handleMatchPromotion(match);
    // }

    // // Save and return the updated match
    // return matchRepository.save(match);
    // }

    // private void updatePlayerElos(Match match) {
    // Player player1 = match.getPlayer1();
    // Player player2 = match.getPlayer2();
    // Player winner = match.getWinner();
    // Player loser = player1;
    // if (winner == player1) {
    // loser = player2;
    // }

    // // Calculate Elo adjustments
    // int eloGain = calculateEloGain(player1, player2, match.getScore());
    // winner.setElo(winner.getElo() + eloGain);
    // loser.setElo(Math.max(loser.getElo() - eloGain, 0));

    // // Save Elo adjustments
    // playerRepository.save(winner);
    // playerRepository.save(loser);
    // }

    // public static int calculateEloGain(Player player1, Player player2, String
    // score) {
    // int K = 32; // K-factor in Elo rating system
    // double eloDifference = player2.getElo() - player1.getElo();
    // double expectedScore = 1 / (1 + Math.pow(10, eloDifference / 400.0));

    // int actualScore = calculateActualScore(score);
    // int eloGain = (int) Math.round(K * (actualScore - expectedScore));
    // return eloGain;
    // }

    // private static int calculateActualScore(String score) {
    // String[] scores = score.split("-");
    // int score1 = Integer.parseInt(scores[0]);
    // int score2 = Integer.parseInt(scores[1]);
    // if (score1 > score2) {
    // return 1;
    // }
    // return 0;
    // }

    // public static int getPerformancePoint(String score) {
    // int totalScoreDifference = calculateTotalScoreDifference(score);
    // return totalScoreDifference;
    // }

    // private static int calculateTotalScoreDifference(String score) {
    // int totalScore1 = 0;
    // int totalScore2 = 0;
    // String[] scores = score.split("-");
    // int score1 = Integer.parseInt(scores[0]);
    // int score2 = Integer.parseInt(scores[1]);
    // totalScore1 += score1;
    // totalScore2 += score2;

    // return totalScore1 - totalScore2;
    // }

    // private void handleMatchPromotion(Match match) {
    // Player winner = match.getWinner();
    // promoteToNextRound(match, winner);
    // }

    // @Transactional
    // public Match updateMatch(Long matchId, Match newMatchInfo) {
    // // Retrieve the match by matchId
    // Match match = matchRepository.findById(matchId)
    // .orElseThrow(() -> new MatchNotFoundException(matchId));

    // // Update match details
    // match.setScore(newMatchInfo.getScore());
    // match.setCompleted(newMatchInfo.isCompleted());
    // match.setWinner(newMatchInfo.getWinner());

    // // Handle Elo updates and promotion if the match is completed
    // if (match.isCompleted()) {
    // updatePlayerElos(match);
    // handleMatchPromotion(match);
    // }

    // // Save and return the updated match
    // return matchRepository.save(match);
    // }

    @Transactional
    public Match updateMatch(Long matchId, Match updatedMatchInfo) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new MatchNotFoundException(matchId));

        // Update match details
        match.setScore(updatedMatchInfo.getScore());
        match.setCompleted(updatedMatchInfo.isCompleted());
        match.setWinner(updatedMatchInfo.getWinner());

        // Save the updated match
        matchRepository.save(match);

        // Promote winner if match is completed
        if (match.isCompleted() && match.getWinner() != null) {
            updatePlayerElos(match);
            promoteWinnerToNextMatch(match);
        }

        return match;
    }

    private void updatePlayerElos(Match match) {
        Player winner = match.getWinner();
        Player loser = match.getPlayer1();
        if (winner == match.getPlayer1())
            loser = match.getPlayer2();

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

    // private void handleMatchPromotion(Match match) {
    // Player winner = match.getWinner();
    // promoteToNextRound(match, winner);
    // }

    // private void promoteToNextRound(Match match, Player winner) {
    // // Get parent match ID and promote winner to the next round
    // Long parentMatchId = (match.getId() - 2) / 2;
    // matchRepository.findById(parentMatchId).ifPresent(parentMatch -> {
    // if (match.getId() % 2 == 0) {
    // parentMatch.setPlayer2(winner);
    // } else {
    // parentMatch.setPlayer1(winner);
    // }
    // matchRepository.save(parentMatch);
    // });
    // }

    @Transactional
    public void promoteWinnerToNextMatch(Match match) {

        if (!match.isCompleted() || match.getWinner() == null) {
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
            Tournament tournament = match.getTournament();
            tournament.setChampion(winner);
            tournamentRepository.save(tournament);
        }
    }
}