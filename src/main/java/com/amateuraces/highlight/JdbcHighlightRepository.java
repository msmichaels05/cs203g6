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
    
    private JdbcTemplate jdbcTemplate;

    // Autowired the JdbcTemplate with constructor injection
    public JdbcHighlightRepository(JdbcTemplate template){
        this.jdbcTemplate = template;
    }

    /**
     * We need to return the auto-generated id of the insert operation
     * 
     */
    @Override
    public Long save(Highlight highlight) {
        // Use KeyHolder to obtain the auto-generated key from the "insert" statement
        GeneratedKeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update((Connection conn) -> {
            PreparedStatement statement = conn.prepareStatement("insert into highlight (title) values (?) ", Statement.RETURN_GENERATED_KEYS);
            statement.setLong(1, highlight.getYear());
            return statement;
        }, holder);

        Long primaryKey = holder.getKey().longValue();
        return primaryKey;
    }

@Override
    public int update(Highlight highlight) {
        return jdbcTemplate.update(
            "update highlight set tournamentofthemonth = ? where year = ? and month = ?",
            highlight.getTournamentOfTheMonth(), highlight.getYear(), highlight.getMonth()
        );
    }

    /**
     * Return the number of rows affected by the delete
     */
    @Override
    public int deleteByYearMonth(Long year, Long month) {
        return jdbcTemplate.update(
                "delete highlight where year = ? and month = ?", year, month);
    }

    @Override
    public List<Highlight> findAll() {
        // your code here
        return jdbcTemplate.query(
                "select * from highlight",
                (rs, rowNum) -> new Highlight(rs.getLong("year"), rs.getLong("month"))
            );
    }
    /**
     * QueryForObject method: to query a single row in the database
     * 
     * The "select *" returns a ResultSet (rs)
     * The Lambda expression (an instance of RowMapper) returns an object instance using "rs"
     * 
     * Optional: a container which may contain null objects
     *  -> To handle the case in which the given id is not found
     */
    @Override
    public Optional<Highlight> findCurrentHighlight(Long year, Long month) {
        try{
            return jdbcTemplate.queryForObject("select * from highlight where year = ?",
            // implement RowMapper interface to return the book found
            // using a lambda expression
            (rs, rowNum) -> Optional.of(new Highlight(rs.getLong("year"), rs.getLong("month"))), year);

        }catch(EmptyResultDataAccessException e){
            // book not found - return an empty object
            return Optional.empty();
        }
    }
}
