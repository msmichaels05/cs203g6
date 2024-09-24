package com.amateuraces.project.repository;

import com.amateuraces.project.entity.User;

public interface UserRepository {
    User findByEmail(String email);
    void save(User user);
    User findById(String id);
    // Other CRUD methods
}