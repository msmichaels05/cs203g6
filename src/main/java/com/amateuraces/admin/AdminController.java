package com.amateuraces.admin;

import com.amateuraces.match.Match;
import com.amateuraces.player.Player;
import com.amateuraces.tournament.Tournament;
import com.amateuraces.tournament.TournamentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;  // Admin service
    private final TournamentService tournamentService;  // Tournament service

    @Autowired
    public AdminController(AdminService adminService, TournamentService tournamentService) {
        this.adminService = adminService;
        this.tournamentService = tournamentService;
    }

    // 1. Retrieve all admins
    @GetMapping("/admins")
    public List<Admin> viewAdmins() {
        return adminService.listAdmins();
    }

    // 2. Add a new admin
    @PostMapping("/admins")
    @ResponseStatus(HttpStatus.CREATED)
    public Admin addAdmin(@RequestBody Admin admin) {
        return adminService.addAdmin(admin);
    }

    // 3. Admin can view the dashboard (placeholder)
    @GetMapping("/dashboard")
    public String viewDashboard() {
        return "admin_dashboard";
    }

    // 4. List available tournaments
    @GetMapping("/tournaments")
    public List<Tournament> viewTournaments() {
        return tournamentService.getAllTournaments();
    }

    // 5. Register player for a tournament
    @PostMapping("/tournaments/register")
    public Tournament registerForTournament(@RequestParam Long tournamentId, @RequestParam Long playerId) {
        return tournamentService.addPlayerToTournament(tournamentId, playerId);
    }

    // 6. Show match schedule (placeholder)
    @GetMapping("/matches")
    public String viewMatchSchedule() {
        return "admin_match_schedule";
    }

    // 7. Show player profile (placeholder)
    @GetMapping("/profile")
    public String viewProfile() {
        return "admin_profile";
    }

    // 8. Show notifications (placeholder)
    @GetMapping("/notifications")
    public String viewNotifications() {
        return "admin_notifications";
    }

    // =================== Admin-only Actions ===================

    // 9. Admin creates a new tournament
    @PostMapping("/tournaments")
    @ResponseStatus(HttpStatus.CREATED)
    public Tournament createTournament(@RequestBody Tournament tournament) {
        return tournamentService.createTournament(tournament);
    }

    // 10. Set tournament registration period
    @PutMapping("/tournaments/{tournamentId}/registration")
    public Tournament setRegistrationPeriod(@PathVariable Long tournamentId,
                                            @RequestParam String startDate,
                                            @RequestParam String endDate) {
        return tournamentService.setRegistrationPeriod(tournamentId, startDate, endDate);
    }

    // 11. View the list of players registered for a tournament
    @GetMapping("/tournaments/{tournamentId}/players")
    public List<Player> viewRegisteredPlayers(@PathVariable Long tournamentId) {
        return tournamentService.getPlayersInTournament(tournamentId);
    }

    // 12. Perform a randomized draw for a tournament (Admin only)
    @PostMapping("/tournaments/{tournamentId}/draw")
    public List<Match> performRandomizedDraw(@PathVariable Long tournamentId) {
        return tournamentService.performRandomDraw(tournamentId);
    }
}
