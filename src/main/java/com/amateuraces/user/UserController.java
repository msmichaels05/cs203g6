package com.amateuraces.user;

import java.util.List;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

@RestController
public class UserController {
    private BCryptPasswordEncoder encoder;
    private UserService userService;

    public UserController(BCryptPasswordEncoder encoder, UserService userService) {
        this.encoder = encoder;
        this.userService = userService;
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        return userService.listUsers();
    }

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

    @PutMapping("/users/{id}")
    public User updateUser(@PathVariable Long id, @Valid @RequestBody User newUserInfo){
        User user = userService.updateUser(id, newUserInfo);
        if(user == null) throw new UserNotFoundException(id);
        
        return user;
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable Long id){
        if (userService.getUser(id) == null) throw new UserNotFoundException(id);
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