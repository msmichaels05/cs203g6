// package com.amateuraces.tournament;

// import java.time.LocalDate;
// import java.util.HashSet;
// import java.util.Set;

// import com.amateuraces.player.Player;
// import com.fasterxml.jackson.annotation.JsonIgnore;

// import jakarta.persistence.Entity;
// import jakarta.persistence.GeneratedValue;
// import jakarta.persistence.GenerationType;
// import jakarta.persistence.Id;
// import jakarta.persistence.JoinColumn;
// import jakarta.persistence.JoinTable;
// import jakarta.persistence.ManyToMany;
// import jakarta.validation.constraints.NotNull;
// import jakarta.validation.constraints.Size;
// import lombok.AllArgsConstructor;
// import lombok.Getter;
// import lombok.NoArgsConstructor;
// import lombok.Setter;
// import lombok.ToString;

// @Entity
// @Getter
// @Setter
// @ToString
// @AllArgsConstructor
// @NoArgsConstructor
// public class Tournament {
//     @Id 
//     @GeneratedValue(strategy = GenerationType.IDENTITY)
//     private Long id;

//     @NotNull
//     @Size(min = 1, max = 100)
//     private String name;

//     private int ELOrequirement;
//     @NotNull
//     private int maxPlayers;

//     private int playerCount = 0;
//     @NotNull
//     private LocalDate startDate;
//     @NotNull
//     private LocalDate endDate;
//     @NotNull
//     private String gender;

//     private LocalDate registrationEndDate;

//     private String location;

//     private String description;

//     @ManyToMany
//     @JoinTable(
//         name = "tournament_players",
//         joinColumns = @JoinColumn(name = "tournament_id"),
//         inverseJoinColumns = @JoinColumn(name = "player_id")
//     )
//     @JsonIgnore
//     private Set<Player> players = new HashSet<>();

//         // // Implement equals and hashCode
//         // @Override
//         // public boolean equals(Object o) {
//         //     if (this == o) return true;
//         //     if (!(o instanceof Tournament)) return false;
//         //     Tournament tournament = (Tournament) o;
//         //     return Objects.equals(id, tournament.id); // Compare based on 'id'
//         // }

//         // @Override
//         // public int hashCode() {
//         //     return Objects.hash(id); // Hash based on 'id'
//         // }

//     // Custom setter to enforce even maxPlayers
//     public void setMaxPlayers(int maxPlayers) {
//         if (maxPlayers < 0) {
//             throw new IllegalArgumentException("max players cannot be negative.");
//         }
//         if (maxPlayers >64){
//             throw new IllegalArgumentException("max players cannot be more than 64");
//         }
//         if (!(isPowerOfTwo(maxPlayers))) {
//             throw new IllegalArgumentException("max players must be a power of 2.");
//         }
//         this.maxPlayers = maxPlayers;
//     }

//     private boolean isPowerOfTwo(int maxPlayers) {
//         double temp = (double) maxPlayers;
//         while (temp >= 4) {
//             temp /= 2;
//         }
//         if (temp % 2 != 0) return false;
//         return true;
//     }

//     public Tournament(String name,String gender, int maxPlayers, String location, int ELOrequirement, String description,
//         LocalDate startDate, LocalDate endDate) {
//         this.ELOrequirement = ELOrequirement;
//         this.gender = gender;
//         this.name = name;
//         this.maxPlayers = maxPlayers;
//         this.location = location;
//         this.description = description;
//         this.startDate = startDate;
//         this.endDate = endDate;
//     }

//     public Tournament(String name) {
//         this.name = name;
//     }

//     public Tournament(String name,String location){
//         this.name = name;
//         this.location = location;
//     }

//     public boolean addPlayer(Player player) {
//         boolean added = players.add(player);
//         if (added) {
//             playerCount++;
//             player.getTournaments().add(this); // Ensure the bi-directional relationship is maintained
//         }
//         return added;
//     }

//     public void removePlayer(Player player) {
//         players.remove(player);
//         player.getTournaments().remove(this); // Also remove this tournament from the player's list
//     }
//     // public boolean addPlayer(Player player){
//     //     players.add(player);
//     //     return true;
//     // }

//     // // Get the count of registered players
//     // public int getRegisteredPlayerCount() {
//     //     return players.size();
//     // }

//     // // Clear the list of players
//     // public void clearPlayers() {
//     //     players.clear();
//     // }
// }

