package com.cryptovault;

import org.springframework.stereotype.Repository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TransactionDAO {

    /**
     * Records a new transaction (BUY or SELL)
     * Returns the generated transaction ID, or -1 if failed
     */
    public int create(int assetId, String transactionType, double quantity,
                      double pricePerUnit, double totalValue) {
        
        String sql = "INSERT INTO transactions " +
                     "(asset_id, transaction_type, quantity, price_per_unit, total_value, transaction_date) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, assetId);
            ps.setString(2, transactionType);        // "BUY" or "SELL"
            ps.setDouble(3, quantity);
            ps.setDouble(4, pricePerUnit);
            ps.setDouble(5, totalValue);
            ps.setTimestamp(6, new Timestamp(System.currentTimeMillis()));

            if (ps.executeUpdate() > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error creating transaction: " + e.getMessage());
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Get all transactions for a specific asset
     */
    public List<Transaction> getByAssetId(int assetId) {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions WHERE asset_id = ? ORDER BY transaction_date DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, assetId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Transaction t = new Transaction();
                t.setId(rs.getInt("id"));
                t.setAssetId(rs.getInt("asset_id"));
                t.setTransactionType(rs.getString("transaction_type"));
                t.setQuantity(rs.getDouble("quantity"));
                t.setPricePerUnit(rs.getDouble("price_per_unit"));
                t.setTotalValue(rs.getDouble("total_value"));
                t.setTransactionDate(rs.getTimestamp("transaction_date"));

                transactions.add(t);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching transactions: " + e.getMessage());
            e.printStackTrace();
        }
        return transactions;
    }

    /**
     * Get all transactions for a portfolio (via assets)
     */
    public List<Transaction> getByPortfolioId(int portfolioId) {
        List<Transaction> transactions = new ArrayList<>();
        String sql = """
            SELECT t.* FROM transactions t
            JOIN assets a ON t.asset_id = a.id
            WHERE a.portfolio_id = ?
            ORDER BY t.transaction_date DESC
            """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, portfolioId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Transaction t = new Transaction();
                t.setId(rs.getInt("id"));
                t.setAssetId(rs.getInt("asset_id"));
                t.setTransactionType(rs.getString("transaction_type"));
                t.setQuantity(rs.getDouble("quantity"));
                t.setPricePerUnit(rs.getDouble("price_per_unit"));
                t.setTotalValue(rs.getDouble("total_value"));
                t.setTransactionDate(rs.getTimestamp("transaction_date"));

                transactions.add(t);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching portfolio transactions: " + e.getMessage());
            e.printStackTrace();
        }
        return transactions;
    }

    /**
     * Fetches recent cloud sync logs to feed into the UI Execution Terminal console
     */
    public List<CloudSyncLog> getCloudSyncLogs() {
        List<CloudSyncLog> logs = new ArrayList<>();
        String sql = "SELECT id, user_id, action, synced_at FROM cloud_sync_logs ORDER BY synced_at DESC LIMIT 50";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                CloudSyncLog log = new CloudSyncLog();
                log.setId(rs.getInt("id"));
                log.setUserId(rs.getInt("user_id"));
                log.setAction(rs.getString("action"));
                log.setSyncedAt(rs.getTimestamp("synced_at"));
                
                logs.add(log);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching cloud sync logs: " + e.getMessage());
            e.printStackTrace();
        }
        return logs;
    }
}