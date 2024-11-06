package com.amateuraces.tournament;

import java.time.LocalDate;
import java.time.Period;
import java.util.*;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import jakarta.transaction.*;

import com.amateuraces.player.*;
import com.amateuraces.user.*;
import com.amateuraces.match.*;

@Service
public class TournamentServiceImpl implements TournamentService {

    private  TournamentRepository tournamentRepository;
    private PlayerRepository playerRepository;
    private MatchRepository matchRepository;
    // private CustomUserDetailsService userDetailsService;

    public TournamentServiceImpl(TournamentRepository tournamentRepository, PlayerRepository playerRepository,
    MatchRepository matchRepository) {
        this.tournamentRepository = tournamentRepository;
        this.playerRepository = playerRepository;
        this.matchRepository = matchRepository;
        // this.userDetailsService = userDetailsService;
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
            throw new TournamentNotFoundException(tournament.getId());
        }

        // Check if a tournament with the same name already exists
        if (tournamentRepository.findByName(tournament.getName()).isPresent()) {
            throw new ExistingTournamentException("Tournament with this name already exists");
        }

        LocalDate currentDate = LocalDate.now();

        // Check if the tournament's start date is at least one month in the future
        if (!tournament.getStartDate().isAfter(currentDate.plusMonths(1))) {
            throw new IllegalArgumentException("The tournament must be scheduled at least one month in advance from today.");
        }
    
        // Validate that the start date is before the end date
        if (!tournament.getStartDate().isBefore(tournament.getEndDate())) {
            throw new IllegalArgumentException("Start date must be before end date.");
        }

        // Set Registration period : 1 week before start date
        LocalDate registrationEndDate = tournament.getStartDate().minusWeeks(1);
        tournament.setRegistrationEndDate(registrationEndDate);
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

    @Transactional
    @Override
    public void joinTournament(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            User userDetails = (User) authentication.getPrincipal();
            Long userId = userDetails.getId(); // This is also the player ID

            // Now you can find the player by userId
            Player player = playerRepository.findById(userId)
                    .orElseThrow(() -> new PlayerNotFoundException("Player not registered"));
    
            // Continue with your logic to join the tournament...
            Tournament tournament = tournamentRepository.findById(id)
                    .orElseThrow(() -> new TournamentNotFoundException(id));
            
            //Check gender of the tournament
            if (!(tournament.getGender().equals(player.getGender()))){
                throw new IllegalArgumentException("Only "+ tournament.getGender()+" allowed");
            }

            //Check Elo requirement to join tournament
            if(tournament.getELOrequirement() > player.getElo()){
                throw new IllegalArgumentException("Min elo requirement: "+ tournament.getELOrequirement());
            }

            //Check if the player is already in the tournament
            if(tournament.getPlayers().contains(player)){
                throw new IllegalArgumentException("Player has already joined this tournament.");
            }
            
            //Check if tournament is full
            if (tournament.getPlayerCount() >= tournament.getMaxPlayers()) {
                throw new IllegalArgumentException("Cannot join tournament: it is full.");
            }

            tournament.getPlayers().add(player);
            //Increment player count
            tournament.setPlayerCount(tournament.getPlayerCount()+1);
            tournamentRepository.save(tournament);
        } else {
            throw new UserAuthenticationException();
        }
    }
    
    
    
    

    @Override
    public Set<Player> getPlayersInTournament(Long tournamentId) {
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new TournamentNotFoundException(tournamentId));
        return tournament.getPlayers();
    }
    


    @Override
    public Tournament updateTournament(Long id, Tournament newTournamentInfo) {
        return tournamentRepository.findById(id).map(tournament -> {
            tournament.setName(newTournamentInfo.getName());
            tournament.setELOrequirement(newTournamentInfo.getELOrequirement());
            tournament.setLocation(newTournamentInfo.getLocation());
            tournament.setMaxPlayers(newTournamentInfo.getMaxPlayers());
            tournament.setDescription(newTournamentInfo.getDescription());
            tournament.setStartDate(newTournamentInfo.getStartDate());
            tournament.setEndDate(newTournamentInfo.getEndDate());
            tournament.setGender(newTournamentInfo.getGender());
            return tournamentRepository.save(tournament);
        }).orElse(null);
    }


    @Override
    public void removePlayerFromTournament(Long tournamentId, Long playerId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            User userDetails = (User) authentication.getPrincipal();
            Long userId = userDetails.getId(); // This is also the player ID

            // Now you can find the player by userId
            Player player = playerRepository.findById(userId)
                    .orElseThrow(() -> new PlayerNotFoundException("Player not registered"));

            Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new TournamentNotFoundException(tournamentId));
    
            tournament.getPlayers().remove(player);
            tournament.setPlayerCount(tournament.getPlayerCount()-1);
            tournamentRepository.save(tournament);
        } else {
            throw new UserAuthenticationException();
        }
    }
    /**
     * Creates matches for the given tournament by pairing up players.
     * 
     * @param tournamentId the ID of the tournament to create matches for.
     * @return a list of created matches.
     */
    @Transactional
    public List<Match> createMatchesForTournament(Long tournamentId) {
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new TournamentNotFoundException(tournamentId));

        Set<Player> players = tournament.getPlayers(); // Get the players in the tournament

        // Generate matches by pairing players
        List<Match> matches = new ArrayList<>();
        int matchId = 1;
        Player[] playerArray = players.toArray(new Player[0]); // Convert the set to an array for pairing

        for (int i = 0; i < playerArray.length; i += 2) {
            if (i + 1 < playerArray.length) {
                Player player1 = playerArray[i];
                Player player2 = playerArray[i + 1];

                // Create a match and set tournament and players
                Match match = new Match();
                match.setTournament(tournament);
                match.setPlayer1(player1);
                match.setPlayer2(player2);
                match.setStatus("Scheduled"); // Default status is "Scheduled"

                // Save the match to the database
                matchRepository.save(match);
                matches.add(match);
            } else {
                // If there’s an odd number of players, one player gets a bye (no opponent)
                Player player1 = playerArray[i];
                Match match = new Match();
                match.setTournament(tournament);
                match.setPlayer1(player1);
                match.setPlayer2(null); // No opponent for the player
                match.setStatus("Scheduled");

                // Save the match to the database
                matchRepository.save(match);
                matches.add(match);
            }
            matchId++;
        }

        return matches; // Return the list of matches that were created
    }
}
