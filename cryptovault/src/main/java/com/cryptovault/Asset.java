package com.cryptovault;

public class Asset {
    private int id;
    private String symbol;
    private String name;
    private double quantity;
    private double averagePurchasePrice;
    private double currentPrice; // Market Price

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getSymbol() { return symbol; }
    public void setSymbol(String symbol) { this.symbol = symbol; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public double getQuantity() { return quantity; }
    public void setQuantity(double quantity) { this.quantity = quantity; }
    public double getAveragePurchasePrice() { return averagePurchasePrice; }
    public void setAveragePurchasePrice(double price) { this.averagePurchasePrice = price; }
    public double getCurrentPrice() { return currentPrice; }
    public void setCurrentPrice(double currentPrice) { this.currentPrice = currentPrice; }
}