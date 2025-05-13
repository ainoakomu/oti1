package com.example.ohjelmistotuotanto;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Yhteysluokka {
    private Connection yhteys;
    final static String  url = "jdbc:mysql://localhost:3306/mokkikodit";
    final static String user = "root";
    final static String password ="TammiKuu2025!";

    //alustaja, joka yrittää luoda yhteyden
    public Yhteysluokka() {
        try {
            yhteys = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);

        }
    }
    //getteri jolla sadaan yhteys
    public Connection getYhteys(){
        return yhteys;
    }

    public static void main(String[] args) {

    }
}
