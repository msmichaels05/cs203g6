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

@Repository
public class JdbcAdminRepository implements AdminRepository {
    
    private JdbcTemplate jdbcTemplate;

    // Autowired the JdbcTemplate with constructor injection
    public JdbcAdminRepository(JdbcTemplate template){
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
            PreparedStatement statement = conn.prepareStatement("insert into admins (name) values (?) ", Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, admin.getName());
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
    public int update(Admin admin) {
        return jdbcTemplate.update(
            "update admins set name = ? where id = ?",
            admin.getName(), admin.getId()
        );
    }

    /**
     * Return the number of rows affected by the delete
     */
    @Override
    public int deleteById(Long id) {
        return jdbcTemplate.update(
                "delete admins where id = ?", id);
    }

    @Override
    public List<Admin> findAll() {
        return jdbcTemplate.query(
                "select * from admins",
                adminRowMapper
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
public Optional<Admin> findById(Long id) {
    try {
        Admin admin = jdbcTemplate.queryForObject("select * from admins where id = ?",
                adminRowMapper, id);
        return Optional.ofNullable(admin); // Wrap admin in Optional
    } catch (EmptyResultDataAccessException e) {
        // admin not found - return an empty Optional
        return Optional.empty();
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
        // Populate other fields of Admin object if necessary
        return admin;
    };
}
