package com.amateuraces.tournament;

import java.util.*;

import org.springframework.stereotype.Service;

import com.amateuraces.match.Match;
import com.amateuraces.match.MatchRepository;
import com.amateuraces.player.Player;
import com.amateuraces.player.PlayerNotFoundException;
import com.amateuraces.player.PlayerRepository;

@Service
public class TournamentServiceImpl implements TournamentService {

    private TournamentRepository tournamentRepository;
    private MatchRepository matchRepository;
    private PlayerRepository playerRepository;
    // private  PlayerRepository playerRepository;

    public TournamentServiceImpl(TournamentRepository tournamentRepository,MatchRepository  MatchRepository, PlayerRepository playerRepository) {
        this.tournamentRepository = tournamentRepository;
        this.playerRepository = playerRepository;
        this.matchRepository = matchRepository;
    }

    @Override
    public List<Tournament> listTournaments() {
        // This uses the JPA method findAll() provided by JpaRepository
        return tournamentRepository.findAll();
    }

    @Override
    public Tournament getTournament(Long id){
        return tournamentRepository.findById(id).orElse(null);
    }

    @Override
    public Tournament addTournament(Tournament tournament) {
        if (tournament.getName()==null || tournament.getName().isEmpty()){
            throw new IllegalArgumentException("Tournament name cannot be empty");
        }
        Optional<Tournament> existingTournament = tournamentRepository.findByName(tournament.getName());
        if (existingTournament.isPresent()) {
            return null; // Prevent creating a tournament with the same name
        }
        return tournamentRepository.save(tournament);
    }

    @Override
    public Tournament addPlayerToTournament(Long tournamentId, Long playerId) {
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new IllegalArgumentException("Tournament not found"));
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new IllegalArgumentException("Player not found"));

        tournament.addPlayer(player); // Add the player
        return tournamentRepository.save(tournament); // Persist the changes
    }

    @Override
    public List<Player> getPlayersInTournament(Long tournamentId) {
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new IllegalArgumentException("Tournament not found"));

        return tournament.getPlayers(); // Directly return the players
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
        return tournamentRepository.findById(id).map(tournament -> {
            tournament.setName(newTournamentInfo.getName());
            tournament.setELOrequirement(newTournamentInfo.getELOrequirement());
            return tournamentRepository.save(tournament);
        }).orElse(null);
    }

    @Override
    public List<Match> performRandomDraw(Long tournamentId) {
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new IllegalArgumentException("Tournament not found"));

        // Ensure there are enough players for the tournament
        List<Player> players = tournament.getPlayers();
        if (players.size() < 2) {
            throw new IllegalArgumentException("Not enough players to perform a draw");
        }

        // Shuffle the players to ensure a random draw
        Collections.shuffle(players);

        // Create matches for every two players
        List<Match> matches = new ArrayList<>();
        for (int i = 0; i < players.size(); i += 2) {
            if (i + 1 < players.size()) {
                Player player1 = players.get(i);
                Player player2 = players.get(i + 1);
                Match match = new Match(tournament, player1, player2);
                matches.add(match);
            }
        }

        // Save all the matches
        matchRepository.saveAll(matches);

        // Return the matches
        return matches;
    }



    @Override
    public Tournament recordMatchResult(Long tournamentId, Long matchId, String result) {
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new IllegalArgumentException("Tournament not found"));

        // Find the match by its ID
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new IllegalArgumentException("Match not found"));

        // Determine the winner based on the result
        if (result.equalsIgnoreCase(match.getPlayer1().getName())) {
            match.setWinner(match.getPlayer1());
        } else if (result.equalsIgnoreCase(match.getPlayer2().getName())) {
            match.setWinner(match.getPlayer2());
        } else {
            throw new IllegalArgumentException("Invalid result: winner not found");
        }

        // Update the match and save it
        matchRepository.save(match);

        // Return the updated tournament
        return tournamentRepository.save(tournament);
    }
    // @Override
    // public Tournament addPlayerToTournament(Long tournamentId, Long playerId) {
    //     // TODO Auto-generated method stub
    //     throw new UnsupportedOperationException("Unimplemented method 'addPlayerToTournament'");
    // }

    // @Override
    // public List<Player> getPlayersInTournament(Long tournamentId) {
    //     // TODO Auto-generated method stub
    //     throw new UnsupportedOperationException("Unimplemented method 'getPlayersInTournament'");
    // }

    // @Override
    // public List<Match> performRandomDraw(Long tournamentId) {
    //     // TODO Auto-generated method stub
    //     throw new UnsupportedOperationException("Unimplemented method 'performRandomDraw'");
    // }

    // @Override
    // public Tournament recordMatchResult(Long tournamentId, Long matchId, String result) {
    //     // TODO Auto-generated method stub
    //     throw new UnsupportedOperationException("Unimplemented method 'recordMatchResult'");
    // }

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
    // public Tournament setRegistrationPeriod(Long tournamentId, String startDate,
    // String endDate) {
    // Tournament tournament = tournamentRepository.findById(tournamentId)
    // .orElseThrow(() -> new IllegalArgumentException("Tournament not found"));

    // tournament.setRegistrationStartDate(startDate);
    // tournament.setRegistrationEndDate(endDate);

    // // Automatically update registration status in the Tournament class
    // return tournamentRepository.save(tournament);
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
    // return null; // Or throw an exception
    // }

    @Override
    public void deleteTournament(Long tournamentId) {
        // Check if the match exists before attempting to delete
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new IllegalArgumentException("Tournament not found"));

        // If the match exists, delete them
        tournamentRepository.deleteById(tournamentId);
    }
}
