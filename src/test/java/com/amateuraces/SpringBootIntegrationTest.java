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















    

    // @Test
	// public void getUsers_Success() throws Exception {
	// 	URI uri = new URI(baseUrl + port + "/users");
	// 	users.save(new User("Timothy", "password"));

	// 	// Need to use array with a ReponseEntity here
	// 	ResponseEntity<User[]> result = restTemplate.getForEntity(uri, User[].class);
	// 	User[] users = result.getBody();

	// 	assertEquals(200, result.getStatusCode().value());
	// 	assertEquals(1, users.length);
	// }
	// public void getPlayers_Success() throws Exception {
    //     // Create and save a User
    //     User user = new User("testuser", encoder.encode("password"), "ROLE_USER");
    //     user = users.save(user);

    //     // Create and save a Player associated with the User
    //     Player user = new Player("Tim");
    //     user.setUser(user);
    //     users.save(user);

    //     // Construct the URI using the saved User's ID
    //     URI uri = new URI(baseUrl + port + "/users/" + user.getId() + "/users");

    //     // Need to use array with a ResponseEntity here
    //     ResponseEntity<Player[]> result = restTemplate.getForEntity(uri, Player[].class);
    //     Player[] userArray = result.getBody();

    //     assertEquals(200, result.getStatusCode().value());
    //     assertEquals(1, userArray.length);
    //     assertEquals("Tim", userArray[0].getName());
    // }

	// @Test
	// public void getUser_ValidUserId_Success() throws Exception {
	// 	User user = new User("Timothy", "password");
	// 	Long id = users.save(user).getId();
	// 	URI uri = new URI(baseUrl + port + "/users/" + id);
	// 	ResponseEntity<User> result = restTemplate.getForEntity(uri, User.class);

	// 	assertEquals(200, result.getStatusCode().value());
	// 	assertEquals(user.getUsername(), result.getBody().getUsername());
	// }

	// @Test
	// public void addUser_Success() throws Exception {
	// 	URI uri = new URI(baseUrl + port + "/users");
		
	// 	User user = new User("Timothy", "password");
	// 	users.save(new User("admin", encoder.encode("goodpassword"), "ROLE_ADMIN"));

	// 	ResponseEntity<User> result = restTemplate.withBasicAuth("admin", "goodpassword")
	// 			.postForEntity(uri, user, User.class);

	// 	assertEquals(201, result.getStatusCode().value());
	// 	assertEquals(user.getUsername(), result.getBody().getUsername());
	// }
    // @Test
    // public void addUser_Success() throws Exception {
    //     URI uri = new URI(baseUrl + port + "/users");

    //     User user = new User("Timothy", encoder.encode("password"), "ROLE_ADMIN");
    //     users.save(new User("admin", encoder.encode("goodpassword"), "ROLE_ADMIN"));

    //     ResponseEntity<User> result = restTemplate.withBasicAuth("admin", "goodpassword")
    //             .postForEntity(uri, user, User.class);

    //     assertEquals(201, result.getStatusCode().value());
    //     assertEquals("Timothy", result.getBody().getUsername());
    // }

	// @Test
	// public void deleteUser_InvalidUserId_Failure() throws Exception {
	// 	URI uri = new URI(baseUrl + port + "/users/1");
	// 	users.save(new User("admin", encoder.encode("goodpassword"), "ROLE_ADMIN"));
	// 	ResponseEntity<Void> result = restTemplate.withBasicAuth("admin", "goodpassword")
	// 			.exchange(uri, HttpMethod.DELETE, null, Void.class);
	// 	assertEquals(404, result.getStatusCode().value());
	// }

	// @Test
	// public void updateUser_ValidUserId_Success() throws Exception {
	// 	User user = users.save(new User("Jayuss", "12345678"));
	// 	URI uri = new URI(baseUrl + port + "/users/" + user.getId().longValue());
	// 	User newUserInfo = new User("Timmy15243", "12345678");
	// 	users.save(new User("admin", encoder.encode("goodpassword"), "ROLE_ADMIN"));
	// 	ResponseEntity<User> result = restTemplate.withBasicAuth("admin", "goodpassword")
	// 			.exchange(uri, HttpMethod.PUT, new HttpEntity<>(newUserInfo), User.class);
	// 	assertEquals(200, result.getStatusCode().value());
	// 	assertEquals(newUserInfo.getUsername(), result.getBody().getUsername());
	// }

	// @Test
	// public void updateUser_InvalidUserId_Failure() throws Exception {
	// 	URI uri = new URI(baseUrl + port + "/users/1");
	// 	User newUserInfo = new User("Jackie", "1029384757");
	// 	users.save(new User("admin", encoder.encode("goodpassword"), "ROLE_ADMIN"));
	// 	ResponseEntity<User> result = restTemplate.withBasicAuth("admin", "goodpassword")
	// 			.exchange(uri, HttpMethod.PUT, new HttpEntity<>(newUserInfo), User.class);
	// 	assertEquals(404, result.getStatusCode().value());
	// }
}

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

