package com.hospitalmanagement.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class DBPropertyUtil {

    private static String connectionString;
    private static boolean connectionStringGenerated = false;

    public static String getPropertyString() {
        if (!connectionStringGenerated) {
            Properties properties = new Properties();

            try {
                FileInputStream input = new FileInputStream("src/com/hospitalmanagement/resources/database.properties");
                properties.load(input);

                String host = properties.getProperty("db.host");
                String port = properties.getProperty("db.port");
                String name = properties.getProperty("db.name");
                String user = properties.getProperty("db.user");
                String password = properties.getProperty("db.password");

                connectionString = String.format(
                    "jdbc:mysql://%s:%s/%s?user=%s&password=%s&serverTimezone=Asia/Kolkata",
                    host, port, name, user, password
                );

                System.out.println("Connection String generated.");
                input.close();
                connectionStringGenerated = true;

            } catch (IOException ex) {
                System.err.println("Error loading properties file: " + ex.getMessage());
                connectionString = null;
            }
        }
        return connectionString;
    }
}