package com.amateuraces.match;

<<<<<<< HEAD
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
=======
import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
public class MatchController {
    private final MatchService matchService;
    
    public MatchController(MatchService ps){
        this.matchService = ps ; 
    }

        /**
     * List all matchs in the system
     * @return list of all matchs
     */
     @GetMapping("/matches")
        public List<Match> getMatches(){
            return matchService.listMatches();
        }

    /**
     * Search for match with the given id
     * If there is no match with the given "id", throw a MatchNotFoundException
     * @param id
     * @return match with the given id
     */
    @GetMapping("/matches/{id}")
    public Match getMatch(@PathVariable Long id){
        Match match = matchService.getMatch(id);

        // Need to handle "match not found" error using proper HTTP status code
        // In this case it should be HTTP 404
        if(match == null) throw new MatchNotFoundException(id);
        return matchService.getMatch(id);

    }

 /**
     * Add a new match with POST request to "/matchs"
     * Note the use of @RequestBody
     * @param match
     * @return list of all matchs
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/matches")
    public Match addMatch(@RequestBody Match match){
        return matchService.addMatch(match);
    }

        /**
     * If there is no match with the given "id", throw a MatchNotFoundException
     * @param id
     * @param newMatchInfo
     * @return the updated, or newly added book
     */
    @PutMapping("/matches/{id}")
    public Match updatMatch(@PathVariable Long id, @Valid @RequestBody Match newMatchInfo){
        Match match = matchService.updateMatch(id, newMatchInfo);
        if(match == null) throw new MatchNotFoundException(id);
        
        return match;
    }

        /**
     * Remove a match with the DELETE request to "/matchs/{id}"
     * If there is no match with the given "id", throw a MatchNotFoundException
     * @param id
     */
    @DeleteMapping("/matches/{id}")
    public void deleteMatch(@PathVariable Long id){
        try{
            matchService.deleteMatch(id);
         }catch(EmptyResultDataAccessException e) {
            throw new MatchNotFoundException(id);
         }
    }
}
>>>>>>> a84a5a3c7149033df22ad0b28304fe880ce57d68
