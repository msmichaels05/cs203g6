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

// import com.amateuraces.admin.Admin;
// import com.amateuraces.admin.AdminRepository;
// import com.amateuraces.admin.Admin;
// import com.amateuraces.admin.AdminRepository;

// /** Start an actual HTTP server listening at a random port */
// @SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
// class AdminIntegrationTest {

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

//     @Autowired
//     private AdminRepository adminRepository;

// 	@Autowired
// 	private BCryptPasswordEncoder encoder;

// 	@AfterEach
// 	void tearDown() {
// 		// clear the database after each test
//         adminRepository.deleteAll();
// 	}

// 	public void getAdmins_Success() throws Exception {
//         URI uri = new URI(baseUrl + port + "/adminRepository");
// 		adminRepository.save(new Admin("Timothy", "12345678"));
// 		adminRepository.save(new Admin("Timothys", "123456789"));

// 		ResponseEntity<Admin[]> result = restTemplate.getForEntity(uri, Admin[].class);
// 		Admin[] adminRepository = result.getBody();

// 		assertEquals(200, result.getStatusCode().value());
// 		assertEquals(1, adminRepository.length);		
//     }
	
// 	@Test
// 	public void getAdmin_ValidAdminId_Success() throws Exception {
// 		Admin admin = new Admin("Timothy", "12345678");
// 		Long id = adminRepository.save(admin).getId();
// 		URI uri = new URI(baseUrl + port + "/users/" + id);
// 		ResponseEntity<Admin> result = restTemplate.getForEntity(uri, Admin.class);

// 		assertEquals(200, result.getStatusCode().value());
// 		assertEquals(admin.getName(), result.getBody().getName());
// 	}

// 	@Test
// 	public void getAdmin_InvalidAdminId_Failure() throws Exception {
// 		URI uri = new URI(baseUrl + port + "/users/1");
// 		ResponseEntity<Admin> result = restTemplate.getForEntity(uri, Admin.class);

// 		assertEquals(404, result.getStatusCode().value());
// 	}

// 	@Test
// 	public void addAdmin_Success() throws Exception {
// 		URI uri = new URI(baseUrl + port + "/users");
		
// 		Admin admin = new Admin("Timothy", "12345678");

// 		ResponseEntity<Admin> result = restTemplate.withBasicAuth("admin", "goodpassword")
// 				.postForEntity(uri, admin, Admin.class);

// 		assertEquals(201, result.getStatusCode().value());
// 		assertEquals(admin.getName(), result.getBody().getName());
// 	}

//     @Test
// 	public void deleteAdmin_ValidAdminId_Success() throws Exception {
// 		Admin admin = adminRepository.save(new Admin("Timothy", "12345678"));
// 		URI uri = new URI(baseUrl + port + "/adminRepository/" + admin.getId());
// 		adminRepository.save(new Admin("admin@gmail.com", "admin", encoder.encode("goodpassword"), "ROLE_ADMIN"));

// 		ResponseEntity<Void> result = restTemplate.withBasicAuth("admin", "goodpassword")
// 				.exchange(uri, HttpMethod.DELETE, null, Void.class);
// 		assertEquals(200, result.getStatusCode().value());

// 		Optional<Admin> emptyValue = Optional.empty();
// 		assertEquals(emptyValue, adminRepository.findById(admin.getId()));
// 	}

// 	@Test
// 	public void deleteAdmin_InvalidAdminId_Failure() throws Exception {
// 		URI uri = new URI(baseUrl + port + "/adminRepository/1");
// 		adminRepository.save(new Admin("admin@gmail.com", "admin", encoder.encode("goodpassword"), "ROLE_ADMIN"));
// 		ResponseEntity<Void> result = restTemplate.withBasicAuth("admin", "goodpassword")
// 				.exchange(uri, HttpMethod.DELETE, null, Void.class);
// 		assertEquals(404, result.getStatusCode().value());
// 	}

// 	@Test
// 	public void updateAdmin_ValidAdminId_Success() throws Exception {
// 		Admin admin = adminRepository.save(new Admin("jayuss@gmail.com", "Jayuss", "12345678"));
// 		URI uri = new URI(baseUrl + port + "/adminRepository/" + admin.getId().longValue());
// 		Admin newAdminInfo = new Admin("jayuss@gmail.com", "Jayuss", "87654321");
// 		adminRepository.save(new Admin("admin@gmail.com", "admin", encoder.encode("goodpassword"), "ROLE_ADMIN"));
// 		ResponseEntity<Admin> result = restTemplate.withBasicAuth("admin", "goodpassword")
// 				.exchange(uri, HttpMethod.PUT, new HttpEntity<>(newAdminInfo), Admin.class);
// 		assertEquals(200, result.getStatusCode().value());
// 		assertEquals(newAdminInfo.getAdminname(), result.getBody().getAdminname());
// 	}

// 	@Test
// 	public void updateAdmin_InvalidAdminId_Failure() throws Exception {
// 		URI uri = new URI(baseUrl + port + "/adminRepository/1");
// 		Admin newAdminInfo = new Admin("jackie@gmail.com", "Jackie", "1029384757");
// 		adminRepository.save(new Admin("admin@gmail.com", "admin", encoder.encode("goodpassword"), "ROLE_ADMIN"));
// 		ResponseEntity<Admin> result = restTemplate.withBasicAuth("admin", "goodpassword")
// 				.exchange(uri, HttpMethod.PUT, new HttpEntity<>(newAdminInfo), Admin.class);
// 		assertEquals(404, result.getStatusCode().value());
// 	}
// }