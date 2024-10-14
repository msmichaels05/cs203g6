package com.amateuraces.user;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
/*This implementation is meant for business logic,which could be added later*Currently,it does not have much in terms of the business logic yet*/

    private UserRepository users;

    public UserServiceImpl(UserRepository users){
        this.users = users;
    }

    @Override
    public List<User> listUsers() {
        return users.findAll();
    }

    @Override
    public User getUser(Long id){
        return users.findById(id).orElse(null);
    }   

    @Override
    public User addUser(User user) {
        return users.save(user);
    }

    @Override
    public User updateUser(Long id, User newUserInfo) {
        return users.findById(id).map(user -> {user.setPassword(newUserInfo.getPassword());
            return users.save(user);
        }).orElse(null);
    }

    /**
     * Remove a player with the given id
     * Spring Data JPA does not return a value for delete operation
     * Cascading: removing a player will also remove all its associated reviews
     */
    @Override
    public void deleteUser(Long id){
        users.deleteById(id);
    }
}
