package com.example.ohjelmistotuotanto;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MokkiData {

    //arraylist jonka saa sisälleen yhteysluokan olion
    public static ArrayList<String> haeMokit(Yhteysluokka yhteysluokka) {
        ArrayList<String> mokkiLista = new ArrayList<>();

        //yritetään saada yhteysluokan yhteys
        try {
            Connection conn = yhteysluokka.getYhteys();
            if (conn == null) {
                System.err.println("Tietokantayhteys epäonnistui.");
                return mokkiLista;
            }

            String sql = """
                SELECT mokki_id, hinta_per_yo, osoite, neliot, vuodepaikat,
                       onko_rantasauna, onko_oma_ranta, onko_wifi, onko_sisa_wc, onko_palju
                FROM mokit
            """;
            //statement object, jolla connection
            Statement statement = conn.createStatement();
            //resultsetille statement yhteydellä ja sql käsky sinne
            ResultSet resultss = statement.executeQuery(sql);
            //while loopilla sarakkeille nimet ja dataretrieval
            while (resultss.next()) {
                int id = resultss.getInt("mokki_id");
                double hinta = resultss.getDouble("hinta_per_yo");
                String osoite = resultss.getString("osoite");
                int neliot = resultss.getInt("neliot");
                int vuodepaikat = resultss.getInt("vuodepaikat");
                boolean rantasauna = resultss.getBoolean("onko_rantasauna");
                boolean omaRanta = resultss.getBoolean("onko_oma_ranta");
                boolean wifi = resultss.getBoolean("onko_wifi");
                boolean sisaWC = resultss.getBoolean("onko_sisa_wc");
                boolean palju = resultss.getBoolean("onko_palju");

                //rivien kirjoitus listviehen
                String rivi = "ID: " + id +
                        ", Osoite: " + osoite +
                        ", Hinta/yö: " + hinta + " €" +
                        ", Neliöt: " + neliot +
                        ", Vuodepaikat: " + vuodepaikat +
                        ", Rantasauna: " + (rantasauna ? "Kyllä" : "Ei") +
                        ", Oma ranta: " + (omaRanta ? "Kyllä" : "Ei") +
                        ", WiFi: " + (wifi ? "Kyllä" : "Ei") +
                        ", Sisä-WC: " + (sisaWC ? "Kyllä" : "Ei") +
                        ", Palju: " + (palju ? "Kyllä" : "Ei");

                mokkiLista.add(rivi);
            }

            //error catching
        } catch (Exception e) {
            e.printStackTrace();
        }
        //palautetaan mokkien data
        return mokkiLista;
    }

    public static ArrayList<String> haeMokinHinta(Yhteysluokka yhteysluokka) {
        ArrayList<String> hintalista = new ArrayList<>();

        //yritetään saada yhteysluokan yhteys
        try {
            Connection conn = yhteysluokka.getYhteys();
            if (conn == null) {
                System.err.println("Tietokantayhteys epäonnistui.");
                return hintalista;
            }

            Map<Double, Integer> hintaToMokkiId = new HashMap<>();

            String sql = "SELECT mokki_id, hinta_per_yo FROM mokit";
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                int id = resultSet.getInt("mokki_id");
                double hinta = resultSet.getDouble("hinta_per_yo");
                hintaToMokkiId.put(hinta, id);
            }
            hintalista.add(String.valueOf(hintaToMokkiId));

            //error catching
        } catch (Exception e) {
            e.printStackTrace();
        }
        //palautetaan mokkien data
        return hintalista;
    }
}
