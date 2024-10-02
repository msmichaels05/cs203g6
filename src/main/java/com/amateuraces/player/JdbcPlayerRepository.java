package com.amateuraces.player;

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
public class JdbcPlayerRepository implements PlayerRepository {
    
    private JdbcTemplate jdbcTemplate;

    // Autowired the JdbcTemplate with constructor injection
    public JdbcPlayerRepository(JdbcTemplate template){
        this.jdbcTemplate = template;
    }

    
    /**
     * We need to return the auto-generated id of the insert operation
     * 
     */
    @Override
    public Long save(Player player) {
        // Use KeyHolder to obtain the auto-generated key from the "insert" statement
        GeneratedKeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update((Connection conn) -> {
            PreparedStatement statement = conn.prepareStatement("insert into players (name) values (?) ", Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, player.getName());
            return statement;
        }, holder);

        Long primaryKey = holder.getKey().longValue();
        return primaryKey;
    }

    /**
     * TODO: Activity 1 - Implement the update method
     * 
     * This method needs to return the number of rows affected by the update
     */
    @Override
    public int update(Player player) {
        return jdbcTemplate.update(
            "update players set name = ? where id = ?",
            player.getName(), player.getId()
        );
    }

    /**
     * Return the number of rows affected by the delete
     */
    @Override
    public int deleteById(Long id) {
        return jdbcTemplate.update(
                "delete players where id = ?", id);
    }

    /**
     * TODO: Activity 1 - Add code to return all players
     * Hint: use the "query" method of JdbcTemplate
     * Refer to the below code of "findByID" method on how to implement a RowMapper using a lambda expression
     * 
     */
    @Override
    public List<Player> findAll() {
        return jdbcTemplate.query(
                "select * from players",
                (rs, rowNum) -> new Player(rs.getLong("id"), rs.getString("name"))
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
    public Optional<Player> findById(Long id) {
        try{
            return jdbcTemplate.queryForObject("select * from players where id = ?",
            // implement RowMapper interface to return the player found
            // using a lambda expression
            (rs, rowNum) -> Optional.of(new Player(rs.getLong("id"), rs.getString("name"))), id);

        }catch(EmptyResultDataAccessException e){
            // player not found - return an empty object
            return Optional.empty();
        }
    }
}