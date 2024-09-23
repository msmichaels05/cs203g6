package com.amateuraces.project.app;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;


@Entity
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String gender;
    private String email;
    private String password;
    private String phoneNumber;
    private int elo = 1500;  // Starting ELO
    private int wins = 0;
    private int losses = 0;

    // Default Constructor
    public Player() {}

    // Updated Constructor with wins and losses
    public Player(String name, String gender, String email, String password, String phoneNumber, int wins, int losses) {
        this.name = name;
        this.gender = gender;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.wins = wins;
        this.losses = losses;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getLosses() {
        return losses;
    }

    public void setLosses(int losses) {
        this.losses = losses;
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
            this.wins++;
        } else {
            this.losses++;
        }
    }
}
