package com.amateuraces.player;


public class Player{

    private @Id @GeneratedValue (strategy = GenerationType.IDENTITY) Long id;
    private String name;
    private String email;
    private String password;
    private String phoneNumber;
    private int age;
    private String gender;
    private int elo = 1500;  // Starting ELO
    private int matchesPlayed;
    private int matchesWon;

    // // Default Constructor
    public Player() {}

    // Updated Constructor with wins and losses
    public Player(String name, String gender, int age, String email, String password, String phoneNumber, int matchesPlayed, int matchesWon) {
        this.age = age;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.matchesPlayed = matchesPlayed;
        this.matchesWon = matchesWon;
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
}