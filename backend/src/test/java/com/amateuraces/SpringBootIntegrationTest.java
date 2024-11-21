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
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.amateuraces.player.Player;
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
	private UserRepository users;

	@Autowired
	private BCryptPasswordEncoder encoder;

	@AfterEach
	void tearDown() {
		// clear the database after each test
		users.deleteAll();	
	}

	@Test
	public void getUser_InvalidUserId_Failure() throws Exception {
		URI uri = new URI(baseUrl + port + "/users/1");
		ResponseEntity<User> result = restTemplate.getForEntity(uri, User.class);

		assertEquals(404, result.getStatusCode().value());
	}

    @Test
	public void deleteUser_ValidUserId_Success() throws Exception {
		User user = users.save(new User("timothy@gmail.com", "Timothy", "password"));
		URI uri = new URI(baseUrl + port + "/users/" + user.getId());
		users.save(new User("admin@gmail.com", "admin", encoder.encode("goodpassword"), "ROLE_ADMIN"));

		ResponseEntity<Void> result = restTemplate.withBasicAuth("admin", "goodpassword")
				.exchange(uri, HttpMethod.DELETE, null, Void.class);
		assertEquals(200, result.getStatusCode().value());

		Optional<User> emptyValue = Optional.empty();
		assertEquals(emptyValue, users.findById(user.getId()));
	}

	public void getPlayers_Success() throws Exception {
        // Create and save a User
        User user = new User("testuser", encoder.encode("password"), "ROLE_USER");
        user = users.save(user);

        // Create and save a Player associated with the User
        Player player = new Player("Tim");
        player.setUser(user);
        users.save(user);

        // Construct the URI using the saved User's ID
        URI uri = new URI(baseUrl + port + "/users/" + user.getId() + "/users");

        // Need to use array with a ResponseEntity here
        ResponseEntity<Player[]> result = restTemplate.getForEntity(uri, Player[].class);
        Player[] userArray = result.getBody();

        assertEquals(200, result.getStatusCode().value());
        assertEquals(1, userArray.length);
        assertEquals("Tim", userArray[0].getName());
    }

}