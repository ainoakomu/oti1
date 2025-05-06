package com.example.ohjelmistotuotanto;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

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
}
