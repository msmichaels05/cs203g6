package com.amateuraces.match;

import java.util.List;

import org.springframework.stereotype.Service;

import com.amateuraces.player.Player;
import com.amateuraces.player.PlayerNotFoundException;
import com.amateuraces.player.PlayerRepository;

/*This implementation is meant for business logic,which could be added later*Currently,it does not have much in terms of the business logic yet*/

@Service
public class MatchServiceImpl implements MatchService {

    private MatchRepository matchRepository;
    private PlayerRepository playerRepository;


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
    //     return matchRepository.findById(id).map(match -> {
    //         match.setWinner(newMatchInfo.getWinner());
    //         return matchRepository.save(match);
    //     }).orElse(null);
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
        //Check if match exsits
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new MatchNotFoundException(matchId));

        //Check if either player exists
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

        //Store elo change in case if need to reverse elo
        match.setElo(winner.changedElo(loser.getElo(), true));

        // save match into repo
        return matchRepository.save(match);
    }

    // public static double expectedProbability(double rating1, double rating2) {
    //     return 1.0 / (1 + Math.pow(10, (rating2 - rating1) / 400.0));
    // }
    
    // public static void updateElo(Player winner, Player loser) {

    //     int kFactor = 32;
    //     double eloWinner = winner.getElo();
    //     double eloLoser = loser.getElo();

    //     double prob1 = expectedProbability(winner, loser);
    //     double prob2 = expectedProbability(loser, winner);

    //     winner.setElo(winner + kFactor * (1 - prob1));
    //     loser.setElo(loser + kFactor * (1 - prob2));
    //     playerRepository.save(winner);
    //     playerRepository.save(loser);
    // }

    @Override
    public Match updateRecordMatchScore(Long matchId, String newScore) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new MatchNotFoundException(matchId));

        //Set new score for match
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
}