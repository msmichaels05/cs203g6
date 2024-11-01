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

// import com.amateuraces.user.User;
// import com.amateuraces.user.UserRepository;

// /** Start an actual HTTP server listening at a random port */
// @SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
// class UserIntegrationTest {

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
// 	private UserRepository users;

// 	@Autowired
// 	private BCryptPasswordEncoder encoder;

// 	@AfterEach
// 	void tearDown() {
// 		// clear the database after each test
// 		users.deleteAll();
// 	}


//     @Test
// 	public void getUsers_Success() throws Exception {
//         URI uri = new URI(baseUrl + port + "/users");
// 		users.save(new User("timothy@gmail.com", "Timothy", "password"));
// 		users.save(new User("jayuss@gmail.com", "Jayuss", "password2"));

// 		ResponseEntity<User[]> result = restTemplate.getForEntity(uri, User[].class);
// 		User[] users = result.getBody();

// 		assertEquals(200, result.getStatusCode().value());
// 		assertEquals(1, users.length);		
//     }
	
// 	@Test
// 	public void getUser_ValidUserId_Success() throws Exception {
// 		User user = new User("timothy@gmail.com", "Timothy", "password");
// 		Long id = users.save(user).getId();
// 		URI uri = new URI(baseUrl + port + "/users/" + id);
// 		ResponseEntity<User> result = restTemplate.getForEntity(uri, User.class);

// 		assertEquals(200, result.getStatusCode().value());
// 		assertEquals(user.getUsername(), result.getBody().getUsername());
// 	}

// 	@Test
// 	public void getUser_InvalidUserId_Failure() throws Exception {
// 		URI uri = new URI(baseUrl + port + "/users/1");
// 		ResponseEntity<User> result = restTemplate.getForEntity(uri, User.class);

// 		assertEquals(404, result.getStatusCode().value());
// 	}

// 	@Test
// 	public void addUser_Success() throws Exception {
// 		URI uri = new URI(baseUrl + port + "/users");
		
// 		User user = new User("timothy@gmail.com", "Timothy", "password");
// 		users.save(new User("admin@gmail.com", "admin", encoder.encode("goodpassword"), "ROLE_ADMIN"));

// 		ResponseEntity<User> result = restTemplate.withBasicAuth("admin", "goodpassword")
// 				.postForEntity(uri, user, User.class);

// 		assertEquals(201, result.getStatusCode().value());
// 		assertEquals(user.getUsername(), result.getBody().getUsername());
// 	}

//     @Test
// 	public void deleteUser_ValidUserId_Success() throws Exception {
// 		User user = users.save(new User("timothy@gmail.com", "Timothy", "password"));
// 		URI uri = new URI(baseUrl + port + "/users/" + user.getId());
// 		users.save(new User("admin@gmail.com", "admin", encoder.encode("goodpassword"), "ROLE_ADMIN"));

// 		ResponseEntity<Void> result = restTemplate.withBasicAuth("admin", "goodpassword")
// 				.exchange(uri, HttpMethod.DELETE, null, Void.class);
// 		assertEquals(200, result.getStatusCode().value());

// 		Optional<User> emptyValue = Optional.empty();
// 		assertEquals(emptyValue, users.findById(user.getId()));
// 	}

// 	@Test
// 	public void deleteUser_InvalidUserId_Failure() throws Exception {
// 		URI uri = new URI(baseUrl + port + "/users/1");
// 		users.save(new User("admin@gmail.com", "admin", encoder.encode("goodpassword"), "ROLE_ADMIN"));
// 		ResponseEntity<Void> result = restTemplate.withBasicAuth("admin", "goodpassword")
// 				.exchange(uri, HttpMethod.DELETE, null, Void.class);
// 		assertEquals(404, result.getStatusCode().value());
// 	}

// 	@Test
// 	public void updateUser_ValidUserId_Success() throws Exception {
// 		User user = users.save(new User("jayuss@gmail.com", "Jayuss", "12345678"));
// 		URI uri = new URI(baseUrl + port + "/users/" + user.getId().longValue());
// 		User newUserInfo = new User("jayuss@gmail.com", "Jayuss", "87654321");
// 		users.save(new User("admin@gmail.com", "admin", encoder.encode("goodpassword"), "ROLE_ADMIN"));
// 		ResponseEntity<User> result = restTemplate.withBasicAuth("admin", "goodpassword")
// 				.exchange(uri, HttpMethod.PUT, new HttpEntity<>(newUserInfo), User.class);
// 		assertEquals(200, result.getStatusCode().value());
// 		assertEquals(newUserInfo.getUsername(), result.getBody().getUsername());
// 	}

// 	@Test
// 	public void updateUser_InvalidUserId_Failure() throws Exception {
// 		URI uri = new URI(baseUrl + port + "/users/1");
// 		User newUserInfo = new User("jackie@gmail.com", "Jackie", "1029384757");
// 		users.save(new User("admin@gmail.com", "admin", encoder.encode("goodpassword"), "ROLE_ADMIN"));
// 		ResponseEntity<User> result = restTemplate.withBasicAuth("admin", "goodpassword")
// 				.exchange(uri, HttpMethod.PUT, new HttpEntity<>(newUserInfo), User.class);
// 		assertEquals(404, result.getStatusCode().value());
// 	}
// }