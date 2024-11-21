package com.amateuraces;

import java.net.URI;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.amateuraces.tournament.Tournament;
import com.amateuraces.tournament.TournamentRepository;
import com.amateuraces.user.User;
import com.amateuraces.user.UserRepository;

/** Start an actual HTTP server listening at a random port */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class TournamentIntegrationTest {

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
	private UserRepository users;

    @Autowired
    private TournamentRepository tournaments;

	@Autowired
	private BCryptPasswordEncoder encoder;

	@AfterEach
	void tearDown() {
		// clear the database after each test
		users.deleteAll();
        tournaments.deleteAll();
	}

    @Test
	public void getTournaments_Success() throws Exception {
        URI uri = new URI(baseUrl + port + "/tournaments");
		tournaments.save(new Tournament("US Open", "USA"));

		ResponseEntity<Tournament[]> result = restTemplate.getForEntity(uri, Tournament[].class);
		Tournament[] tournaments = result.getBody();

		assertEquals(200, result.getStatusCode().value());
		assertEquals(1, tournaments.length);		
    }
	
	@Test
	public void getTournament_ValidTournamentId_Success() throws Exception {
		Tournament tournament = new Tournament("US Open", "USA");
		Long id = tournaments.save(tournament).getId();
		URI uri = new URI(baseUrl + port + "/tournaments/" + id);
		ResponseEntity<Tournament> result = restTemplate.getForEntity(uri, Tournament.class);

		assertEquals(200, result.getStatusCode().value());
		assertEquals(tournament.getName(), result.getBody().getName());
	}

	@Test
	public void getTournament_InvalidTournamentId_Failure() throws Exception {
		URI uri = new URI(baseUrl + port + "/tournaments/1");
		ResponseEntity<Tournament> result = restTemplate.getForEntity(uri, Tournament.class);

		assertEquals(404, result.getStatusCode().value());
	}


    @Test
	public void deleteTournament_ValidTournamentId_Success() throws Exception {
		Tournament tournament = tournaments.save(new Tournament("US Open", "USA"));
		URI uri = new URI(baseUrl + port + "/tournaments/" + tournament.getId());
		users.save(new User("admin@gmail.com", "admin", encoder.encode("goodpassword"), "ROLE_ADMIN"));

		ResponseEntity<Void> result = restTemplate.withBasicAuth("admin", "goodpassword")
				.exchange(uri, HttpMethod.DELETE, null, Void.class);
		assertEquals(200, result.getStatusCode().value());

		Optional<Tournament> emptyValue = Optional.empty();
		assertEquals(emptyValue, tournaments.findById(tournament.getId()));
	}

	@Test
	public void deleteTournament_InvalidTournamentId_Failure() throws Exception {
		URI uri = new URI(baseUrl + port + "/tournaments/1");
		users.save(new User("admin@gmail.com", "admin", encoder.encode("goodpassword"), "ROLE_ADMIN"));
		ResponseEntity<Void> result = restTemplate.withBasicAuth("admin", "goodpassword")
				.exchange(uri, HttpMethod.DELETE, null, Void.class);
		assertEquals(404, result.getStatusCode().value());
	}

	@Test
	public void updateTournament_ValidTournamentId_Success() throws Exception {
		Tournament tournament = tournaments.save(new Tournament("US Open", "USA"));
		URI uri = new URI(baseUrl + port + "/tournaments/" + tournament.getId().longValue());
		Tournament newTournamentInfo = new Tournament("French Open", "France");
		users.save(new User("admin@gmail.com", "admin", encoder.encode("goodpassword"), "ROLE_ADMIN"));
		ResponseEntity<Tournament> result = restTemplate.withBasicAuth("admin", "goodpassword")
				.exchange(uri, HttpMethod.PUT, new HttpEntity<>(newTournamentInfo), Tournament.class);
		assertEquals(200, result.getStatusCode().value());
		assertEquals(newTournamentInfo.getName(), result.getBody().getName());
        assertEquals(newTournamentInfo.getLocation(), result.getBody().getLocation());
	}

	@Test
	public void updateTournament_InvalidTournamentId_Failure() throws Exception {
		URI uri = new URI(baseUrl + port + "/tournaments/1");
		Tournament newTournamentInfo = new Tournament("French Open", "France");
		users.save(new User("admin@gmail.com", "admin", encoder.encode("goodpassword"), "ROLE_ADMIN"));
		ResponseEntity<Tournament> result = restTemplate.withBasicAuth("admin", "goodpassword")
				.exchange(uri, HttpMethod.PUT, new HttpEntity<>(newTournamentInfo), Tournament.class);
		assertEquals(404, result.getStatusCode().value());
	}
}