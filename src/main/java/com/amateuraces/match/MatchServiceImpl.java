package com.amateuraces.match;

import java.util.List;

import org.springframework.stereotype.Service;

import com.amateuraces.player.Player;
import com.amateuraces.player.PlayerNotFoundException;
import com.amateuraces.player.PlayerRepository;

/*This implementation is meant for business logic,which could be added later*Currently,it does not have much in terms of the business logic yet*/

@Service
public class MatchServiceImpl implements MatchService {

    private final MatchRepository matches;
    private final PlayerRepository players;

    public MatchServiceImpl(MatchRepository matches, PlayerRepository players) {
        this.matches = matches;
        this.players = players;
    }

    @Override
    public List<Match> listMatches() {
        return matches.findAll();
    }

    @Override
    public Match getMatch(Long id) {
        return matches.findById(id).orElse(null);
    }

    // @Override
    // public Match updateMatch(Long id, Match newMatchInfo) {
    // return incompleteMatches.findById(id).map(match -> {
    // match.setWinner(newMatchInfo.getWinner());
    // return completedMatches.save(match);
    // }).orElse(null);
    // }

    @Override
    public Match addMatch(Match match) {
        return matches.save(match);
    }

    @Override
    public void deleteMatch(Long id) {
        // Check if the match exists before attempting to delete
        if (!matches.existsById(id)) {
            throw new MatchNotFoundException(id);
        }

        // If the match exists, delete them
        matches.deleteById(id);
    }

    @Override
    public Match recordMatchResult(Long matchId, Long winnerId, Long loserId, String score) {
        Match match = matches.findById(matchId)
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
        match.setMatchResult(winner.getName(), loser.getName(), score);

        // Update both players' statistics
        winner.updateWinsAndLosses(true);
        loser.updateWinsAndLosses(false);

        // Update ELO scores (optional, depends on your system)
        winner.updateElo(loser.getElo(), true);
        loser.updateElo(winner.getElo(), false);

        match.setElo(winner.changedElo(loser.getElo(), true));

        return matches.save(match); // Persist the match result
    }

    @Override
    public Match updateRecordMatchScore(Long matchId, String newScore) {
        Match match = matches.findById(matchId)
                .orElseThrow(() -> new MatchNotFoundException(matchId));

        match.setScore(newScore); // Assuming there's a setScore method in the Match class

        return matches.save(match); // Save updated match
    }

    @Override
    public Match updateRecordMatchWinner(Long matchId, Long oldWinnerId, Long newWinnerId, String newScore) {
        // Fetch the match
        Match match = matches.findById(matchId)
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
        match.setMatchResult(newWinner.getName(), oldWinner.getName(), newScore);
        match.setCompleted(true);
        
        // Revert ELO
        oldWinner.revertElo(match, true);
        newWinner.revertElo(match, false);
        newWinner.updateElo(oldWinner.getElo(), true);
        oldWinner.updateElo(newWinner.getElo(), false);

        // Revert wins and losses
        oldWinner.revertWinsAndLosses(true); // Revert old winner's win
        newWinner.revertWinsAndLosses(false); // Revert old loser's loss
        newWinner.updateWinsAndLosses(true);
        oldWinner.updateWinsAndLosses(false);

        return matches.save(match);
    }
}