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

    // @Transactional
    // public List<Match> generateMatches(Long tournamentId) {
    //     Tournament tournament = tournamentRepository.findById(tournamentId)
    //             .orElseThrow(() -> new TournamentNotFoundException(tournamentId));

    //     int totalPlayers = tournament.getPlayerCount();
    //     List<Player> players = new ArrayList<>(tournament.getPlayers()); // Convert the set to a list
    //     Collections.shuffle(players); // Optional: Shuffle players for random pairing

    //     // Determine the next power of two greater than or equal to totalPlayers
    //     int nextPowerOfTwo = 1;
    //     while (nextPowerOfTwo < totalPlayers) {
    //         nextPowerOfTwo *= 2;
    //     }
    //     int numberOfByes = nextPowerOfTwo - totalPlayers;

    //     // Initialize matches list
    //     List<Match> matches = new ArrayList<>();

    //     // Create first round matches
    //     List<Match> firstRoundMatches = new ArrayList<>();
    //     int playerIndex = 0;

    //     // Assign byes if necessary
    //     while (numberOfByes > 0 && playerIndex < players.size()) {
    //         Player playerWithBye = players.get(playerIndex++);
    //         Match match = new Match(tournament, playerWithBye, null); // Player gets a bye
    //         firstRoundMatches.add(match);
    //         numberOfByes--;
    //     }

    //     // Create matches for remaining players
    //     while (playerIndex < players.size()) {
    //         Player player1 = players.get(playerIndex++);
    //         Player player2 = (playerIndex < players.size()) ? players.get(playerIndex++) : null;
    //         Match match = new Match(tournament, player1, player2);
    //         firstRoundMatches.add(match);
    //     }

    //     matches.addAll(firstRoundMatches);

    //     // Build subsequent rounds
    //     List<Match> previousRoundMatches = firstRoundMatches;
    //     while (previousRoundMatches.size() > 1) {
    //         List<Match> currentRoundMatches = new ArrayList<>();
    //         for (int i = 0; i < previousRoundMatches.size(); i += 2) {
    //             // Create a match where the players will be determined by the winners of
    //             // previous matches
    //             Match match = new Match(tournament, null, null);
    //             // Optionally, set the match dependencies (e.g., match.setPreviousMatches(...))
    //             currentRoundMatches.add(match);
    //             matches.add(match);

    //             // Set the nextMatch in previous matches
    //             previousRoundMatches.get(i).setNextMatch(match);
    //             if (i + 1 < previousRoundMatches.size()) {
    //                 previousRoundMatches.get(i + 1).setNextMatch(match);
    //             }
    //         }
    //         previousRoundMatches = currentRoundMatches;
    //     }

    //     // Save and return generated matches
    //     matchRepository.saveAll(matches);
    //     return matches;
    // }

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
        int numberOfByes = nextPowerOfTwo - totalPlayers;

        // Initialize matches list
        List<Match> matches = new ArrayList<>();

        // Create first round matches
        List<Match> firstRoundMatches = new ArrayList<>();
        int playerIndex = 0;

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

    private int calculateNumberOfSeeds(int playerCount) {
        if (playerCount < 9) return 2; // Only 2 seeded players if there are fewer than 9 players
        return playerCount / 4;
    }

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

    private void assignTop4Seeds(Map<Integer, Player> playerSlotAssignment, Player[] seededPlayers, int totalSlots) {
        int mid = totalSlots / 2;
        playerSlotAssignment.put(0, seededPlayers[0]); // Assign seeded 1 to top of the draw
        playerSlotAssignment.put(totalSlots - 1, seededPlayers[1]); // Assign seeded 2 to bottom of the draw
        if (seededPlayers.length == 2) return; // Only if there are only 2 seeded players
        playerSlotAssignment.put(mid - 1, seededPlayers[3]); // Assign seeded 3 to middle of the draw
        playerSlotAssignment.put(mid, seededPlayers[2]); // Assign seeded 4 to middle of the draw
    }
    
    private void assignSeededPlayers(List<Player> unseededPlayers, Player[] seededPlayers, int numberOfSeeds, int totalSlots, Map<Integer, Player> playerSlotAssignment) {
        int mid = totalSlots / 2;
        int seededPlayersSize = seededPlayers.length;
    
        // Assign top 4 seeds to their slots
        assignTop4Seeds(playerSlotAssignment, seededPlayers, totalSlots);
    
        // Assign unseeded players or BYE to slots adjacent to top seeds
        assignUnseededPlayers(unseededPlayers, playerSlotAssignment, 0);
        assignUnseededPlayers(unseededPlayers, playerSlotAssignment, totalSlots - 1);
    
        if (seededPlayersSize < 4) {
            //fillRemainingSlots(playerSlotAssignment, unseededPlayers, totalSlots);
            return; // If there are only 2 seeded players
        }
    
        // Assign unseeded players or BYE to slots adjacent to seeds 3 & 4
        assignUnseededPlayers(unseededPlayers, playerSlotAssignment, mid);
        assignUnseededPlayers(unseededPlayers, playerSlotAssignment, mid - 1);
    
        if (seededPlayersSize < 8) {
            //fillRemainingSlots(playerSlotAssignment, unseededPlayers, totalSlots);
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

    private void assignUnseededPlayers(List<Player> unseededPlayers, Map<Integer, Player> playerAssignment, int index) {
        if (!unseededPlayers.isEmpty()) {
            if (index % 2 == 0) playerAssignment.put(index + 1, unseededPlayers.remove(0));
            else playerAssignment.put(index - 1, unseededPlayers.remove(0));
        }
    }    

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