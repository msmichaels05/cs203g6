package com.amateuraces.match;

import java.util.Objects;

import com.amateuraces.player.Player;
import com.amateuraces.tournament.Tournament;
import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
    @JoinColumn(name = "tournament_id")
    @JsonBackReference
    private Tournament tournament;

    @ManyToOne
    @JoinColumn(name = "matchPlayer1")
    private Player player1;

    @ManyToOne
    @JoinColumn(name = "matchPlayer2")
    private Player player2;

    @ManyToOne
    @JoinColumn(name = "winner_id")
    private Player winner;

    @Column(name = "matchScore")
    private String score;
    
    private String status = "Scheduled";

    @ManyToOne
    @JoinColumn(name = "next_match_id")
    private Match nextMatch;

    public Match(Tournament tournament) {
        this.tournament = tournament;
    }

    public Match(Tournament tournament, Player player1, Player player2) {
        this.tournament = tournament;
        this.player1 = player1;
        this.player2 = player2;
    }

    public Match(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
    }

    public boolean isPlayerInvolved(Player player) {
        return Objects.equals(player1, player.getId()) || Objects.equals(player2, player.getId());
    }
}
