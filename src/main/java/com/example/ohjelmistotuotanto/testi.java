package com.example.ohjelmistotuotanto;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class testi {

    public static void main(String[] args) {

        String url = "jdbc:mysql://127.0.0.1:3306/mokkikodit";
        String user = "root";
        String password ="TammiKuu2025!";

        try {
            Connection conn = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);

        }


        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            if (conn != null && !conn.isClosed()) {
                System.out.println("✅ Successfully connected to the MySQL database.");
            } else {
                System.out.println("⚠️ Connection established but is closed.");
            }
        } catch (SQLException e) {
            System.out.println("❌ Failed to connect to the MySQL database.");
            e.printStackTrace();

        }
    }
}
