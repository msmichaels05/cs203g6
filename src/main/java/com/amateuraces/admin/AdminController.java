package com.amateuraces.admin;
import com.amateuraces.match.Match;
import com.amateuraces.player.Player; // Import the Player class
import com.amateuraces.tournament.Tournament;
import com.amateuraces.tournament.TournamentService;
import org.springframework.http.HttpStatus;
//import org.springframework.security.access.prepost.PreAuthorize; // Uncomment if using security
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admins")
public class AdminController {

    private final AdminService adminService;  // Admin service
    private final TournamentService tournamentService;  // Tournament service

    // Constructor Injection
    public AdminController(AdminService adminService, TournamentService tournamentService) {
        this.adminService = adminService;
        this.tournamentService = tournamentService;
    }

    // Get all admins
    //change this up 
    @GetMapping("/")
    public List<Admin> viewAdmin() {
        return adminService.listAdmins();
    }

    // Add a new player with POST request to "/admins/players"
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/")
    public Admin addPlayer(@RequestBody Admin admin) {
        return adminService.addAdmin(admin);
    }

    // Admin can view the dashboard
    @GetMapping("/dashboard")
    public String viewDashboard() {
        // Logic for admin dashboard
        return "dashboard";
    }

    // List available tournaments
    @GetMapping("/tournaments")
    public List<Tournament> viewTournaments() {
        // Logic to fetch the list of tournaments
        return tournamentService.getAllTournaments(); 
    }

    // Register player for a tournament
    @PostMapping("/tournaments/register")
    public Tournament registerForTournament(@RequestParam Long tournamentId, @RequestParam Long playerId) {
        // Logic for player registration in tournament
        return tournamentService.addPlayerToTournament(tournamentId, playerId);
    }

    // Show match schedule
    @GetMapping("/matches")
    public String viewMatchSchedule() {
        return "matchSchedule";
    }

    // Show player profile
    @GetMapping("/profile")
    public String viewProfile() {
        // Logic for profile
        return "profile";
    }

    // Show notifications
    @GetMapping("/notifications")
    public String viewNotifications() {
        return "notifications";
    }

    // =================== Admin-only Actions ===================

    // Admin creates a new tournament
    // @PreAuthorize("hasRole('ADMIN')") // Uncomment if using security
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/tournaments")
    public Tournament createTournament(@RequestBody Tournament tournament) {
        return tournamentService.createTournament(tournament);
    }

    // Set tournament registration period (Admin only)
    // @PreAuthorize("hasRole('ADMIN')") // Uncomment if using security
    @PutMapping("/tournaments/{tournamentId}/registration")
    public Tournament setRegistrationPeriod(@PathVariable Long tournamentId, @RequestParam String startDate, @RequestParam String endDate) {
        return tournamentService.setRegistrationPeriod(tournamentId, startDate, endDate);
    }

    // View the list of players registered for a tournament
    // @PreAuthorize("hasRole('ADMIN')") // Uncomment if using security
    @GetMapping("/tournaments/{tournamentId}/players")
    public List<Player> viewRegisteredPlayers(@PathVariable Long tournamentId) {
        return tournamentService.getPlayersInTournament(tournamentId);
    }

    // Perform a randomized draw for a tournament (Admin only)
    // @PreAuthorize("hasRole('ADMIN')") // Uncomment if using security
    @PostMapping("/tournaments/{tournamentId}/draw")
    public List<Match> performRandomizedDraw(@PathVariable Long tournamentId) {
        return tournamentService.performRandomDraw(tournamentId);
    }
}
