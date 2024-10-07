package com.amateuraces.admin;

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
public class JdbcAdminRepository implements AdminRepository {

    private JdbcTemplate jdbcTemplate;

    // Constructor injection of JdbcTemplate
    public JdbcAdminRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Save admin and return the auto-generated ID
     */
    @Override
    public Long save(Admin admin) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update((Connection conn) -> {
            PreparedStatement statement = conn.prepareStatement(
                "INSERT INTO admins (name, email, password) VALUES (?, ?, ?)", 
                Statement.RETURN_GENERATED_KEYS
            );
            statement.setString(1, admin.getName());
            statement.setString(2, admin.getEmail());
            statement.setString(3, admin.getPassword());
            return statement;
        }, keyHolder);
        return keyHolder.getKey().longValue(); // Return the auto-generated key
}



    /**
     * Update admin's information and return the number of rows affected
     */
    @Override
    public int update(Admin admin) {
        return jdbcTemplate.update(
            "UPDATE admins SET name = ? WHERE id = ?",
            admin.getName(), admin.getId()
        );
    }

    /**
     * Delete admin by ID and return the number of rows affected
     */
    @Override
    public int deleteById(Long id) {
        return jdbcTemplate.update(
            "DELETE FROM admins WHERE id = ?", id
        );
    }

    /**
     * Find all admins
     */
    @Override
    public List<Admin> findAll() {
        return jdbcTemplate.query(
            "SELECT * FROM admins",
            (rs, rowNum) -> new Admin(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getString("password")  // Adjust to include required fields
            )
        );
    }

    /**
     * Find admin by ID, using Optional to handle the case where no admin is found
     */
    @Override
    public Optional<Admin> findById(Long id) {
        try {
            return jdbcTemplate.queryForObject(
                "SELECT * FROM admins WHERE id = ?",
                (rs, rowNum) -> Optional.of(new Admin(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("password")  // Adjust fields as per your Admin class
                )),
                id
            );
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
