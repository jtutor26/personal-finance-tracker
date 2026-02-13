package com.financeTracker.dao;

import com.financeTracker.db.Connector;
import com.financeTracker.model.User;

import java.sql.*;
import java.util.Optional;

public class UserDAO {
    // 1. CREATE (Register a new user)
    public int create(User user) {
        String sql = "INSERT INTO users (first_name, last_name, username, password, balance) VALUES (?, ?, ?, ?, ?) RETURNING id";

        try (Connection conn = Connector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getFirst_name());
            ps.setString(2, user.getLast_name());
            ps.setString(3, user.getUsername());
            ps.setString(4, user.getPassword());
            // Important: We use setBigDecimal for money
            ps.setBigDecimal(5, user.getBalance());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int newId = rs.getInt("id");
                    user.setId(newId);
                    return newId;
                }
            }
            return 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error registering user", e);
        }
    }

    // 2. READ (Login / Find by Username)
    public Optional<User> findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";

        try (Connection conn = Connector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
                return Optional.empty();
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error finding user", e);
        }
    }

    // Helper method to keep code clean
    private User mapRow(ResultSet rs) throws SQLException {
        return new User(
                rs.getInt("id"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("username"),
                rs.getString("password"),
                rs.getBigDecimal("balance") // getBigDecimal matches the money type
        );
    }
}
