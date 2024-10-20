package com.amateuraces.admin;

import java.util.*;

import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {

    private  AdminRepository admins;

    public AdminServiceImpl(AdminRepository admins) {
        this.admins = admins;
    }

    // List all admins
    @Override
    public List<Admin> listAdmins() {
        return admins.findAll();
    }

    // Get a single admin by ID
    @Override
    public Admin getAdmin(Long id) {
        return admins.findById(id).orElse(null);
    }

    // Add a new admin
    @Override
    public Admin addAdmin(Admin admin) {
        // Save the admin and return the saved admin object
        Optional<Admin> samePhoneNumber = admins.findByPhoneNumber(admin.getPhoneNumber());
        if (samePhoneNumber.isPresent()) {
            return null;
        }
        return admins.save(admin); // Assuming save returns Admin
    }

    // Update an existing admin
    @Override
    public Admin updateAdmin(Long id, Admin newAdminInfo) {
            return admins.findById(id).map(admin -> {admin.setName(newAdminInfo.getName());
                return admins.save(admin);
            }).orElse(null);
    }
    @Override
    public void deleteAdmin(Long id) {
        admins.deleteById(id);
    }

}
