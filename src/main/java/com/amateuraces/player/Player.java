package com.amateuraces.player;

import java.util.List;

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

    @ManyToMany(mappedBy = "players", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Tournament> tournaments;

    @OneToOne(cascade = CascadeType.REMOVE)
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
