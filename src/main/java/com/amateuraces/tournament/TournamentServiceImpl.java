package com.amateuraces.tournament;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amateuraces.match.Match;
import com.amateuraces.player.Player;
import com.amateuraces.player.PlayerNotFoundException;
import com.amateuraces.player.PlayerRepository;

@Service
public class TournamentServiceImpl implements TournamentService {

    private final TournamentRepository tournamentRepository;
    private final PlayerRepository playerRepository;

    public TournamentServiceImpl(TournamentRepository tournamentRepository, PlayerRepository playerRepository) {
        this.tournamentRepository = tournamentRepository;
        this.playerRepository = playerRepository;
    }

    @Override
    public List<Tournament> listTournaments() {
        // This uses the JPA method findAll() provided by JpaRepository
        return tournamentRepository.findAll();
    }

    @Override
    public Tournament addTournament(Tournament tournament) {
        return tournamentRepository.save(tournament);
    }

    /**
     * Remove a Tournament with the given id
     * Spring Data JPA does not return a value for delete operation
     * Cascading: removing a player will also remove all its associated reviews
     */
    @Override
    public void deleteTournament(Long id){
    // Check if the tournament exists before attempting to delete
    if (!tournamentRepository.existsById(id)) {
        throw new TournamentNotFoundException(id);
    }
    
    // If the tournament exists, delete them
    tournamentRepository.deleteById(id);
    }

    @Override
    public Tournament updateTournament(Long id, Tournament newTournamentInfo) {
        return tournamentRepository.findById(id).map(tournament -> {tournament.setName(newTournamentInfo.getName());
            tournament.setRequirement(newTournamentInfo.getRequirement());
            return tournamentRepository.save(tournament);
        }).orElse(null);
    }

    @Override
    public Tournament createTournament(Tournament tournament) {
        // Validate tournament before saving
        if (tournament.getName() == null || tournament.getName().isEmpty()) {
            throw new IllegalArgumentException("Tournament name cannot be empty");
        }
        return tournamentRepository.save(tournament);
    }

    @Override
    public Tournament addPlayerToTournament(Long tournamentId, Long playerId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addPlayerToTournament'");
    }

    @Override
    public List<Player> getPlayersInTournament(Long tournamentId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getPlayersInTournament'");
    }

    @Override
    public List<Match> performRandomDraw(Long tournamentId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'performRandomDraw'");
    }

    @Override
    public Tournament recordMatchResult(Long tournamentId, Match match, Player winner, String result) {
        // TODO Auto-generated method stub
        //throw new UnsupportedOperationException("Unimplemented method 'recordMatchResult'");
        Tournament tournament = tournamentRepository.findById(tournamentId)
            .orElseThrow(() -> new TournamentNotFoundException(tournamentId));
        Match updatedMatch = tournament.recordMatchResult(match, 0, winner, result);
        if (updatedMatch == null) return null;
        else return tournament;
    }

    @Override
    public void initialiseDraw(Long tournamentId) {
        Tournament tournament = tournamentRepository.findById(tournamentId)
            .orElseThrow(() -> new TournamentNotFoundException(tournamentId));
        tournament.initialiseDraw();
        tournamentRepository.save(tournament); // Save changes if necessary
    }

    @Override
    public String printDraw(Long tournamentId) {
        Tournament tournament = tournamentRepository.findById(tournamentId)
            .orElseThrow(() -> new TournamentNotFoundException(tournamentId));
        return tournament.printDraw(); // Modify printDraw() to return a String
    }

    // @Override
    // public Tournament addPlayerToTournament(Long tournamentId, Long playerId) {
    //     Tournament tournament = tournamentRepository.findById(tournamentId)
    //             .orElseThrow(() -> new IllegalArgumentException("Tournament not found"));
    //     Player player = playerRepository.findById(playerId)
    //             .orElseThrow(() -> new IllegalArgumentException("Player not found"));

    //     tournament.addPlayer(player); // Add the player
    //     return tournamentRepository.save(tournament); // Persist the changes
    // }

    // @Override
    // public Tournament setRegistrationPeriod(Long tournamentId, String startDate, String endDate) {
    //     Tournament tournament = tournamentRepository.findById(tournamentId)
    //             .orElseThrow(() -> new IllegalArgumentException("Tournament not found"));

    //     tournament.setRegistrationStartDate(startDate);
    //     tournament.setRegistrationEndDate(endDate);
        
    //     // Automatically update registration status in the Tournament class
    //     return tournamentRepository.save(tournament);
    // }

    // @Override
    // public List<Player> getPlayersInTournament(Long tournamentId) {
    //     Tournament tournament = tournamentRepository.findById(tournamentId)
    //             .orElseThrow(() -> new IllegalArgumentException("Tournament not found"));

    //     return tournament.getPlayers(); // Directly return the players
    // }

    // @Override
    // public List<Match> performRandomDraw(Long tournamentId) {
    //     Tournament tournament = tournamentRepository.findById(tournamentId)
    //             .orElseThrow(() -> new IllegalArgumentException("Tournament not found"));

    //     // Logic for performing random draw goes here
    //     return new ArrayList<>(); // Return matches after drawing
    // }

    // // @Override
    // // public Tournament updateTournamentStatus(Long tournamentId, String status) {
    // //     Tournament tournament = tournamentRepository.findById(tournamentId)
    // //             .orElseThrow(() -> new IllegalArgumentException("Tournament not found"));

    // //     tournament.setStatus(status);
    // //     return tournamentRepository.save(tournament);
    // // }

    // @Override
    // public Tournament recordMatchResult(Long tournamentId, Long matchId, String result) {
    //     Tournament tournament = tournamentRepository.findById(tournamentId)
    //             .orElseThrow(() -> new IllegalArgumentException("Tournament not found"));

    //     // Logic to find the match and update the result
    //     return tournamentRepository.save(tournament);
    // }

    // // @Override
    // // public boolean validateRegistrationPeriod(Long tournamentId) {
    // //     Tournament tournament = tournamentRepository.findById(tournamentId)
    // //             .orElseThrow(() -> new IllegalArgumentException("Tournament not found"));

    // //     return tournament.checkRegistrationPeriod();
    // // }

    // /**
    //  * Register a player for a tournament.
    //  *
    //  * @param tournamentId the ID of the tournament
    //  * @param playerId     the ID of the player to register
    //  * @return the updated Tournament object after registration
    //  */
    // // public Tournament registerPlayerForTournament(Long tournamentId, Long playerId) {
    // //     Tournament tournament = getTournamentById(tournamentId);
    // //     if (tournament != null && tournament.isRegistrationOpen()) {
    // //         // Assuming you have a Player class and a way to find a Player by ID
    // //         Player player = new Player(); // Retrieve player based on playerId
    // //         tournament.addPlayer(player);
    //         return tournamentRepository.save(tournament);
    //     }
    //     return null; // Or throw an exception
    // }
}
