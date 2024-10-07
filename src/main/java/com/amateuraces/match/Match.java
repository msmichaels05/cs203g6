package com.amateuraces.match;

import java.time.LocalDateTime;
import java.util.regex.MatchResult;

import com.amateuraces.player.Player;

public class Match {

    private Long matchId;
    private Tournament tournament;
    private Integer round;
    private Player player1;
    private Player player2;
    private Player winner; // Null if the match is still ongoing
    private String status; // E.g., "ongoing", "completed"
    private LocalDateTime scheduledDateTime;

    // Constructors
    public Match() {
    }

    public Match(Long matchId, Tournament tournament, Integer round, Player player1, Player player2,
            LocalDateTime scheduledDateTime) {
        this.matchId = matchId;
        this.tournament = tournament;
        this.round = round;
        this.player1 = player1;
        this.player2 = player2;
        this.scheduledDateTime = scheduledDateTime;
        this.status = "ongoing";
    }

    // Getters and Setters
    public Long getMatchId() {
        return matchId;
    }

    public void setMatchId(Long matchId) {
        this.matchId = matchId;
    }

    public Tournament getTournament() {
        return tournament;
    }

    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }

    public Integer getRound() {
        return round;
    }

    public void setRound(Integer round) {
        this.round = round;
    }

    public Player getPlayer1() {
        return player1;
    }

    public void setPlayer1(Player player1) {
        this.player1 = player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public void setPlayer2(Player player2) {
        this.player2 = player2;
    }

    public LocalDateTime getScheduledDateTime() {
        return scheduledDateTime;
    }

    public void setScheduledDateTime(LocalDateTime scheduledDateTime) {
        this.scheduledDateTime = scheduledDateTime;
    }

    public Player getWinner() {
        return winner;
    }

    public void setWinner(Player winner) {
        this.winner = winner;
        this.status = "completed"; // Update status once a winner is set
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void recordResult(MatchResult result) {
        this.setWinner(result.getWinner()); // Assuming MatchResult has a getWinner() method
        this.status = "completed";
        if (winner.equals(player1)) {
            player1.updateElo(player2, true);   // player1 won
            player2.updateElo(player1, false);  // player2 lost
        } else {
            player1.updateElo(player2, false);  // player1 lost
            player2.updateElo(player1, true);   // player2 won
        }
    }

    public void rescheduleMatch(LocalDateTime newDateTime) {
        this.scheduledDateTime = newDateTime;
        this.status = "rescheduled"; // set a new status
    }

    public void cancelMatch() {
        this.status = "cancelled";
    }
}