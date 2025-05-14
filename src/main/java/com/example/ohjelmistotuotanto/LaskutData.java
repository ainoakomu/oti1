package com.example.ohjelmistotuotanto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * kasitellaan laskujen dataa tietokannasta
 */
public class LaskutData {

    // Hakee laskut tietokannasta
    public static ArrayList<String> haeLaskut(Yhteysluokka yhteysluokka) {
        ArrayList<String> laskuLista = new ArrayList<>();

        //yhteys
        try {
            Connection yhteys = yhteysluokka.getYhteys();
            if (yhteys == null) {
                System.err.println("Tietokantayhteys epäonnistui.");
                return laskuLista;
            }
            //sql scripti
            String sql = """
                SELECT lasku_id, varaus_id, onko_laskutettu, onko_maksettu
                FROM laskut
            """;
            //statementilla yhteys
            Statement statement = yhteys.createStatement();
            //yhteys ja sql scripti
            ResultSet rs = statement.executeQuery(sql);

            //tiedot sarakkeet
            while (rs.next()) {
                int laskuId = rs.getInt("lasku_id");
                int varausId = rs.getInt("varaus_id");
                boolean laskutettu = rs.getBoolean("onko_laskutettu");
                boolean maksettu = rs.getBoolean("onko_maksettu");
                //tiedot riville
                String rivi = "Lasku ID: " + laskuId +
                        ", Varaus ID: " + varausId +
                        ", Laskutettu: " + (laskutettu ? "Kyllä" : "Ei") +
                        ", Maksettu: " + (maksettu ? "Kyllä" : "Ei");

                laskuLista.add(rivi);
            }
        //error catching
        } catch (Exception e) {
            e.printStackTrace();
        }
        //valmis lista
        return laskuLista;
    }

    public void tallennaLaskutiedot(Yhteysluokka yhteysluokka, int laskuID, int varausID, boolean laskutettu, boolean maksettu){

        try {
            Connection yhteys = yhteysluokka.getYhteys();
            if (yhteys == null) {
                System.err.println("Tietokantayhteys epäonnistui.");
                return;
            }
            String sql = "insert into laskut values (?,?,?,?);";
            PreparedStatement stmt = yhteys.prepareStatement(sql);
            stmt.setInt(1, laskuID);
            stmt.setInt(2, varausID);
            stmt.setBoolean(3, laskutettu);
            stmt.setBoolean(4, maksettu);
            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void paivitaLaskua(Yhteysluokka yhteysluokka, int laskuid, int varausid,boolean laskutettu, boolean maksettu){
        try {
            Connection yhteys = yhteysluokka.getYhteys();
            if (yhteys == null) {
                System.err.println("Tietokantayhteys epäonnistui.");
            }
            String sql = "UPDATE laskut SET varaus_id = ?, onko_laskutettu = ?, onko_maksettu = ? WHERE lasku_id = ?";
            PreparedStatement stmt = yhteys.prepareStatement(sql);
            stmt.setInt(1, varausid);
            stmt.setBoolean(2, laskutettu);
            stmt.setBoolean(3, maksettu);
            stmt.setInt(4, laskuid);
            stmt.executeUpdate();
            System.out.println("Lasku päivitetty onnistuneesti.");
            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
