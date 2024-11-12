package com.amateuraces.tournament;

import java.time.LocalDate;
import java.util.*;

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
     * Creates matches for the given tournament by pairing up players.
     * 
     * @param tournamentId the ID of the tournament to create matches for.
     * @return a list of created matches.
     */

    // @Transactional
    // public List<Match> createMatchesForTournament(Long tournamentId) {
    // Tournament tournament = tournamentRepository.findById(tournamentId)
    // .orElseThrow(() -> new TournamentNotFoundException(tournamentId));

    // Set<Player> players = tournament.getPlayers(); // Get the players in the
    // tournament

    // // Generate matches by pairing players
    // List<Match> matches = new ArrayList<>();
    // Player[] playerArray = players.toArray(new Player[0]); // Convert the set to
    // an array for pairing

    // for (int i = 0; i < playerArray.length; i += 2) {
    // if (i + 1 < playerArray.length) {
    // Player player1 = playerArray[i];
    // Player player2 = playerArray[i + 1];

    // // Create a match and set tournament and players
    // Match match = new Match(tournament,player1,player2);
    // // Save the match to the database
    // matchRepository.save(match);
    // matches.add(match);
    // } else {
    // // If there’s an odd number of players, one player gets a bye (no opponent)
    // Player player1 = playerArray[i];
    // Match match = new Match(this);
    // match.setTournament(tournament);
    // match.setPlayer1(player1);
    // match.setPlayer2(null); // No opponent for the player

    // // Save the match to the database
    // matchRepository.save(match);
    // matches.add(match);
    // }
    // }

    // return matches; // Return the list of matches that were created
    // }

    // @Transactional
    // public List<Match> generateMatches(Long tournamentId) {
    // Tournament tournament = tournamentRepository.findById(tournamentId)
    // .orElseThrow(() -> new TournamentNotFoundException(tournamentId));

    // int totalPlayers = tournament.getPlayerCount();
    // Set<Player> players = tournament.getPlayers();
    // Player[] registeredPlayers = players.toArray(new Player[0]); // Convert the
    // set to an array for pairing

    // // Determine nearest power of two and excess players
    // int nearestPowerOfTwo = calculateNearestPowerOfTwo(totalPlayers);
    // int excessPlayers = totalPlayers - nearestPowerOfTwo;
    // int treeHeight = (int) Math.floor(Math.log(totalPlayers) / Math.log(2)) - 1;
    // int nonPreliminaryPlayers = nearestPowerOfTwo - excessPlayers;

    // // Initialize the match array for the tournament structure (similar to a
    // heap)
    // Match[] matches = initializeMatchesArray(tournament, treeHeight);

    // // Populate initial matches based on number of players and round structure
    // if (totalPlayers == nearestPowerOfTwo) {
    // populateMatchesForExactPowerOfTwo(matches, registeredPlayers,
    // nearestPowerOfTwo);
    // } else {
    // populateMatchesForNonPowerOfTwo(matches, registeredPlayers,
    // nonPreliminaryPlayers, excessPlayers, treeHeight);
    // }

    // // Save and return generated matches
    // return saveMatches(matches);
    // }

    // private int calculateNearestPowerOfTwo(int totalPlayers) {
    // // Find the largest power of two less than or equal to totalPlayers
    // return (int) Math.pow(2, Math.floor(Math.log(totalPlayers) / Math.log(2)));
    // }

    // private Match[] initializeMatchesArray(Tournament tournament, int treeHeight)
    // {
    // // Initialize a match array for the tournament structure as a heap
    // int totalMatches = (int) Math.pow(2, treeHeight + 1) - 1;
    // Match[] matches = new Match[totalMatches + 1]; // Allocate extra space for
    // the bronze medal match at the end

    // // Create empty matches in the match tree structure
    // for (int i = 0; i <= treeHeight; i++) { // Adjusted to include all levels up
    // to `treeHeight`
    // int numberOfMatchesAtHeight = (int) Math.pow(2, i);
    // int firstMatchAtHeight = numberOfMatchesAtHeight - 1;

    // for (int j = 0; j < numberOfMatchesAtHeight; j++) {
    // int matchIndex = firstMatchAtHeight + j;
    // matches[matchIndex] = new Match(tournament, null, null); // Initialize each
    // match
    // }
    // }

    // // Create an extra match for bronze medal match
    // matches[totalMatches] = new Match(tournament, null, null);

    // return matches;
    // }

    // private void populateMatchesForExactPowerOfTwo(Match[] matches, Player[]
    // players, int nearestPowerOfTwo) {
    // // Populate matches for when totalPlayers is exactly a power of two
    // int matchIndex = nearestPowerOfTwo;
    // for (int i = 0; i < players.length; i++) {
    // if (i % 2 == 0) {
    // matches[matchIndex + i / 2].setPlayer1(players[i]);
    // } else {
    // matches[matchIndex + i / 2].setPlayer2(players[i]);
    // }
    // }
    // }

    // private void populateMatchesForNonPowerOfTwo(Match[] matches, Player[]
    // players, int nonPreliminaryPlayers, int excessPlayers, int treeHeight) {
    // // Handle non-preliminary players
    // int matchIndexAfterPreliminaries = (int) Math.pow(2, treeHeight - 1);
    // for (int i = 0; i < nonPreliminaryPlayers; i++) {
    // if (i % 2 == 0) {
    // matches[matchIndexAfterPreliminaries + i / 2].setPlayer1(players[i]);
    // } else {
    // matches[matchIndexAfterPreliminaries + i / 2].setPlayer2(players[i]);
    // }
    // }

    // // Handle excess players for preliminary matches
    // int preliminaryStartIndex = nonPreliminaryPlayers % 2 == 0 ?
    // 2 * (matchIndexAfterPreliminaries + nonPreliminaryPlayers / 2) + 1 :
    // 2 * (matchIndexAfterPreliminaries + nonPreliminaryPlayers / 2) + 2;

    // for (int i = 0; i < 2 * excessPlayers; i++) {
    // if (i % 2 == 0) {
    // matches[preliminaryStartIndex + i /
    // 2].setPlayer1(players[nonPreliminaryPlayers + i]);
    // } else {
    // matches[preliminaryStartIndex + i /
    // 2].setPlayer2(players[nonPreliminaryPlayers + i]);
    // }
    // }
    // }

    @Transactional
    public List<Match> generateMatches(Long tournamentId) {
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new TournamentNotFoundException(tournamentId));

        int totalPlayers = tournament.getPlayerCount();
        // List<Player> players = new ArrayList<>(tournament.getPlayers()); // Convert the set to a list
        // Collections.shuffle(players); // Optional: Shuffle players for random pairing
        //System.out.println("Total number of players: " + totalPlayers);
        int totalSlots = (int) Math.pow(2, Math.ceil(Math.log(totalPlayers) / Math.log(2)));
        System.out.println("Total slots is " + totalSlots);
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

        for (int i=0; i<totalSlots; i+=2) {
            Player player1 = playerSlotAssignment.get(i);
            Player player2 = playerSlotAssignment.get(i+1);

            //VARIABLES TO SEE ON TERMINAL
            int p1 = -1;
            int p2 = -1;

            if (player1 != null) p1 = player1.getElo(); 
            if (player2 != null) p2 = player2.getElo();
            System.out.println();

            Match match = new Match(tournament, player1, player2);
            if (player1==null && player2==null) {
                match.setWinner(null);
            }
            if (player1 == null) {
                match.setWinner(player2);
            }
            if (player2 == null) {
                match.setWinner(player1);
            }
            System.out.println(String.format("Slots %d & Slots %d", i, i+1));
            System.out.println(String.format("Player %d vs Player %d \n", p1, p2));
            firstRoundMatches.add(match);
        }

        // // Assign byes if necessary
        // while (numberOfByes > 0 && playerIndex < players.size()) {
        //     Player playerWithBye = players.get(playerIndex++);
        //     Match match = new Match(tournament, playerWithBye, null); // Player gets a bye
        //     firstRoundMatches.add(match);
        //     numberOfByes--;
        // }

        // // Create matches for remaining players
        // while (playerIndex < players.size()) {
        //     Player player1 = players.get(playerIndex++);
        //     Player player2 = (playerIndex < players.size()) ? players.get(playerIndex++) : null;
        //     Match match = new Match(tournament, player1, player2);
        //     firstRoundMatches.add(match);
        // }

        matches.addAll(firstRoundMatches);

        //System.out.println("FIRSTROUNDMATCHes");
        //System.out.println(firstRoundMatches.toString());

        // Build subsequent rounds
        List<Match> previousRoundMatches = firstRoundMatches;
        while (previousRoundMatches.size() > 1) {
            List<Match> currentRoundMatches = new ArrayList<>();
            for (int i = 0; i < previousRoundMatches.size(); i += 2) {
                // Create a match where the players will be determined by the winners of
                // previous matches
                Match match = new Match(tournament, null, null);
                // Optionally, set the match dependencies (e.g., match.setPreviousMatches(...))
                currentRoundMatches.add(match);
            }
            matches.addAll(currentRoundMatches);
            //System.out.println("NEXTROUNDMATCHES");
            //System.out.println(currentRoundMatches.toString());
            previousRoundMatches = currentRoundMatches;
        }

        // Add bronze medal match if needed
        if (totalPlayers >= 4) {
            Match bronzeMedalMatch = new Match(tournament, null, null);
            matches.add(bronzeMedalMatch);
        }

        // Save and return generated matches
        matchRepository.saveAll(matches);
        return matches;
    }

    public static int calculateEloGain(Player player1, Player player2, String set1Score, String set2Score,
            String set3Score) {
        int K = 32; // K-factor in Elo rating system
        double eloDifference = 0;
        if (player1 == null && player2 == null) {
            eloDifference = 0;
        }
        else if (player1 == null) {
            eloDifference = player2.getElo();
        }
        else if (player2 == null) {
            eloDifference = player1.getElo();
        }
        else eloDifference = player2.getElo() - player1.getElo();

        double expectedScore = 1 / (1 + Math.pow(10, eloDifference / 400.0));

        int actualScore = calculateActualScore(set1Score, set2Score, set3Score);
        int eloGain = (int) Math.round(K * (actualScore - expectedScore));
        return eloGain;
    }

    private static int calculateActualScore(String set1Score, String set2Score, String set3Score) {
        int setsWonByPlayer1 = 0;
        int setsWonByPlayer2 = 0;

        String[] setScores = { set1Score, set2Score, set3Score };
        for (String setScore : setScores) {
            if (setScore != null && !setScore.isEmpty()) {
                String[] scores = setScore.split("-");
                int score1 = Integer.parseInt(scores[0]);
                int score2 = Integer.parseInt(scores[1]);
                if (score1 > score2) {
                    setsWonByPlayer1++;
                } else if (score2 > score1) {
                    setsWonByPlayer2++;
                }
            }
        }

        if (setsWonByPlayer1 > setsWonByPlayer2) {
            return 1; // Player1 wins
        } else {
            return 0; // Player1 loses
        }
    }

    public static int getPerformancePoint(String set1Score, String set2Score, String set3Score) {
        int totalScoreDifference = calculateTotalScoreDifference(set1Score, set2Score, set3Score);
        return totalScoreDifference;
    }

    private static int calculateTotalScoreDifference(String set1Score, String set2Score, String set3Score) {
        int totalScore1 = 0;
        int totalScore2 = 0;

        String[] setScores = { set1Score, set2Score, set3Score };
        for (String setScore : setScores) {
            if (setScore != null && !setScore.isEmpty()) {
                String[] scores = setScore.split("-");
                int score1 = Integer.parseInt(scores[0]);
                int score2 = Integer.parseInt(scores[1]);
                totalScore1 += score1;
                totalScore2 += score2;
            }
        }

        return totalScore1 - totalScore2;
    }

    private int calculateNumberOfSeeds(int playerCount) {
        if (playerCount < 9) return 2; // Only 2 seeded players if there are fewer than 9 players
        return playerCount / 4;
    }

    private void assignPlayersToMatches(Set<Player> playerSet, int totalSeededPlayers, Map<Integer, Player> playerSlotAssignment, int totalSlots) {
        if (playerSet == null || playerSet.isEmpty()) {
            throw new IllegalArgumentException("Player list is empty or null.");
        }
        System.out.println("Total slots is " + totalSlots);
        // Convert playerSet to an array and sort it
        Player[] playerArray = playerSet.toArray(new Player[0]);
        Arrays.sort(playerArray);
        int totalPlayers = playerArray.length;

        // Determine seeded and unseeded players
        Player[] seededPlayers = Arrays.copyOfRange(playerArray, 0, totalSeededPlayers);
        System.out.println("Number of seedded players: " + seededPlayers.length);
        System.out.println("First seed is " + seededPlayers[0].getElo());
        System.out.println("Second seed is " + seededPlayers[1].getElo());
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
        System.out.println("Total slots is " + totalSlots);
        playerSlotAssignment.put(0, seededPlayers[0]); // Assign seeded 1 to top of the draw
        playerSlotAssignment.put(totalSlots - 1, seededPlayers[1]); // Assign seeded 2 to bottom of the draw
        if (seededPlayers.length == 2) return; // Only if there are only 2 seeded players
        playerSlotAssignment.put(mid - 1, seededPlayers[3]); // Assign seeded 3 to middle of the draw
        playerSlotAssignment.put(mid, seededPlayers[2]); // Assign seeded 4 to middle of the draw
    }
    
    private void assignSeededPlayers(List<Player> unseededPlayers, Player[] seededPlayers, int numberOfSeeds, int totalSlots, Map<Integer, Player> playerSlotAssignment) {
        int mid = totalSlots / 2;
        int seededPlayersSize = seededPlayers.length;
    
        System.out.println("Total slots is " + totalSlots);
        // Assign top 4 seeds to their slots
        assignTop4Seeds(playerSlotAssignment, seededPlayers, totalSlots);
        System.out.println("Slot number 0" + playerSlotAssignment.get(0).getElo());
        System.out.println("Slot number " + (totalSlots-1) + playerSlotAssignment.get(totalSlots-1).getElo());
        System.out.println("TOtal number of unseeded players: " + unseededPlayers.size());
    
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
            if (index % 2 == 0) {
                playerAssignment.put(index + 1, unseededPlayers.remove(0));
            } else {
                playerAssignment.put(index - 1, unseededPlayers.remove(0));
            }
        }
    }    

    private void fillRemainingSlots(Map<Integer, Player> playerSlotAssignment, List<Player> unseededPlayers, int totalSlots) {
        //System.out.println("Unseeded player list " + unseededPlayers.toString());
        int i = 2 ;
        while (unseededPlayers.size() > 0 && i < totalSlots) {
            if (!playerSlotAssignment.containsKey(i)) { //Makes sure that slot is not taken
                playerSlotAssignment.put(i, unseededPlayers.remove(0));
                //if (playerSlotAssignment.get(i) != null) System.out.println("Slot number " + i + " assigned to player" + playerSlotAssignment.get(i));
                //else System.out.println("Slot number " + i + ": BYE");
                //System.out.println("Unseeded players left: " + unseededPlayers.toString() + "\n");
            }
            i += 2;
        }
        i = 3;
        while (unseededPlayers.size() > 0 && i < totalSlots) {
            if (!playerSlotAssignment.containsKey(i)) { //Makes sure that slot is not taken
                playerSlotAssignment.put(i, unseededPlayers.remove(0));
                //if (playerSlotAssignment.get(i) != null) System.out.println("Slot number " + i + " assigned to player" + playerSlotAssignment.get(i));
                //else System.out.println("Slot number " + i + ": BYE");
                //System.out.println("Unseeded players left: " + unseededPlayers.toString() + "\n");
            
            }
            i += 2;
        }
        System.out.println("All players assigned");
    }
    
}