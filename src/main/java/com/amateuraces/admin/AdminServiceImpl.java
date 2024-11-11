package com.amateuraces.admin;

import java.util.*;

import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {

    private  AdminRepository adminRepository;

    public AdminServiceImpl(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    // List all admins
    @Override
    public List<Admin> listAdmins() {
        return adminRepository.findAll();
    }

    // Get a single admin by ID
    @Override
    public Admin getAdmin(Long id) {
        return adminRepository.findById(id).orElse(null);
    }

    // Add a new admin
    @Override
    public Admin addAdmin(Admin admin) {
        // Save the admin and return the saved admin object
        Optional<Admin> samePhoneNumber = adminRepository.findByPhoneNumber(admin.getPhoneNumber());
        if (samePhoneNumber.isPresent()) {
            return null;
        }
        return adminRepository.save(admin); // Assuming save returns Admin
    }

    // Update an existing admin
    @Override
    public Admin updateAdmin(Long id, Admin newAdminInfo) {
            return adminRepository.findById(id).map(admin -> {admin.setName(newAdminInfo.getName());
                return adminRepository.save(admin);
            }).orElse(null);
    }
    @Override
    public void deleteAdmin(Long id) {
        adminRepository.deleteById(id);
    }

}
