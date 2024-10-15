package com.amateuraces.user;
import java.util.List;
import java.util.Optional;


public interface UserService {
    List<User> listUsers();

    User getUser(Long id);

    /**
     * Return the newly added player
     */
    User addUser(User user);

    /**
     * Return the updated player
     * 
     * @param id
     * @param player
     * @return
     */
    User updateUser(Long id, User user);

    /**
     * Change method's signature: do not return a value for delete operation
     * @param id
     */
    void deleteUser(Long id);

    /**
     * Find a user by username
     * 
     * @param username
     * @return Optional<User>
     */
    Optional<User> findByUsername(String username);
} 
