package com.amateuraces.match;

<<<<<<< HEAD
import com.amateuraces.player.Player;
import com.amateuraces.player.PlayerRepository;
import com.amateuraces.tournament.Tournament;
import com.amateuraces.tournament.TournamentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
=======
import java.util.List;

import org.springframework.stereotype.Service;

/*This implementation is meant for business logic,which could be added later*Currently,it does not have much in terms of the business logic yet*/
>>>>>>> a84a5a3c7149033df22ad0b28304fe880ce57d68

@Service
public class MatchServiceImpl implements MatchService {

<<<<<<< HEAD
    private final MatchRepository matchRepository;
    private final PlayerRepository playerRepository;
    private final TournamentRepository tournamentRepository;

    @Autowired
    public MatchServiceImpl(MatchRepository matchRepository, PlayerRepository playerRepository, TournamentRepository tournamentRepository) {
        this.matchRepository = matchRepository;
        this.playerRepository = playerRepository;
        this.tournamentRepository = tournamentRepository;
    }

    @Override
    public Match addMatch(Match match) {
        return matchRepository.save(match);
    }

    @Override
    public void registerPlayerForMatch(Long matchId, Long playerId) {
        Match match = getMatchById(matchId);
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new IllegalArgumentException("Player not found"));

        // Assuming you want to add the player to the match somehow
        if (match.getPlayer1() == null) {
            match.setPlayer1(player);
        } else if (match.getPlayer2() == null) {
            match.setPlayer2(player);
        } else {
            throw new IllegalArgumentException("Match already has two players registered");
        }

        matchRepository.save(match);
=======
    private final MatchRepository matches;

    public MatchServiceImpl(MatchRepository matches){
        this.matches = matches;
>>>>>>> a84a5a3c7149033df22ad0b28304fe880ce57d68
    }

    @Override
    public List<Match> listMatches() {
<<<<<<< HEAD
        return matchRepository.findAll();
    }

    @Override
    public List<Match> getTournamentMatchSchedule(Long tournamentId) {
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new IllegalArgumentException("Tournament not found"));
        return matchRepository.findByTournament(tournament);
    }

    @Override
    public void recordMatchResult(Long matchId, Player winner) {
        Match match = getMatchById(matchId);
        match.setWinner(winner);
        matchRepository.save(match);

        // Update Elo ratings or other player stats based on the result
        Player player1 = match.getPlayer1();
        Player player2 = match.getPlayer2();
        if (player1 != null && player2 != null) {
            if (winner.equals(player1)) {
                player1.updateElo(player2, true);   // player1 won
                player2.updateElo(player1, false);  // player2 lost
            } else {
                player1.updateElo(player2, false);  // player1 lost
                player2.updateElo(player1, true);   // player2 won
            }

            playerRepository.save(player1);
            playerRepository.save(player2);
        }
    }

    @Override
    public void rescheduleMatch(Long matchId, LocalDateTime newDateTime) {
        Match match = getMatchById(matchId);
        match.setScheduledDateTime(newDateTime);
        match.setStatus("rescheduled");
        matchRepository.save(match);
    }

    @Override
    public void cancelMatch(Long matchId) {
        Match match = getMatchById(matchId);
        match.setStatus("cancelled");
        matchRepository.save(match);
    }

    private Match getMatchById(Long matchId) {
        return matchRepository.findById(matchId)
                .orElseThrow(() -> new IllegalArgumentException("Match not found"));
=======
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
>>>>>>> a84a5a3c7149033df22ad0b28304fe880ce57d68
    }
}