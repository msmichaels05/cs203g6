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
// class SpringBootIntegrationTest {

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

// 	@Autowired
// 	private UserRepository users;

// 	@Autowired
// 	private BCryptPasswordEncoder encoder;

// 	@AfterEach
// 	void tearDown() {
// 		// clear the database after each test
// 		players.deleteAll();
// 		users.deleteAll();	
// 	}

// 	@Test
// 	// public void getPlayers_Success() throws Exception {
// 	// 	URI uri = new URI(baseUrl + port + "/users/1/players");
// 	// 	players.save(new Player("Tim"));

// 	// 	// Need to use array with a ReponseEntity here
// 	// 	ResponseEntity<Player[]> result = restTemplate.getForEntity(uri, Player[].class);
// 	// 	Player[] playerArray = result.getBody();

// 	// 	assertEquals(200, result.getStatusCode().value());
// 	// 	assertEquals(1, playerArray.length);
// 	// }
// 	public void getPlayers_Success() throws Exception {
//         // Create and save a User
//         User user = new User("testuser", encoder.encode("password"), "ROLE_USER");
//         user = users.save(user);

//         // Create and save a Player associated with the User
//         Player player = new Player("Tim");
//         player.setUser(user);
//         players.save(player);

//         // Construct the URI using the saved User's ID
//         URI uri = new URI(baseUrl + port + "/users/" + user.getId() + "/players");

//         // Need to use array with a ResponseEntity here
//         ResponseEntity<Player[]> result = restTemplate.getForEntity(uri, Player[].class);
//         Player[] playerArray = result.getBody();

//         assertEquals(200, result.getStatusCode().value());
//         assertEquals(1, playerArray.length);
//         assertEquals("Tim", playerArray[0].getName());
//     }

// 	@Test
// 	public void getPlayer_ValidPlayerId_Success() throws Exception {
// 		Player player = new Player("Tim");
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
// 		URI uri = new URI(baseUrl + port + "/users/1/players");
		
// 		Player player = new Player("Tom");
// 		users.save(new User("admin", encoder.encode("goodpassword"), "ROLE_ADMIN"));

// 		ResponseEntity<Player> result = restTemplate.withBasicAuth("admin", "goodpassword")
// 				.postForEntity(uri, player, Player.class);

// 		assertEquals(201, result.getStatusCode().value());
// 		assertEquals(player.getName(), result.getBody().getName());
// 	}

// 	@Test
// 	public void deletePlayer_ValidPlayerId_Success() throws Exception {
// 		Player player = players.save(new Player("Michael"));
// 		URI uri = new URI(baseUrl + port + "/users/" + player.getId().longValue() + "/players/" + player.getId().longValue());
// 		users.save(new User("admin", encoder.encode("goodpassword"), "ROLE_ADMIN"));

// 		ResponseEntity<Void> result = restTemplate.withBasicAuth("admin", "goodpassword")
// 				.exchange(uri, HttpMethod.DELETE, null, Void.class);
// 		assertEquals(200, result.getStatusCode().value());

// 		Optional<Player> emptyValue = Optional.empty();
// 		assertEquals(emptyValue, players.findById(player.getId()));
// 	}

// 	@Test
// 	public void deletePlayer_InvalidPlayerId_Failure() throws Exception {
// 		URI uri = new URI(baseUrl + port + "/users/1/players/1");
// 		users.save(new User("admin", encoder.encode("goodpassword"), "ROLE_ADMIN"));
// 		ResponseEntity<Void> result = restTemplate.withBasicAuth("admin", "goodpassword")
// 				.exchange(uri, HttpMethod.DELETE, null, Void.class);
// 		assertEquals(404, result.getStatusCode().value());
// 	}

// 	@Test
// 	public void updatePlayer_ValidPlayerId_Success() throws Exception {
// 		Player player = players.save(new Player("Jay"));
// 		URI uri = new URI(baseUrl + port + "/users/" + player.getId().longValue() + "/players/" + player.getId().longValue());
// 		Player newPlayerInfo = new Player("Lee Jay");
// 		users.save(new User("admin", encoder.encode("goodpassword"), "ROLE_ADMIN"));
// 		ResponseEntity<Player> result = restTemplate.withBasicAuth("admin", "goodpassword")
// 				.exchange(uri, HttpMethod.PUT, new HttpEntity<>(newPlayerInfo), Player.class);
// 		assertEquals(200, result.getStatusCode().value());
// 		assertEquals(newPlayerInfo.getName(), result.getBody().getName());
// 	}

