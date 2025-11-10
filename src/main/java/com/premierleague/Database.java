package com.premierleague;

import java.sql.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Database {

    private static final Logger logger = LogManager.getLogger(Database.class);
    private static final String URL = "jdbc:mysql://localhost:3306/premier_league_db";
    private static final String USER = "afanfor";
    private static final String PASSWORD = "UrusaiN4";

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();  // or log the error
            return null;  // return null if connection fails
        }
   } 
}
