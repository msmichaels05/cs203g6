package com.amateuraces.project.entity;

import java.util.Date;

public class Match {
    private String id;
    private Tournament tournament;
    private int round;
    private Player player1;
    private Player player2;
    private Date scheduledDate;
    private Date actualDate;
    private String venue;
    private MatchResult result;

    // Constructors, Getters, Setters
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public Tournament getTournament() {
        return tournament;
    }
    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }
    public int getRound() {
        return round;
    }
    public void setRound(int round) {
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
    public Date getScheduledDate() {
        return scheduledDate;
    }
    public void setScheduledDate(Date scheduledDate) {
        this.scheduledDate = scheduledDate;
    }
    public Date getActualDate() {
        return actualDate;
    }
    public void setActualDate(Date actualDate) {
        this.actualDate = actualDate;
    }
    public String getVenue() {
        return venue;
    }
    public void setVenue(String venue) {
        this.venue = venue;
    }
    
    public MatchResult getResult() {
        return result;
    }
    public void setResult(MatchResult result) {
        this.result = result;
    }

    public void recordResult(MatchResult result) {}
    public void rescheduleMatch(Date newDate) {}
    public void updatePlayerElos(){}
    public void cancelMatch(){}
}