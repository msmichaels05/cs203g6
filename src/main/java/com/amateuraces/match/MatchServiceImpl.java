package com.amateuraces.match;

import static org.mockito.ArgumentMatchers.matches;

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
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new MatchNotFoundException(matchId)); // This line should find the match

        Player winner = playerRepository.findById(winnerId)
                .orElseThrow(() -> new PlayerNotFoundException(winnerId));

        Player loser = playerRepository.findById(loserId)
                .orElseThrow(() -> new PlayerNotFoundException(loserId));

        if (!match.isPlayerInvolved(winner)) {
            throw new PlayerNotPartOfMatchException(winnerId, matchId); // If either player is not part of the match
        }

        if (!match.isPlayerInvolved(loser)) {
            throw new PlayerNotPartOfMatchException(loserId, matchId); // If either player is not part of the match
        }

        // Update the match result
        match.setMatchResult(winner, loser, score);

        // Update both players' statistics
        winner.updateWinsAndLosses(true);
        loser.updateWinsAndLosses(false);

        // Update ELO scores (optional, depends on your system)
        winner.updateElo(loser.getElo(), true);
        loser.updateElo(winner.getElo(), false);

        match.setElo(winner.changedElo(loser.getElo(), true));

        return matchRepository.save(match); // Persist the match result
    }

    @Override
    public Match updateRecordMatchScore(Long matchId, String newScore) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new MatchNotFoundException(matchId));

        match.setScore(newScore); // Assuming there's a setScore method in the Match class

        return matchRepository.save(match); // Save updated match
    }

    @Override
    public Match updateRecordMatchWinner(Long matchId, Long oldWinnerId, Long newWinnerId, String newScore) {
        // Fetch the match
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new MatchNotFoundException(matchId));

        // Fetch the players
        Player oldWinner = playerRepository.findById(oldWinnerId)
                .orElseThrow(() -> new PlayerNotFoundException(oldWinnerId));
        Player newWinner = playerRepository.findById(newWinnerId)
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
        oldWinner.revertElo(match, true);
        newWinner.revertElo(match, false);
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