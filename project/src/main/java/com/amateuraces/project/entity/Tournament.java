package com.amateuraces.project.entity;

import java.util.Date;
import java.util.List;

public class Tournament {
    private String id;
    private String name;
    private String location;
    private Date registrationStartDate;
    private Date registrationEndDate;
    private Date startDate;
    private Date endDate;
    private int minEloRequirement;
    private int maxParticipants;
    private List<Player> participants;
    private List<Match> matches;
    // private TournamentStatus status;
    private double prizeMoney;

    // Constructors, Getters, Setters
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public Date getRegistrationStartDate() {
        return registrationStartDate;
    }
    public void setRegistrationStartDate(Date registrationStartDate) {
        this.registrationStartDate = registrationStartDate;
    }
    public Date getRegistrationEndDate() {
        return registrationEndDate;
    }
    public void setRegistrationEndDate(Date registrationEndDate) {
        this.registrationEndDate = registrationEndDate;
    }
    public Date getStartDate() {
        return startDate;
    }
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    public Date getEndDate() {
        return endDate;
    }
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    public int getMinEloRequirement() {
        return minEloRequirement;
    }
    public void setMinEloRequirement(int minEloRequirement) {
        this.minEloRequirement = minEloRequirement;
    }
    public int getMaxParticipants() {
        return maxParticipants;
    }
    public void setMaxParticipants(int maxParticipants) {
        this.maxParticipants = maxParticipants;
    }
    public List<Player> getParticipants() {
        return participants;
    }
    public void setParticipants(List<Player> participants) {
        this.participants = participants;
    }
    public List<Match> getMatches() {
        return matches;
    }
    public void setMatches(List<Match> matches) {
        this.matches = matches;
    }
    // public TournamentStatus getStatus() {
    //     return status;
    // }
    // public void setStatus(TournamentStatus status) {
    //     this.status = status;
    // }
    public double getPrizeMoney() {
        return prizeMoney;
    }
    public void setPrizeMoney(double prizeMoney) {
        this.prizeMoney = prizeMoney;
    }

    public void registerPlayer(Player player) {}
    public void generateBrackets() {}
    public void startTournament(){}
    public void endTournament(){}
    public double calculateAverageElo(){
        return 0.0;
    }
}