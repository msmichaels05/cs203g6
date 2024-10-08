package com.amateuraces.highlight;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcHighlightRepository implements HighlightRepository {
    
    private final JdbcTemplate jdbcTemplate;

    // Autowired the JdbcTemplate with constructor injection
    public JdbcHighlightRepository(JdbcTemplate template){
        this.jdbcTemplate = template;
    }

    @Override
    public Long save(Highlight highlight) {
        // Use KeyHolder to obtain the auto-generated key from the "insert" statement
        GeneratedKeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update((Connection conn) -> {
            PreparedStatement statement = conn.prepareStatement("insert into highlights (tournamentOfTheMonth) values (?) ", Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, highlight.getTournamentOfTheMonth());
            return statement;
        }, holder);

        Long primaryKey = holder.getKey().longValue();
        return primaryKey;
    }

    @Override
    public int update(Highlight highlight) {
        return jdbcTemplate.update(
            "update highlights set tournamentOfTheMonth = ? where year = ? and month = ?",
            highlight.getTournamentOfTheMonth(), highlight.getYear(), highlight.getMonth()
        );
    }

    @Override
    public int deleteByYearMonth(Long year, Long month) {
        return jdbcTemplate.update(
                "delete highlights where year = ? and month = ?", year, month);
    }

    @Override
    public List<Highlight> findAll() {
        return jdbcTemplate.query(
                "select * from highlights",
                (rs, rowNum) -> new Highlight(rs.getLong("year"), rs.getLong("month"), rs.getString("tournamentOfTheMonth"))
            );
    }

    @Override
    public Optional<Highlight> findCurrentHighlight(Long year, Long month) {
        try{
            return jdbcTemplate.queryForObject("select * from highlights where \"year\" = ? and \"month\" = ?",
            (rs, rowNum) -> Optional.of(new Highlight(rs.getLong("year"), rs.getLong("month"), rs.getString("tournamentOfTheMonth"))), year, month);

        }catch(EmptyResultDataAccessException e){
            return Optional.empty();
        }
    }
}