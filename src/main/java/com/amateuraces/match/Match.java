package com.amateuraces.match;

import com.amateuraces.player.Player;
import com.amateuraces.tournament.Tournament;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
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
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tournamentName")
    private Tournament tournament;

    @OneToOne
    @JoinColumn(name = "matchPlayer1")
    private Player player1;

    @OneToOne
    @JoinColumn(name = "matchPlayer2")
    private Player player2;

    @OneToOne
    @JoinColumn(name = "matchWinner")
    private Player winner;

    @Column(name = "matchScore")
    private String score;

    public Match(Tournament tournament, Player player1, Player player2) {
        this.tournament = tournament;
        this.player1 = player1;
        this.player2 = player2;
    }

    public Match(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
    }

    public Tournament getTournament() {
        return tournament;
    }

    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }

     // Method to update ELO based on match result
     public void updateElo(int opponentElo, boolean hasWon) {
        int kFactor = 32;  // This could be adjusted based on your ranking system
        double expectedScore = 1 / (1 + Math.pow(10, (opponentElo - this.elo) / 400.0));
        int score = hasWon ? 1 : 0;
        this.elo += kFactor * (score - expectedScore);
    }
}