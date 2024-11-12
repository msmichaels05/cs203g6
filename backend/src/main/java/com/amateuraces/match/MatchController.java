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
    private final MatchRepository matchRepository;
    private final PlayerRepository playerRepository;
    private final UserRepository userRepository;

    @Autowired
    public MatchController(MatchService matchService, TournamentRepository tournamentRepository,
            PlayerRepository playerRepository, UserRepository userRepository, MatchRepository matchRepository) {
        this.matchService = matchService;
        this.tournamentRepository = tournamentRepository;
        this.playerRepository = playerRepository;
        this.userRepository = userRepository;
        this.matchRepository = matchRepository;
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

    @GetMapping("/tournaments/{tournamentId}/matches")
    public List<Match> getMatchesInTournament(@PathVariable Long tournamentId) {
        return matchRepository.findByTournamentId(tournamentId);
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

    @PutMapping("/tournaments/{tournamentId}/matches/{matchId}")
    public Match updateMatch(@PathVariable Long tournamentId,
            @PathVariable Long matchId,
            @RequestBody Match updatedMatchInfo) {

        return matchService.updateMatch(matchId, updatedMatchInfo, tournamentId);
    }
}