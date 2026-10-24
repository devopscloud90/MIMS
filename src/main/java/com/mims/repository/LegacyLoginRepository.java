package com.mims.repository;

import com.mims.auth.LegacyLoginAccount;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class LegacyLoginRepository {

    private final JdbcTemplate jdbcTemplate;

    public LegacyLoginRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<LegacyLoginAccount> findActiveByUsername(String username) {
        List<LegacyLoginAccount> accounts = jdbcTemplate.query("""
                SELECT id, username, password, keys, usertype, status
                FROM logindetails
                WHERE username = ? AND UPPER(status) = 'ACTIVE'
                """, this::mapAccount, username);
        return accounts.stream().findFirst();
    }

    public Optional<LegacyLoginAccount> findById(Long id) {
        List<LegacyLoginAccount> accounts = jdbcTemplate.query("""
                SELECT id, username, password, keys, usertype, status
                FROM logindetails WHERE id = ?
                """, this::mapAccount, id);
        return accounts.stream().findFirst();
    }

    public int replacePassword(Long id, String oldPassword, String newPassword) {
        return jdbcTemplate.update(
                "UPDATE logindetails SET password = ? WHERE id = ? AND password = ?",
                newPassword, id, oldPassword);
    }

    public int updatePassword(Long id, String encodedPassword) {
        return jdbcTemplate.update("UPDATE logindetails SET password = ? WHERE id = ?",
                encodedPassword, id);
    }

    private LegacyLoginAccount mapAccount(ResultSet resultSet, int rowNumber) throws SQLException {
        return new LegacyLoginAccount(
                resultSet.getLong("id"),
                resultSet.getString("username"),
                resultSet.getString("password"),
                resultSet.getString("keys"),
                resultSet.getString("usertype"),
                resultSet.getString("status"));
    }
}