// 	@Test
// 	public void updatePlayer_InvalidPlayerId_Failure() throws Exception {
// 		URI uri = new URI(baseUrl + port + "/users/1/players/1");
// 		Player newPlayerInfo = new Player("Jackie");
// 		users.save(new User("admin", encoder.encode("goodpassword"), "ROLE_ADMIN"));
// 		ResponseEntity<Player> result = restTemplate.withBasicAuth("admin", "goodpassword")
// 				.exchange(uri, HttpMethod.PUT, new HttpEntity<>(newPlayerInfo), Player.class);
// 		assertEquals(404, result.getStatusCode().value());
// 	}
// }

// // package com.amateuraces;

// // import java.net.URI;
// // import java.util.Optional;

// // import org.junit.jupiter.api.AfterEach;
// // import static org.junit.jupiter.api.Assertions.assertEquals;
// // import org.junit.jupiter.api.Test;
// // import org.springframework.beans.factory.annotation.Autowired;
// // import org.springframework.boot.test.context.SpringBootTest;
// // import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
// // import org.springframework.boot.test.web.client.TestRestTemplate;
// // import org.springframework.boot.test.web.server.LocalServerPort;
// // import org.springframework.http.HttpEntity;
// // import org.springframework.http.HttpMethod;
// // import org.springframework.http.ResponseEntity;
// // import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

// // import com.amateuraces.player.Player;
// // import com.amateuraces.player.PlayerRepository;
// // import com.amateuraces.user.User;
// // import com.amateuraces.user.UserRepository;

// // /** Start an actual HTTP server listening at a random port */
// // @SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
// // class SpringBootIntegrationTest {

// //     @LocalServerPort
// //     private int port;

// //     private final String baseUrl = "http://localhost:";

// //     @Autowired
// //     private TestRestTemplate restTemplate;

// //     @Autowired
// //     private PlayerRepository players;

// //     @Autowired
// //     private UserRepository users;

// //     @Autowired
// //     private BCryptPasswordEncoder encoder;

// //     @AfterEach
// //     void tearDown() {
// //         // clear the database after each test
// //         players.deleteAll();
// //         users.deleteAll();	
// //     }

// //     @Test
// //     public void getPlayers_Success() throws Exception {
// //         // Create and save a User
// //         User user = new User("testuser", encoder.encode("password"), "ROLE_USER");
// //         user = users.save(user);

// //         // Create and save a Player associated with the User
// //         Player player = new Player("Tim");
// //         player.setUser(user);
// //         players.save(player);

// //         // Construct the URI using the saved User's ID
// //         URI uri = new URI(baseUrl + port + "/users/" + user.getId() + "/players");

// //         // Need to use array with a ResponseEntity here
// //         ResponseEntity<Player[]> result = restTemplate.getForEntity(uri, Player[].class);
// //         Player[] playerArray = result.getBody();

// //         assertEquals(200, result.getStatusCode().value());
// //         assertEquals(1, playerArray.length);
// //         assertEquals("Tim", playerArray[0].getName());
// //     }

// //     @Test
// //     public void getPlayer_ValidPlayerId_Success() throws Exception {
// //         // Create and save a User
// //         User user = new User("testuser", encoder.encode("password"), "ROLE_USER");
// //         user = users.save(user);

// //         // Create and save a Player associated with the User
// //         Player player = new Player("Tim");
// //         player.setUser(user);
// //         player = players.save(player);

// //         // Construct the URI using the saved User's and Player's IDs
// //         URI uri = new URI(baseUrl + port + "/users/" + user.getId() + "/players/" + player.getId());

// //         ResponseEntity<Player> result = restTemplate.getForEntity(uri, Player.class);

// //         assertEquals(200, result.getStatusCode().value());
// //         assertEquals(player.getName(), result.getBody().getName());
// //     }

// //     @Test
// //     public void getPlayer_InvalidPlayerId_Failure() throws Exception {
// //         // Create and save a User
// //         User user = new User("testuser", encoder.encode("password"), "ROLE_USER");
// //         user = users.save(user);

// //         // Construct the URI with non-existing Player ID
// //         URI uri = new URI(baseUrl + port + "/users/" + user.getId() + "/players/9999");
// //         ResponseEntity<Player> result = restTemplate.getForEntity(uri, Player.class);

