package com.amateuraces.admin;

import com.amateuraces.match.Match;
import com.amateuraces.player.Player;
import com.amateuraces.player.PlayerNotFoundException;
import com.amateuraces.tournament.Tournament;
import com.amateuraces.tournament.TournamentService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.amateuraces.user.*;

import java.util.List;

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
    
    @GetMapping("/admins")
    public String viewAdmins(Model model) {
        List<Admin> allAdmins = adminRepository.findAll();
        model.addAttribute("admins", allAdmins);  // Pass admins to the Thymeleaf template
        return "all_admins";  // Return Thymeleaf template name
    }
    @GetMapping("/admin/profile/{id}")
    public String getAdminProfile(@PathVariable("id") Long id, Model model) {
        Admin admin = adminRepository.findByUserId(id);
        if (admin == null) {
            throw new AdminNotFoundException(id); 
        }
        model.addAttribute("admin", admin); 
        return "admin_profile"; 
    }

        // Show the edit form
    @GetMapping("/admin/edit/{id}")
    public String editAdmin(@PathVariable Long id, Model model) {
        Admin admin = adminRepository.findByUserId(id);
        model.addAttribute("admin", admin);
        return "edit_admin";
    }
@PostMapping("/admin/edit/{id}")
public String updateAdmin(@PathVariable Long id, @Valid @ModelAttribute Admin admin, BindingResult result, Model model) {
    System.out.println("Updating admin with ID: " + id);
    
    if (result.hasErrors()) {
        System.out.println("Validation errors occurred: " + result.getAllErrors());
        model.addAttribute("errors", result.getAllErrors());
        return "edit_admin"; // Return to the edit page if there are validation errors
    }

    Admin existingAdmin = adminRepository.findByUserId(id);
    if (existingAdmin == null) {
        throw new AdminNotFoundException(id); 
    }

    existingAdmin.setName(admin.getName());
    existingAdmin.setPhoneNumber(admin.getPhoneNumber());      
    adminRepository.save(existingAdmin); // Save updated admin details
    
    return "redirect:/admins"; // redirect back to admin list after update
}

  // Handle delete action
  @PostMapping("/admin/delete/{id}")
  public String deleteAdmin(@PathVariable("id") Long id) {
      System.out.println("Deleting admin with id: " + id);  // logging
      Admin admin = adminRepository.findByUserId(id);
      if (admin == null) {
        throw new AdminNotFoundException(id); 
    }
              
      adminRepository.delete(admin); // Delete the admin
      return "redirect:/admins"; // Redirect back to the list of admins
  }
      // Show the create admin form
      @GetMapping("/admin/create")
      public String showCreateAdminForm(Model model) {
          model.addAttribute("admin", new Admin()); // Add a new Admin object to the model
          return "create_admin"; // Return the Thymeleaf template name
      }

//       @PostMapping("/admin/create")
// public String createAdmin(@Valid @ModelAttribute Admin admin, BindingResult result) {
//     if (result.hasErrors()) {
//         return "create_admin"; // Return to the form if there are validation errors
//     }

//     // Ensure the user field is properly initialized
//     if (admin.getUser() == null) {
//         admin.setUser(new User()); // Initialize with a new User object if it's null
//     }

//     // Save the User first to generate an ID
//     users.save(admin.getUser()); // Assuming you have a user service to handle User operations

//     // Now set the user ID to the admin (if using @MapsId, it should be the same as User's ID)
//     admin.setUser(admin.getUser()); // This should set the same ID for the Admin

//     // Save the admin
//     admins.save(admin); // Assuming you have an admin service to handle Admin operations

//     return "redirect:/admins"; // Redirect to the admin list after creation
// }



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

