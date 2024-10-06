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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}