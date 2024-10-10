package com.amateuraces.tournament;

import java.util.ArrayList;
import java.util.List;

import com.amateuraces.player.Player;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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
@Table(name = "tournaments") // Define the table name in the database
public class Tournament {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    // @Column(name = "registration_start_date", nullable = false)
    // private String registrationStartDate;

    // @Column(name = "registration_end_date", nullable = false)
    // private String registrationEndDate;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "tournament")
    private List<Player> players = new ArrayList<>();

    @Column(name = "requirements")
    private Long requirement;

    // @Column(name = "is_registration_open")
    // private boolean isRegistrationOpen = false;

    // @Column(name = "status")
    // private String status;

    public Tournament(String name, Long requirement) {
        this.name = name;
        this.requirement = requirement;
    }

    // Add player to the tournament (Later must add check if registration is open)
    public boolean addPlayer(Player player) {
        // if (isRegistrationOpen) {
            players.add(player);
            return true;
        // }
        // return false;
    }

    // // Check if the current date is within the registration period
    // public boolean checkRegistrationPeriod() {
    //     if (registrationStartDate == null || registrationEndDate == null || 
    //         registrationStartDate.isEmpty() || registrationEndDate.isEmpty()) {
    //         return false;
    //     }

    //     java.time.LocalDate currentDate = java.time.LocalDate.now();
    //     return (currentDate.isAfter(java.time.LocalDate.parse(registrationStartDate)) || 
    //             currentDate.isEqual(java.time.LocalDate.parse(registrationStartDate))) &&
    //            (currentDate.isBefore(java.time.LocalDate.parse(registrationEndDate)) || 
    //             currentDate.isEqual(java.time.LocalDate.parse(registrationEndDate)));
    // }

    // Get the count of registered players
    public int getRegisteredPlayerCount() {
        return players.size();
    }

    // Clear the list of players
    public void clearPlayers() {
        players.clear();
    }
}
