package com.amateuraces.admin;

import java.util.List;
import java.util.Optional;

public interface AdminRepository {
    Long save(Admin admin);
    int update(Admin admin);
    int deleteById(Long id);
    List<Admin> findAll();

    // Using Optional - the return value of this method may contain a null value
    Optional<Admin> findById(Long id);
 
}
