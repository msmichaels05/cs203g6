package com.amateuraces.match;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import com.amateuraces.player.Player;
import com.amateuraces.tournament.Tournament;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long tournamentId;

    private Player player1;
    private Player player2;

    private String result; // Could store values like "Player1 wins", "Player2 wins", or "Draw"

    public Match() {}

    public Match(Tournament tournament, Player player1, Player player2) {
        this.tournamentId = tournament.getId();
        this.player1 = player1;
        this.player2 = player2;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getResult() {
        return result;
    }

    // Optionally, you could add logic for determining a winner and updating ELO scores
    public void determineWinner() {
        // Add logic to determine the winner and update result
    }
}
