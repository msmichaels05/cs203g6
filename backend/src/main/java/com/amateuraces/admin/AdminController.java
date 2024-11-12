package com.amateuraces.admin;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.amateuraces.user.ExistingUserException;
import com.amateuraces.user.User;
import com.amateuraces.user.UserRepository;

import jakarta.validation.Valid;

@Controller
public class AdminController {

    private  AdminRepository adminRepository;  
    private UserRepository userRepository;

    public AdminController(AdminRepository adminRepository,UserRepository userRepository) {
        this.adminRepository = adminRepository;
        this.userRepository = userRepository;
    }

    @ResponseBody
    @GetMapping("/admin")
    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();  // This will return the list of admins in JSON format
    }

    @ResponseBody
    @PostMapping("/users/{userId}/admins")
    public Admin addAdmin(@PathVariable(value = "userId") Long userId, @Valid @RequestBody Admin admin) {
        User user = userRepository.findById(userId)
                    .orElseThrow(() -> new AdminNotFoundException(userId));
        
        admin.setUser(user);

        if(adminRepository.findOptionalByUserId(userId).isPresent()){
            throw new ExistingUserException("Admin already exists");
        }
            // Check for duplicates by phone number or name
        if (adminRepository.findByPhoneNumber(admin.getPhoneNumber()).isPresent() || 
            adminRepository.findByName(admin.getName()).isPresent()) {
                throw new ExistingUserException("Admin with this phone number or name already exists");
        }
        return adminRepository.save(admin);
        }
}