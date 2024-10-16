package com.amateuraces.tournament;

import java.util.List;
import java.util.Set;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.amateuraces.player.Player;

import jakarta.validation.Valid;

@Controller
public class TournamentController {

    private TournamentService tournamentService;

    public TournamentController(TournamentService tournamentService) {
        this.tournamentService = tournamentService;
    }

    // Display all tournaments
    @GetMapping("/tournaments")
    public String showAllTournaments(Model model) {
        List<Tournament> allTournaments = tournamentService.listTournaments(); // Retrieve all tournaments from the
                                                                               // database
        model.addAttribute("tournaments", allTournaments); // Add the list of tournaments to the model
        return "all_tournaments"; // Return the Thymeleaf template for displaying tournaments
    }

    @ResponseBody
    @GetMapping("/tournament")
    public List<Tournament> getAllTournaments() {
        return tournamentService.listTournaments(); // This will return the list of tournaments in JSON format
    }

    // Display the Add Tournament page
    @GetMapping("/tournament/add")
    public String showAddTournamentForm(Model model) {
        model.addAttribute("tournament", new Tournament()); // Add an empty tournament object for the form
        return "add_tournament"; // Return the template for adding a tournament
    }

    // Handle form submission to add a tournament
    @PostMapping("/tournament/add")
    public String addTournaments(@ModelAttribute Tournament tournament) {
        tournamentService.addTournament(tournament); // Save the new tournament to the database
        return "redirect:/tournaments"; // Redirect back to the list of tournaments
    }

    // View tournament details
    @GetMapping("/tournament/view/{id}")
    public String viewTournamentDetails(@PathVariable Long id, Model model) {
        Tournament tournament = tournamentService.getTournament(id);
        if (tournament == null) {
            return "redirect:/tournaments"; // Redirect if tournament not found
        }
        model.addAttribute("tournament", tournament);
        return "view_tournament"; // Thymeleaf template for viewing tournament details
    }

    // Display the Edit Tournament page
    @GetMapping("/tournament/edit/{id}")
    public String showEditTournamentForm(@PathVariable Long id, Model model) {
        Tournament tournament = tournamentService.getTournament(id);
        if (tournament == null) {
            return "redirect:/tournaments"; // Redirect if tournament not found
        }
        model.addAttribute("tournament", tournament);
        return "edit_tournament"; // Thymeleaf template for editing a tournament
    }

    // Handle form submission to edit a tournament
    @PostMapping("/tournament/edit/{id}")
    public String editTournament(@PathVariable Long id, @ModelAttribute Tournament updatedTournament) {
        Tournament tournament = tournamentService.getTournament(id);
        if (tournament != null) {
            tournament.setName(updatedTournament.getName());
            tournament.setELOrequirement(updatedTournament.getELOrequirement());
            tournament.setMaxPlayers(updatedTournament.getMaxPlayers());
            tournament.setStartDate(updatedTournament.getStartDate());
            tournament.setEndDate(updatedTournament.getEndDate());
            tournament.setLocation(updatedTournament.getLocation());
            tournament.setDescription(updatedTournament.getDescription());

            tournamentService.updateTournament(id, tournament);
        }
        return "redirect:/tournaments";
    }

    // Delete a tournament
    @PostMapping("/tournament/delete/{id}")
    public String deleteTournaments(@PathVariable Long id) {
        tournamentService.deleteTournament(id);
        return "redirect:/tournaments";
    }

    @GetMapping("/tournaments/{id}")
    public Tournament getTournament(@PathVariable Long id) {
        Tournament tournament = tournamentService.getTournament(id);

        // Need to handle "match not found" error using proper HTTP status code
        // In this case it should be HTTP 404
        if (tournament == null)
            throw new TournamentNotFoundException(id);
        return tournamentService.getTournament(id);

    }

    /**
     * Create a new tournament.
     * 
     * @param tournament the tournament to be created
     * @return ResponseEntity containing the created tournament
     */
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    @PostMapping("/tournaments")
    public Tournament addTournament(@RequestBody Tournament tournament) {
        return tournamentService.addTournament(tournament);
    }

    /**
     * Remove a tournament with the DELETE request to "/tournaments/{id}"
     * If there is no tournament with the given "id", throw a
     * TournamentNotFoundException
     * 
     * @param id
     */
    @ResponseBody
    @DeleteMapping("/tournaments/{id}")
    public void deleteTournament(@PathVariable Long id) {
        try {
            tournamentService.deleteTournament(id);
        } catch (EmptyResultDataAccessException e) {
            throw new TournamentNotFoundException(id);
        }
    }

    /**
     * If there is no tournament with the given "id", throw a
     * TournamentNotFoundException
     * 
     * @param id
     * @param newTournamentInfo
     * @return the updated, or newly added book
     */
    @ResponseBody
    @PutMapping("/tournaments/{id}")
    public Tournament updateTournament(@PathVariable Long id, @Valid @RequestBody Tournament newTournamentInfo) {
        Tournament Tournament = tournamentService.updateTournament(id, newTournamentInfo);
        if (Tournament == null)
            throw new TournamentNotFoundException(id);

        return Tournament;
    }

