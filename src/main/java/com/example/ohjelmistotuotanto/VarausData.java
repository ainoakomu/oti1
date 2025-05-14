package com.example.ohjelmistotuotanto;

import javafx.scene.control.TextField;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Random;

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
                        ", Hinta: " + hinta +
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

    public ArrayList<String> haeArvostetlut(Yhteysluokka olio){
        ArrayList<String> arvostelulista=new ArrayList<>();

        //yritetään yhteysluokan olion yhteys saada
        try{
            Connection lokalYhteys= olio.getYhteys();
            if (lokalYhteys== null){
                System.err.println("Yhdistys epäonnistui");
            }
            //sql script komento
            String arvosteluSql = """
                SELECT arvostelu_id, varaus_id, arvosana, arvostelu
                 FROM arvostelut
            """;
            PreparedStatement stmt = lokalYhteys.prepareStatement(arvosteluSql);

            ResultSet rs = stmt.executeQuery();

            //loopilla tiedot
            while (rs.next()) {
                int arvosteluID = rs.getInt("arvostelu_id");
                int varausID = rs.getInt("varaus_id");
                Double arvosana = rs.getDouble("arvosana");
                String arvostelu = rs.getString("arvostelu");

                //rivit
                String rivi = "Arvostelu ID: "+ arvosteluID +
                        ", Varaus ID: " + varausID +
                        ", arvosana: " + arvosana +
                        ", arvostelu: " + arvostelu;

                arvostelulista.add(rivi);
            }
            //error handling
        } catch (Exception e) {
            e.printStackTrace();
        }
        //valmis lista
        return arvostelulista;
    }

    /**
     * lisataan uusia kayttajia tietokantaan ja yllapidetaan tietokantaa
     * @param yhteysluokka yhteys tietokantaan
     * @param arvID uuden arvostelun id
     * @param varID varauksen id
     * @param arvosana arvostelun arvosana
     * @param arvostelu arvostelu
     */
    public void lisaaArvostelu(Yhteysluokka yhteysluokka, int arvID, int varID, Double arvosana, String arvostelu){
        try {
            Connection yhteys = yhteysluokka.getYhteys();
            if (yhteys == null) {
                System.err.println("Tietokantayhteys epäonnistui.");
            }
            String sql = "insert into arvostelut values (?,?,?,?);";
            PreparedStatement stmt = yhteys.prepareStatement(sql);
            stmt.setInt(1, arvID);
            stmt.setInt(2, varID);
            stmt.setDouble(3, arvosana);
            stmt.setString(4, arvostelu);
            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * luodaan mahdollisuus poistaa arvostelu kokonaan tietokannasta
     * @param yhteysluokka yhteys tietokantaan
     * @param id poistettavan arvostelun id
     */
    public void poistaArvostelu(Yhteysluokka yhteysluokka, int id){
        try {
            Connection yhteys = yhteysluokka.getYhteys();
            if (yhteys == null) {
                System.err.println("Tietokantayhteys epäonnistui.");
            }
            String sql = "DELETE FROM arvostelut WHERE arvostelu_id = ?";
            PreparedStatement st = yhteys.prepareStatement(sql);
            st.setInt(1,id);
            st.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int arvostelunID(Yhteysluokka yhteysolio) {
        Random random = new Random();

        for (int i = 0; i < 100; i++) {
            int arvostelunro = 100 + random.nextInt(900); // 3-digit number

            int exists = tarkistaArvostelunID(yhteysolio, arvostelunro);

            if (exists == 0) {
                System.out.println("Löytyi uniikki arvostelun ID: " + arvostelunro);
                return arvostelunro;
            } else {
                System.out.println("Arvostelun ID " + arvostelunro + " on jo olemassa.");
            }
        }

        throw new RuntimeException("Ei löytynyt vapaata arvostelunroa 100 yrityksen jälkeen.");
    }


    public int tarkistaArvostelunID(Yhteysluokka yhteysluokka, Integer arvostelunro){

        int arvosteluID = 0;

        try{
            Connection lokalYhteys= yhteysluokka.getYhteys();
            if (lokalYhteys== null){
                System.err.println("Yhdistys epäonnistui");
            }
            //sql script komento
            String sql = """
                SELECT arvostelu_id FROM arvostelut WHERE arvostelu_id = ?;
            """;
            PreparedStatement stmt = lokalYhteys.prepareStatement(sql);
            stmt.setInt(1, arvostelunro);

            //yhteys ja sql scripti sinne
            ResultSet rs = stmt.executeQuery();

            //loopilla tiedot
            if (rs.next()) {
                arvosteluID = rs.getInt("arvostelu_id");
            }
            //error handling
        } catch (Exception e) {
            e.printStackTrace();
        }

        return arvosteluID;
    }




    public boolean tarkistaVarausID(Yhteysluokka yhteysluokka, Integer varusnumero){

        boolean olemassa = false;

        try {
            Connection lokalYhteys = yhteysluokka.getYhteys();
            if (lokalYhteys == null) {
                System.err.println("Yhdistys epäonnistui");
                return false; // If the connection failed, return false.
            }

            // SQL query to check if varaus_id olemassa
            String asiakasSql = "SELECT COUNT(*) FROM varaukset WHERE varaus_id = ?";
            PreparedStatement stmt = lokalYhteys.prepareStatement(asiakasSql);
            stmt.setInt(1, varusnumero);

            // Execute the query and check the result
            ResultSet asiRs = stmt.executeQuery();
            if (asiRs.next()) {
                int count = asiRs.getInt(1);  // Get the count of records
                if (count > 0) {
                    olemassa = true; // If count > 0, varaus_id olemassa
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return olemassa;
    }


    public void muokkaaVarausta(Yhteysluokka yhteysluokka, int varausID, LocalDate alkupv, LocalDate loppupv, TextField hinta, TextField kayttaja_id, TextField mokki_id, TextField asiakas_id){

        LocalDateTime lahtien = alkupv.atStartOfDay();
        Timestamp tsLahtien = Timestamp.valueOf(lahtien);
        LocalDateTime saakka = loppupv.atStartOfDay();
        Timestamp tsSaakka = Timestamp.valueOf(saakka);
        int uusihinta = Integer.parseInt(hinta.getText());
        int uusikayt = Integer.parseInt(kayttaja_id.getText());
        int uusimokki = Integer.parseInt(mokki_id.getText());
        int uusiasiakas = Integer.parseInt(asiakas_id.getText());


        try {
            Connection yhteys = yhteysluokka.getYhteys();
            if (yhteys == null) {
                System.err.println("Tietokantayhteys epäonnistui.");
                return;
            }
            String sql = "UPDATE varaukset SET varausalku_date = ?, varausloppu_date = ?, hinta = ?, kayttaja_id = ?, asiakas_id = ?, mokki_id = ? WHERE varaus_id = ?;";
            PreparedStatement stmt = yhteys.prepareStatement(sql);
            stmt.setTimestamp(1, tsLahtien);
            stmt.setTimestamp(2, tsSaakka);
            stmt.setInt(3, uusihinta);
            stmt.setInt(4, uusikayt);
            stmt.setInt(5, uusiasiakas);
            stmt.setInt(6, uusimokki);
            stmt.setInt(7, varausID);
            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void poistaVaraus(Yhteysluokka yhteysluokka, int varID){
        try {
            Connection yhteys = yhteysluokka.getYhteys();
            if (yhteys == null) {
                System.err.println("Tietokantayhteys epäonnistui.");
                return;
            }
            String sql = "DELETE FROM varaukset WHERE varaus_id = ?";
            PreparedStatement st = yhteys.prepareStatement(sql);
            st.setInt(1,varID);
            st.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
