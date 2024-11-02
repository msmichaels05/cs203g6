package com.amateuraces.tournament;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.amateuraces.match.Match;
import com.amateuraces.player.*;
import com.amateuraces.tournament.*;
import com.amateuraces.tournament.TournamentRepository;

import org.springframework.boot.test.web.client.TestRestTemplate;

import java.util.*;

public class TournamentIntegrationTest {

    private String baseUrl = "http://localhost:8080";  // Ensure this matches your server port

    @Autowired
    private TournamentRepository tournamentRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private TestRestTemplate testRestTemplate;  // Autowire TestRestTemplate

    private Tournament tournament;

    @BeforeEach
    public void setUp() {
        // Set up an admin Tournament for authentication
        tournament = new Tournament("Test Tournament", 32, "Test Location", 1000, "Test Description");
;
        tournamentRepository.save(tournament);
    }

    @Test
    public void deleteTournament_ValidTournamentId_Success() throws Exception {
        // Arrange: Create a new Tournament
        Tournament tournament = tournamentRepository.save(new Tournament("Test Tournament", 32, "Test Location", 1000, "Test Description")
);
        URI uri = new URI(baseUrl + "/tournament/" + tournament.getId());

        // Act: Send DELETE request with basic authentication
        ResponseEntity<Void> result = testRestTemplate.withBasicAuth("admin", "goodpassword")
                .exchange(uri, HttpMethod.DELETE, null, Void.class);

        // Assert: Check that the status code is 200 (OK)
        assertEquals(200, result.getStatusCode().value());

        // Assert: Check that the Tournament no longer exists in the repository
        Optional<Tournament> deletedTournament = tournamentRepository.findById(tournament.getId());
        assertTrue(deletedTournament.isEmpty());
    }

    @Test
    public void deleteTournament_InvalidTournamentId_Failure() throws Exception {
        // Act: Try to delete a non-existing Tournament (ID 9999)
        URI uri = new URI(baseUrl + "/Tournaments/9999");
        ResponseEntity<Void> result = testRestTemplate.withBasicAuth("admin", "goodpassword")
                .exchange(uri, HttpMethod.DELETE, null, Void.class);

        // Assert: Check that the status code is 404 (Not Found)
        assertEquals(404, result.getStatusCode().value());
    }

    @Test
    public void updateTournament_ValidTournamentId_Success() throws Exception {
        // Arrange: Create a new Tournament
        Tournament tournament = tournamentRepository.save(new Tournament("Test Tournament", 32, "Test Location", 1000, "Test Description");
);
        URI uri = new URI(baseUrl + "/tournament/" + tournament.getId());

        // Prepare new Tournament data
        Tournament updatedTournamentInfo = new Tournament("Test Tournament", 32, "Test Location", 1000, "Test Description");
;

        // Act: Send PUT request to update the Tournament
        ResponseEntity<Tournament> result = testRestTemplate.withBasicAuth("admin", "goodpassword")
                .exchange(uri, HttpMethod.PUT, new HttpEntity<>(updatedTournamentInfo), Tournament.class);

        // Assert: Check that the status code is 200 (OK)
        assertEquals(200, result.getStatusCode().value());

        // Assert: Check that the Tournament's details have been updated
        Tournament updatedTournament = result.getBody();
        assertEquals(updatedTournamentInfo.getTournamentname(), updatedTournament.getTournamentname());
    }

    @Test
    public void updateTournament_InvalidTournamentId_Failure() throws Exception {
        // Act: Try to update a non-existing Tournament (ID 9999)
        URI uri = new URI(baseUrl + "/Tournaments/9999");
        Tournament updatedTournamentInfo = new Tournament("Test Tournament", 32, "Test Location", 1000, "Test Description");
;

        ResponseEntity<Tournament> result = testRestTemplate.withBasicAuth("admin", "goodpassword")
                .exchange(uri, HttpMethod.PUT, new HttpEntity<>(updatedTournamentInfo), Tournament.class);

        // Assert: Check that the status code is 404 (Not Found)
        assertEquals(404, result.getStatusCode().value());
    }

