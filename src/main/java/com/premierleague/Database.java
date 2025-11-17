package com.premierleague;

import java.sql.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Database {

    private static final Logger logger = LogManager.getLogger(Database.class);
    private static final String URL = "jdbc:sqlserver://YXNG\\SQLEXPRESS:1433;databaseName=premier_league_db;encrypt=false;trustServerCertificate=true";
    private static final String USER = "user";       // your SQL Server login
    private static final String PASSWORD = "Admin1!";
    public static Connection getConnection() {
        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            logger.info("Connected to SQL Server successfully!");
            return conn;
        } catch (SQLException e) {
            logger.error("Failed to connect to SQL Server", e);
            return null;
        }
   } 
    public static void main(String[] args) {
        Connection conn = Database.getConnection();
        if (conn != null) {
            System.out.println("Connection successful!");
        } else {
            System.out.println("Connection failed!");
        }
    }
}
