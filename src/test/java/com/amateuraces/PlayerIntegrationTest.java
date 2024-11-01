// package com.amateuraces;

// import java.net.URI;
// import java.util.Optional;

// import org.junit.jupiter.api.AfterEach;
// import static org.junit.jupiter.api.Assertions.assertEquals;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
// import org.springframework.boot.test.web.client.TestRestTemplate;
// import org.springframework.boot.test.web.server.LocalServerPort;
// import org.springframework.http.HttpEntity;
// import org.springframework.http.HttpMethod;
// import org.springframework.http.ResponseEntity;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

// import com.amateuraces.player.Player;
// import com.amateuraces.player.PlayerRepository;
// import com.amateuraces.user.User;
// import com.amateuraces.user.UserRepository;

// /** Start an actual HTTP server listening at a random port */
// @SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
// class PlayerIntegrationTest {

// 	@LocalServerPort
// 	private int port;

// 	private final String baseUrl = "http://localhost:";

// 	@Autowired
// 	/**
// 	 * Use TestRestTemplate for testing a real instance of your application as an
// 	 * external actor.
// 	 * TestRestTemplate is just a convenient subclass of RestTemplate that is
// 	 * suitable for integration tests.
// 	 * It is fault tolerant, and optionally can carry Basic authentication headers.
// 	 */
// 	private TestRestTemplate restTemplate;

// 	@Autowired
// 	private PlayerRepository players;

//     @Autowired
//     private UserRepository users;

// 	@Autowired
// 	private BCryptPasswordEncoder encoder;

// 	@AfterEach
// 	void tearDown() {
// 		// clear the database after each test
// 		players.deleteAll();
// 	}

// 	public void getPlayers_Success() throws Exception {
//         URI uri = new URI(baseUrl + port + "/users/1/players");
// 		players.save(new Player("Timothy", "12345678"));
// 		players.save(new Player("Jayuss", "12345678"));

// 		ResponseEntity<Player[]> result = restTemplate.getForEntity(uri, Player[].class);
// 		Player[] players = result.getBody();

// 		assertEquals(200, result.getStatusCode().value());
// 		assertEquals(1, players.length);		
//     }
	
// 	@Test
// 	public void getPlayer_ValidPlayerId_Success() throws Exception {
// 		Player player = new Player("Timothy", "12345678");
// 		Long id = players.save(player).getId();
// 		URI uri = new URI(baseUrl + port + "/users/" + id + "/players/" + id);
// 		ResponseEntity<Player> result = restTemplate.getForEntity(uri, Player.class);

// 		assertEquals(200, result.getStatusCode().value());
// 		assertEquals(player.getName(), result.getBody().getName());
// 	}

// 	@Test
// 	public void getPlayer_InvalidPlayerId_Failure() throws Exception {
// 		URI uri = new URI(baseUrl + port + "/users/1/players/1");
// 		ResponseEntity<Player> result = restTemplate.getForEntity(uri, Player.class);

// 		assertEquals(404, result.getStatusCode().value());
// 	}

// 	@Test
// 	public void addPlayer_Success() throws Exception {
// 		URI uri = new URI(baseUrl + port + "/players");
		
// 		Player player = new Player("Timothy", "12345678");
// 		users.save(new User("admin@gmail.com", "admin", encoder.encode("goodpassword"), "ROLE_ADMIN"));

// 		ResponseEntity<Player> result = restTemplate.withBasicAuth("admin", "goodpassword")
// 				.postForEntity(uri, player, Player.class);

// 		assertEquals(201, result.getStatusCode().value());
// 		assertEquals(player.getName(), result.getBody().getName());
// 	}

//     @Test
// 	public void deletePlayer_ValidPlayerId_Success() throws Exception {
// 		Player player = players.save(new Player("Timothy", "12345678"));
// 		URI uri = new URI(baseUrl + port + "/players/" + player.getId());
// 		users.save(new User("admin@gmail.com", "admin", encoder.encode("goodpassword"), "ROLE_ADMIN"));

// 		ResponseEntity<Void> result = restTemplate.withBasicAuth("admin", "goodpassword")
// 				.exchange(uri, HttpMethod.DELETE, null, Void.class);
// 		assertEquals(200, result.getStatusCode().value());

// 		Optional<Player> emptyValue = Optional.empty();
// 		assertEquals(emptyValue, players.findById(player.getId()));
// 	}

// 	@Test
// 	public void deletePlayer_InvalidPlayerId_Failure() throws Exception {
// 		URI uri = new URI(baseUrl + port + "/players/1");
// 		users.save(new User("admin@gmail.com", "admin", encoder.encode("goodpassword"), "ROLE_ADMIN"));
// 		ResponseEntity<Void> result = restTemplate.withBasicAuth("admin", "goodpassword")
// 				.exchange(uri, HttpMethod.DELETE, null, Void.class);
// 		assertEquals(404, result.getStatusCode().value());
// 	}

// 	@Test
// 	public void updatePlayer_ValidPlayerId_Success() throws Exception {
// 		Player player = players.save(new Player("Timothy", "12345678"));
// 		URI uri = new URI(baseUrl + port + "/players/" + player.getId().longValue());
// 		Player newPlayerInfo = new Player("Mariah", "12345678");
// 		users.save(new User("admin@gmail.com", "admin", encoder.encode("goodpassword"), "ROLE_ADMIN"));
// 		ResponseEntity<Player> result = restTemplate.withBasicAuth("admin", "goodpassword")
// 				.exchange(uri, HttpMethod.PUT, new HttpEntity<>(newPlayerInfo), Player.class);
// 		assertEquals(200, result.getStatusCode().value());
// 		assertEquals(newPlayerInfo.getName(), result.getBody().getName());
// 	}

// 	@Test
// 	public void updatePlayer_InvalidPlayerId_Failure() throws Exception {
// 		URI uri = new URI(baseUrl + port + "/players/1");
// 		Player newPlayerInfo = new Player("Jackie", "1029384757");
// 		users.save(new User("admin@gmail.com", "admin", encoder.encode("goodpassword"), "ROLE_ADMIN"));
// 		ResponseEntity<Player> result = restTemplate.withBasicAuth("admin", "goodpassword")
// 				.exchange(uri, HttpMethod.PUT, new HttpEntity<>(newPlayerInfo), Player.class);
// 		assertEquals(404, result.getStatusCode().value());
// 	}
// }