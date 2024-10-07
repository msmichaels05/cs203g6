package com.amateuraces.admin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Repository
public class JdbcAdminRepository implements AdminRepository {

    private JdbcTemplate jdbcTemplate;
    private static final Logger logger = LoggerFactory.getLogger(JdbcAdminRepository.class); // Add logger

    // Autowired the JdbcTemplate with constructor injection
    public JdbcAdminRepository(JdbcTemplate template) {
        this.jdbcTemplate = template;
    }

    /**
     * We need to return the auto-generated id of the insert operation
     */
    @Override
    public Long save(Admin admin) {
        // Use KeyHolder to obtain the auto-generated key from the "insert" statement
        GeneratedKeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update((Connection conn) -> {
            PreparedStatement statement = conn.prepareStatement(
                "INSERT INTO admins (name) VALUES (?)", Statement.RETURN_GENERATED_KEYS
            );
            statement.setString(1, admin.getName());
            return statement;
        }, holder);

        Long primaryKey = holder.getKey().longValue();
        return primaryKey;
    }

    /**
     * Implement the update method
     */
    @Override
    public int update(Admin admin) {
        return jdbcTemplate.update(
            "UPDATE admins SET name = ? WHERE id = ?",
            admin.getName(), admin.getId()
        );
    }

    /**
     * Return the number of rows affected by the delete
     */
    @Override
    public int deleteById(Long id) {
        return jdbcTemplate.update(
            "DELETE FROM admins WHERE id = ?", id);  // Fix the missing "FROM"
    }

    /**
     * Find all admins
     */
    @Override
    public List<Admin> findAll() {
        try {
            return jdbcTemplate.query("SELECT * FROM admins", adminRowMapper);
        } catch (Exception e) {
            logger.error("Error while querying all admins", e);  // Add logging
            throw e;
        }
    }

    /**
     * Find admin by ID
     */
    @Override
    public Optional<Admin> findById(Long id) {
        try {
            Admin admin = jdbcTemplate.queryForObject(
                "SELECT * FROM admins WHERE id = ?",
                adminRowMapper, id
            );
            return Optional.ofNullable(admin); // Wrap admin in Optional
        } catch (EmptyResultDataAccessException e) {
            logger.warn("Admin not found for ID: " + id);  // Add warning log
            return Optional.empty();
        } catch (Exception e) {
            logger.error("Error while finding admin by ID", e);  // Add logging
            throw e;
        }
    }

    // RowMapper for Admin class
    private RowMapper<Admin> adminRowMapper = (rs, rowNum) -> {
        Admin admin = new Admin(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getString("email"),
            rs.getString("password")
            // Add more fields as needed
        );
        return admin;
    };
}
