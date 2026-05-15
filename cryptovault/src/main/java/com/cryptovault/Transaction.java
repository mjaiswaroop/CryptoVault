package com.cryptovault;

import java.sql.Timestamp;

public class Transaction {
    private int id;
    private int assetId;
    private String transactionType;  // BUY or SELL
    private double quantity;
    private double pricePerUnit;
    private double totalValue;
    private Timestamp transactionDate;

    public Transaction() {}

    public Transaction(int id, int assetId, String transactionType, double quantity,
                       double pricePerUnit, double totalValue, Timestamp transactionDate) {
        this.id = id;
        this.assetId = assetId;
        this.transactionType = transactionType;
        this.quantity = quantity;
        this.pricePerUnit = pricePerUnit;
        this.totalValue = totalValue;
        this.transactionDate = transactionDate;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getAssetId() { return assetId; }
    public void setAssetId(int assetId) { this.assetId = assetId; }

    public String getTransactionType() { return transactionType; }
    public void setTransactionType(String transactionType) { this.transactionType = transactionType; }

    public double getQuantity() { return quantity; }
    public void setQuantity(double quantity) { this.quantity = quantity; }

    public double getPricePerUnit() { return pricePerUnit; }
    public void setPricePerUnit(double pricePerUnit) { this.pricePerUnit = pricePerUnit; }

    public double getTotalValue() { return totalValue; }
    public void setTotalValue(double totalValue) { this.totalValue = totalValue; }

    public Timestamp getTransactionDate() { return transactionDate; }
    public void setTransactionDate(Timestamp transactionDate) { this.transactionDate = transactionDate; }
}