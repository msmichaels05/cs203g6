package com.amateuraces.login;

import java.util.Base64;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.amateuraces.security.AuthenticationService;
import com.amateuraces.user.User;

@RestController
public class loginController {

    private final AuthenticationService authenticationService;

    public loginController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestHeader("Authorization") String authHeader) {
        try {
            // Extract credentials from Basic Auth header
            String credentials = new String(Base64.getDecoder().decode(authHeader.replace("Basic ", "")));
            String[] parts = credentials.split(":", 2);
            String username = parts[0];
            String password = parts[1];

            // Perform authentication
            User user = authenticationService.authenticate(username, password);

            if (user != null) {
                return ResponseEntity.ok(user); // Or a token, depending on your setup
            } else {
                return ResponseEntity.status(401).body("Invalid credentials");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred during authentication");
        }
    }
}

