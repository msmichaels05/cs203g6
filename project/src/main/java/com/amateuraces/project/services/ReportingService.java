package com.amateuraces.project.services;

public interface ReportingService {
    void generatePlayerReport(String playerId);
    void generateTournamentReport(String tournamentId);
    void generateMonthlyHighlights(int month, int year);
}