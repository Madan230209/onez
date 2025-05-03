package com.onez.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;

import com.onez.config.DbConfig;
import com.onez.model.FeedbackModel;

public class FeedbackService {
    private Connection dbConn;

    public FeedbackService() {
        try {
            this.dbConn = DbConfig.getDbConnection();
        } catch (SQLException | ClassNotFoundException ex) {
            System.err.println("Database connection error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public boolean submitFeedback(FeedbackModel feedback) {
        String sql = "INSERT INTO feedback (feedbackDetails, rating, createdAt, user_id, product_id) " +
                     "VALUES (?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = dbConn.prepareStatement(sql)) {
            stmt.setString(1, feedback.getFeedbackDetails());
            stmt.setInt(2, feedback.getRating());
            stmt.setObject(3, LocalDateTime.now());
            stmt.setInt(4, feedback.getUser().getId());
            stmt.setInt(5, feedback.getProduct().getProductId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}