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
import org.springframework.web.bind.annotation.*;

import com.amateuraces.user.*;

import java.util.List;

@RestController
public class AdminController {

    private  AdminRepository admins;  
    private UserRepository users;

    public AdminController(AdminRepository admins,UserRepository users) {
        this.admins = admins;
        this.users = users;
    }

    // 1. Retrieve all admins
    @GetMapping("/admins")
    public List<Admin> viewAdmins() {
        return admins.findAll();
    }

    // @ResponseBody
    @PostMapping("/users/{userId}/admins")
    public Admin addAdmin(@PathVariable(value = "userId") Long userId, @Valid @RequestBody Admin admin) {
        return users.findById(userId).map(user -> {
            admin.setUser(user);
            return admins.save(admin);
        }).orElseThrow(() -> new AdminNotFoundException(userId));
    }

}
