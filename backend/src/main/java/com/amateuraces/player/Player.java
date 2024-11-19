package com.amateuraces.player;

import java.util.*;

import com.amateuraces.tournament.Tournament;
import com.amateuraces.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
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
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Player implements Comparable<Player>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Name cannot be empty")
    @Size(min = 1, max = 100, message = "Name must be 1 to 100 characters")
    private String name;

    @NotNull(message = "Phone number cannot be empty")
    @Size(min = 8, max = 16, message = "Phone number should be 8-16 numbers")
    private String phoneNumber;

    @NotNull(message = "Age cannot be empty")
    private int age;

    @NotNull(message = "Gender cannot be empty")
    @Size(max = 10, message = "Gender cannot be more than 10 characters")
    private String gender;

    private int elo = 1500; // Starting ELO
    private int matchesPlayed = 0;
    private int matchesWon = 0;

    @ManyToMany(mappedBy = "players", cascade = CascadeType.PERSIST)
    @JsonIgnore
    private Set<Tournament> tournaments = new HashSet<>();

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Single champion Player for a Tournament
    @OneToMany(mappedBy = "champion", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Tournament> tournamentsWon;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Player))
            return false;
        Player player = (Player) o;
        return id != null && id.equals(player.id); // Compare IDs
    }

    @Override
    public int hashCode() {
        return Objects.hash(id); // Generate hash code based on ID
    }

    // Constructor with wins and losses
    public Player(String name, String gender, int age, String phoneNumber, int matchesPlayed, int matchesWon) {
        this.age = age;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.matchesPlayed = matchesPlayed;
        this.matchesWon = matchesWon;
        this.name = name;
    }

    public Player(String name) {
        this.name = name;
    }

    // Get losses (derived from matches played and matches won)
    public int getMatchesLost() {
        return this.matchesPlayed - this.matchesWon;
    }

    public void removeFromTournamentHistory(Tournament tournament) {
        tournaments.remove(tournament);
    }

    @Override
    public int compareTo(Player otherPlayer) {
        return Integer.compare(otherPlayer.getElo(), getElo());
    }
}