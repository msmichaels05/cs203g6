package com.amateuraces;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.amateuraces.user.User;
import com.amateuraces.user.UserRepository;
import com.amateuraces.user.UserServiceImpl;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    
    @Mock
    private UserRepository users;

    @InjectMocks
    private UserServiceImpl userService;
    
    @Test
    void addUser_NewUsername_ReturnSavedUser(){
        
        User user = new User("New User", "12345");
        when(users.save(any(User.class))).thenReturn(user);
        User savedUser = userService.addUser(user);
        
        assertNotNull(savedUser);
        assertEquals(user.getUsername(),savedUser.getUsername());
        verify(users).save(user);
    }

    @Test
    void addUser_SameUsername_ReturnNull(){
        User user = new User("The Same Name Exists", "12345");
        Optional<User> sameUsernames = Optional.of(new User("The Same Name Exists", "12345"));
        
        when(users.findByUsername(user.getUsername())).thenReturn(sameUsernames);
        User savedUser = userService.addUser(user);
        assertNull(savedUser);
        verify(users).findByUsername(user.getUsername());
        verify(users, never()).save(any(User.class));
    }

    @Test
    void updateUser_NotFound_ReturnNull(){
        User user = new User("Updated Name", "12345678");
        Long userID = 10L;
        when(users.findById(userID)).thenReturn(Optional.empty());
        
        User updatedUser = userService.updateUser(userID, user);
        
        assertNull(updatedUser);
        verify(users).findById(userID);
    }

    @Test
    void addUser_NewEmail_ReturnSavedUser(){
        User user = new User("test123@gmail.com");
        when(users.save(any(User.class))).thenReturn(user);
        User savedUser = userService.addUser(user);
        
        assertNotNull(savedUser);
        assertEquals(user.getEmail(),savedUser.getEmail());
        verify(users).save(user);
    }

    @Test
    void addUser_SameEmail_ReturnNull(){
        User user = new User();
        user.setEmail("test123@gmail.com");
        User sameUserEmail = new User();
        sameUserEmail.setEmail("test123@gmail.com");

        Optional<User> sameEmail = Optional.of(sameUserEmail);

        when(users.findByEmail(user.getEmail())).thenReturn(sameEmail);
        User savedUser = userService.addUser(user);
        assertNull(savedUser);

        verify(users).findByEmail(user.getEmail());
        verify(users, never()).save(any(User.class));
    }
}