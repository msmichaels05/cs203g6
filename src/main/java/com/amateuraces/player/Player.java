package com.amateuraces.player;

import com.amateuraces.tournament.Tournament;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import com.amateuraces.user.*;


@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Player {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 1, max = 100)
    private String name;

    @Size(min = 1, max = 15)
    private String phoneNumber;

    private String email;
    private int age;
    private String gender;
    private int elo = 1500;  // Starting ELO
    private int matchesPlayed;
    private int matchesWon;

    @ManyToOne
    @JoinColumn(name = "tournament_id")
    private Tournament tournament;

    @OneToOne
    @MapsId
    @JoinColumn(name="user_id",nullable = false)
    private User user;

    // Constructor with wins and losses
    public Player(String name, String gender, int age, String email, String phoneNumber, int matchesPlayed, int matchesWon) {
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
}
