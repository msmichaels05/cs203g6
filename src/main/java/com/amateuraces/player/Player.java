<<<<<<< HEAD
<<<<<<< HEAD
package com.amateuraces.player;

import com.amateuraces.tournament.Tournament;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tournament_id")
    private Tournament tournament;

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
=======
package com.amateuraces.player;


public class Player{

    private String phoneNumber;
    private int age;
    private String gender;
    private int elo = 1500;  // Starting ELO
    private int matchesPlayed;
    private int matchesWon;
    private String name;
    private Long id; 

    // // Default Constructor
    public Player() {}

    public Player(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    // Updated Constructor with wins and losses
    public Player(Long id, String name, String gender, int age, String email, String password, String phoneNumber, int matchesPlayed, int matchesWon) {
        this.age = age;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.matchesPlayed = matchesPlayed;
        this.matchesWon = matchesWon;
        this.name = name;
        this.id = id;
        // this.elo = elo;
    }

    // Getters and Setters

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getWins() {
        return matchesWon;
    }

    public void setWins(int matchesWon) {
        this.matchesWon = matchesWon;
    }

    public int getLosses() {
        return matchesPlayed - matchesWon;
    }

    // Methods for updating ELO, wins, and losses
    public void updateElo(int opponentElo, boolean hasWon) {
        int K = 32;
        double expectedScore = 1 / (1 + Math.pow(10, (opponentElo - this.elo) / 400.0));
        if (hasWon) {
            this.elo += (int) (K * (1 - expectedScore));
        } else {
            this.elo += (int) (K * (0 - expectedScore));
        }
    }

    public void updateWinsAndLosses(boolean hasWon) {
        if (hasWon) {
            this.matchesWon++;
        }
        matchesPlayed++;
    }


    public String getName() {
        return name;
    }

    public void setName(String newName) {
        name = newName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
>>>>>>> 2a2ff68 (Updated playerservice & playercontroller)
=======
package com.amateuraces.player;

import com.amateuraces.tournament.Tournament;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
>>>>>>> 4de22c8975a822eef95aef27a91cb5f4b2b102a0
