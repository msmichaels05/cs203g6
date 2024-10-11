package com.amateuraces.player;

import java.util.List;
<<<<<<< HEAD
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.amateuraces.tournament.*;

import jakarta.persistence.EntityNotFoundException;

=======

import org.springframework.stereotype.Service;

>>>>>>> 4de22c8975a822eef95aef27a91cb5f4b2b102a0
/*This implementation is meant for business logic,which could be added later*Currently,it does not have much in terms of the business logic yet*/

@Service
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository players;
<<<<<<< HEAD
    private final TournamentRepository tournamentRepository;

    public PlayerServiceImpl(PlayerRepository players, TournamentRepository tournamentRepository){
        this.players = players;
        this.tournamentRepository = tournamentRepository;
=======

    public PlayerServiceImpl(PlayerRepository players){
        this.players = players;
>>>>>>> 4de22c8975a822eef95aef27a91cb5f4b2b102a0
    }

    @Override
    public List<Player> listPlayers() {
        return players.findAll();
    }

    @Override
    public Player getPlayer(Long id){
        return players.findById(id).orElse(null);
    }

    @Override
    public Player addPlayer(Player player) {
        return players.save(player);
    }

    @Override
    public Player updatePlayer(Long id, Player newPlayerInfo) {
        return players.findById(id).map(player -> {player.setName(newPlayerInfo.getName());
            return players.save(player);
        }).orElse(null);
    }

    /**
     * Remove a player with the given id
     * Spring Data JPA does not return a value for delete operation
     * Cascading: removing a player will also remove all its associated reviews
     */
    @Override
    public void deletePlayer(Long id){
    // Check if the player exists before attempting to delete
    if (!players.existsById(id)) {
        throw new PlayerNotFoundException(id);
    }
    
    // If the player exists, delete them
    players.deleteById(id);
    }
<<<<<<< HEAD

    @Override
    public Player registerForTournament(Long playerId, Tournament tournament) {
        // Fetch player by ID
        Optional<Player> optionalPlayer = players.findById(playerId);

        if (optionalPlayer.isPresent()) {
            Player player = optionalPlayer.get();

            // Check if the tournament already exists in the DB or save it
            Optional<Tournament> existingTournament = tournamentRepository.findById(tournament.getId());

            if (existingTournament.isPresent()) {
                tournament = existingTournament.get(); // Use the existing tournament from DB
            } else {
                tournament = tournamentRepository.save(tournament); // Save new tournament to DB
            }

            // Register the player for the tournament
            player.addToTournamentHistory(tournament);

            // Save the updated player back to the repository
            return players.save(player);
        } else {
            throw new EntityNotFoundException("Player with ID " + playerId + " not found.");
        }
    }
}




=======
}
>>>>>>> 4de22c8975a822eef95aef27a91cb5f4b2b102a0
