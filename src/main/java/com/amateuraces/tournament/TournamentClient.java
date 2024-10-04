package com.amateuraces.tournament;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.core.ParameterizedTypeReference;

import java.util.List;

@Component
public class TournamentClient {

    private final RestTemplate restTemplate;
    
    // Assuming you have a property defined in application.properties for the base URL
    private final String baseUrl = "http://localhost:8080/tournaments"; // Adjust the URL as necessary

    @Autowired
    public TournamentClient(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    /**
     * Create a new tournament.
     * 
     * @param tournament the Tournament object to create
     * @return the created Tournament object
     */
    public Tournament createTournament(Tournament tournament) {
        ResponseEntity<Tournament> response = restTemplate.postForEntity(baseUrl, tournament, Tournament.class);
        return response.getBody();
    }

    /**
     * Get a tournament by ID.
     * 
     * @param tournamentId the ID of the tournament to retrieve
     * @return the Tournament object with the given ID
     */
    public Tournament getTournamentById(Long tournamentId) {
        ResponseEntity<Tournament> response = restTemplate.getForEntity(baseUrl + "/" + tournamentId, Tournament.class);
        return response.getBody();
    }

    /**
     * Get all tournaments.
     * 
     * @return a list of all Tournament objects
     */
    public List<Tournament> getAllTournaments() {
        ResponseEntity<List<Tournament>> response = restTemplate.exchange(
                baseUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Tournament>>() {}
        );
        return response.getBody();
    }

    /**
     * Update an existing tournament.
     * 
     * @param tournamentId the ID of the tournament to update
     * @param tournament   the Tournament object with updated details
     * @return the updated Tournament object
     */
    public Tournament updateTournament(Long tournamentId, Tournament tournament) {
        HttpEntity<Tournament> entity = new HttpEntity<>(tournament);
        ResponseEntity<Tournament> response = restTemplate.exchange(baseUrl + "/" + tournamentId, HttpMethod.PUT, entity, Tournament.class);
        return response.getBody();
    }

    /**
     * Delete a tournament by ID.
     * 
     * @param tournamentId the ID of the tournament to delete
     */
    public void deleteTournament(Long tournamentId) {
        restTemplate.delete(baseUrl + "/" + tournamentId);
    }

    /**
     * Register a player for a tournament.
     * 
     * @param tournamentId the ID of the tournament
     * @param playerId     the ID of the player to register
     * @return the updated Tournament object after registration
     */
    public Tournament registerPlayerForTournament(Long tournamentId, Long playerId) {
        ResponseEntity<Tournament> response = restTemplate.postForEntity(baseUrl + "/" + tournamentId + "/register?playerId=" + playerId, null, Tournament.class);
        return response.getBody();
    }
}
