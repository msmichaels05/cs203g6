package com.amateuraces.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;  

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;
    
    @Test
    void addUser_NewName_ReturnSavedUser() {
        // Arrange ***
        User user = new User("username", "password");
        
        // Mock the findByUsername operation to return empty (no user found)
        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.empty());
        
        // Mock the save operation
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act ***
        User savedUser = userService.addUser(user);
        
        // Assert ***
        assertNotNull(savedUser);
        
        // Verify interactions
        verify(userRepository).findByUsername(user.getUsername());
        verify(userRepository).save(user);
    }

    /**
     * TODO: Activity 1 (Week 6)
     * Write a test case: when adding a new book but the title already exists
     * The test case should pass if BookServiceImpl.addBook(book)
     * returns null (can't add book), otherwise it will fail.
     * Remember to include suitable "verify" operations
     * 
     */
    @Test
    void addUser_SameName_ReturnNull() {
        // Arrange ***
        User user = new User("username", "password");
        
        // Mock the findByUsername operation to return an existing user
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        
        // Act ***
        User savedUser = userService.addUser(user);
        
        // Assert ***
        assertNull(savedUser);
        
        // Verify interactions
        verify(userRepository).findByUsername(user.getUsername());
        verify(userRepository, never()).save(user);  // Ensure save is NOT called
    }


    @Test
    void updatePlayer_NotFound_ReturnNull() {
        User user = new User("username", "password");
        Long userId = 10L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        
        User updatedUser = userService.updateUser(userId, user);
        
        assertNull(updatedUser);
        verify(userRepository).findById(userId);
    }

}