// //         assertEquals(404, result.getStatusCode().value());
// //     }

// //     @Test
// //     public void addPlayer_Success() throws Exception {
// //         // Create and save a User
// //         User user = new User("admin", encoder.encode("goodpassword"), "ROLE_ADMIN");
// //         user = users.save(user);

// //         // Construct the URI using the saved User's ID
// //         URI uri = new URI(baseUrl + port + "/users/" + user.getId() + "/players");
        
// //         Player player = new Player("Tom");

// //         // Add authentication for the admin user
// //         ResponseEntity<Player> result = restTemplate.withBasicAuth("admin", "goodpassword")
// //                 .postForEntity(uri, player, Player.class);

// //         assertEquals(201, result.getStatusCode().value());
// //         assertEquals(player.getName(), result.getBody().getName());
// //     }

// //     @Test
// //     public void deletePlayer_ValidPlayerId_Success() throws Exception {
// //         // Create and save a User
// //         User user = new User("admin", encoder.encode("goodpassword"), "ROLE_ADMIN");
// //         user = users.save(user);

// //         // Create and save a Player associated with the User
// //         Player player = new Player("Michael");
// //         player.setUser(user);
// //         player = players.save(player);

// //         // Construct the URI using the saved User's and Player's IDs
// //         URI uri = new URI(baseUrl + port + "/users/" + user.getId() + "/players/" + player.getId());

// //         // Add authentication for the admin user
// //         ResponseEntity<Void> result = restTemplate.withBasicAuth("admin", "goodpassword")
// //                 .exchange(uri, HttpMethod.DELETE, null, Void.class);
// //         assertEquals(200, result.getStatusCode().value());

// //         // Verify that the Player has been deleted
// //         Optional<Player> emptyValue = players.findById(player.getId());
// //         assertEquals(Optional.empty(), emptyValue);
// //     }

// //     @Test
// //     public void deletePlayer_InvalidPlayerId_Failure() throws Exception {
// //         // Create and save a User
// //         User user = new User("admin", encoder.encode("goodpassword"), "ROLE_ADMIN");
// //         user = users.save(user);

// //         // Construct the URI with non-existing Player ID
// //         URI uri = new URI(baseUrl + port + "/users/" + user.getId() + "/players/9999");

// //         // Add authentication for the admin user
// //         ResponseEntity<Void> result = restTemplate.withBasicAuth("admin", "goodpassword")
// //                 .exchange(uri, HttpMethod.DELETE, null, Void.class);
// //         assertEquals(404, result.getStatusCode().value());
// //     }

// //     @Test
// //     public void updatePlayer_ValidPlayerId_Success() throws Exception {
// //         // Create and save a User
// //         User user = new User("admin", encoder.encode("goodpassword"), "ROLE_ADMIN");
// //         user = users.save(user);

// //         // Create and save a Player associated with the User
// //         Player player = new Player("Jay");
// //         player.setUser(user);
// //         player = players.save(player);

// //         // Construct the URI using the saved User's and Player's IDs
// //         URI uri = new URI(baseUrl + port + "/users/" + user.getId() + "/players/" + player.getId());
// //         Player newPlayerInfo = new Player("Lee Jay");

// //         // Add authentication for the admin user
// //         ResponseEntity<Player> result = restTemplate.withBasicAuth("admin", "goodpassword")
// //                 .exchange(uri, HttpMethod.PUT, new HttpEntity<>(newPlayerInfo), Player.class);
// //         assertEquals(200, result.getStatusCode().value());
// //         assertEquals(newPlayerInfo.getName(), result.getBody().getName());
// //     }

// //     @Test
// //     public void updatePlayer_InvalidPlayerId_Failure() throws Exception {
// //         // Create and save a User
// //         User user = new User("admin", encoder.encode("goodpassword"), "ROLE_ADMIN");
// //         user = users.save(user);

// //         // Construct the URI with non-existing Player ID
// //         URI uri = new URI(baseUrl + port + "/users/" + user.getId() + "/players/9999");
// //         Player newPlayerInfo = new Player("Jackie");

// //         // Add authentication for the admin user
// //         ResponseEntity<Player> result = restTemplate.withBasicAuth("admin", "goodpassword")
// //                 .exchange(uri, HttpMethod.PUT, new HttpEntity<>(newPlayerInfo), Player.class);
// //         assertEquals(404, result.getStatusCode().value());
// //     }
// // }