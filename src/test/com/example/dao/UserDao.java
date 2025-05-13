package com.example.dao;

import java.sql.*;

public class UserDao {

    private Connection connection;

    public UserDao(Connection connection) {
        this.connection = connection;
    }

    // Lisää käyttäjä tietokantaan
    public void addUser(int kayttajaId, String nimi, String kayttajaTunnus, String salasana, String kayttajaTaso, boolean onkoAnniskeluOikeus, boolean onkoHygieniapassi) throws SQLException {
        String sql = "INSERT INTO kayttajat (kayttaja_id, nimi, kayttaja_tunnus, salasana, kayttaja_taso, onko_anniskelu_oikeus, onko_hygieniapassi) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, kayttajaId);
            stmt.setString(2, nimi);
            stmt.setString(3, kayttajaTunnus);
            stmt.setString(4, salasana);
            stmt.setString(5, kayttajaTaso);
            stmt.setBoolean(6, onkoAnniskeluOikeus);
            stmt.setBoolean(7, onkoHygieniapassi);
            stmt.executeUpdate();
        }
    }

    // Hae käyttäjä ID:n perusteella
    public User getUserById(int kayttajaId) throws SQLException {
        String sql = "SELECT * FROM kayttajat WHERE kayttaja_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, kayttajaId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String nimi = rs.getString("nimi");
                String kayttajaTunnus = rs.getString("kayttaja_tunnus");
                String salasana = rs.getString("salasana");
                String kayttajaTaso = rs.getString("kayttaja_taso");
                boolean onkoAnniskeluOikeus = rs.getBoolean("onko_anniskelu_oikeus");
                boolean onkoHygieniapassi = rs.getBoolean("onko_hygieniapassi");

                return new User(kayttajaId, nimi, kayttajaTunnus, salasana, kayttajaTaso, onkoAnniskeluOikeus, onkoHygieniapassi);
            } else {
                return null;
            }
        }
    }
}