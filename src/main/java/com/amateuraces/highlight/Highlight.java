package com.amateuraces.highlight;

import com.amateuraces.player.Player;
import com.amateuraces.tournament.Tournament;
import com.fasterxml.jackson.databind.deser.DataFormatReaders.Match;

public class Highlight {
    private Long month;
    private Long year;
    private Tournament tournamentOfTheMonth;
    private Player playerOfTheMonth;
    private Player mostImprovedPlayer;
    private Match highestScoringMatch;

    public Highlight(Long year, Long month) {
        this.month = month;
        this.year = year;
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