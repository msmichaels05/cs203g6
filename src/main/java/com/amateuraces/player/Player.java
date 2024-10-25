package com.amateuraces.player;

import java.util.Set;
import java.util.HashSet;
//import org.hibernate.mapping.Set;

import com.amateuraces.tournament.Tournament;

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
// import lombok.AllArgsConstructor;
// import lombok.EqualsAndHashCode;
// import lombok.Getter;
// import lombok.NoArgsConstructor;
// import lombok.Setter;
// import lombok.ToString;


@Entity
// @Getter
// @Setter
// @ToString
// @AllArgsConstructor
// @NoArgsConstructor
// @EqualsAndHashCode

public class Player {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 1, max = 100)
    private String name;

    @Size(min = 1, max = 15)
    private String phoneNumber;

    private int age;
    private String gender;
    private int elo = 1500;  // Starting ELO
    private int matchesPlayed;
    private int matchesWon;

    @ManyToOne
    @JoinColumn(name = "tournament_id")
    private Tournament tournament;

    @ManyToMany
    @JoinTable(
        name = "player_tournament_history", // Name of the join table
        joinColumns = @JoinColumn(name = "player_id"), // Foreign key for player
        inverseJoinColumns = @JoinColumn(name = "tournament_id") // Foreign key for tournament
    )
    private Set<Tournament> tournamentHistory = new HashSet<>();

    // Constructor with wins and losses
    public Player(String name, String gender, int age, String email, String password, String phoneNumber, int matchesPlayed, int matchesWon) {
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

    public int getElo() {return elo;}

    // Method to update ELO based on match result
    public void updateElo(int opponentElo, boolean hasWon) {
        int kFactor = 32;  // This could be adjusted based on your ranking system
        double expectedScore = 1 / (1 + Math.pow(10, (opponentElo - this.elo) / 400.0));
        int score = hasWon ? 1 : 0;
        this.elo += kFactor * (score - expectedScore);
    }

    // Method to update wins and losses
    public void updateWinsAndLosses(boolean hasWon) {
        this.matchesPlayed++;
        if (hasWon) {
            this.matchesWon++;
        }
    }

    // Get losses (derived from matches played and matches won)
    public int getLosses() {
        return this.matchesPlayed - this.matchesWon;
    }

    public void addToTournamentHistory(Tournament tournament) {
        tournamentHistory.add(tournament);
    }

    public String getName() {
        return name;
    }

    public void setName(String newname) {
        this.name = newname;
    }
}
// >>>>>>> 4de22c8975a822eef95aef27a91cb5f4b2b102a0
