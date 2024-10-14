// package com.amateuraces.admin;

// import com.amateuraces.match.Match;
// import com.amateuraces.tournament.Tournament;
// import com.amateuraces.tournament.TournamentService;

// import org.springframework.stereotype.Service;
// import org.springframework.transaction.annotation.Transactional;

// import java.util.List;
// import java.util.Optional;

// @Service
// @Transactional // Ensure transactions are managed appropriately
// public class AdminServiceImpl implements AdminService {

//     private final AdminRepository adminRepository;
//     private final TournamentService tournamentService; // Inject TournamentService for tournament operations

//     public AdminServiceImpl(AdminRepository adminRepository, TournamentService tournamentService) {
//         this.adminRepository = adminRepository;
//         this.tournamentService = tournamentService;
//     }

//     // List all admins
//     @Override
//     public List<Admin> listAdmins() {
//         return adminRepository.findAll();
//     }

//     // Get a single admin by ID
//     @Override
//     public Admin getAdmin(Long id) {
//         Optional<Admin> admin = adminRepository.findById(id);
//         return admin.orElseThrow(() -> new AdminNotFoundException(id)); // Throw exception if not found
//     }

//     // Add a new admin
//     @Override
//     public Admin addAdmin(Admin admin) {
//         // Save the admin and return the saved admin object
//         return adminRepository.save(admin); // Assuming save returns Admin
//     }

//     // Update an existing admin
//     @Override
//     public Admin updateAdmin(Long id, Admin newAdminInfo) {
//         return adminRepository.findById(id)
//             .map(existingAdmin -> {
//                 existingAdmin.setName(newAdminInfo.getName()); // Update admin info (e.g., name)
//                 existingAdmin.setEmail(newAdminInfo.getEmail());
//                 return adminRepository.save(existingAdmin); // Save updated admin
//             })
//             .orElseThrow(() -> new AdminNotFoundException(id)); // Throw if not found
//     }
//     @Override
//     public void deleteAdmin(Long id) {
//         adminRepository.deleteById(id);
//     }
//     // List all available tournaments (calls TournamentService)
//     // @Override
//     // public List<Tournament> viewTournaments() {
//     //     return tournamentService.getAllTournaments();
//     // }

//     // Perform a randomized draw for a tournament
//     @Override
//     public List<Match> performRandomizedDraw(Long tournamentId) {
//         return tournamentService.performRandomDraw(tournamentId);
//     }
// }
