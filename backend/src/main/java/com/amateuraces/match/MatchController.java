package com.amateuraces.match;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.amateuraces.player.PlayerRepository;
import com.amateuraces.tournament.Tournament;
import com.amateuraces.tournament.TournamentNotFoundException;
import com.amateuraces.tournament.TournamentRepository;
import com.amateuraces.user.UserRepository;

import jakarta.validation.Valid;

@RestController
public class MatchController {

    private final MatchService matchService;
    private final TournamentRepository tournamentRepository;
    private final PlayerRepository playerRepository;
    private final UserRepository userRepository;

    @Autowired
    public MatchController(MatchService matchService, TournamentRepository tournamentRepository,
                           PlayerRepository playerRepository, UserRepository userRepository) {
        this.matchService = matchService;
        this.tournamentRepository = tournamentRepository;
        this.playerRepository = playerRepository;
        this.userRepository = userRepository;
    }

    /**
     * List all matches in the system
     *
     * @return list of all matches
     */
    @GetMapping("/matches")
    public List<Match> getMatches() {
        return matchService.listMatches();
    }

    /**
     * Get match by ID
     *
     * @param id
     * @return match with the given id
     */
    @GetMapping("/matches/{id}")
    public Match getMatch(@PathVariable Long id) {
        Match match = matchService.getMatch(id);
        if (match == null) {
            throw new MatchNotFoundException(id);
        }
        return match;
    }

    /**
     * Remove a match
     *
     * @param tournamentId
     * @param matchId
     */
    @DeleteMapping("/tournaments/{tournamentId}/matches/{matchId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMatch(@PathVariable Long tournamentId,
                            @PathVariable Long matchId) {
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new TournamentNotFoundException(tournamentId));

        Match match = matchService.getMatch(matchId);
        if (match == null || !match.getTournament().getId().equals(tournamentId)) {
            throw new MatchNotFoundException(matchId);
        }

        matchService.deleteMatch(matchId);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/tournaments/{tournamentId}/matches")
    public Match addMatch(@PathVariable Long tournamentId,
                          @Valid @RequestBody Match match) {
        // Retrieve the tournament and check for existence
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElse(null);

        match.setTournament(tournament);

        // Save and return the new match
        return matchService.addMatch(match);
    }

    /**
     * Record match result
     *
     * @param tournamentId
     * @param matchId
     * @param winnerId
     * @param loserId
     * @param score
     */
    @PostMapping("/tournaments/{tournamentId}/matches/{matchId}/result")
    public Match recordMatchResult(@PathVariable Long tournamentId,
                                   @PathVariable Long matchId,
                                   @RequestParam Long winnerId,
                                   @RequestParam Long loserId,
                                   @RequestParam String score) {
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new TournamentNotFoundException(tournamentId));

        Match match = matchService.getMatch(matchId);
        if (match == null || !match.getTournament().getId().equals(tournamentId)) {
            throw new MatchNotFoundException(matchId);
        }

        return matchService.recordMatchResult(matchId, winnerId, loserId, score);
    }

    /**
     * Update match score
     *
     * @param tournamentId
     * @param matchId
     * @param newScore
     */
    @PutMapping("/tournaments/{tournamentId}/matches/{matchId}")
    public Match updateRecordMatchScore(@PathVariable Long tournamentId,
                                        @PathVariable Long matchId,
                                        @RequestParam String newScore) {
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new TournamentNotFoundException(tournamentId));

        Match match = matchService.getMatch(matchId);
        if (match == null || !match.getTournament().getId().equals(tournamentId)) {
            throw new MatchNotFoundException(matchId);
        }

        return matchService.updateRecordMatchScore(matchId, newScore);
    }
}