package com.amateuraces.user;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {
    private UserRepository users;
    private BCryptPasswordEncoder encoder;

    public UserController(UserRepository users, BCryptPasswordEncoder encoder){
        this.users = users;
        this.encoder = encoder;
    }

<<<<<<< Updated upstream
=======
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

    //JwtTokenisation things

    // ---------------------------------------------------
    @ResponseBody
    @GetMapping("/users/me")
    // @PreAuthorize("isAuthenticated()")
    public ResponseEntity<User> authenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User currentUser = (User) authentication.getPrincipal();

        return ResponseEntity.ok(currentUser);
    }

    // @ResponseBody
    // @GetMapping("/users")
    // @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    // public ResponseEntity<List<User>> allUsers() {
    //     List<User> users = userService.allUsers();
    //     return ResponseEntity.ok(users);
    // }

    // ---------------------------------------------------

    @ResponseBody
>>>>>>> Stashed changes
    @GetMapping("/users")
    // @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public List<User> getUsers() {
        return users.findAll();
    }

    /**
    * Using BCrypt encoder to encrypt the password for storage 
    * @param user
     * @return
     */
    @PostMapping("/users")
    public User addUser(@Valid @RequestBody User user){
        user.setPassword(encoder.encode(user.getPassword()));
        return users.save(user);
    }
   
}