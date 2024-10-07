package com.amateuraces.match;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class JdbcMatchRepository implements MatchRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcMatchRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Existing methods...

    @Override
    public void recordMatchResult(Long matchId, Long winnerId) {
        String sql = "UPDATE matches SET winner_id = ? WHERE match_id = ?";
        jdbcTemplate.update(sql, winnerId, matchId);
    }

    @Override
    public void rescheduleMatch(Long matchId, LocalDateTime newDateTime) {
        String sql = "UPDATE matches SET scheduled_date_time = ? WHERE match_id = ?";
        jdbcTemplate.update(sql, newDateTime, matchId);
    }

    @Override
    public void cancelMatch(Long matchId) {
        String sql = "DELETE FROM matches WHERE match_id = ?";
        jdbcTemplate.update(sql, matchId);
    }
}

