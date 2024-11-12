package com.amateuraces.security;

import com.amateuraces.user.*;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository; // Assuming you have a UserRepository

    public AuthenticationService(UserDetailsService userDetailsService, 
                                 BCryptPasswordEncoder passwordEncoder,
                                 UserRepository userRepository) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    /**
     * Authenticate user based on username and password.
     * 
     * @param username The username of the user
     * @param password The password to authenticate
     * @return User object if authentication is successful, otherwise throws exception
     */
    public User authenticate(String username, String password) throws BadCredentialsException {
        // Load user details based on username
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        
        // Check if the password is correct using the BCryptPasswordEncoder
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid credentials");
        }
        
        // Return the authenticated user (Assuming you have a User model)
        return userRepository.findByUsername(username).orElseThrow(() -> 
            new BadCredentialsException("User not found"));
    }

    // If using JWTs or other methods, you could add additional methods for token generation, etc.
}
