package com.amateuraces.tournament;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

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
}

