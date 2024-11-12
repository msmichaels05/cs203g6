package com.amateuraces.admin;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    Admin findByUserId(Long userId);
    Optional<Admin> findOptionalByUserId(Long userId);
    Optional<Admin> findByName(String name);
    Optional<Admin> findByPhoneNumber(String phoneNumber);
}