// import com.amateuraces.user.Player;
// import com.amateuraces.user.PlayerRepository;
// import com.amateuraces.user.User;
// import com.amateuraces.user.UserRepository;

// /** Start an actual HTTP server listening at a random port */
// @SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
// class SpringBootIntegrationTest {

//     @LocalServerPort
//     private int port;

//     private final String baseUrl = "http://localhost:";

//     @Autowired
//     private TestRestTemplate restTemplate;

//     @Autowired
//     private PlayerRepository users;

//     @Autowired
//     private UserRepository users;

//     @Autowired
//     private BCryptPasswordEncoder encoder;

//     @AfterEach
//     void tearDown() {
//         // clear the database after each test
//         users.deleteAll();
//         users.deleteAll();	
//     }

//     @Test
//     public void getPlayers_Success() throws Exception {
//         // Create and save a User
//         User user = new User("testuser", encoder.encode("password"), "ROLE_USER");
//         user = users.save(user);

//         // Create and save a Player associated with the User
//         Player user = new Player("Tim");
//         user.setUser(user);
//         users.save(user);

//         // Construct the URI using the saved User's ID
//         URI uri = new URI(baseUrl + port + "/users/" + user.getId() + "/users");

//         // Need to use array with a ResponseEntity here
//         ResponseEntity<Player[]> result = restTemplate.getForEntity(uri, Player[].class);
//         Player[] userArray = result.getBody();

//         assertEquals(200, result.getStatusCode().value());
//         assertEquals(1, userArray.length);
//         assertEquals("Tim", userArray[0].getName());
//     }

//     @Test
//     public void getPlayer_ValidPlayerId_Success() throws Exception {
//         // Create and save a User
//         User user = new User("testuser", encoder.encode("password"), "ROLE_USER");
//         user = users.save(user);

//         // Create and save a Player associated with the User
//         Player user = new Player("Tim");
//         user.setUser(user);
//         user = users.save(user);

//         // Construct the URI using the saved User's and Player's IDs
//         URI uri = new URI(baseUrl + port + "/users/" + user.getId() + "/users/" + user.getId());

//         ResponseEntity<Player> result = restTemplate.getForEntity(uri, Player.class);

//         assertEquals(200, result.getStatusCode().value());
//         assertEquals(user.getName(), result.getBody().getName());
//     }

//     @Test
//     public void getPlayer_InvalidPlayerId_Failure() throws Exception {
//         // Create and save a User
//         User user = new User("testuser", encoder.encode("password"), "ROLE_USER");
//         user = users.save(user);

//         // Construct the URI with non-existing Player ID
//         URI uri = new URI(baseUrl + port + "/users/" + user.getId() + "/users/9999");
//         ResponseEntity<Player> result = restTemplate.getForEntity(uri, Player.class);

//         assertEquals(404, result.getStatusCode().value());
//     }

//     @Test
//     public void addPlayer_Success() throws Exception {
//         // Create and save a User
//         User user = new User("admin", encoder.encode("goodpassword"), "ROLE_ADMIN");
//         user = users.save(user);

