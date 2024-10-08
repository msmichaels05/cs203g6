package com.amateuraces.highlight;

import com.amateuraces.player.Player;
import com.fasterxml.jackson.databind.deser.DataFormatReaders.Match;

public class Highlight {
    private Long month;
    private Long year;
    // private Tournament tournamentOfTheMonth;
    private String tournamentOfTheMonth;
    private Player playerOfTheMonth;
    private Player mostImprovedPlayer;
    private Match highestScoringMatch;

    public Highlight() {

    }
    
    public Highlight(Long year, Long month) {
        this.year = year;
        this.month = month;
        tournamentOfTheMonth = null;
    }

    public Highlight(Long year, Long month, String tournamentOfTheMonth) {
        this.year = year;
        this.month = month;
        this.tournamentOfTheMonth = tournamentOfTheMonth;
    }

    public Long getMonth() {
        return month;
    }

    public void setMonth(Long month) {
        this.month = month;
    }

    public Long getYear() {
        return year;
    }

    public void setYear(Long year) {
        this.year = year;
    }

    public String getTournamentOfTheMonth() {
        return tournamentOfTheMonth;
    }

    public void setTournamentOfTheMonth(String tournamentOfTheMonth) {
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