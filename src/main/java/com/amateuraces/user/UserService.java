package com.amateuraces.user;
import java.util.List;

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
} 
