package com.hospitalmanagement.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnUtil {

    private static Connection connection = null;
    private static boolean connectionEstablished = false;
    private static String connectionString = null;

    public static Connection getConnection() throws SQLException {
        if (!connectionEstablished) {
            connectionString = DBPropertyUtil.getPropertyString();

            if (connectionString == null) {
                throw new SQLException("Database connection string is null. Check your properties file.");
            }

            try {
                connection = DriverManager.getConnection(connectionString);
                System.out.println("Database connection established.");
                connectionEstablished = true;
            } catch (SQLException e) {
                System.err.println("Error establishing database connection: " + e.getMessage());
                throw e;
            }
        } else if (connection == null || connection.isClosed()) {
            //System.out.println("Attempting to re-establish database connection.");
            if (connectionString == null) {
                connectionString = DBPropertyUtil.getPropertyString();
                if (connectionString == null) {
                    throw new SQLException("Database connection string is null for re-establishment.");
                }
            }
            try {
                connection = DriverManager.getConnection(connectionString);
                //System.out.println("Database connection re-established.");
            } catch (SQLException e) {
                System.err.println("Error re-establishing database connection: " + e.getMessage());
                throw e;
            }
        }
        return connection;
    }

    public static void closeConnection() throws SQLException {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                    System.out.println("Database connection closed.");
                } else {
                    System.out.println("Database connection is closed.");
                }
            } catch (SQLException e) {
                System.err.println("Error closing database connection: " + e.getMessage());
                throw e;
            } finally {
                connectionEstablished = false;
                connectionString = null;
                connection = null;
            }
        } else {
            System.out.println("No active database connection to close.");
        }
    }
}