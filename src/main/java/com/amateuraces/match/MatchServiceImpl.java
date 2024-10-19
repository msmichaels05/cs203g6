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

    public MatchServiceImpl(MatchRepositoryCompleted completedMatches, MatchRepositoryIncomplete incompleteMatches ,PlayerRepository players) {
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
    //     return incompleteMatches.findById(id).map(match -> {
    //         match.setWinner(newMatchInfo.getWinner());
    //         return completedMatches.save(match);
    //     }).orElse(null);
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

        // Update the match result
        match.setMatchResult(winner, loser, score);

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
}