//         // Construct the URI using the saved User's ID
//         URI uri = new URI(baseUrl + port + "/users/" + user.getId() + "/users");
        
//         Player user = new Player("Tom");

//         // Add authentication for the admin user
//         ResponseEntity<Player> result = restTemplate.withBasicAuth("admin", "goodpassword")
//                 .postForEntity(uri, user, Player.class);

//         assertEquals(201, result.getStatusCode().value());
//         assertEquals(user.getName(), result.getBody().getName());
//     }

//     @Test
//     public void deletePlayer_ValidPlayerId_Success() throws Exception {
//         // Create and save a User
//         User user = new User("admin", encoder.encode("goodpassword"), "ROLE_ADMIN");
//         user = users.save(user);

//         // Create and save a Player associated with the User
//         Player user = new Player("Michael");
//         user.setUser(user);
//         user = users.save(user);

//         // Construct the URI using the saved User's and Player's IDs
//         URI uri = new URI(baseUrl + port + "/users/" + user.getId() + "/users/" + user.getId());

//         // Add authentication for the admin user
//         ResponseEntity<Void> result = restTemplate.withBasicAuth("admin", "goodpassword")
//                 .exchange(uri, HttpMethod.DELETE, null, Void.class);
//         assertEquals(200, result.getStatusCode().value());

//         // Verify that the Player has been deleted
//         Optional<Player> emptyValue = users.findById(user.getId());
//         assertEquals(Optional.empty(), emptyValue);
//     }

//     @Test
//     public void deletePlayer_InvalidPlayerId_Failure() throws Exception {
//         // Create and save a User
//         User user = new User("admin", encoder.encode("goodpassword"), "ROLE_ADMIN");
//         user = users.save(user);

//         // Construct the URI with non-existing Player ID
//         URI uri = new URI(baseUrl + port + "/users/" + user.getId() + "/users/9999");

//         // Add authentication for the admin user
//         ResponseEntity<Void> result = restTemplate.withBasicAuth("admin", "goodpassword")
//                 .exchange(uri, HttpMethod.DELETE, null, Void.class);
//         assertEquals(404, result.getStatusCode().value());
//     }

//     @Test
//     public void updatePlayer_ValidPlayerId_Success() throws Exception {
//         // Create and save a User
//         User user = new User("admin", encoder.encode("goodpassword"), "ROLE_ADMIN");
//         user = users.save(user);

//         // Create and save a Player associated with the User
//         Player user = new Player("Jay");
//         user.setUser(user);
//         user = users.save(user);

//         // Construct the URI using the saved User's and Player's IDs
//         URI uri = new URI(baseUrl + port + "/users/" + user.getId() + "/users/" + user.getId());
//         Player newPlayerInfo = new Player("Lee Jay");

//         // Add authentication for the admin user
//         ResponseEntity<Player> result = restTemplate.withBasicAuth("admin", "goodpassword")
//                 .exchange(uri, HttpMethod.PUT, new HttpEntity<>(newPlayerInfo), Player.class);
//         assertEquals(200, result.getStatusCode().value());
//         assertEquals(newPlayerInfo.getName(), result.getBody().getName());
//     }

//     @Test
//     public void updatePlayer_InvalidPlayerId_Failure() throws Exception {
//         // Create and save a User
//         User user = new User("admin", encoder.encode("goodpassword"), "ROLE_ADMIN");
//         user = users.save(user);

//         // Construct the URI with non-existing Player ID
//         URI uri = new URI(baseUrl + port + "/users/" + user.getId() + "/users/9999");
//         Player newPlayerInfo = new Player("Jackie");

//         // Add authentication for the admin user
//         ResponseEntity<Player> result = restTemplate.withBasicAuth("admin", "goodpassword")
//                 .exchange(uri, HttpMethod.PUT, new HttpEntity<>(newPlayerInfo), Player.class);
//         assertEquals(404, result.getStatusCode().value());
//     }
// }