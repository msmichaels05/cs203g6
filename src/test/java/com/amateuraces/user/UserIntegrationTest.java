package com.amateuraces.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URI;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.boot.test.web.client.TestRestTemplate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)  // Defined port instead of random port
public class UserIntegrationTest {

    private String port = "http://localhost:8080";  // Hardcoded base URL for local server

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    private TestRestTemplate testrestTemplate = new TestRestTemplate();

    private User adminUser;

    @BeforeEach
    public void setUp() {
        // Set up an admin user for authentication
        adminUser = new User("admin", encoder.encode("goodpassword"), "ROLE_ADMIN");
        userRepository.save(adminUser);
    }

    @Test
    public void deleteUser_ValidUserId_Success() throws Exception {
        // Arrange: Create a new user
        User user = userRepository.save(new User("user1", encoder.encode("password1"), "ROLE_USER"));
        URI uri = new URI(port + port + "/users/" + user.getId().longValue());
        userRepository.save(new User("admin", encoder.encode("goodpassword"), "ROLE_ADMIN"));
        ResponseEntity<Void> result = testrestTemplate.withBasicAuth("admin", "goodpassword")
        .exchange(uri, HttpMethod.DELETE, null, Void.class);
        assertEquals(200, result.getStatusCode().value());
        // An empty Optional should be returned by "findById" after deletion
        Optional<User> emptyValue = Optional.empty();
        assertEquals(emptyValue, userRepository.findById(user.getId()));

        // Act: Send DELETE request with basic authentication
        ResponseEntity<Void> newresult = testrestTemplate.withBasicAuth("admin", "goodpassword")
                .exchange(uri, HttpMethod.DELETE, null, Void.class);

        // Assert: Check that the status code is 200 (OK)
        assertEquals(200, newresult.getStatusCode().value());

        // Assert: Check that the user no longer exists in the repository
        Optional<User> deletedUser = userRepository.findById(user.getId());
        assertTrue(deletedUser.isEmpty());
    }
    
    @Test
    public void deleteUser_InvalidUserId_Failure() throws Exception {
        // Act: Try to delete a non-existing user (ID 9999)
        URI uri = new URI(port + port + "/users/9999");
        ResponseEntity<Void> result = testrestTemplate.withBasicAuth("admin", "goodpassword")
                .exchange(uri, HttpMethod.DELETE, null, Void.class);

        // Assert: Check that the status code is 404 (Not Found)
        assertEquals(404, result.getStatusCode().value());
    }

    @Test
    public void updateUser_ValidUserId_Success() throws Exception {
        // Arrange: Create a new user
        User user = userRepository.save(new User("user1", encoder.encode("password1"), "ROLE_USER"));
        URI uri = new URI(port + "/users/" + user.getId());

        // Prepare new user data
        User updatedUserInfo = new User("updatedUser1", encoder.encode("newpassword"), "ROLE_USER");

        // Act: Send PUT request to update the user
        ResponseEntity<User> result = testrestTemplate.withBasicAuth("admin", "goodpassword")
                .exchange(uri, HttpMethod.PUT, new HttpEntity<>(updatedUserInfo), User.class);

        // Assert: Check that the status code is 200 (OK)
        assertEquals(200, result.getStatusCodeValue());

        // Assert: Check that the user's details have been updated
        User updatedUser = result.getBody();
        assertEquals(updatedUserInfo.getUsername(), updatedUser.getUsername());
    }

    @Test
    public void updateUser_InvalidUserId_Failure() throws Exception {
        // Arrange: Prepare user data for a non-existing user (ID 9999)
        URI uri = new URI(port + "/users/9999");
        User updatedUserInfo = new User("updatedUser1", encoder.encode("newpassword"), "ROLE_USER");

        // Act: Try to update a non-existing user
        ResponseEntity<User> result = testrestTemplate.withBasicAuth("admin", "goodpassword")
                .exchange(uri, HttpMethod.PUT, new HttpEntity<>(updatedUserInfo), User.class);

        // Assert: Check that the status code is 404 (Not Found)
        assertEquals(404, result.getStatusCodeValue());
    }
}
