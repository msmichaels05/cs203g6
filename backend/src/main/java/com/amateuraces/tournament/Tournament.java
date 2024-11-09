package com.amateuraces.tournament;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
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

    // The champion player of a tournament
    @ManyToOne
    @JoinColumn(name = "player_id")
    private Player championPlayer;

    @ManyToMany
    @JoinTable(
        name = "tournament_players",
        joinColumns = @JoinColumn(name = "tournament_id"),
        inverseJoinColumns = @JoinColumn(name = "player_id")
    )
    @JsonIgnore
    private Set<Player> players = new HashSet<>();

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

}

