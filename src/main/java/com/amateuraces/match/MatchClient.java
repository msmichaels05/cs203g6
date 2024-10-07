package com.amateuraces.match;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class MatchClient {

    private final RestTemplate restTemplate;
    
    // Assuming you have a base URL defined for match-related endpoints
    private final String baseUrl = "http://localhost:8080/api/matches"; // Adjust the URL as necessary

    @Autowired
    public MatchClient(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    /**
     * Get all matches.
     * 
     * @return a list of all matches
     */
    public List<Match> getAllMatches() {
        ResponseEntity<List<Match>> response = restTemplate.exchange(
                baseUrl + "/",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Match>>() {}
        );
        return response.getBody();
    }

    /**
     * Get a specific match by its ID.
     * 
     * @param matchId the ID of the match to retrieve
     * @return the match object with the given ID
     */
    public Match getMatchById(Long matchId) {
        ResponseEntity<Match> response = restTemplate.getForEntity(baseUrl + "/" + matchId, Match.class);
        return response.getBody();
    }

    /**
     * Add a new match.
     * 
     * @param tournamentId the ID of the tournament the match belongs to
     * @param match the match object to be created
     * @return the created match object
     */
    public Match addMatch(Long tournamentId, Match match) {
        HttpEntity<Match> entity = new HttpEntity<>(match);
        ResponseEntity<Match> response = restTemplate.exchange(
                baseUrl + "/tournament/" + tournamentId,
                HttpMethod.POST,
                entity,
                Match.class
        );
        return response.getBody();
    }

    /**
     * Register a player for a match.
     * 
     * @param tournamentId the ID of the tournament
     * @param matchId the ID of the match
     * @param playerId the ID of the player to register
     * @return a success message
     */
    public String registerPlayerForMatch(Long tournamentId, Long matchId, Long playerId) {
        ResponseEntity<String> response = restTemplate.postForEntity(
                baseUrl + "/" + tournamentId + "/matches/" + matchId + "/register?playerId=" + playerId,
                null,
                String.class
        );
        return response.getBody();
    }

    /**
     * Record the result of a match.
     * 
     * @param matchId the ID of the match
     * @param winnerId the ID of the player who won
     * @return a success message
     */
    public String recordMatchResult(Long matchId, Long winnerId) {
        HttpEntity<String> entity = new HttpEntity<>(null); // Body isn't necessary for this request
        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl + "/" + matchId + "/result?winnerId=" + winnerId,
                HttpMethod.POST,
                entity,
                String.class
        );
        return response.getBody();
    }

    /**
     * Get the match schedule for a specific tournament.
     * 
     * @param tournamentId the ID of the tournament
     * @return a list of matches scheduled for the tournament
     */
    public List<Match> getMatchSchedule(Long tournamentId) {
        ResponseEntity<List<Match>> response = restTemplate.exchange(
                baseUrl + "/tournament/" + tournamentId + "/schedule",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Match>>() {}
        );
        return response.getBody();
    }

    /**
     * Reschedule a match to a new date.
     * 
     * @param matchId the ID of the match to reschedule
     * @param newDateTime the new date and time for the match
     * @return the updated match object
     */
    public Match rescheduleMatch(Long matchId, String newDateTime) {
        HttpEntity<String> entity = new HttpEntity<>(newDateTime);
        ResponseEntity<Match> response = restTemplate.exchange(
                baseUrl + "/" + matchId + "/reschedule",
                HttpMethod.PUT,
                entity,
                Match.class
        );
        return response.getBody();
    }

    /**
     * Cancel a match.
     * 
     * @param matchId the ID of the match to cancel
     */
    public void cancelMatch(Long matchId) {
        restTemplate.delete(baseUrl + "/" + matchId + "/cancel");
    }
}
