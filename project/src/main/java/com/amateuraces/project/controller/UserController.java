package com.amateuraces.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {

    // private AuthenticationService authenticationService; to be used

    // Constructor Injection

    @GetMapping("/login")
    public String showLoginPage(Model model) {
        // Return login view
        return "";
    }

    // @PostMapping("/login")
    // public String login(@ModelAttribute UserDTO userDTO) {
    //     // Handle login
    // }

    @GetMapping("/logout")
    public String logout() {
        // Handle logout
        return "";
    }

    @GetMapping("/register")
    public String showRegistrationPage(Model model) {
        // Return registration view
        return "";
    }

    // @PostMapping("/register")
    // public String register(@ModelAttribute UserDTO userDTO) {
    //     // Handle registration
    // }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam String email) {
        // Handle password reset
        return "";
    }
}
