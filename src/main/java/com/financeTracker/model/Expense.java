package com.financeTracker.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Expense {
    private int id;
    private int userId; //foreign-key to com.financeTracker.model.User
    private BigDecimal amount;
    private String label;
    private String description;
    private Timestamp date;

    public Expense(int id, int userId, BigDecimal amount, String label, String description, Timestamp date) {
        this.id = id;
        this.userId = userId;
        this.amount = amount;
        this.label = label;
        this.description = description;
        this.date = date;
    }

    public Expense(int userId, BigDecimal amount, String label, String description, Timestamp date) {
        this.userId = userId;
        this.amount = amount;
        this.label = label;
        this.description = description;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }
}
