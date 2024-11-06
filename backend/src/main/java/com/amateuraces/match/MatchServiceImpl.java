package com.amateuraces.match;

import java.util.List;

import org.springframework.stereotype.Service;

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
}