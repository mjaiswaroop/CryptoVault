package com.cryptovault;

import org.springframework.stereotype.Repository;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PortfolioDAO {

    public Portfolio getById(int id) {
        String sql = "SELECT * FROM portfolios WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Portfolio(rs.getInt("id"), rs.getInt("user_id"), rs.getString("name"));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    // This fixes the 'getByUser' error in the Controller
    public List<Portfolio> getByUser(int userId) {
        List<Portfolio> list = new ArrayList<>();
        String sql = "SELECT * FROM portfolios WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Portfolio(rs.getInt("id"), rs.getInt("user_id"), rs.getString("name")));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }
}