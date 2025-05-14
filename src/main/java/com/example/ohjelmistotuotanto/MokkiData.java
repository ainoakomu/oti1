package com.example.ohjelmistotuotanto;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * kasitellaan tietokanna mokkit-taulun dataa, jota kaytetaan muissa ikkunoissa eri toimintoihin
 * lisaa,poista,muokka, etsi
 */
public class MokkiData {

    /**
     * tehdaan arraylisti joka sisaltaa tiedot kaikista mokeista, jotka saadaan esille visuaalisesti listviewn avulla
     * @param yhteysluokka yhteys tietokantaan
     * @return haluttu tietuelista
     */
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
                        ", Hinta/yö: " + hinta +
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

    /**
     * haetaan mokit-taulun datasta mokin id korreloiva hinta per yo hashmap
     * @param yhteysluokka yhteys tietokantaan
     * @return haluttu hashmap tietueista
     */
    public static Map<Double, Integer> haeMokinHinta(Yhteysluokka yhteysluokka) {
        //key-value mappi, johon ID ja siihen liityvä hinta tallennetaan
        Map<Double, Integer> hintalista = new HashMap<>();

        //yritetään saada yhteysluokan yhteys
        try {
            Connection conn = yhteysluokka.getYhteys();
            if (conn == null) {
                System.err.println("Tietokantayhteys epäonnistui.");
                return hintalista;
            }
            //vaan id ja hinta per yö sarakkeet
            String sql = "SELECT mokki_id, hinta_per_yo FROM mokit";
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            //kerätää loopilla
            while (resultSet.next()) {
                int id = resultSet.getInt("mokki_id");
                double hinta = resultSet.getDouble("hinta_per_yo");
                hintalista.put(hinta, id);
            }
            //error catching
        } catch (Exception e) {
            e.printStackTrace();
        }
        //palautetaan hinta data
        return hintalista;
    }

    /**
     * lisataan tietokantaa uusi mokki kaikilla tiedoilla
     * @param yhteysluokka yhteys tietokantaan
     * @param mokkiID uuden mokin id
     * @param hintaPerYo uuden mokin hinta per yo
     * @param mokinOsoite uuden mokin osoite
     * @param neliot uuden mokin koko nelioina
     * @param vuodepaikat uuden mokin vuodepaikat
     * @param rantasauna onko uudella mokilla rantasauna
     * @param omaranta onko uudella mokilla oma ranta
     * @param wifi onko uudella mokilla wifi
     * @param sisavessa onko uudella mokilla sisavessa
     * @param palju onko uudella mokilla palju
     */
    public void lisaaMokki(Yhteysluokka yhteysluokka, int mokkiID, double hintaPerYo,
                           String mokinOsoite, int neliot, int vuodepaikat, boolean rantasauna, boolean omaranta,
                           boolean wifi, boolean sisavessa, boolean palju){

        MokkiLuokka mokkiLuokka = new MokkiLuokka();
        try {
            Connection yhteys = yhteysluokka.getYhteys();
            if (yhteys == null) {
                System.err.println("Tietokantayhteys epäonnistui.");
                return;
            }
            String sql = "insert into mokit values (?,?,?,?,?,?,?,?,?,?);";
            PreparedStatement stmt = yhteys.prepareStatement(sql);
            stmt.setInt(1, mokkiID);
            stmt.setDouble(2, hintaPerYo);
            stmt.setString(3, mokinOsoite);
            stmt.setInt(4, neliot);
            stmt.setInt(5, vuodepaikat);
            stmt.setBoolean(6, rantasauna);
            stmt.setBoolean(7,omaranta);
            stmt.setBoolean(8,wifi);
            stmt.setBoolean(9,sisavessa);
            stmt.setBoolean(10,palju);
            stmt.executeUpdate();

            mokkiLuokka.setMokkiID(0);
            mokkiLuokka.setHintaPerYo(0);
            mokkiLuokka.setMokinOsoite("");
            mokkiLuokka.setNeliot(0);
            mokkiLuokka.setVuodepaikat(0);
            mokkiLuokka.setRantasauna(false);
            mokkiLuokka.setOmaranta(false);
            mokkiLuokka.setWifi(false);
            mokkiLuokka.setSisavessa(false);
            mokkiLuokka.setPalju(false);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * muokataan mokin tietoja mokit-taulussa tietokannassa
     * @param yhteysluokka yhteys tietokantaan
     * @param mokkiID uusi id
     * @param hintaPerYo uusi hinta per yo
     * @param mokinOsoite uusi osoite
     * @param neliot uusi koko nelioina
     * @param vuodepaikat uusi maara vuodepaikkoja
     * @param rantasauna onko rantasaunaa
     * @param omaranta onko omaa rantaa
     * @param wifi onko wifia
     * @param sisavessa onko sisavessa
     * @param palju onko paljua
     */
    public void muokkaaMokkia(Yhteysluokka yhteysluokka, int mokkiID, double hintaPerYo,
                              String mokinOsoite, int neliot, int vuodepaikat, boolean rantasauna, boolean omaranta,
                              boolean wifi, boolean sisavessa, boolean palju){

        try {
            Connection yhteys = yhteysluokka.getYhteys();
            if (yhteys == null) {
                System.err.println("Tietokantayhteys epäonnistui.");
                return;
            }
            String sql = "UPDATE mokit SET hinta_per_yo = ?, osoite = ?, neliot = ?, vuodepaikat = ?, onko_rantasauna = ?, onko_oma_ranta = ?, onko_wifi = ?, onko_sisa_wc = ?, onko_palju = ? WHERE mokki_id = ?;";
            PreparedStatement stmt = yhteys.prepareStatement(sql);
            stmt.setDouble(1, hintaPerYo);
            stmt.setString(2, mokinOsoite);
            stmt.setInt(3, neliot);
            stmt.setInt(4, vuodepaikat);
            stmt.setBoolean(5, rantasauna);
            stmt.setBoolean(6, omaranta);
            stmt.setBoolean(7, wifi);
            stmt.setBoolean(8, sisavessa);
            stmt.setBoolean(9, palju);
            stmt.setInt(10, mokkiID);
            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * muokataan tietokantaa poistamalla valittu mokki tietokannasta
     * @param yhteysluokka yhteys tietokantaan
     * @param mokkiID poistettavan mokin id
     */
    public void poistaMokki(Yhteysluokka yhteysluokka, int mokkiID){
        try {
            Connection yhteys = yhteysluokka.getYhteys();
            if (yhteys == null) {
                System.err.println("Tietokantayhteys epäonnistui.");
                return;
            }
            String sql = "DELETE FROM mokit WHERE mokki_id = ?";
            PreparedStatement st = yhteys.prepareStatement(sql);
            st.setInt(1,mokkiID);
            st.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * etsitaan generoidun id avulla, onko sellainen jo tietokannassa.
     * jos id loytyy palautetaan se, jos ei palautetaan false
     * @param yhteysluokka yhteys tietokantaan
     * @param mokkiID id jolla etsitaan toista samanlaista
     * @return loydetty id tietokannasta tai true jos sita ei ole
     */
    public static boolean haeMokinID(Yhteysluokka yhteysluokka, int mokkiID) {
        //yritetään saada yhteysluokan yhteys
        try {
            Connection conn = yhteysluokka.getYhteys();
            if (conn == null) {
                System.err.println("Tietokantayhteys epäonnistui.");
                return true;
            }
            //vaan id ja hinta per yö sarakkeet
            String sql = "SELECT COUNT(*) FROM mokit WHERE mokki_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, mokkiID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0; // true = ID on käytössä
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

}
