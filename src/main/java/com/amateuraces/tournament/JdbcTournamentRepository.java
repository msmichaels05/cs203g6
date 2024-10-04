package com.amateuraces.tournament;

import com.amateuraces.player.Player;
import com.amateuraces.match.Match;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcTournamentRepository implements TournamentRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public JdbcTournamentRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public List<Tournament> findAll() {
        String sql = "SELECT * FROM tournaments";
        return namedParameterJdbcTemplate.query(sql, this::mapRowToTournament);
    }

    @Override
    public Optional<Tournament> findById(Long id) {
        String sql = "SELECT * FROM tournaments WHERE id = :id";
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("id", id);
        try {
            Tournament tournament = namedParameterJdbcTemplate.queryForObject(sql, parameters, this::mapRowToTournament);
            return Optional.ofNullable(tournament);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Tournament save(Tournament tournament) {
        String sql = "INSERT INTO tournaments (name, start_date, end_date) VALUES (:name, :start_date, :end_date)";
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("name", tournament.getName());
        parameters.addValue("start_date", tournament.getRegistrationStartDate());
        parameters.addValue("end_date", tournament.getRegistrationEndDate());
        namedParameterJdbcTemplate.update(sql, parameters);
        return tournament;
    }

    @Override
    public void addPlayerToTournament(Long tournamentId, Long playerId) {
        String sql = "INSERT INTO tournament_players (tournament_id, player_id) VALUES (:tournamentId, :playerId)";
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("tournamentId", tournamentId);
        parameters.addValue("playerId", playerId);
        namedParameterJdbcTemplate.update(sql, parameters);
    }

    @Override
    public List<Player> getPlayersInTournament(Long tournamentId) {
        String sql = "SELECT p.* FROM players p JOIN tournament_players tp ON p.id = tp.player_id WHERE tp.tournament_id = :tournamentId";
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("tournamentId", tournamentId);
        return namedParameterJdbcTemplate.query(sql, parameters, (rs, rowNum) -> {
            // Use the default constructor and setters to populate the Player object
            Player player = new Player();
            player.setId(rs.getLong("id"));
            player.setName(rs.getString("name"));
            player.setElo(rs.getInt("elo")); // Make sure Player has a setElo method
            return player;
        });
    }

    @Override
    public List<Match> performRandomDraw(Long tournamentId) {
        // Logic to perform a random draw
        return List.of(); // Placeholder: replace with actual logic
    }

    private Tournament mapRowToTournament(ResultSet rs, int rowNum) throws SQLException {
        Tournament tournament = new Tournament();
        tournament.setId(rs.getLong("id")); // Assuming you have an ID field
        tournament.setName(rs.getString("name"));
        
        // Get the dates as Strings
        tournament.setRegistrationStartDate(rs.getString("start_date")); // Assuming the date is stored as a string
        tournament.setRegistrationEndDate(rs.getString("end_date")); // Assuming the date is stored as a string
        
        return tournament;
    }
}
