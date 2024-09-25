package com.amateuraces.project.services;

import com.amateuraces.project.entity.Match;
// import com.amateuraces.project.dtos.MatchResultDTO;

public interface MatchService {
    Match getMatchById(String matchId);
    // void recordMatchResult(MatchResultDTO resultDTO);
    void rescheduleMatch(String matchId, String newDate);
    void cancelMatch(String matchId);
}