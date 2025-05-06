package com.example.ohjelmistotuotanto;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class AsiakasData {

    // Metodi hakee asiakkaat tietokannasta
    public static ArrayList<String> haeAsiakkaat(Yhteysluokka yhteysluokka) {
        ArrayList<String> asiakasLista = new ArrayList<>();
        //yhteys
        try {
            Connection yhteys = yhteysluokka.getYhteys();
            if (yhteys == null) {
                System.err.println("Tietokantayhteys epäonnistui.");
                return asiakasLista;
            }
            //sql scripti
            String sql = """
                SELECT asiakas_id, asiakkaan_nimi, asiakkaan_sahkoposti, 
                       puhelinnumero, koti_osoite
                FROM asiakkaat
            """;
            //Statementilla yhteys
            Statement statement = yhteys.createStatement();
            //yhteys ja sql scripti
            ResultSet resultSet = statement.executeQuery(sql);
            //loopilla tiedot sarakkeisiin
            while (resultSet.next()) {
                int asiakasId = resultSet.getInt("asiakas_id");
                String nimi = resultSet.getString("asiakkaan_nimi");
                String sahkoposti = resultSet.getString("asiakkaan_sahkoposti");
                String puhelin = resultSet.getString("puhelinnumero");
                String osoite = resultSet.getString("koti_osoite");
                //ja riville
                String rivi = "ID: " + asiakasId +
                        ", Nimi: " + nimi +
                        ", Sähköposti: " + sahkoposti +
                        ", Puhelin: " + puhelin +
                        ", Osoite: " + osoite;

                asiakasLista.add(rivi);
            }
        //error handling
        } catch (Exception e) {
            e.printStackTrace();
        }
        //valmislista
        return asiakasLista;
    }
}
