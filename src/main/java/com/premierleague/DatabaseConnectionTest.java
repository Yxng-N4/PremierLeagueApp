package com.premierleague;

import com.premierleague.Database;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConnectionTest {
    public static void main(String[] args) {
        try (Connection conn = Database.getConnection()) {
            if (conn != null && !conn.isClosed()) {
                System.out.println("✅ Database connection successful!");
                System.out.println("Connected to database: " + conn.getCatalog());
            } else {
                System.out.println("❌ Connection is null or closed.");
            }
        } catch (SQLException e) {
            System.out.println("❌ Failed to connect to database.");
            e.printStackTrace();
        }
    }
}
