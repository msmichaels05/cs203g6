package com.amateuraces.match;

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
import com.amateuraces.player.*;

@RestController
public class MatchController {
    private final MatchService matchService;
    
    public MatchController(MatchService matchService){
        this.matchService = matchService ; 
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
    @DeleteMapping("/matches/{id}")
    public void deleteMatch(@PathVariable Long id){
        try{
            matchService.deleteMatch(id);
         }catch(EmptyResultDataAccessException e) {
            throw new MatchNotFoundException(id);
         }
    }

       /**
     * Record match result with the POST request to "/matches/{id}/result"
     * If there is no match with the given "id", throw a MatchNotFoundException
     * If there is no player with the given "id", throw a PlayerNotFoundException
     * @param id
     * @param winnerId
     * @param loserId
     * @param score
     */
    @PostMapping("matches/{id}/result")
    public Match recordMatchResult(Long id, Long winnerId, Long loserId, String score) {
        try {
            Match match = matchService.recordMatchResult(id, winnerId, loserId, score);
            return match;
        } catch (MatchNotFoundException e) {
            throw new MatchNotFoundException(id);
        } catch (PlayerNotFoundException e) {
            throw new PlayerNotFoundException(winnerId, loserId);
        } catch (PlayerNotPartOfMatchException e) {
            throw new PlayerNotPartOfMatchException(winnerId, loserId, id);
        }
    }

       /**
     * Update match score with the PUT request to "/matches/{id}/result/updateScore"
     * If there is no match with the given "id", throw a MatchNotFoundException
     * If there is no player with the given "id", throw a PlayerNotFoundException
     * @param id
     * @param winnerId
     * @param loserId
     * @param score
     */
    @PutMapping("matches/{id}/result/updateScore")
    public Match updateRecordMatchScore(Long id, String newScore) {
        try {
            Match updatedMatch = matchService.updateRecordMatchScore(id, newScore);
            return updatedMatch;
        } catch (MatchNotFoundException e) {
            throw new MatchNotFoundException(id);
        }
    }

       /**
     * Update match winner with the PUT request to "/matches/{id}/result/updateWinner"
     * If there is no match with the given "id", throw a MatchNotFoundException
     * If there is no player with the given "id", throw a PlayerNotFoundException
     * @param id
     * @param winnerId
     * @param loserId
     * @param score
     */
    @PutMapping("matches/{id}/result/updateWinner")
    public Match updateRecordMatchWinner(Long id, Long oldWinnerId, Long newWinnerId, String newScore) {
        try {
            Match updatedMatch = matchService.updateRecordMatchWinner(id, oldWinnerId, newWinnerId, newScore);
            return updatedMatch;
        } catch (MatchNotFoundException e) {
            throw new MatchNotFoundException(id);
        } catch (PlayerNotFoundException e) {
            throw new PlayerNotFoundException(newWinnerId, oldWinnerId);
        } catch (PlayerNotPartOfMatchException e) {
            throw new PlayerNotPartOfMatchException(newWinnerId, oldWinnerId, id);
        }
    }
}