package com.example.ohjelmistotuotanto;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

    public ArrayList<String> asiakasRaportti(Yhteysluokka olio){
        ArrayList<String> raportti =new ArrayList<>();

        try{
            Connection lokalYhteys= olio.getYhteys();
            if (lokalYhteys== null){
                System.err.println("Yhdistys epäonnistui");
                return raportti;
            }
            //sql script komento
            String asiakasSql = """
                SELECT asiakas_id, asiakkaan_nimi, asiakkaan_sahkoposti, 
                       puhelinnumero, koti_osoite
                FROM asiakkaat
            """;
            PreparedStatement stmt = lokalYhteys.prepareStatement(asiakasSql);

            //yhteys ja sql scripti sinne
            ResultSet asiRs = stmt.executeQuery();

            //loopilla tiedot
            while (asiRs.next()) {
                int asiakasId = asiRs.getInt("asiakas_id");
                String nimi = asiRs.getString("asiakkaan_nimi");
                String sahkoposti = asiRs.getString("asiakkaan_sahkoposti");
                String puhelin = asiRs.getString("puhelinnumero");
                String osoite = asiRs.getString("koti_osoite");
                //ja riville
                String rivi1 = "ID: " + asiakasId + " | Nimi: " + nimi + " | Sähköposti: " + sahkoposti;
                String rivi2 =  "Puhelin: " + puhelin + " | Osoite: " + osoite;
                String rivi3 = "";

                raportti.add(rivi1);
                raportti.add(rivi2);
                raportti.add(rivi3);
            }
            //error handling
        } catch (Exception e) {
            e.printStackTrace();
        }

        return raportti;
    }
}
