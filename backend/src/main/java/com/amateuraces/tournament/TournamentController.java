package com.amateuraces.tournament;

import java.util.List;
import java.util.Set;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.amateuraces.match.Match;
import com.amateuraces.player.Player;
import com.amateuraces.player.PlayerNotFoundException;

@Controller
public class TournamentController {

    private TournamentService tournamentService;

    public TournamentController(TournamentService tournamentService) {
        this.tournamentService = tournamentService;
    }

    // Call match algorithm
    // @ResponseBody
    // @PostMapping("/tournaments/{id}/matches")
    // public List<Match> createMatchesForTournament(@PathVariable Long id) {
    //     return tournamentService.createMatchesForTournament(id); // Create and save matches for a given tournament
    // }

    @ResponseBody
    @GetMapping("/tournaments")
    public List<Tournament> getAllTournaments() {
        return tournamentService.listTournaments(); // This will return the list of tournaments in JSON format
    }

    @ResponseBody
    @GetMapping("/tournaments/{id}")
    public Tournament getTournament(@PathVariable Long id) {
        Tournament tournament = tournamentService.getTournament(id);

        // Need to handle "match not found" error using proper HTTP status code
        // In this case it should be HTTP 404
        if (tournament == null)
            throw new TournamentNotFoundException(id);
        return tournament;

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
    // @ResponseBody
    // @PutMapping("/tournaments/{id}")
    // public Tournament updateTournament(@PathVariable Long id, @Valid @RequestBody Tournament newTournamentInfo) {
    //     Tournament Tournament = tournamentService.updateTournament(id, newTournamentInfo);
    //     if (Tournament == null)
    //         throw new TournamentNotFoundException(id);

    //     return Tournament;
    // }

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

    @ResponseBody
    @DeleteMapping("/tournaments/{tournamentId}/players/{playerId}")
    public void removePlayerFromTournament(@PathVariable(value="tournamentId")Long tournamentId,
        @PathVariable(value="playerId")Long playerId) {
      try {
        tournamentService.removePlayerFromTournament(tournamentId, playerId);
      } catch(TournamentNotFoundException e){
        throw new TournamentNotFoundException(tournamentId);
      }catch(PlayerNotFoundException e){
        throw new PlayerNotFoundException(playerId);
      }
    }

    @PostMapping("/tournaments/{tournamentId}/initialise-draw")
    @ResponseBody
    public List<Match> initialiseDraw(@PathVariable Long tournamentId) {
        List<Match> round1Matches = tournamentService.initialiseDraw(tournamentId);
        return round1Matches;
    }

    @PostMapping("/tournaments/{tournamentId}/update-next-round")
    @ResponseBody
    public List<Match> updateNextRound(@PathVariable Long tournamentId) {
        List<Match> updatedMatches = tournamentService.updateNextRound(tournamentId);
        return updatedMatches;
    }

}
