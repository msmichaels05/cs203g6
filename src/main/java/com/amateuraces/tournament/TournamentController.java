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
     * Endpoint to record match results
     * @param tournamentId ID of the tournament where the match result is recorded
     * @param matchId ID of the match being updated
     * @param winnerId ID of the winning player
     * @param result Result of the match (e.g., score)
     * @return Response indicating success or failure
     */
    @PostMapping("/{tournamentId}/recordMatchResult")
    public ResponseEntity<String> recordMatchResult(
            @PathVariable Long tournamentId,
            @RequestParam Long matchId,
            @RequestParam Long winnerId,
            @RequestParam String result) {
        
        try {
            // Retrieve the Match and Player instances based on IDs
            Match match = new Match(); // Create a Match object with the specified ID (you may need to customize this for your setup)
            match.setId(matchId);      // Set the ID to identify the match

            Player winner = new Player(); // Create a Player object with the specified ID (customize as needed)
            winner.setId(winnerId);       // Set the ID to identify the player

            Tournament tournament = tournamentService.recordMatchResult(tournamentId, match, winner, result);

            if (tournament == null) {
                return ResponseEntity.status(404).body("Match not found in the tournament with ID: " + tournamentId);
            }

            return ResponseEntity.ok("Match result recorded successfully in tournament ID: " + tournamentId);

        } catch (TournamentNotFoundException ex) {
            return ResponseEntity.status(404).body("Tournament not found with ID: " + tournamentId);
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("An error occurred while recording the match result.");
        }
    }

    /**
     * Endpoint to update the next round with the match result
     * @param tournamentId ID of the tournament where the match result is recorded
     * @param matchId ID of the completed match
     * @param winnerId ID of the winning player
     * @param result Result of the match (e.g., score)
     * @return Response indicating success or failure
     */
    @PostMapping("/{tournamentId}/updateNextRound")
    public ResponseEntity<String> updateNextRound(
            @PathVariable Long tournamentId,
            @RequestParam Long matchId,
            @RequestParam Long winnerId,
            @RequestParam String result) {

        try {
            // Retrieve the Match and Player instances based on IDs
            Match match = new Match(); // Create a Match object with the specified ID (minimal initialization)
            match.setId(matchId);      // Set the ID to identify the match

            Player winner = new Player(); // Create a Player object with the specified ID
            winner.setId(winnerId);       // Set the ID to identify the player

            // Call the service method to update the next round
            Tournament tournament = tournamentService.updateNextRound(tournamentId, match, winner, result);

            if (tournament == null) {
                return ResponseEntity.status(404).body("Match not found in the tournament with ID: " + tournamentId);
            }

            return ResponseEntity.ok("Next round updated successfully in tournament ID: " + tournamentId);

        } catch (TournamentNotFoundException ex) {
            return ResponseEntity.status(404).body("Tournament not found with ID: " + tournamentId);
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("An error occurred while updating the next round.");
        }
    }

    /**
     * Endpoint to initialize the tournament draw
     * @param tournamentId ID of the tournament to initialize the draw for
     * @return Response indicating success or failure
     */
    @PostMapping("/{tournamentId}/initialiseDraw")
    public ResponseEntity<String> initialiseDraw(@PathVariable Long tournamentId) {
        try {
            tournamentService.initialiseDraw(tournamentId); // Call service method to initialize the draw
            return ResponseEntity.ok("Draw initialized successfully for tournament ID: " + tournamentId);
        } catch (TournamentNotFoundException ex) {
            return ResponseEntity.status(404).body("Tournament not found with ID: " + tournamentId);
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("An error occurred while initializing the draw.");
        }
    }

    /**
     * Endpoint to print the tournament draw
     * @param tournamentId ID of the tournament to print the draw for
     * @return Response containing the draw structure or an error message
     */
    @GetMapping("/{tournamentId}/printDraw")
    public ResponseEntity<String> printDraw(@PathVariable Long tournamentId) {
        try {
            String drawOutput = tournamentService.printDraw(tournamentId); // Call service method to get draw output
            return ResponseEntity.ok(drawOutput);
        } catch (TournamentNotFoundException ex) {
            return ResponseEntity.status(404).body("Tournament not found with ID: " + tournamentId);
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("An error occurred while printing the draw.");
        }
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
