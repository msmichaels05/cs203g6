package com.amateuraces.match;

import com.amateuraces.player.Player;

public class MatchResult {
    private Player winner;
    private String score;

    public MatchResult(Player winner, String score) {
        this.winner = winner;
        this.score = score;
    }

    public Player getWinner() {
        return winner;
    }

    public void setWinner(Player winner) {
        this.winner = winner;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }
}
