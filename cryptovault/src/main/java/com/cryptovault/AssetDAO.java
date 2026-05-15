package com.cryptovault;

import org.springframework.stereotype.Repository;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class AssetDAO {

    public void addOrUpdateAsset(Integer portfolioId, String symbol, String name, double quantity, double price, String type) {
        String sql = "INSERT INTO assets (portfolio_id, symbol, name, quantity, average_purchase_price, asset_type) " +
                     "VALUES (?, ?, ?, ?, ?, ?) " +
                     "ON DUPLICATE KEY UPDATE quantity = quantity + ?";
                     
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, portfolioId);
            ps.setString(2, symbol.toUpperCase());
            ps.setString(3, name);
            ps.setDouble(4, quantity);
            ps.setDouble(5, price);
            ps.setString(6, type);
            ps.setDouble(7, quantity);
            
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public List<Asset> getByPortfolioId(int portfolioId) throws SQLException {
        List<Asset> assets = new ArrayList<>();
        String sql = "SELECT * FROM assets WHERE portfolio_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, portfolioId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Asset a = new Asset();
                a.setSymbol(rs.getString("symbol"));
                a.setName(rs.getString("name"));
                a.setQuantity(rs.getDouble("quantity"));
                a.setAveragePurchasePrice(rs.getDouble("average_purchase_price"));
                
                // Fetch more realistic prices based on the symbol
                a.setCurrentPrice(getMockRealPrice(a.getSymbol()));
                assets.add(a);
            }
        }
        return assets;
    }

    private double getMockRealPrice(String symbol) {
        // These are closer to April 2026 projected market values
        switch (symbol.toUpperCase()) {
            case "BTC": return 68432.50 + (Math.random() * 100);
            case "ETH": return 3521.20 + (Math.random() * 10);
            case "SOL": return 145.60 + (Math.random() * 2);
            default: return 1.00; // Stablecoin or unknown
        }
    }
}