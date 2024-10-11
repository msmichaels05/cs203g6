package com.amateuraces.match;

import java.util.List;

import org.springframework.stereotype.Service;

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
}