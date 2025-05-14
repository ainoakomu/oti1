package com.example.ohjelmistotuotanto;

import javafx.scene.control.TextField;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * kasitellaan tietokanna varausdataa ja hyodynnetaan metoja hae, poista, muokkaa ja tarkista
 * luodaan myos reporttien metodit
 */
public class VarausData {


    /**
     * haetaan kaikki varaukset tietokannasta
     * @param olio yhteys tietokantaan
     * @return varauksien tiedot arraylistana
     */
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

    /**
     * luodaan varauksille oma raportti johon lisataan tietoja tietokannasta
     * @param olio yhteys tietokantaan
     * @param alkaen raportin alkamispvm
     * @param asti raportin loppumispvm
     * @return raportti arraylistana
     */
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

    /**
     * haetaan taloustietoja tietokannasta
     * @param olio yhteys tietokantaan
     * @return taloustiedot arraylistana
     */
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

    /**
     * luodaan taloustiedoille oma raportti, johon lisataan tietoja tietokannasta
     * @param olio yhteys tietokantaan
     * @param alkaen raportin alkamispvm
     * @param asti raportin loppumispvm
     * @return raportti tieto arraylistana
     */
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

    /**
     * etsitaan onko varaus olemassa tietylla generoidulla numerolla
     * @param yhteysluokka yhteys tietokantaan
     * @param varusnumero verrattava varausnumero
     * @return false jos epaoonistuis, true jos uniikki, numero jos jo olemassa
     */
    public boolean tarkistaVarausID(Yhteysluokka yhteysluokka, Integer varusnumero){

        boolean olemassa = false;

        try {
            Connection lokalYhteys = yhteysluokka.getYhteys();
            if (lokalYhteys == null) {
                System.err.println("Yhdistys epäonnistui");
                return false;
            }

            // SQL query to check if varaus_id olemassa
            String asiakasSql = "SELECT COUNT(*) FROM varaukset WHERE varaus_id = ?";
            PreparedStatement stmt = lokalYhteys.prepareStatement(asiakasSql);
            stmt.setInt(1, varusnumero);

            // Execute the query and check the result
            ResultSet asiRs = stmt.executeQuery();
            if (asiRs.next()) {
                int count = asiRs.getInt(1);
                if (count > 0) {
                    olemassa = true; // If count > 0, varaus_id olemassa
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return olemassa;
    }

    /**
     * muokataan varauksen tietoja tietokannassa
     * @param yhteysluokka yhteys tietokantaan
     * @param varausID muokattava varaus id
     * @param alkupv muokattava varauksen alkupvm
     * @param loppupv muokattava varauksen loppupvm
     * @param hinta muokattava varauksen hinta
     * @param kayttaja_id muokattavan varausen kayttajan id
     * @param mokki_id muokattavan varauksen mokin id
     * @param asiakas_id muokattavan varausen asiakkaan id
     */
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

    /**
     * poistetaan varaus id mukaan tietokannasta pysyvasti
     * @param yhteysluokka yhteys tietokantaan
     * @param varID varaus joka poistetaan
     */
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
