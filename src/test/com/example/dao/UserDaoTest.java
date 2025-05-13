package com.example.dao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.*;
import static org.junit.jupiter.api.Assertions.*;

class UserDaoTest {
    private Connection connection;
    private UserDao userDao;

    @BeforeEach
    void setUp() throws SQLException {
        // Yhteys omaan tietokantaan
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mokkikodit", "root", "Romppanen123");
        userDao = new UserDao(connection);
    }

    @Test
    void testAddUser() throws SQLException {
        // Testaa käyttäjän lisäämistä
        userDao.addUser(2001, "Testi Testinen", "testikäyttäjä", "testisalasana", "perus", true, true);

        // Testaa, että käyttäjä on lisätty oikein
        String expectedName = "Testi Testinen";
        String actualName = userDao.getUserById(2001).getName();
        assertEquals(expectedName, actualName);
    }

    @Test
    void testGetUserById() throws SQLException {
        // Testaa käyttäjän hakemista ID:n perusteella
        userDao.addUser(2002, "Testi Testinen", "testikäyttäjä", "testisalasana", "perus", true, true);
        String expectedName = "Testi Testinen";
        String actualName = userDao.getUserById(2002).getName();
        assertEquals(expectedName, actualName);
    }

    @Test
    void testGetUserById_NotFound() throws SQLException {
        // Testaa, että virheellistä ID:tä käyttäen ei löydy käyttäjää
        User result = userDao.getUserById(9999); // Tämä ID ei ole tietokannassa
        assertNull(result);
    }

    @AfterEach
    void tearDown() throws SQLException {
        // Poistetaan testikäyttäjä testin jälkeen
        PreparedStatement stmt = connection.prepareStatement("DELETE FROM kayttajat WHERE kayttaja_id = ?");
        stmt.setInt(1, 2001); // Poista käyttäjä, jonka ID:llä lisättiin
        stmt.executeUpdate();

        stmt.setInt(1, 2002); // Poista myös toinen testikäyttäjä
        stmt.executeUpdate();

        connection.close();
    }
}