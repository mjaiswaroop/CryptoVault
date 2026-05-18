package com.cryptovault;

import java.sql.Timestamp;

public class CloudSyncLog {
    private int id;
    private int userId;
    private String action;
    private Timestamp syncedAt;

    public CloudSyncLog() {}

    public CloudSyncLog(int id, int userId, String action, Timestamp syncedAt) {
        this.id = id;
        this.userId = userId;
        this.action = action;
        this.syncedAt = syncedAt;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public Timestamp getSyncedAt() { return syncedAt; }
    public void setSyncedAt(Timestamp syncedAt) { this.syncedAt = syncedAt; }
}