package com.financeTracker.dao;

import com.financeTracker.db.Connector;
import com.financeTracker.model.Expense;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExpenseDAO {
    public int create(Expense expense) {
        String sql = "INSERT INTO expenses (user_id, amount, label, description, date) VALUES (?, ?, ?, ?, ?) RETURNING id";

        try (Connection conn = Connector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, expense.getUserId());
            ps.setBigDecimal(2, expense.getAmount());
            ps.setString(3, expense.getLabel());
            ps.setString(4, expense.getDescription());
            ps.setTimestamp(5, expense.getDate());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int newId = rs.getInt("id");
                    expense.setId(newId);
                    return newId;
                }
            }
            return 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error saving expense", e);
        }
    }

    public List<Expense> listByUser(int userId) {
        String sql = "SELECT * FROM expenses WHERE user_id = ? ORDER BY date DESC";
        List<Expense> list = new ArrayList<>();

        try (Connection conn = Connector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
            return list;

        } catch (SQLException e) {
            throw new RuntimeException("Error listing expenses", e);
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM expenses WHERE id = ?";

        try (Connection conn = Connector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected == 1;

        } catch (SQLException e) {
            throw new RuntimeException("Error deleting expense", e);
        }
    }

    private Expense mapRow(ResultSet rs) throws SQLException {
        return new Expense(
                rs.getInt("id"),
                rs.getInt("user_id"),
                rs.getBigDecimal("amount"),
                rs.getString("label"),
                rs.getString("description"),
                rs.getTimestamp("date")
        );
    }
}