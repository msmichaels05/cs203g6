package com.amateuraces.user;

import java.util.List;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@Controller
public class UserController {
    private BCryptPasswordEncoder encoder;
    private UserService userService;

    public UserController(BCryptPasswordEncoder encoder, UserService userService) {
        this.encoder = encoder;
        this.userService = userService;
    }

    // 1. Registration Endpoint
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User()); // New user object for the registration form
        return "register"; // Return Thymeleaf template for registration
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute User user, Model model) {
        // Check if the username already exists
        if (userService.findByUsername(user.getUsername()).isPresent()) {
            model.addAttribute("error", "Username already exists. Please choose another one.");
            return "register";
        }

        // Encrypt password and set the authority (role)
        user.setPassword(encoder.encode(user.getPassword()));
        user.setAuthorities("ROLE_USER");

        // Save the user to the database
        userService.addUser(user);

        // Manually create the Authentication object and set it in the SecurityContext
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user.getUsername(), user.getPassword(), user.getAuthorities());

        // Set the authentication object in the SecurityContextHolder
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Redirect to player registration page
        System.out.println("Registered User ID: " + user.getId());
        return "redirect:/player/register?userId=" + user.getId(); // Pass the user ID to the player form
    }

    // 2. Login Endpoint
    @GetMapping("/login")
    public String showLoginForm() {
        return "login"; // Return Thymeleaf template for login
    }

    // Spring Security handles the actual login process, so you don't need to write
    // extra code for it

    @GetMapping("/home")
    public String home(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName(); // Get the logged-in username
            model.addAttribute("username", username);
            return "home"; // Return Thymeleaf template for home
        } else {
            return "redirect:/login"; // Redirect to login if not authenticated
        }
    }


    @ResponseBody
    @GetMapping("/users")
    public List<User> getUsers() {
        return userService.listUsers();
    }

    @ResponseBody
    @GetMapping("/users/{id}")
    public User getUser(@PathVariable Long id) {
        User user = userService.getUser(id);

        // Need to handle "book not found" error using proper HTTP status code
        // In this case it should be HTTP 404
        if (user == null)
            throw new UserNotFoundException(id);
        return userService.getUser(id);

    }

    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    @PostMapping("/users")
    public User addUser(@Valid @RequestBody User user) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"))) {
            user.setAuthorities("ROLE_ADMIN");
        } else {
            user.setAuthorities("ROLE_USER");
        }
        user.setPassword(encoder.encode(user.getPassword()));

        return userService.addUser(user);
    }

    @ResponseBody
    @PutMapping("/users/{id}")
    public User updateUser(@PathVariable Long id, @Valid @RequestBody User newUserInfo) {
        User user = userService.updateUser(id, newUserInfo);
        if (user == null)
            throw new UserNotFoundException(id);

        return user;
    }
    
    @ResponseBody
    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable Long id) {
        if (userService.getUser(id) == null)
            throw new UserNotFoundException(id);
        userService.deleteUser(id);
    }
    /**
     * Using BCrypt encoder to encrypt the password for storage
     * 
     * @param user
     * @return
     */
    // @PostMapping("/users")
    // public User addUser(@Valid @RequestBody User user){
    // user.setPassword(encoder.encode(user.getPassword()));
    // user.setAuthorities("ROLE_USER");
    // return userService.save(user);
    // }

    // @DeleteMapping("/users/{id}")
    // public void deleteUser(@PathVariable Long id){
    // if (users.getUser(id) == null) throw new UserNotFoundException(id);
    // users.deleteUser(id);
    // }

}