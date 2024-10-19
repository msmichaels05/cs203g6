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

    @Override
    public Match addMatch(Match match) {
        return matches.save(match);
    }

    @Override
    public Match updateMatch(Long id, Match newMatchInfo) {
        return matches.findById(id).map(match -> {
            match.setWinner(newMatchInfo.getWinner());
            return matches.save(match);
        }).orElse(null);
    }

    /**
     * Remove a match with the given id
     * Spring Data JPA does not return a value for delete operation
     * Cascading: removing a match will also remove all its associated reviews
     */
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

        return matches.save(match); // Persist the match result
    }
}