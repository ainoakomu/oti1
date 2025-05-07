package com.example.ohjelmistotuotanto;

import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.io.IOException;
import java.util.Date;

// lähteet mistä opeteltu:
//https://www.tutorialspoint.com/pdfbox/index.htm
//https://carbonrider.github.io/pdfbox_tutorial/pdf_add_image.html

//tässä tehdään lasku
public class LaskunLuonti {

    private int varausNro;
    private String asiakkaanNimi;
    private Date alkuPvm;
    private Date loppuPvm;
    private int hinta;
    private int kayttajaNro;
    private int laskuNro;

    public void luoLasku(int varaus){
        try {
            setLaskuNro(1590);
            setVarausNro(varaus);
            // setAsiakkaanNimi --> tähän sql-query jolla haetaan varauksesta asiakkaan IDtä vastaava asiakkaan nimi
            // setAlkuPvm --> tähän sql-query jolla haetaan varauksesta alkupäivä
            // setLoppuPvm --> tähän sql-query jolla haetaan varauksesta loppupäivä
            // setHinta --> tähän sql-query jolla haetaan varauksesta hinta
            // setKayttajaNro --> setataan kirjautuneen käyttäjän ID


            // Luodaan dokumentti
            PDDocument dokumentti = new PDDocument();

            // Lisätään sivu dokumenttiin
            PDPage sivu = new PDPage(PDRectangle.A4);
            dokumentti.addPage(sivu);

            //asiakkaan ja laskun tiedot
            setAsiakkaanNimi("Pekka"); //korvataan getAsiakkaanNimi-metodilla

            //laskutiedoston nimi
            String laskuTiedosto = "Laskut/"+getLaskuNro() +".pdf";

            //laskulle tulostuvat rivit
            String rivi1 = "Lasku";
            String rivi2 = "Lasku nro: " + getLaskuNro();
            String rivi3 = "Asiakas: " + getAsiakkaanNimi();
            String rivi4 = "Kiitos, kun vierailitte Mökkikodeilla!";
            String rivi5 = "";

            String[] rivit = {rivi1,rivi2,rivi3,rivi4,rivi5};

            // stream
            PDPageContentStream contentStream = new PDPageContentStream(dokumentti, sivu);

            // lisätään logo
            PDImageXObject logoKuva = PDImageXObject.createFromFile("logo.png", dokumentti);
            contentStream.drawImage(logoKuva,20,675);

            // lisätään tekstit
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
            contentStream.setLeading(14.5f);
            contentStream.newLineAtOffset(80, 650);

            for(int i=0;i< rivit.length;i++){
                contentStream.showText(rivit[i]);
                contentStream.newLineAtOffset(0, -14.5f);
                contentStream.newLine();
            }
            contentStream.endText();

            // Sulje stream
            contentStream.close();

            // Tallennus ja dokumentin sulku
            dokumentti.save(laskuTiedosto);
            dokumentti.close();

            System.out.println("Lasku nro " + getLaskuNro() + " luotu ja tallennettu.");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static void main(String[] args) throws Exception {
        LaskunLuonti laskutus = new LaskunLuonti();
        laskutus.luoLasku(4466);
    }





    public int getVarausNro() {
        return varausNro;
    }

    public void setVarausNro(int vararusNro) {
        this.varausNro = vararusNro;
    }

    public String getAsiakkaanNimi() {
        return asiakkaanNimi;
    }

    public void setAsiakkaanNimi(String asiakkaanNimi) {
        this.asiakkaanNimi = asiakkaanNimi;
    }

    public Date getAlkuPvm() {
        return alkuPvm;
    }

    public void setAlkuPvm(Date alkuPvm) {
        this.alkuPvm = alkuPvm;
    }

    public Date getLoppuPvm() {
        return loppuPvm;
    }

    public void setLoppuPvm(Date loppuPvm) {
        this.loppuPvm = loppuPvm;
    }

    public int getKayttajaNro() {
        return kayttajaNro;
    }

    public void setKayttajaNro(int kayttajaNro) {
        this.kayttajaNro = kayttajaNro;
    }

    public int getLaskuNro() {
        return laskuNro;
    }

    public void setLaskuNro(int laskuNro) {
        this.laskuNro = laskuNro;
    }

    public int getHinta() {
        return hinta;
    }

    public void setHinta(int hinta) {
        this.hinta = hinta;
    }
}
