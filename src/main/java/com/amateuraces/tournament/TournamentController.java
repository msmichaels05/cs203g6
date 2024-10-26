package com.amateuraces.tournament;

import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.amateuraces.match.Match;
import com.amateuraces.player.Player;
import com.amateuraces.player.PlayerNotFoundException;

import jakarta.validation.Valid;

@RestController
public class TournamentController {

    private final TournamentService tournamentService;

    public TournamentController(TournamentService tournamentService) {
        this.tournamentService = tournamentService;
    }

    @GetMapping("/tournaments")
        public List<Tournament> getTournaments(){
            return tournamentService.listTournaments();
        }    

    /**
     * Create a new tournament.
     * 
     * @param tournament the tournament to be created
     * @return ResponseEntity containing the created tournament
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/tournaments")
    public Tournament addTournament(@RequestBody Tournament tournament){
        return tournamentService.addTournament(tournament);
    }

            /**
     * Remove a tournament with the DELETE request to "/tournaments/{id}"
     * If there is no tournament with the given "id", throw a TournamentNotFoundException
     * @param id
     */
    @DeleteMapping("/tournaments/{id}")
    public void deleteTournament(@PathVariable Long id){
        try{
            tournamentService.deleteTournament(id);
         }catch(EmptyResultDataAccessException e) {
            throw new TournamentNotFoundException(id);
         }
    }

            /**
     * If there is no tournament with the given "id", throw a TournamentNotFoundException
     * @param id
     * @param newTournamentInfo
     * @return the updated, or newly added book
     */
    @PutMapping("/tournaments/{id}")
    public Tournament updateTournament(@PathVariable Long id, @Valid @RequestBody Tournament newTournamentInfo){
        Tournament Tournament = tournamentService.updateTournament(id,newTournamentInfo);
        if(Tournament == null) throw new TournamentNotFoundException(id);
        
        return Tournament;
    }
    /**
     * Set the registration period for a tournament.
     * 
     * @param tournamentId the ID of the tournament
     * @param startDate    the start date of the registration period
     * @param endDate      the end date of the registration period
     * @return ResponseEntity containing the updated tournament
     */
    // @PutMapping("/tournaments/{tournamentId}/registration")
    // public ResponseEntity<Tournament> setRegistrationPeriod(
    //         @PathVariable Long tournamentId, 
    //         @RequestParam String startDate, 
    //         @RequestParam String endDate) {
        
    //     Tournament updatedTournament = tournamentService.setRegistrationPeriod(tournamentId, startDate, endDate);
    //     return ResponseEntity.ok(updatedTournament);
    // }

    /**
     * Add a player to the tournament.
     * 
     * @param tournamentId the ID of the tournament
     * @param playerId     the ID of the player to be added
     * @return ResponseEntity containing the updated tournament
     */
    @PostMapping("/tournaments/{tournamentId}/players/{playerId}")
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
    @GetMapping("/tournaments/{tournamentId}/players")
    public ResponseEntity<List<Player>> getPlayersInTournament(@PathVariable Long tournamentId) {
        List<Player> players = tournamentService.getPlayersInTournament(tournamentId);
        return ResponseEntity.ok(players);
    }

    /**
     * Get all tournaments.
     * 
     * @return ResponseEntity containing the list of tournaments
     */
    // @GetMapping("/tournaments")
    // public ResponseEntity<List<Tournament>> listTournaments() {
    //     List<Tournament> tournaments = tournamentService.listTournaments();
    //     return ResponseEntity.ok(tournaments);
    // }

    /**
     * Perform a randomized draw for the tournament.
     * 
     * @param tournamentId the ID of the tournament
     * @return ResponseEntity containing the list of match pairings after the draw
     */
    @PostMapping("/tournaments/{tournamentId}/draw")
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
    // @PutMapping("/tournaments/{tournamentId}/status")
    // public ResponseEntity<Tournament> updateTournamentStatus(
    //         @PathVariable Long tournamentId, 
    //         @RequestParam String status) {
        
    //     Tournament updatedTournament = tournamentService.updateTournamentStatus(tournamentId, status);
    //     return ResponseEntity.ok(updatedTournament);
    // }

    /**
     * Record the result of a match.
     * 
     * @param tournamentId the ID of the tournament
     * @param matchId     the ID of the match
     * @param result      the result of the match
     * @return ResponseEntity containing the updated tournament
     */
    @PostMapping("/tournaments/{tournamentId}/matches/{matchId}/result")
    public ResponseEntity<Tournament> recordMatchResult(
            @PathVariable Long tournamentId, 
            @PathVariable Long matchId, 
            @RequestParam String result) {
        
        Tournament updatedTournament = tournamentService.recordMatchResult(tournamentId, matchId, result);
        return ResponseEntity.ok(updatedTournament);
    }

    @PostMapping("/initialise-draw")
    public ResponseEntity<String> initialiseDraw(@PathVariable Long id) {
        tournamentService.initialiseDraw(id);
        return ResponseEntity.ok("Tournament draw initialised.");
    }

    @GetMapping("/print-draw")
    public ResponseEntity<String> printDraw(@PathVariable Long id) {
        String drawOutput = tournamentService.printDraw(id);
        return ResponseEntity.ok(drawOutput);
    }

    /**
     * Validate if the registration period is valid.
     * 
     * @param tournamentId the ID of the tournament
     * @return ResponseEntity containing the validation result
     */
    // @GetMapping("/tournaments/{tournamentId}/registration/valid")
    // public ResponseEntity<Boolean> validateRegistrationPeriod(@PathVariable Long tournamentId) {
    //     boolean isValid = tournamentService.validateRegistrationPeriod(tournamentId);
    //     return ResponseEntity.ok(isValid);
    // }
}
