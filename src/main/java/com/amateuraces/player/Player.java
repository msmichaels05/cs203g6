package com.amateuraces.player;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;


@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Player{
    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private  Long id;

   
    @Size(min = 1, max = 15)
    private String phoneNumber;
  
    private int age;
  
    private String gender;
    private int elo = 1500;  // Starting ELO
    private int matchesPlayed;
    private int matchesWon;

    @NotNull
    @Size(min = 1, max = 100)
    private String name;

    // Updated Constructor with wins and losses
    public Player( String name, String gender, int age, String email, String password, String phoneNumber, int matchesPlayed, int matchesWon) {
        this.age = age;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.matchesPlayed = matchesPlayed;
        this.matchesWon = matchesWon;
        this.name = name;
        // this.elo = elo;
    }

    public Player( String name) {
        this.name = name;
    }

     // Method to update the wins and losses
     public void updateWinsAndLosses(boolean hasWon) {
        this.matchesPlayed += 1; // Increment matches played

        if (hasWon) {
            this.matchesWon += 1; // Increment matches won if player has won
        }
    }

    // Method to update ELO based on opponent's ELO and result
    public void updateElo(int opponentElo, boolean hasWon) {
        // Elo updating logic here (for example)
        if (hasWon) {
            this.elo += 10; // Example logic: Increase ELO if player won
        } else {
            this.elo -= 10; // Example logic: Decrease ELO if player lost
        }
    }

}