    @ResponseBody
    @PostMapping("/tournaments/{id}/players")
    public void joinTournament(@PathVariable Long id) {
        tournamentService.joinTournament(id);
    }

    @ResponseBody
    @GetMapping("/tournaments/{id}/players")
    public Set<Player> getPlayersInTournament(@PathVariable("id") Long tournamentId) {
        return tournamentService.getPlayersInTournament(tournamentId);
    }

    // @PostMapping("/tournaments/{tournamentId}/draw")
    // public ResponseEntity<List<Match>> performRandomDraw(@PathVariable Long
    // tournamentId) {
    // Set<Match> matchPairings = tournamentService.performRandomDraw(tournamentId);
    // return ResponseEntity.ok(matchPairings);
    // }

    // @PostMapping("/tournaments/{tournamentId}/matches/{matchId}/result")
    // public ResponseEntity<Tournament> recordMatchResult(
    // @PathVariable Long tournamentId,
    // @PathVariable Long matchId,
    // @RequestParam String result) {

    // Tournament updatedTournament =
    // tournamentService.recordMatchResult(tournamentId, matchId, result);
    // return ResponseEntity.ok(updatedTournament);
    // }
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
    // @PathVariable Long tournamentId,
    // @RequestParam String startDate,
    // @RequestParam String endDate) {

    // Tournament updatedTournament =
    // tournamentService.setRegistrationPeriod(tournamentId, startDate, endDate);
    // return ResponseEntity.ok(updatedTournament);
    // }

    /**
     * Add a player to the tournament.
     * 
     * @param tournamentId the ID of the tournament
     * @param playerId     the ID of the player to be added
     * @return ResponseEntity containing the updated tournament
     */
    // @PostMapping("/tournaments/{tournamentId}/players/{playerId}")
    // public ResponseEntity<Tournament> addPlayerToTournament(
    // @PathVariable Long tournamentId,
    // @PathVariable Long playerId) {

    // Tournament updatedTournament =
    // tournamentService.addPlayerToTournament(tournamentId, playerId);
    // return ResponseEntity.ok(updatedTournament);
    // }

    /**
     * Get the list of players registered for the tournament.
     * 
     * @param tournamentId the ID of the tournament
     * @return ResponseEntity containing the list of players
     */
    // @GetMapping("/tournaments/{tournamentId}/players")
    // public ResponseEntity<List<Player>> getPlayersInTournament(@PathVariable Long
    // tournamentId) {
    // List<Player> players =
    // tournamentService.getPlayersInTournament(tournamentId);
    // return ResponseEntity.ok(players);
    // }

    /**
     * Get all tournaments.
     * 
     * @return ResponseEntity containing the list of tournaments
     */
    // @GetMapping("/tournaments")
    // public ResponseEntity<List<Tournament>> listTournaments() {
    // List<Tournament> tournaments = tournamentService.listTournaments();
    // return ResponseEntity.ok(tournaments);
    // }

    /**
     * Perform a randomized draw for the tournament.
     * 
     * @param tournamentId the ID of the tournament
     * @return ResponseEntity containing the list of match pairings after the draw
     */
    // @PostMapping("/tournaments/{tournamentId}/draw")
    // public ResponseEntity<List<Match>> performRandomDraw(@PathVariable Long
    // tournamentId) {
    // List<Match> matchPairings =
    // tournamentService.performRandomDraw(tournamentId);
    // return ResponseEntity.ok(matchPairings);
    // }

    /**
     * Update the status of the tournament.
     * 
     * @param tournamentId the ID of the tournament
     * @param status       the new status of the tournament
     * @return ResponseEntity containing the updated tournament
     */
    // @PutMapping("/tournaments/{tournamentId}/status")
    // public ResponseEntity<Tournament> updateTournamentStatus(
    // @PathVariable Long tournamentId,
    // @RequestParam String status) {

    // Tournament updatedTournament =
    // tournamentService.updateTournamentStatus(tournamentId, status);
    // return ResponseEntity.ok(updatedTournament);
    // }

    /**
     * Record the result of a match.
     * 
     * @param tournamentId the ID of the tournament
     * @param matchId      the ID of the match
     * @param result       the result of the match
     * @return ResponseEntity containing the updated tournament
     */
    // @PostMapping("/tournaments/{tournamentId}/matches/{matchId}/result")
    // public ResponseEntity<Tournament> recordMatchResult(
    // @PathVariable Long tournamentId,
    // @PathVariable Long matchId,
    // @RequestParam String result) {

    // Tournament updatedTournament =
    // tournamentService.recordMatchResult(tournamentId, matchId, result);
    // return ResponseEntity.ok(updatedTournament);
    // }

    /**
     * Validate if the registration period is valid.
     * 
     * @param tournamentId the ID of the tournament
     * @return ResponseEntity containing the validation result
     */
    // @GetMapping("/tournaments/{tournamentId}/registration/valid")
    // public ResponseEntity<Boolean> validateRegistrationPeriod(@PathVariable Long
    // tournamentId) {
    // boolean isValid = tournamentService.validateRegistrationPeriod(tournamentId);
    // return ResponseEntity.ok(isValid);
    // }
}