package com.amateuraces.tournament;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import com.amateuraces.match.Match;
import com.amateuraces.player.EloComparator;
import com.amateuraces.player.Player;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
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

    private int ELOrequirement;

    private int maxPlayers;

    private int playerCount = 0;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    private LocalDate registrationEndDate;

    private String location;

    private String description;

    private String gender;

    // The champion player of a tournament
    @ManyToOne
    @JoinColumn(name = "player_id")
    private Player championPlayer;

    @ManyToMany
    @JoinTable(name = "tournament_players", joinColumns = @JoinColumn(name = "tournament_id"), inverseJoinColumns = @JoinColumn(name = "player_id"))
    @JsonIgnore
    private Set<Player> players = new TreeSet<>(new EloComparator());

    // Custom setter to enforce even maxPlayers
    public void setMaxPlayers(int maxPlayers) {
        if (maxPlayers < 0) {
            throw new IllegalArgumentException("max players cannot be negative.");
        }
        if (maxPlayers > 64) {
            throw new IllegalArgumentException("max players cannot be more than 64");
        }
        // if (!(isPowerOfTwo(maxPlayers))) {
        // throw new IllegalArgumentException("max players must be a power of 2.");
        // }
        this.maxPlayers = maxPlayers;
    }

    public Tournament(String name, int maxPlayers, String location, int ELOrequirement, String description,
            String gender) {
        this.ELOrequirement = ELOrequirement;
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

    public int getRequirement() {
        return ELOrequirement;
    }

    public void setRequirement(int newRequirement) {
        ELOrequirement = newRequirement;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String newgender) {
        gender = newgender;
    }

    public boolean addPlayer(Player player) {
        boolean added = players.add(player);
        if (added) {
            playerCount++;
            player.addToTournamentHistory(this);
            ; // Ensure the bi-directional relationship is maintained
        }
        return added;
    }

    public void removePlayer(Player player) {
        players.remove(player);
        player.removeFromTournamentHistory(this);// Also remove this tournament from the player's list
    }

    // // Get the count of registered players
    // public int getRegisteredPlayerCount() {
    // return players.size();
    // }

    // // Clear the list of players
    // public void clearPlayers() {
    // players.clear();
    // }

    public String getTournamentname() {
        return name;
    }

    private void setChampion(Player champion) {
        championPlayer = champion;
    }

    // algo to create sets for multiple rounds if 64 will have 6 rounds
    // includes:
    // createIntialRound(), progressToNextRound()
    // startTournament() to start the specific round,
    public List<Match> updateNextRound(List<Match> currentMatches) {

        if (currentMatches.size() == 1) { // Declare champion of the tournament
            setChampion(currentMatches.get(0).getWinner());
            return null;
        }

        List<Match> nextRound = new ArrayList<>();
        for (int i = 0; i < currentMatches.size(); i += 2) {
            Player player1 = currentMatches.get(i).getWinner();
            Player player2 = currentMatches.get(i + 1).getWinner();
            Match tempMatch = new Match(this, player1, player2);
            /// Update winner if there are walkovers / BYE
            if (player1 == null && player2 == null)
                tempMatch.setWinner(null);

            if (player1 == null)
                tempMatch.setWinner(player2);

            if (player2 == null)
                tempMatch.setWinner(player1);

            nextRound.add(tempMatch);
        }
        return nextRound;
    }

    public List<Match> initialiseDraw() {
        int totalSlots = (int) Math.pow(2, (Math.ceil(Math.log(playerCount) / Math.log(2)))); // Total number of slots
                                                                                              // in the match

        // Create round 1
        List<Match> round1Matches = new ArrayList<>(totalSlots / 2); // Each match has two slots

        int totalSeededPlayers = calculateNumberOfSeeds(totalSlots);
        Map<Integer, Player> playerSlotAssignment = new HashMap<>();
        assignPlayersToMatches(round1Matches, players, totalSeededPlayers);

        for (int match = 0; match < totalSlots; match += 2) {
            Player player1 = playerSlotAssignment.get(match);
            Player player2 = playerSlotAssignment.get(match + 1);
            Match tempMatch = new Match(this, player1, player2);

            // Update winner if there are walkovers / BYE
            if (player1 == null && player2 == null)
                tempMatch.setWinner(null);

            if (player1 == null)
                tempMatch.setWinner(player2);

            if (player2 == null)
                tempMatch.setWinner(player1);

            round1Matches.add(tempMatch);
        }

        return round1Matches;
    }

    private void assignPlayersToMatches(List<Match> matchList, Set<Player> playerList, int totalSeededPlayers) {
        // // Sort players by ELO rating ALREADY DONE IF I SET PLAYERLIST AS TREESET
        // playerList.sort(new EloComparator());

        // Determine number of seeds
        /// int numberOfSeeds = calculateNumberOfSeeds(totalSeededPlayers);
        int numberOfSeeds = totalSeededPlayers;

        Player[] playerArray = playerList.toArray(new Player[0]);
        Arrays.sort(playerArray); // Sort players in descender order of ELO
        int totalPlayers = playerArray.length;

        // Seeded players, sub array from index 0 to numberofseeds
        Player[] seededPlayers = Arrays.copyOfRange(playerArray, 0, numberOfSeeds);

        // Unseeded players, rest of players who are not seeded
        List<Player> unseededPlayers = Arrays.asList(Arrays.copyOfRange(playerArray, numberOfSeeds, totalPlayers));

        // Assign seeds to predefined positions
        Map<Integer, Player> playerPositions = assignSeededPlayers(unseededPlayers, seededPlayers, numberOfSeeds,
                totalPlayers);

        // // Assign unseeded players to remaining matches
        fillRemainingSlots(playerPositions, unseededPlayers, totalPlayers);
        ////// assignUnseededPlayers(unseededPlayers, seededPositions, matchList);

        for (int i = 0; i < matchList.size(); i++) {
            matchList.get(i).setPlayer1(playerPositions.get(i + 1));
            matchList.get(i).setPlayer1(playerPositions.get(i + 2));
        }
    }

    private int calculateNumberOfSeeds(int playerCount) {
        if (playerCount < 9)
            return 2; // Only 2 seeded players if there are fewer than 9 players
        return playerCount / 4;
    }

    public static Map<Integer, Player> assignSeededPlayers(List<Player> unseededPlayers, Player[] seededPlayers,
            int numberOfSeeds, int totalPlayers) {
        Map<Integer, Player> playerSlotAssignment = new TreeMap<>();
        int totalSlots = numberOfSeeds * 4;
        int mid = totalSlots / 2;
        int seededPlayersSize = seededPlayers.length;

        // Assign top 4 seeds to their slots (REFACTORING)
        assignTop4Seeds(playerSlotAssignment, seededPlayers, totalSlots);

        // Assign unlucky players or BYE to map to top 2 seeds
        assignUnseededPlayers(unseededPlayers, playerSlotAssignment, 0, numberOfSeeds);

        assignUnseededPlayers(unseededPlayers, playerSlotAssignment, totalSlots - 1, numberOfSeeds);

        if (seededPlayersSize < 4) {
            fillRemainingSlots(playerSlotAssignment, unseededPlayers, totalSlots);
            return playerSlotAssignment;
        } // If there are only 2 seeded players

        // Assign unlucky players or BYE to map to seeded 3 & 4
        assignUnseededPlayers(unseededPlayers, playerSlotAssignment, mid, numberOfSeeds);
        assignUnseededPlayers(unseededPlayers, playerSlotAssignment, mid - 1, numberOfSeeds);

        if (seededPlayersSize < 8) {
            fillRemainingSlots(playerSlotAssignment, unseededPlayers, totalSlots);
            return playerSlotAssignment;
        } // If there are only 4 seeded players

        // SlotsToAssign
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

    private static void assignTop4Seeds(Map<Integer, Player> playerSlotAssignment, Player[] seededPlayers,
            int totalSlots) {
        int mid = totalSlots / 2;
        playerSlotAssignment.put(0, seededPlayers[0]); // Assign seeded 1 to top of the draw
        playerSlotAssignment.put(totalSlots - 1, seededPlayers[1]); // Assign seeded 2 to bottom of the draw
        playerSlotAssignment.put(mid - 1, seededPlayers[2]); // Assign seeded 3 to middle of the draw
        playerSlotAssignment.put(mid, seededPlayers[3]); // Assign seeded 4 to middle of the draw
    }

    private static void assignUnseededPlayers(List<Player> unseededPlayers, Map<Integer, Player> playerAssignment,
            int index, int numberOfSeeds) {
        if (!unseededPlayers.isEmpty()) {
            if (index % 2 == 0) {
                playerAssignment.put(index + 1, unseededPlayers.get(0));
            } else {
                playerAssignment.put(index - 1, unseededPlayers.get(0));
            }
            unseededPlayers.remove(0);
        }
    }

    private static void fillRemainingSlots(Map<Integer, Player> playerSlotAssignment, List<Player> unseededPlayers,
            int slots) {
        Random random = new Random();
        for (int i = 0; i < slots; i++) {
            if (!playerSlotAssignment.containsKey(i)) {
                playerSlotAssignment.put(i, unseededPlayers.get(random.nextInt(unseededPlayers.size())));
            }
        }
    }

    // public static void displayDraw(Map<Integer, Integer> map) {
    // System.out.println("Line Player");
    // for (Map.Entry<Integer, Integer> entry : map.entrySet()) {//
    // System.out.println("" + entry.getKey() + ": " + entry.getValue());
    // }
    // }

    // public void setRegistrationEndDate(LocalDate registrationEndDate) {
    //     // TODO Auto-generated method stub
    //     throw new UnsupportedOperationException("Unimplemented method 'setRegistrationEndDate'");
    // }

    // public boolean addPlayer(Player player){
    // players.add(player);
    // return true;
    // }

    // // Get the count of registered players
    // public int getRegisteredPlayerCount() {
    // return players.size();
    // }

    // // Clear the list of players
    // public void clearPlayers() {
    // players.clear();
    // }
}