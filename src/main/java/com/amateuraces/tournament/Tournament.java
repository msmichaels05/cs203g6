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

    private int maxPlayers;

    private int playerCount = 0;

    private LocalDate startDate;
    
    private LocalDate endDate;

    private String location;

    private String description;

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

    public boolean addPlayer(Player player) {
        boolean added = players.add(player);
        if (added) {
            playerCount++;
            player.getTournaments().add(this); // Ensure the bi-directional relationship is maintained
        }
        return added;
    }

    public void removePlayer(Player player) {
        players.remove(player);
        player.getTournaments().remove(this); // Also remove this tournament from the player's list
    }
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

    public void initialiseDraw() { //Algorithm to create the draw
        //Once there are 17 players, there has to be 16 1st round matches, only 2 unlucky players have to face off in 1st round, everybody else gets BYE in the 1st round
        int round1Matches = (int) Math.pow(2, (int) Math.ceil((Math.log(playerCount) / Math.log(2))) -1); //Get total number of 1st round matches, even matches with BYES
        int totalRounds = (int) Math.log((double)round1Matches); //total number of rounds excluding 1st round
        for (int i=1; i<totalRounds; i*=2) {
            for (int j=0; j<i; j++) { //create match for each round, start from finals -> 2nd round
                Match tempmatch = new Match(null, null);  //Since subsequent matches outcome not determined yet, players are set to null 
                draw.insert(tempmatch); //insert match for the particular round
            }
        } //All matches for subsequent rounds have been created and added to the drawHeap

        //ALL PLAYERS players attribute
        Player[] seededPlayers = null;
        Player[] unseededPlayers = null;
        
        assignSeedsToSlots(seededPlayers, null);
        for (int r=0; r<round1Matches; r++) {  //1st match 1st seed, last match 2nd seed, n/2 match 3rd seed, n/2 + 1 4th seed
            Match temp1stRoundMatch = null;
            if (r==0) {//Assign player of highest elo (highest seed) to first match
                //temp1stRoundMatch = new Match(seededPlayers[0], getRandomUnseededPlayer) ;
            }
            if (r==round1Matches - 1) {
                //temp1stRoundMatch = new Match(getRandomUnseededPlayer, seededPlayers[seededPlayers.length-1]);
            }
            else {
                //temp1stRoundMatch = new Match(getRandomUnseededPlayer, getRandomUnseededPlayer);
            }
            draw.insert(temp1stRoundMatch);
        }
    }

    // Method to assign seeded players to specific slots
    private static void assignSeedsToSlots(Player[] seededPlayers, Player[] slots) {
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
    private static int[] getSeedPositions(int numberOfSeeds, int totalSlots) {
        // Positions can be predefined according to tournament standards
        // For simplicity, we'll return an array of positions
        // Adjust this method based on the specific seeding rules

        // Example positions for seeds 5 to 8 in a 32-player draw
        return new int[]{(totalSlots / 4) - 1, (3 * totalSlots / 4), (totalSlots / 4), (3 * totalSlots / 4) - 1};
    }

    // Method to fill remaining slots with unseeded players
    private static void fillUnseededPlayers(Player[] slots, List<Player> unseededPlayers) {
        Random rand = new Random();
        for (int i = 0; i < slots.length; i++) {
            if (slots[i] == null) {
                int index = rand.nextInt(unseededPlayers.size());
                slots[i] = unseededPlayers.remove(index);
            }
        }
    }

    // Placeholder methods to get seeded and unseeded players
    private void seededUnseeded() { //Create list of seeded & unseeded players
        int no = (int) Math.pow(2, (int) Math.ceil((Math.log(playerCount) / Math.log(2))) -1);
        int numberOfSeeds = no / 4;
        //TOBE IMPLEMENTEDDD
        Player[] playersByElo = players.toArray(new Player[0]);  //Get all players in the tournament, sorting them by elo desc order
        Arrays.sort(playersByElo, new EloComparator());
        Player[] seededPlayers = Arrays.copyOfRange(playersByElo, 0, numberOfSeeds); //All seeded players
        Player[] unseededPlayers = Arrays.copyOfRange(playersByElo, numberOfSeeds + 1, playersByElo.length); //All unseeded players 
    }

}

