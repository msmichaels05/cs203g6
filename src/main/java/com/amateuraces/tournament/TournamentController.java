package com.amateuraces.tournament;

import com.amateuraces.player.Player;
import com.amateuraces.match.Match;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tournaments")
public class TournamentController {

    private final TournamentService tournamentService;

    @Autowired
    public TournamentController(TournamentService tournamentService) {
        this.tournamentService = tournamentService;
    }

    /**
     * Create a new tournament.
     * 
     * @param tournament the tournament to be created
     * @return ResponseEntity containing the created tournament
     */
    @PostMapping
    public ResponseEntity<Tournament> createTournament(@RequestBody Tournament tournament) {
        Tournament createdTournament = tournamentService.createTournament(tournament);
        return new ResponseEntity<>(createdTournament, HttpStatus.CREATED);
    }

    /**
     * Set the registration period for a tournament.
     * 
     * @param tournamentId the ID of the tournament
     * @param startDate    the start date of the registration period
     * @param endDate      the end date of the registration period
     * @return ResponseEntity containing the updated tournament
     */
    @PutMapping("/{tournamentId}/registration")
    public ResponseEntity<Tournament> setRegistrationPeriod(
            @PathVariable Long tournamentId, 
            @RequestParam String startDate, 
            @RequestParam String endDate) {
        
        Tournament updatedTournament = tournamentService.setRegistrationPeriod(tournamentId, startDate, endDate);
        return ResponseEntity.ok(updatedTournament);
    }

    /**
     * Add a player to the tournament.
     * 
     * @param tournamentId the ID of the tournament
     * @param playerId     the ID of the player to be added
     * @return ResponseEntity containing the updated tournament
     */
    @PostMapping("/{tournamentId}/players/{playerId}")
    public ResponseEntity<Tournament> addPlayerToTournament(
            @PathVariable Long tournamentId, 
            @PathVariable Long playerId) {
        
        Tournament updatedTournament = tournamentService.addPlayerToTournament(tournamentId, playerId);
        return ResponseEntity.ok(updatedTournament);
    }

    /**
     * Get the list of players registered for the tournament.
     * 
     * @param tournamentId the ID of the tournament
     * @return ResponseEntity containing the list of players
     */
    @GetMapping("/{tournamentId}/players")
    public ResponseEntity<List<Player>> getPlayersInTournament(@PathVariable Long tournamentId) {
        List<Player> players = tournamentService.getPlayersInTournament(tournamentId);
        return ResponseEntity.ok(players);
    }

    /**
     * Get all tournaments.
     * 
     * @return ResponseEntity containing the list of tournaments
     */
    @GetMapping
    public ResponseEntity<List<Tournament>> getAllTournaments() {
        List<Tournament> tournaments = tournamentService.getAllTournaments();
        return ResponseEntity.ok(tournaments);
    }

    /**
     * Perform a randomized draw for the tournament.
     * 
     * @param tournamentId the ID of the tournament
     * @return ResponseEntity containing the list of match pairings after the draw
     */
    @PostMapping("/{tournamentId}/draw")
    public ResponseEntity<List<Match>> performRandomDraw(@PathVariable Long tournamentId) {
        List<Match> matchPairings = tournamentService.performRandomDraw(tournamentId);
        return ResponseEntity.ok(matchPairings);
    }

    /**
     * Update the status of the tournament.
     * 
     * @param tournamentId the ID of the tournament
     * @param status       the new status of the tournament
     * @return ResponseEntity containing the updated tournament
     */
    @PutMapping("/{tournamentId}/status")
    public ResponseEntity<Tournament> updateTournamentStatus(
            @PathVariable Long tournamentId, 
            @RequestParam String status) {
        
        Tournament updatedTournament = tournamentService.updateTournamentStatus(tournamentId, status);
        return ResponseEntity.ok(updatedTournament);
    }

    /**
     * Record the result of a match.
     * 
     * @param tournamentId the ID of the tournament
     * @param matchId     the ID of the match
     * @param result      the result of the match
     * @return ResponseEntity containing the updated tournament
     */
    @PostMapping("/{tournamentId}/matches/{matchId}/result")
    public ResponseEntity<Tournament> recordMatchResult(
            @PathVariable Long tournamentId, 
            @PathVariable Long matchId, 
            @RequestParam String result) {
        
        Tournament updatedTournament = tournamentService.recordMatchResult(tournamentId, matchId, result);
        return ResponseEntity.ok(updatedTournament);
    }

    /**
     * Validate if the registration period is valid.
     * 
     * @param tournamentId the ID of the tournament
     * @return ResponseEntity containing the validation result
     */
    @GetMapping("/{tournamentId}/registration/valid")
    public ResponseEntity<Boolean> validateRegistrationPeriod(@PathVariable Long tournamentId) {
        boolean isValid = tournamentService.validateRegistrationPeriod(tournamentId);
        return ResponseEntity.ok(isValid);
    }
}
