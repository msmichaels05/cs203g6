package com.amateuraces.tournament;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import com.amateuraces.match.Match;
import com.amateuraces.player.Player;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Tournament {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 1, max = 100)
    private String name;

    private int ELORequirement;

    private int maxPlayers = 32;

    private int playerCount = 0;

    private LocalDate startDate;
    
    private LocalDate endDate;

    private String location;

    private String description;

    private String gender;

    private LocalDate registrationEndDate;

    // Removed lowerReservedId and upperReservedId from class variables
    // They will be used as local variables in methods

    @OneToMany(mappedBy = "tournament", 
        orphanRemoval = true,
        cascade = CascadeType.ALL)
    private List<Match> matches = null;

    // The champion player of a tournament
    @ManyToOne
    @JoinColumn(name = "player_id")
    private Player champion;
    
    @ManyToMany
    @JoinTable(
        name = "tournament_players",
        joinColumns = @JoinColumn(name = "tournament_id"),
        inverseJoinColumns = @JoinColumn(name = "player_id")
    )
    @JsonIgnore
    private Set<Player> players = new TreeSet<>();

    // Custom setter to enforce maxPlayers constraints
    public void setMaxPlayers(int maxPlayers) {
        if (maxPlayers < 0) {
            throw new IllegalArgumentException("max players cannot be negative.");
        }
        if (maxPlayers > 64){
            throw new IllegalArgumentException("max players cannot be more than 64");
        }
        this.maxPlayers = maxPlayers;
    }

    // Constructors
    public Tournament(String name, int maxPlayers, String location, int ELORequirement, String description, String gender) {
        this.ELORequirement = ELORequirement;
        this.name = name;
        this.maxPlayers = maxPlayers;
        this.location = location;
        this.description = description;
        this.gender = gender;
    }

    public Tournament(String name) {
        this.name = name;
    }

    public Tournament(String name, String location) {
        this.name = name;
        this.location = location;
    }

    // Methods to manage players
    public boolean addPlayer(Player player) {
        boolean added = players.add(player);
        if (added) {
            playerCount++;
            player.addToTournamentHistory(this); // Ensure the bi-directional relationship is maintained
        }
        return added;
    }

    public void removePlayer(Player player) {
        if (players.remove(player)) {
            player.removeFromTournamentHistory(this); // Also remove this tournament from the player's list
            playerCount--;
        }
    }

    // Method to update to the next round
    public List<Match> updateNextRound() {
        int totalRounds = (int) Math.ceil(Math.log(playerCount) / Math.log(2)); // Total number of rounds 
        
        int nextRoundIndex = 0;
        for (int i = totalRounds - 1; i > 0; i--) { // Start from round 2 onwards
            nextRoundIndex += (int) Math.pow(2, i);
            if (matches.get(nextRoundIndex).getStatus().equals("Scheduled")) { // First match of the next round

                int currentRoundIndex = nextRoundIndex - (int) Math.pow(2, i); // Get index of the first match of the current round
                
                for (int j = currentRoundIndex; j < nextRoundIndex; j += 2) {
                    Player player1 = matches.get(j).getWinner();
                    Player player2 = matches.get(j + 1).getWinner();
        
                    matches.get(nextRoundIndex).setPlayer1(player1);
                    matches.get(nextRoundIndex).setPlayer2(player2);
                }

                setMatches(matches);
                return matches; // Next round all updated
            }
        }

        // Final match completed, no more subsequent matches to update
        return null;
    }

    // Corrected initialiseDraw method
    // public List<Match> initialiseDraw() {
    //     if (playerCount <= 0) {
    //         throw new IllegalStateException("Cannot initialize draw: No players registered in the tournament.");
    //     }
    //     int totalSlots = (int) Math.pow(2, Math.ceil(Math.log(playerCount) / Math.log(2))); // Total number of slots in the match

    //     // Create round 1
    //     List<Match> matches = new ArrayList<>(totalSlots / 2); // Each match has two slots

    //     int totalSeededPlayers = calculateNumberOfSeeds(totalSlots);
    //     Map<Integer, Player> playerSlotAssignment = new HashMap<>();
    //     assignPlayersToMatches(matches, players, totalSeededPlayers);

    //     boolean isFirstMatch = true;
    //     int match;
    //     int lowerReservedId = 0, upperReservedId = 0; // Corrected variable declaration

    //     for (match = 0; match < totalSlots; match += 2) {
    //         Player player1 = playerSlotAssignment.get(match);
    //         Player player2 = playerSlotAssignment.get(match + 1);
    //         Match tempMatch = new Match(this, player1, player2);

    //         if (isFirstMatch) {
    //             lowerReservedId = match;
    //             isFirstMatch = false;
    //         }

    //         // Update winner if there are walkovers / BYE
    //         if (player1 == null && player2 == null) tempMatch.setWinner(null);
    //         else if (player1 == null) tempMatch.setWinner(player2);
    //         else if (player2 == null) tempMatch.setWinner(player1);

    //         matches.add(tempMatch);
    //     }

    //     upperReservedId = lowerReservedId + totalSlots - 1;

    //     // Adding matches in subsequent rounds first 
    //     for (int i = lowerReservedId + match; i < upperReservedId; i++) {
    //         matches.add(new Match(this, null, null));
    //     }

    //     setMatches(matches);
    //     return matches;
    // }
    
    // private void assignPlayersToMatches(Set<Player> playerList, int totalSeededPlayers, Map<Integer, Player> playerSlotAssignment) {
    //     if (playerList == null || playerList.isEmpty()) {
    //         throw new IllegalArgumentException("Player list is empty or null.");
    //     }

    //     int numberOfSeeds = totalSeededPlayers;

    //     Player[] playerArray = playerList.toArray(new Player[0]);
    //     Arrays.sort(playerArray); // Assuming Player implements Comparable
    //     int totalPlayers = playerArray.length;

    //     // Seeded players
    //     Player[] seededPlayers = Arrays.copyOfRange(playerArray, 0, numberOfSeeds);

    //     // Unseeded players
    //     List<Player> unseededPlayers = new ArrayList<>(Arrays.asList(Arrays.copyOfRange(playerArray, numberOfSeeds, totalPlayers)));

    //     // Assign seeds to predefined positions
    //     Map<Integer, Player> playerPositions = assignSeededPlayers(unseededPlayers, seededPlayers, numberOfSeeds, totalPlayers);

    //     // Assign unseeded players to remaining matches
    //     fillRemainingSlots(playerPositions, unseededPlayers, totalPlayers);

    //     for (int i = 0; i < matchList.size(); i++) {
    //         matchList.get(i).setPlayer1(playerPositions.get(i + 1));
    //         matchList.get(i).setPlayer2(playerPositions.get(i + 2));
    //     }
    // }

    private void assignPlayersToMatches(Set<Player> playerList, int totalSeededPlayers, Map<Integer, Player> playerSlotAssignment) {
        if (playerList == null || playerList.isEmpty()) {
            throw new IllegalArgumentException("Player list is empty or null.");
        }
    
        // Convert playerList to an array and sort it (assuming Player implements Comparable)
        Player[] playerArray = playerList.toArray(new Player[0]);
        Arrays.sort(playerArray);
        int totalPlayers = playerArray.length;
    
        // Determine seeded and unseeded players
        Player[] seededPlayers = Arrays.copyOfRange(playerArray, 0, totalSeededPlayers);
        List<Player> unseededPlayers = new ArrayList<>(Arrays.asList(Arrays.copyOfRange(playerArray, totalSeededPlayers, totalPlayers)));
    
        // Place seeded players in predefined positions in playerSlotAssignment
        assignSeededPlayers(unseededPlayers, seededPlayers, totalSeededPlayers, totalPlayers, playerSlotAssignment);
    
        // Place unseeded players in the remaining slots
        fillRemainingSlots(playerSlotAssignment, unseededPlayers, totalPlayers * 2);
    }
    
    private static void assignSeededPlayers(List<Player> unseededPlayers, Player[] seededPlayers, int numberOfSeeds, int totalSlots, Map<Integer, Player> playerSlotAssignment) {
        int mid = totalSlots / 2;
    
        playerSlotAssignment.put(0, seededPlayers[0]); // Assign top seed to slot 0
        playerSlotAssignment.put(totalSlots - 1, seededPlayers[1]); // Assign second seed to the last slot
    
        if (numberOfSeeds >= 4) {
            playerSlotAssignment.put(mid - 1, seededPlayers[2]); // Assign third seed
            playerSlotAssignment.put(mid, seededPlayers[3]); // Assign fourth seed
        }
    
        // Additional logic for seeds beyond the top four, if required
    }
    
    private static void fillRemainingSlots(Map<Integer, Player> playerSlotAssignment, List<Player> unseededPlayers, int totalSlots) {
        int slot = 0;
        for (Player player : unseededPlayers) {
            while (playerSlotAssignment.containsKey(slot)) {
                slot++; // Skip filled slots
            }
            playerSlotAssignment.put(slot, player); // Assign unseeded player to next available slot
            slot++;
        }
    }    

    public List<Match> initialiseDraw() {
        if (playerCount <= 0) {
            throw new IllegalStateException("Cannot initialize draw: No players registered in the tournament.");
        }

        int totalSlots = (int) Math.pow(2, Math.ceil(Math.log(playerCount) / Math.log(2))); // Total number of slots
        int firstRoundMatchesCount = totalSlots / 2;

        int totalSeededPlayers = calculateNumberOfSeeds(totalSlots);
        Map<Integer, Player> playerSlotAssignment = new HashMap<>();
        assignPlayersToMatches(players, totalSeededPlayers, playerSlotAssignment);

        // Initialize the matches list if it's null
        if (this.matches == null) {
            this.matches = new ArrayList<>();
        } else {
            this.matches.clear(); // Clear existing matches
        }

        // Create matches for the first round
        for (int matchIndex = 0; matchIndex < firstRoundMatchesCount; matchIndex++) {
            int player1Index = matchIndex * 2;
            int player2Index = player1Index + 1;

            Player player1 = playerSlotAssignment.get(player1Index);
            Player player2 = playerSlotAssignment.get(player2Index);
            Match tempMatch = new Match(this, player1, player2);

            // Update winner if there are walkovers / BYE
            if (player1 == null && player2 == null) tempMatch.setWinner(null);
            else if (player1 == null) tempMatch.setWinner(player2);
            else if (player2 == null) tempMatch.setWinner(player1);

            this.matches.add(tempMatch); // Add to existing collection
        }

        // Create matches for subsequent rounds
        int totalMatches = totalSlots - 1;
        int remainingMatchesCount = totalMatches - firstRoundMatchesCount;
        for (int i = 0; i < remainingMatchesCount; i++) {
            this.matches.add(new Match(this, null, null)); // Add to existing collection
        }

        return this.matches;
    }

    private int calculateNumberOfSeeds(int playerCount) {
        if (playerCount < 9) return 2; // Only 2 seeded players if there are fewer than 9 players 
        return playerCount / 4;
    }

    public static Map<Integer, Player> assignSeededPlayers(List<Player> unseededPlayers, Player[] seededPlayers, int numberOfSeeds, int totalPlayers) {
        Map<Integer, Player> playerSlotAssignment = new TreeMap<>();
        int totalSlots = numberOfSeeds * 4;
        int mid = totalSlots / 2;
        int seededPlayersSize = seededPlayers.length;

        // Assign top 4 seeds to their slots
        assignTop4Seeds(playerSlotAssignment, seededPlayers, totalSlots);

        // Assign unseeded players or BYE to slots adjacent to top seeds
        assignUnseededPlayers(unseededPlayers, playerSlotAssignment, 0, numberOfSeeds);
        assignUnseededPlayers(unseededPlayers, playerSlotAssignment, totalSlots - 1, numberOfSeeds);

        if (seededPlayersSize < 4) {
            fillRemainingSlots(playerSlotAssignment, unseededPlayers, totalSlots);
            return playerSlotAssignment;
        } // If there are only 2 seeded players

        // Assign unseeded players or BYE to slots adjacent to seeds 3 & 4
        assignUnseededPlayers(unseededPlayers, playerSlotAssignment, mid, numberOfSeeds);
        assignUnseededPlayers(unseededPlayers, playerSlotAssignment, mid - 1, numberOfSeeds);

        if (seededPlayersSize < 8) {
            fillRemainingSlots(playerSlotAssignment, unseededPlayers, totalSlots);
            return playerSlotAssignment;
        } // If there are only 4 seeded players

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
                    assignUnseededPlayers(unseededPlayers, playerSlotAssignment, tempindex, numberOfSeeds);
                    
                    slotsToAssign.add(tempindex - 8);
                    slotsToAssign.add(tempindex + 8);
                    slotsToAssign.remove(0);
                }
            }
        }
        return playerSlotAssignment;
    }

    private static void assignTop4Seeds(Map<Integer, Player> playerSlotAssignment, Player[] seededPlayers, int totalSlots) {
        int mid = totalSlots / 2;
        playerSlotAssignment.put(0, seededPlayers[0]); // Assign seeded 1 to top of the draw
        playerSlotAssignment.put(totalSlots - 1, seededPlayers[1]); // Assign seeded 2 to bottom of the draw
        if (seededPlayers.length == 2) return; // Only if there are only 2 seeded players
        playerSlotAssignment.put(mid - 1, seededPlayers[2]); // Assign seeded 3 to middle of the draw
        playerSlotAssignment.put(mid, seededPlayers[3]); // Assign seeded 4 to middle of the draw
    }

    private static void assignUnseededPlayers(List<Player> unseededPlayers, Map<Integer, Player> playerAssignment, int index, int numberOfSeeds) {
        if (!unseededPlayers.isEmpty()) {
            if (index % 2 == 0) {
                playerAssignment.put(index + 1, unseededPlayers.remove(0));
            } else {
                playerAssignment.put(index - 1, unseededPlayers.remove(0));
            }
        }
    }

    // private static void fillRemainingSlots(Map<Integer, Player> playerSlotAssignment, List<Player> unseededPlayers, int slots) {
    //     if (unseededPlayers == null || unseededPlayers.isEmpty()) {
    //         throw new IllegalArgumentException("No unseeded players available to fill remaining slots.");
    //     }
    //     Random random = new Random();
    //     for (int i = 0; i < slots; i++) {
    //         if (!playerSlotAssignment.containsKey(i)) {
    //             playerSlotAssignment.put(i, unseededPlayers.get(random.nextInt(unseededPlayers.size())));
    //         }
    //     }
    // }
}
