package com.amateuraces.match;

import java.util.Objects;

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
    private Long player1Id;

    @OneToOne
    @JoinColumn(name = "matchPlayer2")
    private Long player2Id;

    @OneToOne
    @JoinColumn(name = "matchWinner")
    private Long winner;

    @OneToOne
    @JoinColumn(name = "matchLoser")
    private Long loser;

    @Column(name = "matchScore")
    private String score;

    @Column(name = "changedElo")
    private double elo;

    @Column(name = "isCompleted", nullable = false)
    private boolean isCompleted = false;

    public Match(Tournament tournament, Long player1Id, Long player2Id) {
        this.tournament = tournament;
        this.player1Id = player1Id;
        this.player2Id = player2Id;
    }

    public Match(Long player1Id, Long player2Id) {
        this.player1Id = player1Id;
        this.player2Id = player2Id;
    }

    public Match(Tournament tournament, Long winner, Long loser, String score) {
        this.tournament = tournament;
        this.winner = winner;
        this.loser = loser;
        this.score = score;
    }

    public void setMatchResult(Long winner, Long loser, String score) {
        this.winner = winner;
        this.loser = loser;
        this.score = score;
        this.isCompleted = true;
    }

    public boolean isPlayerInvolved(Player player) {
        return (Objects.equals(player1Id, player.getId()) || Objects.equals(player2Id, player.getId()));
    }
}