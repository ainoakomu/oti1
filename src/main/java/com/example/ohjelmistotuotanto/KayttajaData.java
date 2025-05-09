package com.example.ohjelmistotuotanto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class KayttajaData {

    // Hakee käyttäjät tietokannasta
    public static ArrayList<String> haeKayttajat(Yhteysluokka yhteysluokka) {
        ArrayList<String> kayttajaLista = new ArrayList<>();
        //yhteys
        try {
            Connection yhteys = yhteysluokka.getYhteys();
            if (yhteys == null) {
                System.err.println("Tietokantayhteys epäonnistui.");
                return kayttajaLista;
            }
            //sql scripti
            String sql = """
                SELECT kayttaja_id, nimi, kayttaja_tunnus, salasana,
                       kayttaja_taso, onko_anniskelu_oikeus, onko_hygieniapassi
                FROM kayttajat
            """;
            //statementille yhteys
            Statement statement = yhteys.createStatement();
            //yhteys ja sql scripti
            ResultSet rs = statement.executeQuery(sql);

            //loopilla tiedot sarakkeet
            while (rs.next()) {
                int kayttajaId = rs.getInt("kayttaja_id");
                String nimi = rs.getString("nimi");
                String tunnus = rs.getString("kayttaja_tunnus");
                String salasana = rs.getString("salasana");
                String taso = rs.getString("kayttaja_taso");
                boolean anniskelu = rs.getBoolean("onko_anniskelu_oikeus");
                boolean hygieniapassi = rs.getBoolean("onko_hygieniapassi");
                //riville tiedot
                String rivi = "ID: " + kayttajaId +
                        ", Nimi: " + nimi +
                        ", Käyttäjätunnus: " + tunnus +
                        ", Salasana: " + salasana +
                        ", Käyttäjätaso: " + taso +
                        ", Anniskeluoikeus: " + (anniskelu ? "Kyllä" : "Ei") +
                        ", Hygieniapassi: " + (hygieniapassi ? "Kyllä" : "Ei");

                kayttajaLista.add(rivi);
            }
        //error chatching
        } catch (Exception e) {
            e.printStackTrace();
        }
        //valmis lista
        return kayttajaLista;
    }

    public void lisaaKayttaja(Yhteysluokka yhteysluokka, int id, String nimi, String tunnus, String ss, String kayttajaTaso, int anniskeluOikeus, int hygieniaPassi){
        try {
            Connection yhteys = yhteysluokka.getYhteys();
            if (yhteys == null) {
                System.err.println("Tietokantayhteys epäonnistui.");
            }
            String sql = "insert into kayttajat values (?,?,?,?,?,?,?);";
            PreparedStatement stmt = yhteys.prepareStatement(sql);
            stmt.setInt(1, id);
            stmt.setString(2, nimi);
            stmt.setString(3, tunnus);
            stmt.setString(4, ss);
            stmt.setString(5, kayttajaTaso);
            stmt.setInt(6, anniskeluOikeus);
            stmt.setInt(7,hygieniaPassi);
            stmt.executeUpdate();

    } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
