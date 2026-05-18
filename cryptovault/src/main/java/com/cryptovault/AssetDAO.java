package com.cryptovault;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public class AssetDAO {

    /**
     * Adds a new asset or updates the quantity if the user buys/sells an existing one
     */
    public void addOrUpdateAsset(Integer portfolioId, String symbol, String name, double quantity, double price, String type) {
        // The ultimate fix: adding current_price = ? to the ON DUPLICATE KEY UPDATE clause
        String sql = "INSERT INTO assets (portfolio_id, symbol, name, quantity, average_purchase_price, asset_type, current_price) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?) " +
                     "ON DUPLICATE KEY UPDATE quantity = quantity + ?, current_price = ?";
                     
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, portfolioId);
            ps.setString(2, symbol.toUpperCase());
            ps.setString(3, name);
            ps.setDouble(4, quantity);
            ps.setDouble(5, price);
            ps.setString(6, type);
            ps.setDouble(7, price);    // <-- Starts brand new coins at the execution price
            ps.setDouble(8, quantity); // <-- Updates quantity for coins you already own
            ps.setDouble(9, price);    // <-- OVERWRITES the $0 bug with the new live price!
            
            ps.executeUpdate();
        } catch (SQLException e) { 
            e.printStackTrace(); 
        }
    }

    /**
     * Fetches all assets for the dashboard and pulls the LIVE current price from the DB
     */
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
                
                // Fetching the REAL price that the background worker saves to the database
                a.setCurrentPrice(rs.getDouble("current_price")); 
                
                assets.add(a);
            }
        }
        return assets;
    }

    /**
     * Used by the CryptoPriceService background worker to update live market rates automatically
     */
    public void updateAssetPrice(String symbol, double currentPrice) {
        String sql = "UPDATE assets SET current_price = ? WHERE symbol = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
             
            ps.setDouble(1, currentPrice);
            ps.setString(2, symbol);
            ps.executeUpdate();
            
        } catch (SQLException e) {
            System.err.println("Error updating live price for " + symbol + ": " + e.getMessage());
        }
    }
}