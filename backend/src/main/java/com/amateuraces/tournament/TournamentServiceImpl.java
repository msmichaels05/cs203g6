package com.amateuraces.tournament;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.amateuraces.match.Match;
import com.amateuraces.match.MatchRepository;
import com.amateuraces.player.Player;
import com.amateuraces.player.PlayerNotFoundException;
import com.amateuraces.player.PlayerRepository;
import com.amateuraces.user.User;
import com.amateuraces.user.UserAuthenticationException;

import jakarta.transaction.Transactional;

@Service
public class TournamentServiceImpl implements TournamentService {

    private TournamentRepository tournamentRepository;
    private PlayerRepository playerRepository;
    private MatchRepository matchRepository;

    public TournamentServiceImpl(TournamentRepository tournamentRepository, PlayerRepository playerRepository,
            MatchRepository matchRepository) {
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
    public Tournament getTournament(Long id) {
        return tournamentRepository.findById(id).orElse(null);
    }

    @Override
    public Tournament addTournament(Tournament tournament) {
        if (tournament.getName() == null || tournament.getName().isEmpty()) {
            throw new TournamentNotFoundException(tournament.getId());
        }

        // Check if a tournament with the same name already exists
        if (tournamentRepository.findByName(tournament.getName()).isPresent()) {
            throw new ExistingTournamentException("Tournament with this name already exists");
        }

        LocalDate currentDate = LocalDate.now();

        // Check if the tournament's start date is at least one month in the future
        if (!tournament.getStartDate().isAfter(currentDate.plusMonths(1))) {
            throw new IllegalArgumentException(
                    "The tournament must be scheduled at least one month in advance from today.");
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
    public void deleteTournament(Long id) {
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

            // Check gender of the tournament
            if (!(tournament.getGender().equals(player.getGender()))) {
                throw new IllegalArgumentException("Only " + tournament.getGender() + " allowed");
            }

            // Check Elo requirement to join tournament
            if (tournament.getELORequirement() > player.getElo()) {
                throw new IllegalArgumentException("Min elo requirement: " + tournament.getELORequirement());
            }

            // Check if the player is already in the tournament
            if (tournament.getPlayers().contains(player)) {
                throw new IllegalArgumentException("Player has already joined this tournament.");
            }

            // Check if tournament is full
            if (tournament.getPlayerCount() >= tournament.getMaxPlayers()) {
                throw new IllegalArgumentException("Cannot join tournament: it is full.");
            }

            tournament.getPlayers().add(player);
            // Increment player count
            tournament.setPlayerCount(tournament.getPlayerCount() + 1);
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
            tournament.setELORequirement(newTournamentInfo.getELORequirement());
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
            tournament.setPlayerCount(tournament.getPlayerCount() - 1);
            tournamentRepository.save(tournament);
        } else {
            throw new UserAuthenticationException();
        }
    }

    /**
     * If there is no tournament with the given "id", throw a
     * TournamentNotFoundException
     * 
     * Creates draw while generating all matches for the first round
     * Subsequently creates all matches for subsequent rounds
     * 
     * @param id
     * 
     * Every match has 2 slots, each slot contains either a Player or null
     * 
     * @return List of all matches from round 1 to finals
     */
    @Transactional
    public List<Match> generateMatches(Long tournamentId) {
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new TournamentNotFoundException(tournamentId));

        int totalPlayers = tournament.getPlayerCount();

        int totalSlots = (int) Math.pow(2, Math.ceil(Math.log(totalPlayers) / Math.log(2)));
        int totalSeededPlayers = calculateNumberOfSeeds(totalSlots);
        Map<Integer, Player> playerSlotAssignment = new HashMap<>();
        assignPlayersToMatches(tournament.getPlayers(), totalSeededPlayers, playerSlotAssignment, totalSlots);

        // Determine the next power of two greater than or equal to totalPlayers
        int nextPowerOfTwo = 1;
        while (nextPowerOfTwo < totalPlayers) {
            nextPowerOfTwo *= 2;
        }

        // Initialize matches list
        List<Match> matches = new ArrayList<>();

        // Create first round matches
        List<Match> firstRoundMatches = new ArrayList<>();

        for (int i = 0; i < totalSlots; i += 2) {
            Player player1 = playerSlotAssignment.get(i);
            Player player2 = playerSlotAssignment.get(i+1);

            Match match = new Match(tournament, player1, player2);

            if (player1==null && player2==null) {
                match.setWinner(null);
            } else if (player1 == null) {
                match.setWinner(player2);
            } else if (player2 == null) {
                match.setWinner(player1);
            }

            firstRoundMatches.add(match);
        }

        matches.addAll(firstRoundMatches);

        // Build subsequent rounds
        List<Match> previousRoundMatches = firstRoundMatches;
        while (previousRoundMatches.size() > 1) {
            List<Match> currentRoundMatches = new ArrayList<>();
            for (int i = 0; i < previousRoundMatches.size(); i += 2) {
                // Create a match where the players will be determined by the winners of previous matches
                Match match = new Match(tournament, null, null);
                currentRoundMatches.add(match);
                matches.add(match);

                previousRoundMatches.get(i).setNextMatch(match);
                if (i + 1 < previousRoundMatches.size()) {
                    previousRoundMatches.get(i + 1).setNextMatch(match);
                }
            }
            previousRoundMatches = currentRoundMatches;
        }

        // Save and return generated matches
        matchRepository.saveAll(matches);
        return matches;
    }

    /**
     * 
     * Helper method to determines the total number of seeded players given 
     * 
     * @param playerCount The total number of players participating in the tournament
     * 
     * Seeded players are players who are ranked in the tournament
     * Among the seeded players, the higher his ELO ranking, the his seed
     * Our rationale is we set the total number of seeded players as total number of slots / 4
     * But if there are only 8 or less players, there will only be 2 seeded players
     * 
     * @return Total number of seeded players
     */
    private int calculateNumberOfSeeds(int totalSlots) {
        if (totalSlots < 9) return 2; // Only 2 seeded players if there are fewer than 9 players
        return totalSlots / 4;
    }

    /**
     * 
     * Helper method to edit the PlayerSlotAssignment Map
     * 
     * @param playerSet Set of players participating in the tournament
     * @param totalSeededPlayers Total number of seeded players
     * @param playerSlotAssignment Map with Slot number as key, its value is the player assigned to the slot number
     * @param totalSlots Total number of slots
     * 
     * 1. Sort player array by descending order of ELO ranking
     * 2. Get array of seeded players, followed by getting array of unseeded players
     * 
     * If the total number of players is a power of 2, nobody will get a walkover in the first round
     * Else, the number of players who get a walkover in the first round is the difference between the total slots & the total number of players
     * Adding null indicates that the player mapped to null gets a walkover.
     * Null added to the front of the list because we assign an unseeded player to play a seeded player first before assigning them to a random empty slot
     * We first assign seeded players, and we assign seeded players in descending order of ELO ranking
     * Ex. if there are 13 players, there will be 3 nulls added to unseeded players. Algo ensures that only top 3 seeds get walkover in the first round
     */
    private void assignPlayersToMatches(Set<Player> playerSet, int totalSeededPlayers, Map<Integer, Player> playerSlotAssignment, int totalSlots) {
        if (playerSet == null || playerSet.isEmpty()) {
            throw new IllegalArgumentException("Player list is empty or null.");
        }
        // Convert playerSet to an array and sort it
        Player[] playerArray = playerSet.toArray(new Player[0]);
        Arrays.sort(playerArray);
        int totalPlayers = playerArray.length;

        // Determine seeded and unseeded players
        Player[] seededPlayers = Arrays.copyOfRange(playerArray, 0, totalSeededPlayers);
        List<Player> unseededPlayers = new ArrayList<>(Arrays.asList(Arrays.copyOfRange(playerArray, totalSeededPlayers, totalPlayers)));

        for (int i=totalPlayers; i<totalSlots; i++) {
            unseededPlayers.add(0, null);
        }

        // Place seeded players in predefined positions
        assignSeededPlayers(unseededPlayers, seededPlayers, totalSeededPlayers, totalSlots, playerSlotAssignment);

        // // Place unseeded players in the remaining slots
        fillRemainingSlots(playerSlotAssignment, unseededPlayers, totalSlots);
    }

    /**
     * 
     * Helper method to edit the PlayerSlotAssignment Map
     * 
     * @param playerSlotAssignment Map with Slot number as key, its value is the player assigned to the slot number
     * @param seededPlayers Array of all seeded players sorted in descending order of ELO ranking
     * @param totalSlots Total number of slots
     * 
     * Assign seeded 1 & 2 to the top & bottom of the draw respectively as they are projected to meet in the finals seems they are the '2 best players' in the torunament by ELO 
     * Assign seeded 3 & 4 to the middle of the draw so that they meet the top 2 seeds in the semi finals 
     */
    private void assignTop4Seeds(Map<Integer, Player> playerSlotAssignment, Player[] seededPlayers, int totalSlots) {
        int mid = totalSlots / 2;
        playerSlotAssignment.put(0, seededPlayers[0]); // Assign seeded 1 to top of the draw
        playerSlotAssignment.put(totalSlots - 1, seededPlayers[1]); // Assign seeded 2 to bottom of the draw
        if (seededPlayers.length == 2) return; // Only if there are only 2 seeded players
        playerSlotAssignment.put(mid - 1, seededPlayers[3]); // Assign seeded 3 to middle of the draw
        playerSlotAssignment.put(mid, seededPlayers[2]); // Assign seeded 4 to middle of the draw
    }
    
    /**
     * 
     * Helper method to edit the PlayerSlotAssignment Map
     * 
     * @param unseededPlayers List of unseeded players
     * @param seededPlayers Array of seeded players
     * @param playerSlotAssignment Map with Slot number as key, its value is the player assigned to the slot number
     * @param totalSlots Total number of slots
     * 
     * mid = Index of the slot in the middle 
     * Each time we assign a seeded player, we assign an unlucky unseeded player to meet the seeded player in the first round
     * 
     * The lowerBound and upperBound is to set to assign the next set of seeded players
     * After assigning top 4 seeds, the next set of seeded players to be assigned is seeded 5-8. 
     * We assign 2 of these new set of seeded players to the middle of 2 nearest previously assigned seeded player.
     * Ex. 5 & 7 may be assigned to the middle of 1 & 4 so that 1 will meet 5 & 4 will meet 7 in the quarter finals
     * Repeat previous 2 steps again, but next set of seeded players will be seeded 9-16
     * 
     * slotsToAssign stores the slot number to assign the next set of seeded players
     * Every time a seeded player is assigned a slot from seeded 5 onwards, the next slot for the next set of seeded players will be added to the List
     */
    private void assignSeededPlayers(List<Player> unseededPlayers, Player[] seededPlayers, int numberOfSeeds, int totalSlots, Map<Integer, Player> playerSlotAssignment) {
        int mid = totalSlots / 2;
        int seededPlayersSize = seededPlayers.length;
    
        // Assign top 4 seeds to their slots
        assignTop4Seeds(playerSlotAssignment, seededPlayers, totalSlots);
    
        // Assign unseeded players or BYE to slots adjacent to top seeds
        assignUnseededPlayers(unseededPlayers, playerSlotAssignment, 0);
        assignUnseededPlayers(unseededPlayers, playerSlotAssignment, totalSlots - 1);
    
        if (seededPlayersSize < 4) {
            return; // If there are only 2 seeded players
        }
    
        // Assign unseeded players or BYE to slots adjacent to seeds 3 & 4
        assignUnseededPlayers(unseededPlayers, playerSlotAssignment, mid);
        assignUnseededPlayers(unseededPlayers, playerSlotAssignment, mid - 1);
    
        if (seededPlayersSize < 8) {
            return; // If there are only 4 seeded players
        }
    
        // Assign remaining seeds
        List<Integer> slotsToAssign = new ArrayList<>();
        slotsToAssign.add(numberOfSeeds - 1);
        slotsToAssign.add(numberOfSeeds);
    
        int temp1 = numberOfSeeds * 3;
        slotsToAssign.add(temp1);
        slotsToAssign.add(temp1 - 1);
    
        int upper = (int) (Math.log(numberOfSeeds) / Math.log(2));
        for (int r = 2; r < upper; r++) {
            int lowerbound = (int) Math.pow(2, r);
            int upperbound = (int) Math.pow(2, r + 1);
            for (int i = lowerbound; i < upperbound; i++) {
                if (i < seededPlayers.length) {
                    int tempindex = slotsToAssign.get(0);
                    playerSlotAssignment.put(tempindex, seededPlayers[i]);
                    assignUnseededPlayers(unseededPlayers, playerSlotAssignment, tempindex);
                    
                    slotsToAssign.add(tempindex - 8);
                    slotsToAssign.add(tempindex + 8);
                    slotsToAssign.remove(0);
                }
            }
        }
    }

    /**
     * 
     * Helper method to asign unlucky unseeded player to play seeded player in the first round
     * 
     * @param playerSlotAssignment Map with Slot number as key, its value is the player assigned to the slot number
     * @param unseededPlayers List of all unseeded players (Players who are not seeded)
     * @param index index to assign the unseeded player
     * 
     * If the most recently assigned seeded player is assigned to an even number slot number, 
     * Then assign the unseeded player to the slot above 
     * Else assign the unseeded to the slot number below 
     */
    private void assignUnseededPlayers(List<Player> unseededPlayers, Map<Integer, Player> playerAssignment, int index) {
        if (!unseededPlayers.isEmpty()) {
            if (index % 2 == 0) playerAssignment.put(index + 1, unseededPlayers.remove(0));
            else playerAssignment.put(index - 1, unseededPlayers.remove(0));
        }
    }    

    /**
     * 
     * Helper method to asign remaining unseeded players who are lucky to be not mapped to a seeded player in the first round
     * Assign them to remianing slots that are still empty after assigning all seeded players & unlucky unseeded players
     * 
     * @param playerSlotAssignment Map with Slot number as key, its value is the player assigned to the slot number
     * @param unseededPlayers List of all unseeded players (Players who are not seeded)
     * @param totalSlots total number of slots
     * 
     * Iterate through every 2 slots, to evenly distribute the unseeded players
     * Ensures that in the case of many walkovers, there won't be too many empty slots in 1 half of the draw
     * Ensures that no players will get a walkover in the 2nd round. (Unless both players in the first round don't show up)
     */
    private void fillRemainingSlots(Map<Integer, Player> playerSlotAssignment, List<Player> unseededPlayers, int totalSlots) {
        int i = 2 ;
        while (unseededPlayers.size() > 0 && i < totalSlots) {
            if (!playerSlotAssignment.containsKey(i)) { //Makes sure that slot is not taken
                playerSlotAssignment.put(i, unseededPlayers.remove(0));
            }
            i += 2;
        }

        i = 3;
        while (unseededPlayers.size() > 0 && i < totalSlots) {
            if (!playerSlotAssignment.containsKey(i)) { //Makes sure that slot is not taken
                playerSlotAssignment.put(i, unseededPlayers.remove(0));
            }
            i += 2;
        }

    }
}