package com.example.ohjelmistotuotanto;

import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.Random;

// lähteet mistä opeteltu:
//https://www.tutorialspoint.com/pdfbox/index.htm
//https://carbonrider.github.io/pdfbox_tutorial/pdf_add_image.html

/**
 * luodaan metodeja jotka rakentavat laskun ja laskutusmenetelman
 * lahteet mista opeteltu:
 * <a href="https://www.tutorialspoint.com/pdfbox/index.htm">...</a>
 * <a href="https://carbonrider.github.io/pdfbox_tutorial/pdf_add_image.html">...</a>
 */
public class LaskunLuonti {
    /**
     * laskussa olevan varauksen id numero
     */
    private int varausNro;
    /**
     * laskussa olevan asiakkaan nimi
     */
    private String asiakkaanNimi;
    /**
     * laskussa olevan asiakkaan id
     */
    private int asiakasID;
    /**
     * laskussa olevan mokin id
     */
    private int mokkiID;
    /**
     * laskussa oleva mokin osoite
     */
    private String mokinOsoite;
    /**
     * laskussa oleva varauksen alkupaivamaara
     */
    private LocalDate alkuPvm;
    /**
     * laskussa oleva varauksen loppupaivamaara
     */
    private LocalDate loppuPvm;
    /**
     * laskussa oleva varauksen hin
     */
    private int hinta;
    /**
     * laskussa oleva kayttajanumeron
     */
    private int kayttajaNro;
    /**
     * laskun identifioiva numero
     */
    private int laskuNro;

