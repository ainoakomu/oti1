package com.example.ohjelmistotuotanto;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * SQl-yhteydesta vastava luokka, joka luo yhteyden lokaaliin serveriin
 * Luokkaan pitaa vaihtaa oman toimivan serverin tiedot toimiakseen
 */
public class Yhteysluokka {
    /**
     * Yhdistyksen tallentava muuttuja, jota kutsutaan muualla
     */
    private Connection yhteys;
    /**
     * antaa yhteydelle serverin osoitteen
     */
    final static String  url = "jdbc:mysql://localhost:3306/mokkikodit";
    /**
     * antaa yhteydelle serverin osoitteen kayttajanimen
     */
    final static String user = "root";
    /**
     * antaa yhteydelle serverin osoitteen salasanan
     */
    final static String password ="ToukoKuu2025!";

    /**
     * luodaan parametriton alustaja, joka yrittaa saada yhteyden MySQl-workbenchiin
     */
    public Yhteysluokka() {
        try {
            yhteys = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);

        }
    }

    /**
     * haetaan luokasta yhteys kaytettavaksi muualla
     *
     * @return yhteyden tietokantaan
     */
    public Connection getYhteys(){
        return yhteys;
    }

}
