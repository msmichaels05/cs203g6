package com.amateuraces.match;

import java.util.Objects;

import com.amateuraces.player.Player;
import com.amateuraces.tournament.*;

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
    @JoinColumn(name = "winner")
    private Player winner;

    @OneToOne
    @JoinColumn(name = "loser")
    private Player loser;

    @Column(name = "matchScore")
    private String score;

    @Column(name = "changedElo")
    private double elo;

    @Column(name = "isCompleted", nullable = false)
    private boolean isCompleted = false;

    public Match(Tournament tournament, Player player1, Player player2) {
        this.tournament = tournament;
        this.player1 = player1;
        this.player2 = player2;
    }

    public Match(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
    }

    public void setMatchResult(Player winner, Player loser, String score) {
        this.winner = winner;
        this.loser = loser;
        this.score = score;
        this.isCompleted = true;
    }

    public boolean isPlayerInvolved(Player player) {
        return Objects.equals(player1, player.getId()) || Objects.equals(player2, player.getId());
    }
}