    /**
     * luodaan lasku tietokaantaan
     *
     * @param yhteysluokka yhteys tieokantaan
     * @param varausnro    identifioiva laskunumero
     */
    public void luoLasku(Yhteysluokka yhteysluokka, int varausnro) {

        try {
            //haetaan laskulle varauksen tiedot
            try {
                Connection lokalYhteys = yhteysluokka.getYhteys();
                if (lokalYhteys == null) {
                    System.err.println("Yhdistys epäonnistui");
                    return;
                }
                //sql script komento
                String sql = """
                        SELECT varausalku_date, varausloppu_date, hinta,
                               kayttaja_id, asiakas_id, mokki_id FROM varaukset WHERE varaus_id = ?
                        """;
                //statement saa yhteyden
                PreparedStatement stmt = lokalYhteys.prepareStatement(sql);
                stmt.setInt(1, varausnro);

                //yhteys ja sql scripti sinne
                ResultSet rs = stmt.executeQuery();

                //loopilla tiedot
                while (rs.next()) {
                    Timestamp alku = rs.getTimestamp("varausalku_date");
                    Timestamp loppu = rs.getTimestamp("varausloppu_date");
                    int hinta = rs.getInt("hinta");
                    int kaId = rs.getInt("kayttaja_id");
                    int asId = rs.getInt("asiakas_id");
                    int moId = rs.getInt("mokki_id");

                    setAsiakasID(asId);
                    setMokkiID(moId);
                    setKayttajaNro(kaId);
                    setHinta(hinta);
                    setAlkuPvm(alku.toLocalDateTime().toLocalDate());
                    setLoppuPvm(loppu.toLocalDateTime().toLocalDate());
                }
                //error handling
            } catch (Exception e) {
                e.printStackTrace();
            }
            setLaskuNro(laskuID(yhteysluokka));
            setVarausNro(varausnro);
            setMokinOsoite(mokinOsoite(yhteysluokka, getMokkiID()));

            // Luodaan dokumentti
            PDDocument dokumentti = new PDDocument();

            // Lisätään sivu dokumenttiin
            PDPage sivu = new PDPage(PDRectangle.A4);
            dokumentti.addPage(sivu);

            //laskutiedoston nimi
            String laskuTiedosto = "Laskut/" + getLaskuNro() + ".pdf";

            //laskulle tulostuvat rivit
            String rivi1 = "Majoituslasku";
            String rivi3 = "Päiväys: " + LocalDate.now();
            String rivi5 = "Lasku nro: " + getLaskuNro();
            String rivi6 = "Asiakas: " + asiakkaanNimi(yhteysluokka, getAsiakasID());
            String rivi7 = asiakkaanYhteystiedot(yhteysluokka, getAsiakasID());
            String rivi8 = "";
            String rivi9 = "Kiitos, kun vierailitte MökkiKodeilla!";
            String rivi10 = "";
            String rivi11 = "Majoitus kohteessa: " + getMokinOsoite();
            String rivi12 = "Ajalta: " + getAlkuPvm() + " - " + getLoppuPvm();
            String rivi14 = "Majoituksen loppusumma: " + getHinta() + " €";
            String rivi15 = "";
            String rivi16 = "Saaja: Mökkikodit Oy";
            String rivi17 = "Eräpäivä: " + LocalDate.now().plusDays(14);
            String rivi18 = "Viitenumero: " + getVarausNro();
            String rivi19 = "Saajan tilinumero: FI12 3456 7890 1234 56";
            String rivi20 = "";
            String rivi21 = "Sydämellisesti tervetuloa uudestaan!";
            String rivi22 = "Halutessanne voitte antaa meille asiakaspalautetta";
            String rivi23 = "sosiaalisen median kanavissamme @mokkikodit tai ";
            String rivi24 = "sähköpostitse osoitteeseen info@mokkikodit.fi";

            String[] rivit = {rivi1, rivi3, rivi5, rivi6, rivi7, rivi8, rivi9, rivi10,
                    rivi11, rivi12, rivi14, rivi15, rivi16, rivi17, rivi18, rivi19, rivi20,
                    rivi21, rivi22, rivi23, rivi24};

            // stream
            PDPageContentStream contentStream = new PDPageContentStream(dokumentti, sivu);

            // lisätään logo
            PDImageXObject logoKuva = PDImageXObject.createFromFile("logo.png", dokumentti);
            contentStream.drawImage(logoKuva, 20, 675);

            // lisätään tekstit
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
            contentStream.setLeading(14.5f);
            contentStream.newLineAtOffset(80, 650);

            for (int i = 0; i < rivit.length; i++) {
                contentStream.showText(rivit[i]);
                contentStream.newLineAtOffset(0, -14.5f);
                contentStream.newLine();
            }
            contentStream.endText();

            // Sulje stream
            contentStream.close();

            // Tallennus ja dokumentin sulku
            dokumentti.save(laskuTiedosto);

            LaskutData laskutData = new LaskutData();
            laskutData.tallennaLaskutiedot(yhteysluokka, getLaskuNro(), getVarausNro(), false, false);

            dokumentti.close();

            System.out.println("Lasku nro " + getLaskuNro() + " luotu ja tallennettu.");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * generoidaan uusi laskuid
     *
     * @param yhteysolio yhteys tietokantaan
     * @return uusi generoitu laskun id
     */
    public int laskuID(Yhteysluokka yhteysolio) {
        Random random = new Random();

        for (int i = 0; i < 100; i++) {
            int laskunumero = 100 + random.nextInt(900); // 3-digit number

            int exists = tarkistaLaskuID(yhteysolio, laskunumero);

            if (exists == 0) {
                System.out.println("Löytyi uniikki laskun ID: " + laskunumero);
                return laskunumero;
            } else {
                System.out.println("Laskun ID " + laskunumero + " on jo olemassa.");
            }
        }

        throw new RuntimeException("Ei löytynyt vapaata laskunumeroa 100 yrityksen jälkeen.");
    }

    /**
     * etsitaan tietokannasta laskua tietylla laskuidlla.
     * 0 laskua etsitylla id ei ole tai palauttaa loydetyn laskuid
     *
     * @param yhteysluokka yhteysluokka
     * @param laskunnumero verrattava laskuid
     * @return 0 jos id ei ole tai laskun id kokonaisuudessaan
     */
    public int tarkistaLaskuID(Yhteysluokka yhteysluokka, Integer laskunnumero) {

        int laskunID = 0;

        try {
            Connection lokalYhteys = yhteysluokka.getYhteys();
            if (lokalYhteys == null) {
                System.err.println("Yhdistys epäonnistui");
            }
            //sql script komento
            String sql = """
                        SELECT lasku_id FROM laskut WHERE lasku_id = ?;
                    """;
            PreparedStatement stmt = lokalYhteys.prepareStatement(sql);
            stmt.setInt(1, laskunnumero);

            //yhteys ja sql scripti sinne
            ResultSet rs = stmt.executeQuery();

            //loopilla tiedot
            if (rs.next()) {
                laskunID = rs.getInt("lasku_id");
            }
            //error handling
        } catch (Exception e) {
            e.printStackTrace();
        }

        return laskunID;
    }

    /**
     * etsitaan tietokannasta asiakasid avulla sen asiakkaan oikea nimi
     *
     * @param yhteysluokka yhteys tietokantaan
     * @param asNro        asiakkan id
     * @return asiakkaan nimi tai tyhjaa jos sita ei ole
     */
    public String asiakkaanNimi(Yhteysluokka yhteysluokka, int asNro) {

        String asNimi = "";

        try {
            Connection lokalYhteys = yhteysluokka.getYhteys();
            if (lokalYhteys == null) {
                System.err.println("Yhdistys epäonnistui");
            }
            //sql script komento
            String asiakasSql = """
                        SELECT asiakkaan_nimi FROM asiakkaat WHERE asiakas_id = ?;
                    """;
            PreparedStatement stmt = lokalYhteys.prepareStatement(asiakasSql);
            stmt.setInt(1, asNro);

            //yhteys ja sql scripti sinne
            ResultSet asiRs = stmt.executeQuery();

            //loopilla tiedot
            if (asiRs.next()) {
                asNimi = asiRs.getString("asiakkaan_nimi");
            }
            //error handling
        } catch (Exception e) {
            e.printStackTrace();
        }

        return asNimi;
    }

    /**
     * etsitaan asiakasnumerolla asiakkaan yhteystiedot
     * jos ei ole palautetaan tyhjaa
     *
     * @param yhteysluokka yhteys tietokantaan
     * @param asNro        asiakkaan numero jolla tietoja etsitaan
     * @return asiakkaan tiedot tai tyhjaa
     */
    public String asiakkaanYhteystiedot(Yhteysluokka yhteysluokka, int asNro) {

        String asPuh = "";
        String asSpo = "";
        String asOso = "";

        try {
            Connection lokalYhteys = yhteysluokka.getYhteys();
            if (lokalYhteys == null) {
                System.err.println("Yhdistys epäonnistui");
            }
            //sql script komento
            String asiakasSql = """
                        SELECT puhelinnumero, asiakkaan_sahkoposti, koti_osoite FROM asiakkaat WHERE asiakas_id = ?;
                    """;
            PreparedStatement stmt = lokalYhteys.prepareStatement(asiakasSql);
            stmt.setInt(1, asNro);

            //yhteys ja sql scripti sinne
            ResultSet asiRs = stmt.executeQuery();

            //loopilla tiedot
            if (asiRs.next()) {
                asPuh = asiRs.getString("puhelinnumero");
                asSpo = asiRs.getString("asiakkaan_sahkoposti");
                asOso = asiRs.getString("koti_osoite");
            }
            //error handling
        } catch (Exception e) {
            e.printStackTrace();
        }

        String yhteystiedot = asPuh + " | " + asSpo + " | " + asOso;

        return yhteystiedot;
    }



    /**
     * haetaan tietokannasta mokin id avulla mokin osoite
     *
     * @param yhteysluokka yhteys tietokantaan
     * @param mokkiID      mokin id milla etsitaan
     * @return mokin osoite tai tyhjaa
     */
    public String mokinOsoite(Yhteysluokka yhteysluokka, int mokkiID) {

        String osoite = "";

        //yritetään saada yhteysluokan yhteys
        try {
            Connection conn = yhteysluokka.getYhteys();
            if (conn == null) {
                System.err.println("Tietokantayhteys epäonnistui.");
            }
            //vaan id ja hinta per yö sarakkeet
            String sql = "SELECT osoite FROM mokit WHERE mokki_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, mokkiID);

            //yhteys ja sql scripti sinne
            ResultSet rs = stmt.executeQuery();
            //loopilla tiedot
            if (rs.next()) {
                osoite = rs.getString("osoite");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return osoite;
    }


    /**
     * haetaan varauksen numero
     *
     * @return varauksen numero
     */
    public int getVarausNro() {
        return varausNro;
    }

    /**
     * asetetaan varauksen numero
     *
     * @param vararusNro haluttu numero
     */
    public void setVarausNro(int vararusNro) {
        this.varausNro = vararusNro;
    }

    /**
     * haetaan asiakkaan nimi
     *
     * @return asiakkaan nimi
     */
    public String getAsiakkaanNimi() {
        return asiakkaanNimi;
    }

    /**
     * asetetaan asiakkaan nimi
     *
     * @param asiakkaanNimi haluttu nimi
     */
    public void setAsiakkaanNimi(String asiakkaanNimi) {
        this.asiakkaanNimi = asiakkaanNimi;
    }

    /**
     * haetaan varauksen alkupvm
     *
     * @return alkupvm
     */
    public LocalDate getAlkuPvm() {
        return alkuPvm;
    }

    /**
     * asetetaan varauksen alkupvm
     *
     * @param alkuPvm varauksen alku
     */
    public void setAlkuPvm(LocalDate alkuPvm) {
        this.alkuPvm = alkuPvm;
    }

    /**
     * haetaan varauksen loppupvm
     *
     * @return varauksen loppupvm
     */
    public LocalDate getLoppuPvm() {
        return loppuPvm;
    }

    /**
     * asetetaan varauksen loppupvm
     *
     * @param loppuPvm varauksen loppu
     */
    public void setLoppuPvm(LocalDate loppuPvm) {
        this.loppuPvm = loppuPvm;
    }

    /**
     * haetaan kayttajan numero
     *
     * @return numero
     */
    public int getKayttajaNro() {
        return kayttajaNro;
    }

    /**
     * asetetaan kayttajan numero
     *
     * @param kayttajaNro haluttu numero
     */
    public void setKayttajaNro(int kayttajaNro) {
        this.kayttajaNro = kayttajaNro;
    }

    /**
     * haetaan laskun numero
     *
     * @return numero
     */
    public int getLaskuNro() {
        return laskuNro;
    }

    /**
     * asetetaan laskun numero
     *
     * @param laskuNro haluttu numero
     */
    public void setLaskuNro(int laskuNro) {
        this.laskuNro = laskuNro;
    }

    /**
     * haetaan varauksen hinta
     *
     * @return hinta
     */
    public int getHinta() {
        return hinta;
    }

    /**
     * asetetaan varauksen hinta
     *
     * @param hinta haluttu hinta
     */
    public void setHinta(int hinta) {
        this.hinta = hinta;
    }

    /**
     * haetaan asiakkaan id
     *
     * @return asiakas id
     */
    public int getAsiakasID() {
        return asiakasID;
    }

    /**
     * asetetaan asiakkan id
     *
     * @param asiakasID haluttu id
     */
    public void setAsiakasID(int asiakasID) {
        this.asiakasID = asiakasID;
    }

    /**
     * haetaan mokin id
     *
     * @return id numero
     */
    public int getMokkiID() {
        return mokkiID;
    }

    /**
     * asetetaan mokin id
     *
     * @param mokkiID haluttu id numero
     */
    public void setMokkiID(int mokkiID) {
        this.mokkiID = mokkiID;
    }

    /**
     * haetaan mokin osoite
     *
     * @return osoite
     */
    public String getMokinOsoite() {
        return mokinOsoite;
    }

    /**
     * asetetaan mokin osoite
     *
     * @param mokinOsoite haluttu osoite
     */
    public void setMokinOsoite(String mokinOsoite) {
        this.mokinOsoite = mokinOsoite;
    }

}
