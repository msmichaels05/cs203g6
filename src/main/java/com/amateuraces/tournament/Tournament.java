package com.amateuraces.tournament;

import com.amateuraces.player.EloComparator;
import com.amateuraces.player.Player;
import com.amateuraces.match.Match;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Random;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

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

    @OneToMany(mappedBy = "tournament", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Match> draw = new ArrayList<>(); //Contains all matches in the tournament

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

    public void addMatch(Match match) {
        draw.add(match);
        match.setTournament(this);
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

    public String getTournamentname() {
        return name;
    }

    public void initialiseDraw() {
        int round1Matches = (int) Math.pow(2, Math.ceil((Math.log(playerCount) / Math.log(2))) -1); 
        int totalRounds = (int) ( Math.log(round1Matches) / Math.log(2) ); 
        int totalMatches = round1Matches + totalRounds;

        // Create a list to hold all matches indexed by their positions
        List<Match> matchList = new ArrayList<>(Collections.nCopies(totalMatches, null));
    
        // Create matches starting from the final match
        createMatchesRecursively(matchList, 0, totalRounds);
    
        // Assign players to first-round matches
        List<Player> playerList = new ArrayList<>(players);
    
        // Seed players and assign to matches
        assignPlayersToMatches(matchList, playerList);
    
        // Add matches to the tournament
        for (Match match : matchList) {
            addMatch(match);
        }
    }

    private void collectMatchesPerRound(Match match, Map<Integer, List<Match>> rounds) {
        if (match == null) {
            return;
        }

        // Add the match to the list for its round
        rounds.computeIfAbsent(match.getRoundNumber(), k -> new ArrayList<>()).add(match);

        // Recursively collect matches from child matches
        collectMatchesPerRound(match.getLeftChild(), rounds);
        collectMatchesPerRound(match.getRightChild(), rounds);
    }

    private void createMatchesRecursively(List<Match> matchList, int index, int roundsRemaining) {
        if (roundsRemaining == 0) {
            return;
        }
    
        Match match = new Match();
        match.setRoundNumber(roundsRemaining);
    
        matchList.set(index, match);
    
        // Left child
        int leftChildIndex = 2 * index + 1;
        createMatchesRecursively(matchList, leftChildIndex, roundsRemaining - 1);
        if (matchList.get(leftChildIndex) != null) {
            matchList.get(leftChildIndex).setParentMatch(match);
            match.getChildMatches().add(matchList.get(leftChildIndex));
        }
    
        // Right child
        int rightChildIndex = 2 * index + 2;
        createMatchesRecursively(matchList, rightChildIndex, roundsRemaining - 1);
        if (matchList.get(rightChildIndex) != null) {
            matchList.get(rightChildIndex).setParentMatch(match);
            match.getChildMatches().add(matchList.get(rightChildIndex));
        }
    }
    
    private void assignPlayersToMatches(List<Match> matchList, List<Player> playerList) {
        // Sort players by ELO rating
        playerList.sort(new EloComparator());

        // Determine number of seeds
        int numberOfSeeds = calculateNumberOfSeeds(playerList.size());

        // Assign seeds to predefined positions
        Map<Integer, Player> seededPositions = assignSeeds(playerList, numberOfSeeds, matchList);

        // Assign unseeded players to remaining matches
        assignUnseededPlayers(playerList, seededPositions, matchList);
    }

    private int calculateNumberOfSeeds(int playerCount) {
        // Implement your logic to calculate the number of seeds
        return playerCount / 4;
    }

    private Map<Integer, Player> assignSeeds(List<Player> playerList, int numberOfSeeds, List<Match> matchList) {
        Map<Integer, Player> seededPositions = new HashMap<>();

        // Example positions for seeds (adjust as needed)
        int[] seedPositions = {0, matchList.size() - 1, matchList.size() / 2 - 1, matchList.size() / 2};

        for (int i = 0; i < numberOfSeeds && i < seedPositions.length; i++) {
            int position = seedPositions[i];
            Player player = playerList.get(i);
            Match match = matchList.get(position);
            match.setPlayer1(player); // Or assign according to your logic
            seededPositions.put(position, player);
        }

        // Remove seeded players from the list
        playerList.subList(0, numberOfSeeds).clear();

        return seededPositions;
    }

    private void assignUnseededPlayers(List<Player> playerList, Map<Integer, Player> seededPositions, List<Match> matchList) {
        Random random = new Random();
        for (int i = 0; i < matchList.size(); i++) {
            if (!seededPositions.containsKey(i)) {
                Match match = matchList.get(i);
                if (match.getPlayer1() == null && !playerList.isEmpty()) {
                    int index = random.nextInt(playerList.size());
                    match.setPlayer1(playerList.remove(index));
                }
                if (match.getPlayer2() == null && !playerList.isEmpty()) {
                    int index = random.nextInt(playerList.size());
                    match.setPlayer2(playerList.remove(index));
                }
            }
        }
    }

    public Match recordMatchResult(Match match, Player winner, String result) {
        for (Match m : draw) {
            if (m.equals(match)) {
                m.setWinner(winner);
                return m;
            }
        }
        return null;
    }

    public String printDraw() {
        // Find the final match (match without a parent)
        Match finalMatch = findFinalMatch();
        if (finalMatch == null) {
            return "No matches to display, draw not out yet";
        }

        // Use a recursive method to collect matches per round
        Map<Integer, List<Match>> rounds = new TreeMap<>();
        collectMatchesPerRound(finalMatch, rounds);

        // Print matches round by round
        for (Map.Entry<Integer, List<Match>> entry : rounds.entrySet()) {
            int roundNumber = entry.getKey();
            List<Match> matches = entry.getValue();

            System.out.println("Round " + roundNumber + ":");
            for (Match match : matches) {
                String player1Name = (match.getPlayer1() != null) ? match.getPlayer1().getName() : "TBD";
                String player2Name = (match.getPlayer2() != null) ? match.getPlayer2().getName() : "TBD";
                System.out.println("  Match ID " + match.getId() + ": " + player1Name + " vs. " + player2Name);
            }
            System.out.println();
        }

        return "END OF DRAW";
    }

    private Match findFinalMatch() {
        for (Match match : draw) {
            if (match.getParentMatch() == null) {
                return match;
            }
        }
        return null;
    }

    //Returns next round match 
    public Match updateNextRound(Match match, Player winner, String result) {
        for (Match m : draw) {
            if (m.equals(match)) {
                Match nextRound = match.getParentMatch();
                if (nextRound.getLeftChild().equals(m)) {
                    nextRound.setPlayer1(winner);
                }
                if (nextRound.getRightChild().equals(m)) {
                    nextRound.setPlayer2(winner);  
                }
                return nextRound;
            }
        }
        return null;
    }

}

