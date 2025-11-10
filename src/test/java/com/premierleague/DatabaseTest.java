package com.premierleague;

import java.sql.Connection;
import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DatabaseTest {
    private static final Logger logger = LogManager.getLogger(DatabaseTest.class);

    public static void main(String[] args) {
        try (Connection conn = Database.getConnection()) {
            if (conn != null) {
                logger.info("Connection successful!");
            }
        } catch (SQLException e) {
            logger.error("Connection failed!", e);
        }
    }
}
