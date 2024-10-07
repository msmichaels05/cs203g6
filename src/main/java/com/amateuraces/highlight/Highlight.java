package com.amateuraces.highlight;

import com.amateuraces.player.Player;
import com.fasterxml.jackson.databind.deser.DataFormatReaders.Match;

public class Highlight {
    private int month;
    private int year;
    private Tournament tournamentOfTheMonth;
    private Player playerOfTheMonth;
    private Player mostImprovedPlayer;
    private Match highestScoringMatch;

    public Highlight(int month, int year) {
        this.month = month;
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Tournament getTournamentOfTheMonth() {
        return tournamentOfTheMonth;
    }

    public void setTournamentOfTheMonth(Tournament tournamentOfTheMonth) {
        this.tournamentOfTheMonth = tournamentOfTheMonth;
    }

    public Player getPlayerOfTheMonth() {
        return playerOfTheMonth;
    }

    public void setPlayerOfTheMonth(Player playerOfTheMonth) {
        this.playerOfTheMonth = playerOfTheMonth;
    }

    public Player getMostImprovedPlayer() {
        return mostImprovedPlayer;
    }

    public void setMostImprovedPlayer(Player mostImprovedPlayer) {
        this.mostImprovedPlayer = mostImprovedPlayer;
    }

    public Match getHighestScoringMatch() {
        return highestScoringMatch;
    }

    public void setHighestScoringMatch(Match highestScoringMatch) {
        this.highestScoringMatch = highestScoringMatch;
    }
}