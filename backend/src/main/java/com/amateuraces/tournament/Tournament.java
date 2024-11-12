package com.amateuraces.tournament;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.amateuraces.match.Match;
import com.amateuraces.player.Player;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

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

    @OneToMany(mappedBy = "tournament", 
        orphanRemoval = true,
        cascade = CascadeType.ALL)
    @JsonManagedReference
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

    public void removePlayer(Player player) {
        if (players.remove(player)) {
            player.removeFromTournamentHistory(this); // Also remove this tournament from the player's list
            playerCount--;
        }
    }
}