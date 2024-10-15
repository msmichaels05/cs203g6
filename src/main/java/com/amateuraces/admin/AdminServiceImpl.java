package com.amateuraces.admin;

import com.amateuraces.match.Match;
import com.amateuraces.tournament.Tournament;
import com.amateuraces.tournament.TournamentService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
        Optional<Admin> admin = adminRepository.findById(id);
        return admin.orElseThrow(() -> new AdminNotFoundException(id)); // Throw exception if not found
    }

    // Add a new admin
    @Override
    public Admin addAdmin(Admin admin) {
        // Save the admin and return the saved admin object
        return adminRepository.save(admin); // Assuming save returns Admin
    }

    // Update an existing admin
    @Override
    public Admin updateAdmin(Long id, Admin newAdminInfo) {
        return adminRepository.findById(id)
            .map(existingAdmin -> {
                existingAdmin.setName(newAdminInfo.getName()); // Update admin info (e.g., name)
                existingAdmin.setEmail(newAdminInfo.getEmail());
                return adminRepository.save(existingAdmin); // Save updated admin
            })
            .orElseThrow(() -> new AdminNotFoundException(id)); // Throw if not found
    }
    @Override
    public void deleteAdmin(Long id) {
        adminRepository.deleteById(id);
    }

}
