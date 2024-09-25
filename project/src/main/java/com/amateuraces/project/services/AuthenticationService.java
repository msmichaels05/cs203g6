package com.amateuraces.project.services;

import com.amateuraces.project.entity.User;
// import com.amateuraces.project.dtos.UserDTO;

public interface AuthenticationService {
    User authenticate(String email, String password);
    // User registerUser(UserDTO userDTO);
    void resetPassword(String email);
}