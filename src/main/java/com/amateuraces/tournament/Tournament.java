package com.amateuraces.tournament;

import java.time.LocalDate;
import java.util.*;

import com.amateuraces.match.*;
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
    @NotNull
    private int maxPlayers;

    private int playerCount = 0;
    @NotNull
    private LocalDate startDate;
    @NotNull
    private LocalDate endDate;
    @NotNull
    private String gender;

    private LocalDate registrationEndDate;

    private String location;

    private String description;

    @OneToMany(mappedBy = "tournament", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Match> matches = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "tournament_players",
        joinColumns = @JoinColumn(name = "tournament_id"),
        inverseJoinColumns = @JoinColumn(name = "player_id")
    )
    @JsonIgnore
    private Set<Player> players = new HashSet<>();

        // Implement equals and hashCode
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Tournament)) return false;
            Tournament tournament = (Tournament) o;
            return Objects.equals(id, tournament.id); // Compare based on 'id'
        }
    
        @Override
        public int hashCode() {
            return Objects.hash(id); // Hash based on 'id'
        }

    // Custom setter to enforce even maxPlayers
    public void setMaxPlayers(int maxPlayers) {
        if (maxPlayers < 0) {
            throw new IllegalArgumentException("max players cannot be negative.");
        }
        if (maxPlayers >64){
            throw new IllegalArgumentException("max players cannot be more than 64");
        }
        if (!(isPowerOfTwo(maxPlayers))) {
            throw new IllegalArgumentException("max players must be a power of 2.");
        }
        this.maxPlayers = maxPlayers;
    }

    private boolean isPowerOfTwo(int maxPlayers) {
        double temp = (double) maxPlayers;
        while (temp >= 4) {
            temp /= 2;
        }
        if (temp % 2 != 0) return false;
        return true;
    }


    public Tournament(String name,String gender, int maxPlayers, String location, int ELOrequirement, String description,
        LocalDate startDate, LocalDate endDate) {
        this.ELOrequirement = ELOrequirement;
        this.gender = gender;
        this.name = name;
        this.maxPlayers = maxPlayers;
        this.location = location;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Tournament(String name) {
        this.name = name;
    }

    public Tournament(String name,String location){
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

    public void addMatch(Match match) {
        matches.add(match);
        match.setTournament(this); // Ensure the bidirectional relationship is maintained
    }
    
    public void removeMatch(Match match) {
        matches.remove(match);
        match.setTournament(null); // Remove the tournament reference from the match
    }

    // // Get the count of registered players
    // public int getRegisteredPlayerCount() {
    //     return players.size();
    // }

    // // Clear the list of players
    // public void clearPlayers() {
    //     players.clear();
    // }
}

