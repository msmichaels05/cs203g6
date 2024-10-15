package com.amateuraces.tournament;

import java.util.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import com.amateuraces.player.*;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Tournament {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 1, max = 100)
    private String name;

    private int ELOrequirement;

    @ManyToMany
    @JoinTable(
        name = "tournament_player",
        joinColumns = @JoinColumn(name = "tournament_id"),
        inverseJoinColumns = @JoinColumn(name = "player_id")
    )
    @JsonIgnore
    private List<Player> players = new ArrayList<>();

    // Constructor with wins and losses
    public Tournament(String name, int ELOrequirement) {
        this.ELOrequirement = ELOrequirement;
        this.name = name;
    }

    public boolean addPlayer(Player player){
        players.add(player);
        return true;
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

