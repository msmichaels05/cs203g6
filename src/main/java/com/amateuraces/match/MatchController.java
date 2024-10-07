package com.amateuraces.match;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.amateuraces.player.Player;
import com.amateuraces.tournament.Tournament;

import java.util.*;



@RestController
@RequestMapping("/api/matches")
public class MatchController {

    private final MatchService matchService;
    private final TournamentService tournamentService;  // Inject TournamentService

    // Constructor Injection for MatchService and TournamentService
    public MatchController(MatchService matchService, TournamentService tournamentService) {
        this.matchService = matchService;
        this.tournamentService = tournamentService;  // Inject TournamentService
    }

    /**
     * Get all matches.
     * @return List of matches.
     */
    @GetMapping("/")
    public List<Match> viewMatches() {
        return matchService.listMatches();  // Get all matches
    }

    /**
     * Add a new match within a tournament.
     * @param tournamentId the ID of the tournament the match belongs to
     * @param match the match details
     * @return the added match
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/tournament/{tournamentId}")
    public Match addMatch(
            @PathVariable Long tournamentId, 
            @RequestBody Match match) {
        
        // Check if the tournament exists
        Tournament tournament = tournamentService.getTournamentById(tournamentId);
        if (tournament == null) {
            throw new IllegalArgumentException("Tournament not found");
        }

        // Set the match's tournament
        match.setTournament(tournament);

        // Add match to the tournament
        return matchService.addMatch(match);
    }

    /**
     * Register a player for a match in the tournament.
     * @param tournamentId the ID of the tournament
     * @param matchId the ID of the match
     * @param playerId the ID of the player
     * @return Success message
     */
    @PostMapping("/{tournamentId}/matches/{matchId}/register")
    public String registerPlayerForMatch(
            @PathVariable Long tournamentId, 
            @PathVariable Long matchId, 
            @RequestParam Long playerId) {

        // Ensure the player is registered for the tournament
        Tournament tournament = tournamentService.getTournamentById(tournamentId);
        if (!tournamentService.isPlayerRegistered(tournamentId, playerId)) {
            throw new IllegalArgumentException("Player not registered for the tournament");
        }

        // Register player for the match
        matchService.registerPlayerForMatch(matchId, playerId);

        return "Player registered for the match!";
    }

    /**
     * View the match schedule for a tournament.
     * @param tournamentId the ID of the tournament
     * @return List of matches scheduled
     */
    @GetMapping("/tournament/{tournamentId}/schedule")
    public List<Match> viewTournamentMatchSchedule(@PathVariable Long tournamentId) {
        return matchService.getTournamentMatchSchedule(tournamentId);  // Get match schedule for tournament
    }

    // /**
    //  * Record the result of a match.
    //  * @param matchId the ID of the match
    //  * @param winnerId the ID of the winner
    //  * @return Success message
    //  */
    // @PostMapping("/{matchId}/result")
    // public String recordMatchResult(
    //         @PathVariable Long matchId, 
    //         @RequestParam Long winnerId) {

    //     matchService.recordMatchResult(matchId, winnerId);  // Record match result
    //     return "Match result recorded!";
    // }
}