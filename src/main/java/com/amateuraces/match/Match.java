package com.amateuraces.match;
import com.amateuraces.player.Player;

public class Match {

    private Long matchId;
    private Player player1;
    private Player player2;
    private Player winner;  // Null if the match is still ongoing
    private String status;  // E.g., "ongoing", "completed"

    // Constructors
    public Match() {}

    public Match(Long matchId, Player player1, Player player2) {
        this.matchId = matchId;
        this.player1 = player1;
        this.player2 = player2;
        this.status = "ongoing";
    }

    // Getters and Setters
    public Long getMatchId() {
        return matchId;
    }

    public void setMatchId(Long matchId) {
        this.matchId = matchId;
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
}
