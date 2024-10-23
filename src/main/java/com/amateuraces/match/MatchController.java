package com.amateuraces.match;

import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.amateuraces.player.PlayerNotFoundException;
import com.amateuraces.tournament.Tournament;

//import jakarta.validation.Valid;

@RestController
public class MatchController {
    private final MatchService matchService;
    
    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

        /**
     * List all completed matches in the system
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
    @GetMapping("/matches/complete/{id}")
    public Match getMatch(@PathVariable Long id){
        Match match = matchService.getMatch(id);

        // Need to handle "match not found" error using proper HTTP status code
        // In this case it should be HTTP 404
        if(match == null) throw new MatchNotFoundException(id);
        return match;
    }

    /**
     * Add a new match with POST request to "/matches"
     * Note the use of @RequestBody
     * @param match
     * @return list of all matchs
     */
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    @PostMapping("/matches")
    public Match addMatch(@RequestBody Match match){
        return matchService.addMatch(match);
    }

    // Handle form submission to add a match
    @PostMapping("/matches/add")
    public String addMatches(@ModelAttribute Match match) {
        matchService.addMatch(match); // Save the new match to the database
        return "redirect:/tournaments"; // Redirect back to the list of tournaments
    }

    /**
     * If there is no match with the given "id", throw a MatchNotFoundException
     * @param id
     * @param newMatchInfo
     * @return the updated, or newly added book
     */
    // Can uncomment code below if we want to implement updateMatch
    // @PutMapping("/matches/{id}")
    // public Match updateMatch(@PathVariable Long id, @Valid @RequestBody Match newMatchInfo){
    //     Match match = matchService.updateMatch(id, newMatchInfo);
    //     if(match == null) throw new MatchNotFoundException(id);
        
    //     return match;
    // }

        /**
     * Remove a match with the DELETE request to "/matchs/{id}"
     * If there is no match with the given "id", throw a MatchNotFoundException
     * @param id
     */
    @ResponseBody
    @DeleteMapping("/matches/{id}")
    public void deleteMatch(@PathVariable Long id){
        try{
            matchService.deleteMatch(id);
         }catch(EmptyResultDataAccessException e) {
            throw new MatchNotFoundException(id);
         }
    }

    // Delete a tournament
    @PostMapping("/matches/delete/{id}")
    public String deleteMatches(@PathVariable Long id) {
        matchService.deleteMatch(id);
        return "redirect:/tournaments";
    }

    @PostMapping("matches/{id}/result")
    public Match recordMatchResult(Long id, Long winnerId, Long loserId, String score) {
        try {
            Match match = matchService.recordMatchResult(id, winnerId, loserId, score);
            return match;
        } catch (MatchNotFoundException e) {
            throw new MatchNotFoundException(id);
        } catch (PlayerNotFoundException e) {
            throw new PlayerNotFoundException(winnerId);
        } catch (PlayerNotFoundException e) {
            throw new PlayerNotFoundException(loserId);
        } catch (PlayerNotPartOfMatchException e) {
            throw new PlayerNotPartOfMatchException(winnerId, id);
        } catch (PlayerNotPartOfMatchException e) {
            throw new PlayerNotPartOfMatchException(loserId, id);
        }
    }

    @PutMapping("matches/{id}/result/updateScore")
    public Match updateRecordMatchScore(Long id, String newScore) {
        try {
            Match updatedMatch = matchService.updateRecordMatchScore(id, newScore);
            return updatedMatch;
        } catch (MatchNotFoundException e) {
            throw new MatchNotFoundException(id);
        }
    }

    @PutMapping("matches/{id}/result/update-winner")
    public Match updateRecordMatchWinner(Long id, Long oldWinnerId, Long newWinnerId, String newScore) {
        try {
            Match updatedMatch = matchService.updateRecordMatchWinner(id, oldWinnerId, newWinnerId, newScore);
            return updatedMatch;
        } catch (MatchNotFoundException e) {
            throw new MatchNotFoundException(id);
        } catch (PlayerNotFoundException e) {
            throw new PlayerNotFoundException(newWinnerId);
        } catch (PlayerNotFoundException e) {
            throw new PlayerNotFoundException(oldWinnerId);
        } catch (PlayerNotPartOfMatchException e) {
            throw new PlayerNotPartOfMatchException(newWinnerId, id);
        } catch (PlayerNotPartOfMatchException e) {
            throw new PlayerNotPartOfMatchException(oldWinnerId, id);
        }
    }
}