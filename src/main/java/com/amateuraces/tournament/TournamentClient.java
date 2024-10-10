package com.amateuraces.tournament;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TournamentClient {

    private final TournamentRepository tournamentRepository;

    @Autowired
    public TournamentClient(TournamentRepository tournamentRepository) {
        this.tournamentRepository = tournamentRepository;
    }

    /**
     * Create a new tournament.
     *
     * @param tournament the Tournament object to create
     * @return the created Tournament object
     */
    public Tournament createTournament(Tournament tournament) {
        return tournamentRepository.save(tournament);
    }

    /**
     * Get a tournament by ID.
     *
     * @param tournamentId the ID of the tournament to retrieve
     * @return the Tournament object with the given ID
     */
    public Tournament getTournamentById(Long tournamentId) {
        return tournamentRepository.findById(tournamentId).orElse(null);
    }

    /**
     * Get all tournaments.
     *
     * @return a list of all Tournament objects
     */
    public List<Tournament> getAllTournaments() {
        return tournamentRepository.findAll();
    }

    /**
     * Update an existing tournament.
     *
     * @param tournamentId the ID of the tournament to update
     * @param tournament   the Tournament object with updated details
     * @return the updated Tournament object
     */
    public Tournament updateTournament(Long tournamentId, Tournament tournament) {
        if (!tournamentRepository.existsById(tournamentId)) {
            return null; // Or throw an exception
        }
        tournament.setId(tournamentId);
        return tournamentRepository.save(tournament);
    }

    /**
     * Delete a tournament by ID.
     *
     * @param tournamentId the ID of the tournament to delete
     */
    public void deleteTournament(Long tournamentId) {
        tournamentRepository.deleteById(tournamentId);
    }

    /**
     * Register a player for a tournament.
     *
     * @param tournamentId the ID of the tournament
     * @param playerId     the ID of the player to register
     * @return the updated Tournament object after registration
     */
    // public Tournament registerPlayerForTournament(Long tournamentId, Long playerId) {
    //     Tournament tournament = getTournamentById(tournamentId);
    //     if (tournament != null && tournament.isRegistrationOpen()) {
    //         // Assuming you have a Player class and a way to find a Player by ID
    //         Player player = new Player(); // Retrieve player based on playerId
    //         tournament.addPlayer(player);
    //         return tournamentRepository.save(tournament);
    //     }
    //     return null; // Or throw an exception
    // }
}
