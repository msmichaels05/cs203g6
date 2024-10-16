package com.amateuraces.admin;

import com.amateuraces.match.Match;
import com.amateuraces.player.Player;
import com.amateuraces.player.PlayerNotFoundException;
import com.amateuraces.tournament.Tournament;
import com.amateuraces.tournament.TournamentService;
import com.amateuraces.user.UserRepository;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.amateuraces.user.*;

import java.util.List;

@Controller
public class AdminController {

    private  AdminRepository admins;  
    private UserRepository users;

    public AdminController(AdminRepository admins,UserRepository users) {
        this.admins = admins;
        this.users = users;
    }
    
    @GetMapping("/admins")
    public String viewAdmins(Model model) {
        List<Admin> allAdmins = admins.findAll();
        model.addAttribute("admins", allAdmins);  // Pass admins to the Thymeleaf template
        return "all_admins";  // Return Thymeleaf template name
    }
    @GetMapping("/admin/profile/{id}")
    public String getAdminProfile(@PathVariable("id") Long id, Model model) {
        Admin admin = admins.findByUserId(id);
                // .orElseThrow(() -> new AdminNotFoundException(id)); // Ensure this exception is handled
        model.addAttribute("admin", admin); // Add admin to the model
        return "admin_profile"; // Return the correct view name
    }
    

    @ResponseBody
    @PostMapping("/users/{userId}/admins")
    public Admin addAdmin(@PathVariable(value = "userId") Long userId, @Valid @RequestBody Admin admin) {
        return users.findById(userId).map(user -> {
            admin.setUser(user);
            return admins.save(admin);
        }).orElseThrow(() -> new AdminNotFoundException(userId));
    }
}