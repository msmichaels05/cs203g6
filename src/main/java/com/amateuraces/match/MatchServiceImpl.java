package com.amateuraces.match;

import java.util.List;

import org.springframework.stereotype.Service;

import com.amateuraces.player.Player;

/*This implementation is meant for business logic,which could be added later*Currently,it does not have much in terms of the business logic yet*/

@Service
public class MatchServiceImpl implements MatchService {

    private final MatchRepository matches;

    public MatchServiceImpl(MatchRepository matches){
        this.matches = matches;
    }

    @Override
    public List<Match> listMatches() {
        return matches.findAll();
    }

    @Override
    public Match getMatch(Long id){
        return matches.findById(id).orElse(null);
    }

    @Override
    public Match addMatch(Match match) {
        return matches.save(match);
    }

    @Override
    public Match updateMatch(Long id, Match newMatchInfo) {
        return matches.findById(id).map(match -> {match.setWinner(newMatchInfo.getWinner());
            return matches.save(match);
        }).orElse(null);
    }

    /**
     * Remove a match with the given id
     * Spring Data JPA does not return a value for delete operation
     * Cascading: removing a match will also remove all its associated reviews
     */
    @Override
    public void deleteMatch(Long id){
    // Check if the match exists before attempting to delete
    if (!matches.existsById(id)) {
        throw new MatchNotFoundException(id);
    }
    
    // If the match exists, delete them
    matches.deleteById(id);
    }

    @Override
    public Match declareWinner(Long matchId, Long winnerId) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new IllegalArgumentException("Match not found"));
        
        Player winner = playerRepository.findById(winnerId)
                .orElseThrow(() -> new IllegalArgumentException("Player not found"));
        
        match.setWinner(winner); // Set the winner
        return matchRepository.save(match); // Persist the changes
    }

    @Override
    public Match recordMatchResult(Long matchId, Long winnerId, Long loserId, String score) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new IllegalArgumentException("Match not found"));

        Player winner = playerRepository.findById(winnerId)
                .orElseThrow(() -> new IllegalArgumentException("Player not found"));

        Player loser = playerRepository.findById(loserId)
                .orElseThrow(() -> new IllegalArgumentException("Player not found"));

        // Update the match result
        match.setWinner(winner);

        // Update both players' statistics
        winner.updateWinsAndLosses(true);
        loser.updateWinsAndLosses(false);

        // Update ELO scores (optional, depends on your system)
        match.updateElo(loser.getElo(), true);
        match.updateElo(winner.getElo(), false);

        // Save the changes
        playerRepository.save(winner);
        playerRepository.save(loser);

        return matchRepository.save(match); // Persist the match result
    }
}