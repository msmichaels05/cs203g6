package com.amateuraces.tournament;

import com.amateuraces.player.EloComparator;
import com.amateuraces.player.Player;
import com.amateuraces.match.Match;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.util.Arrays;

//import org.hibernate.mapping.List;

import com.amateuraces.player.Player;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;


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

    private int maxPlayers = 32;

    private int playerCount = 0;

    private LocalDate startDate;
    
    private LocalDate endDate;

    private String location;

    private String description;

    @Transient
    private MatchMinHeap draw = null; //need to determine size eventually

    @ManyToMany
    @JoinTable(
        name = "tournament_players",
        joinColumns = @JoinColumn(name = "tournament_id"),
        inverseJoinColumns = @JoinColumn(name = "player_id")
    )
    @JsonIgnore
    private Set<Player> players = new HashSet<>();

        // // Implement equals and hashCode
        // @Override
        // public boolean equals(Object o) {
        //     if (this == o) return true;
        //     if (!(o instanceof Tournament)) return false;
        //     Tournament tournament = (Tournament) o;
        //     return Objects.equals(id, tournament.id); // Compare based on 'id'
        // }
    
        // @Override
        // public int hashCode() {
        //     return Objects.hash(id); // Hash based on 'id'
        // }


    public Tournament(String name, int maxPlayers, String location, int ELOrequirement, String description) {
        this.ELOrequirement = ELOrequirement;
        this.name = name;
        this.maxPlayers = maxPlayers;
        this.location = location;
        this.description = description;
        this.draw = new MatchMinHeap(maxPlayers);
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

    public boolean addPlayer(Player player) {
        boolean added = players.add(player);
        if (added) {
            playerCount++;
            player.addToTournamentHistory(this);; // Ensure the bi-directional relationship is maintained
        }
        return added;
    }

    public void removePlayer(Player player) {
        players.remove(player);
        player.removeFromTournamentHistory(this);// Also remove this tournament from the player's list
    }

    public MatchMinHeap getDraw() {return draw;}
    // public boolean addPlayer(Player player){
    //     players.add(player);
    //     return true;
    // }

    // // Get the count of registered players
    // public int getRegisteredPlayerCount() {
    //     return players.size();
    // }

    // // Clear the list of players
    // public void clearPlayers() {
    //     players.clear();
    // }

    /**
     * Create Draw of the tournament. 
     * Draw is a min-heap struct, similar to binary tree, but condition is parent is always smaller than child
     * @param round1Matches Get total number of 1st round matches, even matches with BYES. 
     *  1: int of log2(playerCount) 
     *  2: Take the ceiling of it, then subtract by 1
     *  3: Calculate 2 to the power of the result above
     * @param totalRounds Total number of rounds excluding 1st round (2nd round - Finals)
     *  1: Take log2(round1Matches) to calculate totalRounds
     * @param numberOfSeeds Total number of seeded players, which is least number that is gives int when log2(number) >= playerCount, then divide by 4
     * @param playersByElo An array which is first initialised as an array of all players in the tournament before sorting by desc order of ELO ranking
     */

    public void initialiseDraw() { //Algorithm to create the draw
        //Once there are 17 players, there has to be 16 1st round matches, only 2 unlucky players have to face off in 1st round, everybody else gets BYE in the 1st round
        int round1Matches = (int) Math.pow(2, (int) Math.ceil((Math.log(playerCount) / Math.log(2))) -1); 
        int totalRounds = (int) ( Math.log((double)round1Matches) / Math.log(2) ); 
        for (int i=1; i<totalRounds+1; i++) { //Insert Final Round first, then the rounds below it
            for (int j=0; j<i; j++) { //create match for each round, start from finals -> 2nd round
                Match tempmatch = new Match(null, null);  //Since subsequent matches outcome not determined yet, players are set to null 
                draw.insert(tempmatch); //insert match for the particular round
            }
        } //All matches for subsequent rounds have been created and added to the drawHeap

        //ALL PLAYERS players attribute
        Player[] seededPlayers = null;
        Player[] unseededPlayers = null;

        //Segregate seeded & unseeded players;
        int numberOfSeeds = ((int) Math.pow(2, (int) Math.ceil((Math.log(playerCount) / Math.log(2))) -1)) / 4; 
        Player[] playersByElo = players.toArray(new Player[0]); 
        Arrays.sort(playersByElo, new EloComparator()); //Sort players in desc order of elo
        seededPlayers = Arrays.copyOfRange(playersByElo, 0, numberOfSeeds); //All seeded players
        unseededPlayers = Arrays.copyOfRange(playersByElo, numberOfSeeds + 1, playersByElo.length); //All unseeded players 
  
        Player[] slots = new Player[seededPlayers.length + unseededPlayers.length];
        
        assignSeedsToSlots(seededPlayers, slots); 
        for (int r=0; r<slots.length; r+=2) {
            Match temp1stRoundMatch = new Match(slots[r], slots[r+1]);
            draw.insert(temp1stRoundMatch); //Add every matched up player to draw
        } //DONE
    } 

    public String printDraw() {
        return draw.printHeap();
    }

    public Player[] recursion(Player[] slots, Player[] seededPlayers, int playerCOunt, int index) {
        if (playerCOunt < 16) {
            slots[0] = seededPlayers[0];
            slots[1] = seededPlayers[1];
            return slots;
        }
        if (index == slots.length / 2 -1) {
            slots[index + 1] = seededPlayers[2];
            slots[index] = seededPlayers[3];
        }
        else {
            slots[index] = seededPlayers[seededPlayers.length - index - 1];
            slots[index] = seededPlayers[seededPlayers.length - index - 2];
            slots[slots.length - index - 1] = seededPlayers[seededPlayers.length - index - 3];
            slots[slots.length - index - 2] = seededPlayers[seededPlayers.length - index - 4];
            //return recursion(slots, seededPlayers, index + )
        }

    }

    // Method to assign seeded players to specific slots
    public static void assignSeedsToSlots(Player[] seededPlayers, Player[] slots) {
        int totalMatches = slots.length / 2;

        // Seed 1
        slots[0] = seededPlayers[0];

        // Seed 2
        slots[slots.length - 1] = seededPlayers[1];

        // Seeds 3 and 4
        int midPoint = totalMatches / 2;
        slots[midPoint - 1] = seededPlayers[2];
        slots[midPoint] = seededPlayers[3];

        // Seeds 5 to n
        int seedIndex = 4;
        int[] predefinedPositions = getSeedPositions(seededPlayers.length, slots.length);

        for (int pos : predefinedPositions) {
            if (seedIndex >= seededPlayers.length) break;
            slots[pos] = seededPlayers[seedIndex];
            seedIndex++;
        }
    }

    // Method to get predefined positions for seeds 5 to n
    public static int[] getSeedPositions(int numberOfSeeds, int totalSlots) {
        // Positions can be predefined according to tournament standards
        // For simplicity, we'll return an array of positions
        // Adjust this method based on the specific seeding rules

        // Example positions for seeds 5 to 8 in a 32-player draw
        return new int[]{(totalSlots / 4) - 1, (3 * totalSlots / 4), (totalSlots / 4), (3 * totalSlots / 4) - 1};
    }

    // Method to fill remaining slots with unseeded players
    public static void fillUnseededPlayers(Player[] slots, List<Player> unseededPlayers) {
        Random rand = new Random();
        for (int i = 0; i < slots.length; i++) {
            if (slots[i] == null) {
                int index = rand.nextInt(unseededPlayers.size());
                slots[i] = unseededPlayers.remove(index);
            }
        }
    }

    //Update match results
    public Match recordMatchResult(Match match, int roundNo, Player winner, String score) {
        return draw.recordMatchResult(match, roundNo, winner, score);
    }

    //Update next round results
    public Match updateNextRound(Match match, int roundNo, Player winner, String score) {
        //Returns & updates next round match by adding the player that moved forward
        return draw.updateNextRound(match, roundNo, winner);
    }

    public String getTournamentname() {
        // TODO Auto-generated method stub
        return name;
    }

}

