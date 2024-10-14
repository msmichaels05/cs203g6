package com.amateuraces.user;

import org.springframework.ui.Model;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;


@Controller  // Use @Controller instead of @RestController
public class UserController {
    private UserRepository users;
    private BCryptPasswordEncoder encoder;

    public UserController(UserRepository users, BCryptPasswordEncoder encoder){
        this.users = users;
        this.encoder = encoder;
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        return users.findAll();
    }

    @GetMapping("login")
    public List<User> getLogin() {
        return users.findAll();
    }

    // Display the registration form
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());  // Bind a new User object to the form
        return "register";  // Return the Thymeleaf template for registration
    }


    /**
    * Using BCrypt encoder to encrypt the password for storage 
    * @param user
     * @return
     */

     @PostMapping("/register")
    public String addUser(@Valid @ModelAttribute User user){
        user.setPassword(encoder.encode(user.getPassword()));
        user.setAuthorities("ROLE_USER");
        users.save(user);
        return "redirect:/login";
    }


}