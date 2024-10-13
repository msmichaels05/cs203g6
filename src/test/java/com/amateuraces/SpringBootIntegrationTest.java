package com.amateuraces.tournament;

import static org.junit.jupiter.api.Assertions.*;

import java.net.URI;
import java.util.Optional;

import org.apache.catalina.connector.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.amateuraces.match.MatchRepository;
import com.amateuraces.player.PlayerRepository;
import com.amateuraces.tournament.Tournament;
import com.amateuraces.tournament.TournamentRepository;
import com.amateuraces.user.User;
import com.amateuraces.user.UserRepository;

/** Start an actual HTTP server listening at a random port */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class SpringBootIntegrationTest {

	@LocalServerPort
	private int port;

	private final String baseUrl = "http://localhost:";

	@Autowired
	/**
	 * Use TestRestTemplate for testing a real instance of your application as an
	 * external actor.
	 * TestRestTemplate is just a convenient subclass of RestTemplate that is
	 * suitable for integration tests.
	 * It is fault tolerant, and optionally can carry Basic authentication headers.
	 */
	private TestRestTemplate restTemplate;

	@Autowired
	private PlayerRepository players;

	@Autowired
	private TournamentRepository tournaments;

	@Autowired
	private MatchRepository matches;

	@Autowired
	private UserRepository users;

	@Autowired
	private BCryptPasswordEncoder encoder;

	@AfterEach
	void tearDown() {
		// clear the database after each test
		players.deleteAll();
		users.deleteAll();
		tournaments.deleteAll();
		matches.deleteAll();
	}

	@Test
	public void getTournaments_Success() throws Exception {
		URI uri = new URI(baseUrl + port + "/tournaments");
		tournaments.save(new Tournament("US OPEN", 1500)); // Save a tournament to test retrieval

		// Use array with ResponseEntity to retrieve the list of tournaments
		ResponseEntity<Tournament[]> result = restTemplate.getForEntity(uri, Tournament[].class);
		Tournament[] tournaments = result.getBody();

		// Assertions
		assertEquals(200, result.getStatusCode().value());
		assertEquals(1, tournaments.length);
		assertEquals("US OPEN", tournaments[0].getName());
	}

	@Test
	public void getTournament_ValidTournamentId_Success() throws Exception {
		// Step 1: Create a new tournament
		Tournament tournament = new Tournament("US OPEN", 1500);
		Long tournamentId = tournaments.save(tournament).getId();

		// Step 2: Construct the URI for fetching the tournament
		URI uri = new URI(baseUrl + port + "/tournaments/" + tournamentId);

		// Step 3: Make the GET request to the tournament endpoint
		ResponseEntity<Tournament> result = restTemplate.getForEntity(uri, Tournament.class);

		// Step 4: Verify the status code and the retrieved tournament details
		assertEquals(200, result.getStatusCode().value());
		assertEquals(tournament.getName(), result.getBody().getName());
		assertEquals(tournament.getRequirement(), result.getBody().getRequirement());
	}

	@Test
	public void getTournament_InvalidTournamentId_Failure() throws Exception {
		URI uri = new URI(baseUrl + port + "/tournaments/1");

		ResponseEntity<Tournament> result = restTemplate.getForEntity(uri, Tournament.class);

		assertEquals(404, result.getStatusCode().value());
	}

	@Test
    public void createTournament_Success() throws Exception {
        // Create the URI for the POST request
        URI uri = new URI(baseUrl + port + "/tournaments");

        // Create a sample tournament object
        Tournament tournament = new Tournament("US OPEN", 1500);

        // Add a user with admin role for authentication
        users.save(new User("admin", encoder.encode("goodpassword"), "ROLE_ADMIN"));

        // Send a POST request with Basic authentication to create the tournament
        ResponseEntity<Tournament> result = restTemplate.withBasicAuth("admin", "goodpassword")
                .postForEntity(uri, tournament, Tournament.class);

        // Check the HTTP response and the returned tournament data
        assertEquals(201, result.getStatusCode().value()); // Ensure the response is 201 CREATED
        assertEquals(tournament.getName(), result.getBody().getName()); // Ensure tournament name matches
        assertEquals(tournament.getRequirement(), result.getBody().getRequirement()); // Ensure the requirement matches
    }

	// @Test
	// public void deletePlayer_ValidPlayerId_Success() throws Exception {
	// Player player = players.save(new Player("Michael"));
	// URI uri = new URI(baseUrl + port + "/players/" + player.getId().longValue());
	// users.save(new User("admin", encoder.encode("goodpassword"), "ROLE_ADMIN"));

	// ResponseEntity<Void> result = restTemplate.withBasicAuth("admin",
	// "goodpassword")
	// .exchange(uri, HttpMethod.DELETE, null, Void.class);
	// assertEquals(200, result.getStatusCode().value());

	// Optional<Player> emptyValue = Optional.empty();
	// assertEquals(emptyValue, players.findById(player.getId()));
	// }

	// @Test
	// public void deletePlayer_InvalidPlayerId_Failure() throws Exception {
	// URI uri = new URI(baseUrl + port + "/players/1");
	// users.save(new User("admin", encoder.encode("goodpassword"), "ROLE_ADMIN"));
	// ResponseEntity<Void> result = restTemplate.withBasicAuth("admin",
	// "goodpassword")
	// .exchange(uri, HttpMethod.DELETE, null, Void.class);
	// assertEquals(404, result.getStatusCode().value());
	// }

	// @Test
	// public void updatePlayer_ValidPlayerId_Success() throws Exception {
	// Player player = players.save(new Player("Jay"));
	// URI uri = new URI(baseUrl + port + "/players/" + player.getId().longValue());
	// Player newPlayerInfo = new Player("Lee Jay");
	// users.save(new User("admin", encoder.encode("goodpassword"), "ROLE_ADMIN"));
	// ResponseEntity<Player> result = restTemplate.withBasicAuth("admin",
	// "goodpassword")
	// .exchange(uri, HttpMethod.PUT, new HttpEntity<>(newPlayerInfo),
	// Player.class);
	// assertEquals(200, result.getStatusCode().value());
	// assertEquals(newPlayerInfo.getName(), result.getBody().getName());
	// }

	// @Test
	// public void updatePlayer_InvalidPlayerId_Failure() throws Exception {
	// URI uri = new URI(baseUrl + port + "/players/1");
	// Player newPlayerInfo = new Player("Jackie");
	// users.save(new User("admin", encoder.encode("goodpassword"), "ROLE_ADMIN"));
	// ResponseEntity<Player> result = restTemplate.withBasicAuth("admin",
	// "goodpassword")
	// .exchange(uri, HttpMethod.PUT, new HttpEntity<>(newPlayerInfo),
	// Player.class);
	// assertEquals(404, result.getStatusCode().value());
	// }

}