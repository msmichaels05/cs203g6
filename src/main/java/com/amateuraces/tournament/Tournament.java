package com.amateuraces.tournament;

import com.amateuraces.player.Player;

import java.util.ArrayList;
import java.util.List;

public class Tournament {
    private Long id;
    private String name;
    private String registrationStartDate;
    private String registrationEndDate;
    private List<Player> players = new ArrayList<>();
    private String requirements;
    private boolean isRegistrationOpen = false;
    private String status;  // Add a status field to the tournament class

    // Default Constructor
    public Tournament() {}

    // Parameterized Constructor
    public Tournament(Long id, String name, String registrationStartDate, String registrationEndDate, List<Player> players, String requirements, String status) {
        this.id = id;
        this.name = name;
        this.registrationStartDate = registrationStartDate;
        this.registrationEndDate = registrationEndDate;
        this.players = players != null ? players : new ArrayList<>();
        this.requirements = requirements;
        this.status = status;  // Initialize the status field
        this.isRegistrationOpen = checkRegistrationPeriod(); // Update registration status
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

    public String getRegistrationStartDate() {
        return registrationStartDate;
    }

    public void setRegistrationStartDate(String registrationStartDate) {
        this.registrationStartDate = registrationStartDate;
        this.isRegistrationOpen = checkRegistrationPeriod();
    }

    public String getRegistrationEndDate() {
        return registrationEndDate;
    }

    public void setRegistrationEndDate(String registrationEndDate) {
        this.registrationEndDate = registrationEndDate;
        this.isRegistrationOpen = checkRegistrationPeriod();
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public String getRequirements() {
        return requirements;
    }

    public void setRequirements(String requirements) {
        this.requirements = requirements;
    }

    public boolean isRegistrationOpen() {
        return isRegistrationOpen;
    }

    public String getStatus() {
        return status;
    }

    // New setter for status
    public void setStatus(String status) {
        this.status = status;  // Set the status of the tournament
    }

    // Additional Methods
    public boolean addPlayer(Player player) {
        if (isRegistrationOpen) {
            players.add(player);
            return true;
        }
        return false;
    }

    public boolean checkRegistrationPeriod() {
        if (registrationStartDate == null || registrationEndDate == null || 
            registrationStartDate.isEmpty() || registrationEndDate.isEmpty()) {
            return false;
        }

        java.time.LocalDate currentDate = java.time.LocalDate.now();
        return (currentDate.isAfter(java.time.LocalDate.parse(registrationStartDate)) || 
                currentDate.isEqual(java.time.LocalDate.parse(registrationStartDate))) &&
               (currentDate.isBefore(java.time.LocalDate.parse(registrationEndDate)) || 
                currentDate.isEqual(java.time.LocalDate.parse(registrationEndDate)));
    }

    public int getRegisteredPlayerCount() {
        return players.size();
    }

    public void clearPlayers() {
        players.clear();
    }
}