    @Test
    public void testInitialiseDraw() {
        // Create a Tournament instance
        Tournament tournament = new Tournament("Test Tournament", 32, "Test Location", 1000, "Test Description");

        // Create Players with varying ELO ratings
        Player[] players = new Player[17];
        int eloRating = 1200;
        for (int i = 0; i < 17; i++) {
            Player p = new Player();
            p.setElo(eloRating - (i * 50));
            players[i] = p;
            tournament.addPlayer(players[i]);
        }

        // Call initialiseDraw()
        tournament.initialiseDraw();

        // Verify that the draw has been initialised
        MatchMinHeap draw = tournament.getDraw();
        assertNotNull(draw, "Draw should not be null after initialisation.");

        // Check the size of the draw
        int expectedMatches = calculateExpectedMatches(tournament.getPlayerCount());
        assertEquals(expectedMatches, draw.size(), "Draw size should match the expected number of matches.");

        // Print the draw
        System.out.println("Tournament Draw:");
        tournament.printDraw();

        // Verify that seeded players are in the correct positions
        verifySeededPlayersPlacement(tournament, players);

        // Verify that all players are included in the draw
        verifyAllPlayersIncluded(tournament, players);
    }

    // Helper method to calculate the expected number of matches
    private int calculateExpectedMatches(int playerCount) {
        int round1Matches = (int) Math.pow(2, Math.ceil(Math.log(playerCount) / Math.log(2)) - 1);
        int totalRounds = (int) (Math.log(round1Matches) / Math.log(2));

        // Calculate matches in subsequent rounds
        int subsequentMatches = 0;
        for (int i = 1; i < totalRounds; i *= 2) {
            subsequentMatches += i;
        }

        // Calculate matches in the first round
        int firstRoundMatches = (int) Math.ceil(playerCount / 2.0);

        return subsequentMatches + firstRoundMatches;
    }

    // Helper method to verify seeded players placement
    private void verifySeededPlayersPlacement(Tournament tournament, Player[] players) {
        // Sort players by ELO in descending order
        Arrays.sort(players, new EloComparator());

        // Get the number of seeds
        int numberOfSeeds = tournament.getPlayerCount() / 4;

        // Access the slots array via reflection (since it's private)
        Player[] slots = new Player[(int) Math.pow(2, (int) Math.ceil((Math.log(tournament.getPlayerCount()) / Math.log(2))) -1) * 2];
        tournament.assignSeedsToSlots(players, slots);

        // Verify top seeds are in the correct positions
        assertEquals(players[0], slots[0], "Top seed should be in the first slot.");
        assertEquals(players[1], slots[slots.length - 1], "Second seed should be in the last slot.");

        // Additional checks for seeds 3 and 4
        int totalMatches = slots.length / 2;
        int midPoint = totalMatches / 2;
        assertEquals(players[2], slots[midPoint - 1], "Third seed should be in the correct slot.");
        assertEquals(players[3], slots[midPoint], "Fourth seed should be in the correct slot.");
    }

    // Helper method to verify all players are included in the draw
    private void verifyAllPlayersIncluded(Tournament tournament, Player[] players) {
        Set<Player> playersInDraw = new HashSet<>();
        MatchMinHeap draw = tournament.getDraw();

        // Retrieve all matches from the draw
        List<Match> matches = draw.getAllMatches();
        for (Match match : matches) {
            if (match.getPlayer1() != null) {
                playersInDraw.add(match.getPlayer1());
            }
            if (match.getPlayer2() != null) {
                playersInDraw.add(match.getPlayer2());
            }
        }

        // Verify that all players are included
        assertEquals(players.length, playersInDraw.size(), ("All players should be included in the draw."));
    }
}



