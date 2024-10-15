package com.amateuraces.player;

import com.amateuraces.tournament.Tournament;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import com.amateuraces.user.*;
import java.util.List;


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

    @NotNull(message = "Name cannot be empty")
    @Size(min = 1, max = 100 , message = "Name must be 1 to 100 characters")
    private String name;

    @NotNull(message = "Phone number cannot be empty")
    @Size(min=8,max = 8, message = "Phone number must be a valid number")
    private Long phoneNumber;

    @NotNull(message="Email cannot be empty")
    @Size(max = 30, message= "Email cannot be more than 30 characters")
    private String email;

    @NotNull(message = "Age cannot be empty")
    private int age;

    @NotNull(message = "Gender cannot be empty")
    @Size(max=10,message = "Gender cannot be more than 10 characters")
    private String gender;

    private int elo = 1500;  // Starting ELO
    private int matchesPlayed;
    private int matchesWon;

    @ManyToMany(mappedBy = "player", cascade = CascadeType.ALL)
    private List<Tournament> tournaments;

    @OneToOne
    @MapsId
    @JoinColumn(name="user_id",nullable = false)
    private User user;

    // Constructor with wins and losses
    public Player(String name, String gender, int age, String email, Long phoneNumber, int matchesPlayed, int matchesWon) {
        this.age = age;
        this.gender = gender;
        this.email = email;
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
