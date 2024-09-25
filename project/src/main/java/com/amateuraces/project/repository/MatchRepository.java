package com.amateuraces.project.repository;
import com.amateuraces.project.entity.Match;

public interface MatchRepository {
    void save(Match match);
    Match findById(String id);
    // Other CRUD methods
}