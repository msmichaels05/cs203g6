package com.amateuraces.tournament;

import com.amateuraces.player.Player;

import java.util.ArrayList;
import java.util.List;

public class Tournament {
    private Long id;
    private String name;
    private String registrationStartDate;  // Separate Start Date
    private String registrationEndDate;    // Separate End Date
    private List<Player> players = new ArrayList<>();  // Players who have registered
    private String requirements;  // E.g., Age, ELO minimum, etc.
    private boolean isRegistrationOpen = false;  // Boolean to check if registration is currently open

    // Default Constructor
    public Tournament() {}

    // Parameterized Constructor
    public Tournament(Long id, String name, String registrationStartDate, String registrationEndDate, List<Player> players, String requirements) {
        this.id = id;
        this.name = name;
        this.registrationStartDate = registrationStartDate;
        this.registrationEndDate = registrationEndDate;
        this.players = players != null ? players : new ArrayList<>();
        this.requirements = requirements;
        this.isRegistrationOpen = checkRegistrationPeriod(); // Update the registration status
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
        this.isRegistrationOpen = checkRegistrationPeriod(); // Update registration status
    }

    public String getRegistrationEndDate() {
        return registrationEndDate;
    }

    public void setRegistrationEndDate(String registrationEndDate) {
        this.registrationEndDate = registrationEndDate;
        this.isRegistrationOpen = checkRegistrationPeriod(); // Update registration status
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

    // Additional Methods

    /**
     * Add a player to the tournament's player list if they meet the requirements.
     * @param player the player to be added
     * @return true if the player was added, false if not
     */
    public boolean addPlayer(Player player) {
        if (isRegistrationOpen) {
            players.add(player);
            return true;
        }
        return false;
    }

    /**
     * Check if the current date is within the registration period.
     * @return true if registration is open, false otherwise
     */
    public boolean checkRegistrationPeriod() {
        // Assume the start and end dates are in the format "YYYY-MM-DD"
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

    /**
     * Get the number of players registered for the tournament.
     * @return the number of players
     */
    public int getRegisteredPlayerCount() {
        return players.size();
    }

    /**
     * Clear all players from the tournament (useful for resetting tournaments).
     */
    public void clearPlayers() {
        players.clear();
    }
}
