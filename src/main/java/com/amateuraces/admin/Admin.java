package com.amateuraces.admin;

import com.amateuraces.player.Player;
import jakarta.persistence.*;

@Entity
@Table(name = "admins") // Optional: customize the table name
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-generate ID
    private Long id;

    @Column(nullable = false) // Name is required
    private String name;

    @Column(nullable = false, unique = true) // Email is required and must be unique
    private String email;

    @Column(nullable = false) // Password is required
    private String password;

    private String phoneNumber;

    private int age;

    private String gender;

    private int matchesPlayed;

    private int matchesWon;

    // Default Constructor
    public Admin() {}

    // Constructor with mandatory fields
    public Admin(Long id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // Methods for updating Player ELO and matches
    public void updatePlayerElo(Player player, int opponentElo, boolean hasWon) {
        player.updateElo(opponentElo, hasWon);
    }

    public void updatePlayerWinsAndLosses(Player player, boolean hasWon) {
        player.updateWinsAndLosses(hasWon);
    }

    // Email and password setters and getters
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
}
