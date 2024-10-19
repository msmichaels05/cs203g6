package com.amateuraces.match;

import java.util.List;

import org.springframework.stereotype.Service;

import com.amateuraces.player.Player;
import com.amateuraces.player.PlayerNotFoundException;
import com.amateuraces.player.PlayerRepository;

/*This implementation is meant for business logic,which could be added later*Currently,it does not have much in terms of the business logic yet*/

@Service
public class MatchServiceImpl implements MatchService {

    private final MatchRepositoryCompleted completedMatches;
    private final MatchRepositoryIncomplete incompleteMatches;
    private final PlayerRepository players;

    public MatchServiceImpl(MatchRepositoryCompleted completedMatches, MatchRepositoryIncomplete incompleteMatches,
            PlayerRepository players) {
        this.completedMatches = completedMatches;
        this.incompleteMatches = incompleteMatches;
        this.players = players;
    }

    @Override
    public List<Match> listIncompleteMatches() {
        return completedMatches.findAll();
    }

    @Override
    public List<Match> listCompletedMatches() {
        return completedMatches.findAll();
    }

    @Override
    public Match getIncompleteMatch(Long id) {
        return incompleteMatches.findById(id).orElse(null);
    }

    @Override
    public Match getCompletedMatch(Long id) {
        return completedMatches.findById(id).orElse(null);
    }

    @Override
    public Match addMatch(Match match) {
        return incompleteMatches.save(match);
    }

    // @Override
    // public Match updateMatch(Long id, Match newMatchInfo) {
    // return incompleteMatches.findById(id).map(match -> {
    // match.setWinner(newMatchInfo.getWinner());
    // return completedMatches.save(match);
    // }).orElse(null);
    // }

    @Override
    public void deleteMatch(Long id) {
        // Check if the match exists before attempting to delete
        if (!incompleteMatches.existsById(id)) {
            throw new MatchNotFoundException(id);
        }

        // If the match exists, delete them
        incompleteMatches.deleteById(id);
    }

    @Override
    public Match recordMatchResult(Long matchId, Long winnerId, Long loserId, String score) {
        Match match = incompleteMatches.findById(matchId)
                .orElseThrow(() -> new MatchNotFoundException(matchId)); // This line should find the match

        Player winner = players.findById(winnerId)
                .orElseThrow(() -> new PlayerNotFoundException(winnerId));

        Player loser = players.findById(loserId)
                .orElseThrow(() -> new PlayerNotFoundException(loserId));

        if (!match.isPlayerInvolved(winner)) {
            throw new PlayerNotPartOfMatchException(winnerId, matchId); // If either player is not part of the match
        }

        if (!match.isPlayerInvolved(loser)) {
            throw new PlayerNotPartOfMatchException(loserId, matchId); // If either player is not part of the match
        }

        // Update the match result
        match.setMatchResult(winner, loser, score);

        // Update the match status to completed
        match.setCompleted(true);

        // Update both players' statistics
        winner.updateWinsAndLosses(true);
        loser.updateWinsAndLosses(false);

        // Update ELO scores (optional, depends on your system)
        winner.updateElo(loser.getElo(), true);
        loser.updateElo(winner.getElo(), false);

        // Save the changes
        players.save(winner);
        players.save(loser);

        return completedMatches.save(match); // Persist the match result
    }

    @Override
    public Match updateRecordMatchScore(Long matchId, String newScore) {
        Match match = completedMatches.findById(matchId)
                .orElseThrow(() -> new MatchNotFoundException(matchId));

        match.setScore(newScore); // Assuming there's a setScore method in the Match class

        return completedMatches.save(match); // Save updated match
    }

    @Override
    public Match updateRecordMatchWinner(Long matchId, Long oldWinnerId, Long newWinnerId, String newScore) {
        // Fetch the match
        Match match = completedMatches.findById(matchId)
                .orElseThrow(() -> new MatchNotFoundException(matchId));

        // Fetch the players
        Player oldWinner = players.findById(oldWinnerId)
                .orElseThrow(() -> new PlayerNotFoundException(oldWinnerId));
        Player newWinner = players.findById(newWinnerId)
                .orElseThrow(() -> new PlayerNotFoundException(newWinnerId));

        if (!match.isPlayerInvolved(oldWinner)) {
            throw new PlayerNotPartOfMatchException(oldWinnerId, matchId); // If either player is not part of the match
        }

        if (!match.isPlayerInvolved(newWinner)) {
            throw new PlayerNotPartOfMatchException(newWinnerId, matchId); // If either player is not part of the match
        }
        
        // Update the match result with new winner and loser
        match.setMatchResult(newWinner, oldWinner, newScore);
        match.setCompleted(true);
        
        // Revert ELO
        oldWinner.revertElo(newWinner.getElo(), true);
        newWinner.revertElo(oldWinner.getElo(), false);
        newWinner.updateElo(oldWinner.getElo(), true);
        oldWinner.updateElo(newWinner.getElo(), false);

        // Revert wins and losses
        oldWinner.revertWinsAndLosses(true); // Revert old winner's win
        newWinner.revertWinsAndLosses(false); // Revert old loser's loss
        newWinner.updateWinsAndLosses(true);
        oldWinner.updateWinsAndLosses(false);

        return completedMatches.save(match);
    }
}