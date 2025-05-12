package com.example.ohjelmistotuotanto;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class VarausData {


    //metodi joka hakee
    public static ArrayList<String> haeVaraukset(Yhteysluokka olio){
        ArrayList<String> varauslista=new ArrayList<>();
        //yritetään yhteysluokan olion yhteys saada
        try{
            Connection lokalYhteys= olio.getYhteys();
            if (lokalYhteys== null){
                System.err.println("Yhdistys epäonnistui");
                return varauslista;
            }
            //sql script komento
            String sql = """
                SELECT varaus_id, varausalku_date, varausloppu_date, hinta, 
                       kayttaja_id, asiakas_id, mokki_id
                 FROM varaukset
            """;
            //statement saa yhteyden
            Statement stmt = lokalYhteys.createStatement();
            //yhteys ja sql scripti sinne
            ResultSet rs = stmt.executeQuery(sql);

            //loopilla tiedot
            while (rs.next()) {
                int varausId = rs.getInt("varaus_id");
                Timestamp alku = rs.getTimestamp("varausalku_date");
                Timestamp loppu = rs.getTimestamp("varausloppu_date");
                int hinta = rs.getInt("hinta");
                int kayttajaId = rs.getInt("kayttaja_id");
                int asiakasId = rs.getInt("asiakas_id");
                int mokkiId = rs.getInt("mokki_id");

                //rivit
                String rivi = "Varaus ID: " + varausId +
                        ", Alku: " + alku +
                        ", Loppu: " + loppu +
                        ", Hinta: " + hinta + " €" +
                        ", Käyttäjä ID: " + kayttajaId +
                        ", Asiakas ID: " + asiakasId +
                        ", Mökki ID: " + mokkiId;

                varauslista.add(rivi);
            }
        //error handling
        } catch (Exception e) {
            e.printStackTrace();
        }
        //valmis lista
        return varauslista;
    }

    public ArrayList<String> varausRaportti(Yhteysluokka olio, LocalDate alkaen, LocalDate asti){
        ArrayList<String> raportti =new ArrayList<>();
        //yritetään yhteysluokan olion yhteys saada

        LocalDateTime lahtien = alkaen.atStartOfDay();
        Timestamp tsLahtien = Timestamp.valueOf(lahtien);
        LocalDateTime saakka = asti.atStartOfDay();
        Timestamp tsSaakka = Timestamp.valueOf(saakka);

        try{
            Connection lokalYhteys= olio.getYhteys();
            if (lokalYhteys== null){
                System.err.println("Yhdistys epäonnistui");
                return raportti;
            }
            //sql script komento
            String varausSql = """
                SELECT varaus_id, varausalku_date, varausloppu_date, hinta, 
                       kayttaja_id, asiakas_id, mokki_id
                 FROM varaukset
                 WHERE varausalku_date >= ? AND varausloppu_date <= ?
            """;
            PreparedStatement stmt = lokalYhteys.prepareStatement(varausSql);
            stmt.setTimestamp(1, tsLahtien);
            stmt.setTimestamp(2, tsSaakka);

            //statement saa yhteyden
            //Statement stmt = lokalYhteys.createStatement();
            //yhteys ja sql scripti sinne
            ResultSet varRs = stmt.executeQuery();


            //loopilla tiedot
            while (varRs.next()) {
                int varausId = varRs.getInt("varaus_id");
                Timestamp alku = varRs.getTimestamp("varausalku_date");
                Timestamp loppu = varRs.getTimestamp("varausloppu_date");
                int hinta = varRs.getInt("hinta");
                int kayttajaId = varRs.getInt("kayttaja_id");
                int asiakasId = varRs.getInt("asiakas_id");
                int mokkiId = varRs.getInt("mokki_id");

                //rivit
                String rivi1 = "Varaus: " + varausId +
                        " | Mökki: " + mokkiId +
                        " | Välillä: " + alku + " - " + loppu;
                String rivi2 = " | Hinta: " + hinta + " €" +
                        " | Varauksen tekijä: " + kayttajaId;
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

    public ArrayList<String> haeTaloustiedot(Yhteysluokka olio){
        ArrayList<String> talouslista=new ArrayList<>();


        //yritetään yhteysluokan olion yhteys saada
        try{
            Connection lokalYhteys= olio.getYhteys();
            if (lokalYhteys== null){
                System.err.println("Yhdistys epäonnistui");
            }
            //sql script komento
            String talousSql = """
                SELECT varaus_id, varausalku_date, varausloppu_date, hinta, kayttaja_id
                 FROM varaukset
            """;
            PreparedStatement stmt = lokalYhteys.prepareStatement(talousSql);


            ResultSet rs = stmt.executeQuery();

            //loopilla tiedot
            while (rs.next()) {
                int varausId = rs.getInt("varaus_id");
                int hinta = rs.getInt("hinta");
                int kayttajaId = rs.getInt("kayttaja_id");


                //rivit
                String rivi = "Varaus ID: " + varausId +
                        ", Hinta: " + hinta + " €" +
                        ", Käyttäjä ID: " + kayttajaId;

                talouslista.add(rivi);
            }
            //error handling
        } catch (Exception e) {
            e.printStackTrace();
        }
        //valmis lista
        return talouslista;
    }

    public ArrayList<String> talousRaportti(Yhteysluokka olio, LocalDate alkaen, LocalDate asti){
        ArrayList<String> raportti =new ArrayList<>();
        //yritetään yhteysluokan olion yhteys saada

        LocalDateTime lahtien = alkaen.atStartOfDay();
        Timestamp tsLahtien = Timestamp.valueOf(lahtien);
        LocalDateTime saakka = asti.atStartOfDay();
        Timestamp tsSaakka = Timestamp.valueOf(saakka);

        try{
            Connection lokalYhteys= olio.getYhteys();
            if (lokalYhteys== null){
                System.err.println("Yhdistys epäonnistui");
                return raportti;
            }
            //sql script komento
            String talousSql = """
                SELECT varaus_id, varausalku_date, varausloppu_date, hinta, kayttaja_id
                 FROM varaukset
                 WHERE varausalku_date >= ? AND varausloppu_date <= ?
            """;
            PreparedStatement stmt = lokalYhteys.prepareStatement(talousSql);
            stmt.setTimestamp(1, tsLahtien);
            stmt.setTimestamp(2, tsSaakka);

            //statement saa yhteyden
            //Statement stmt = lokalYhteys.createStatement();
            //yhteys ja sql scripti sinne
            ResultSet talRs = stmt.executeQuery();

            int tuotto = 0;

            //loopilla tiedot
            while (talRs.next()) {
                int varausId = talRs.getInt("varaus_id");
                Timestamp alku = talRs.getTimestamp("varausalku_date");
                Timestamp loppu = talRs.getTimestamp("varausloppu_date");
                int hinta = talRs.getInt("hinta");
                int kayttajaId = talRs.getInt("kayttaja_id");

                //rivit
                String rivi1 = "Varaus: " + varausId +
                        " | Välillä: " + alku + " - " + loppu;
                String rivi2 = " | Tuotto: " + hinta + " €" +
                        " | Varauksen tekijä: " + kayttajaId;
                String rivi3 = "";

                raportti.add(rivi1);
                raportti.add(rivi2);
                raportti.add(rivi3);

                tuotto += hinta;
            }

            raportti.add("Tuotto yhteensä: " + String.valueOf(tuotto)+ " €");

            //error handling
        } catch (Exception e) {
            e.printStackTrace();
        }

        return raportti;
    }


}
