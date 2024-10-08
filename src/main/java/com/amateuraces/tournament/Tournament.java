package com.amateuraces.tournament;

import com.amateuraces.player.Player;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tournaments") // Define the table name in the database
public class Tournament {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "registration_start_date", nullable = false)
    private String registrationStartDate;

    @Column(name = "registration_end_date", nullable = false)
    private String registrationEndDate;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "tournament_id")
    private List<Player> players = new ArrayList<>();

    @Column(name = "requirements")
    private String requirements;

    @Column(name = "is_registration_open")
    private boolean isRegistrationOpen = false;

    @Column(name = "status")
    private String status;

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
        this.status = status;
        this.isRegistrationOpen = checkRegistrationPeriod();
    }

    // Getters and Setters...

    // Additional methods...

    // Getters and setters
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

    public void setStatus(String status) {
        this.status = status;
    }

    // Additional Methods...

    // Add player to the tournament if registration is open
    public boolean addPlayer(Player player) {
        if (isRegistrationOpen) {
            players.add(player);
            return true;
        }
        return false;
    }

    // Check if the current date is within the registration period
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

    // Get the count of registered players
    public int getRegisteredPlayerCount() {
        return players.size();
    }

    // Clear the list of players
    public void clearPlayers() {
        players.clear();
    }
}
