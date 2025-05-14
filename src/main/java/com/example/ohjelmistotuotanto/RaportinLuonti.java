package com.example.ohjelmistotuotanto;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * luodaan metodeja joilla rakennetaan erilaisia raportteja tietokannan datasta
 *
 */
public class RaportinLuonti {
    /**
     * raportin uniikki nimi
     */
    private String raportinNimi;
    /**
     * raportin luomispaivan
     */
    private LocalDate date = LocalDate.now();
    /**
     * raportin luomispaiva stringina
     */
    private String paivaNyt = date.format(DateTimeFormatter.BASIC_ISO_DATE);
    /**
     * raportin aikajanan alkupvm
     */
    private LocalDate raporttiAlkaen;
    /**
     * raportin aikajana loppupvm
     */
    private LocalDate raporttiAsti;

    /**
     * luodaan raportti varauksesta asiakkasta tai taloudesta
     * @param raportti raportin nimi
     * @param alkaen raportin aikajanan alkupvm
     * @param asti * raportin aikajana loppupvm
     */
    public void luoRaportti(String raportti, LocalDate alkaen, LocalDate asti) {

        setRaportinNimi(raportti);
        setRaporttiAlkaen(alkaen);
        setRaporttiAsti(asti);

        try {
            Yhteysluokka olio=new Yhteysluokka();
            AsiakasData asiakasData = new AsiakasData();
            VarausData varausData = new VarausData();

            // Luodaan dokumentti
            PDDocument dokumentti = new PDDocument();

            // Lisätään sivu dokumenttiin
            PDPage sivu = new PDPage(PDRectangle.A4);
            dokumentti.addPage(sivu);

            //laskutiedoston nimi
            String raporttiTiedosto = "Raportit/"+ getRaportinNimi() +"_"+ paivaNyt + ".pdf";

            //laskulle tulostuvat rivit
            String rivi1 = "Raportti: " + getRaportinNimi();
            String rivi2 = "Ajalta: " + getRaporttiAlkaen() +" - "+ getRaporttiAsti() ;
            String rivi3 = "";

            String[] rivit = {rivi1, rivi2, rivi3};
            Object[] rapsa = {};

            if (raportti == "Varausraportti"){
                rapsa = varausData.varausRaportti(olio,getRaporttiAlkaen(),getRaporttiAsti()).toArray();
            }
            else if (raportti == "Asiakasraportti"){
                rapsa = asiakasData.asiakasRaportti(olio).toArray();
            }
            else if (raportti == "Talousraportti"){
                rapsa = varausData.talousRaportti(olio, getRaporttiAlkaen(), getRaporttiAsti()).toArray();
            }



            // stream
            PDPageContentStream contentStream = new PDPageContentStream(dokumentti, sivu);

            // lisätään tekstit
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
            contentStream.setLeading(14.5f);
            contentStream.newLineAtOffset(80, 800);

            for (int i = 0; i < rivit.length; i++) {
                contentStream.showText(rivit[i]);
                contentStream.newLineAtOffset(0, -14.5f);
                contentStream.newLine();
            }
            contentStream.setLeading(14.5f);

            for (Object rap : rapsa){
                contentStream.showText(rap.toString());
                contentStream.newLineAtOffset(0, -14.5f);
                contentStream.newLine();
            }
            contentStream.endText();

            // Sulje stream
            contentStream.close();

            // Tallennus ja dokumentin sulku
            dokumentti.save(raporttiTiedosto);
            dokumentti.close();

            System.out.println("Raportti luotu.");
        } catch (
                IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * haetaan raportin nimi
     * @return nimi
     */
    public String getRaportinNimi() {
        return raportinNimi;
    }

    /**
     * asetetaan raportille nimi
     * @param raportinNimi haluttu nimi
     */
    public void setRaportinNimi(String raportinNimi) {
        this.raportinNimi = raportinNimi;
    }

    /**
     * haetaan raportin alkupvm
     * @return pvm
     */
    public LocalDate getRaporttiAlkaen() {
        return raporttiAlkaen;
    }

    /**
     * asetetaan raportin alkupvm
     * @param raporttiAlkaen haluttu pvm
     */
    public void setRaporttiAlkaen(LocalDate raporttiAlkaen) {
        this.raporttiAlkaen = raporttiAlkaen;
    }

    /**
     * haetaan raportin loppupvm
     * @return pvm
     */
    public LocalDate getRaporttiAsti() {
        return raporttiAsti;
    }

    /**
     * asetetaan raportin loppupvm
     * @param raporttiAsti haluttu pvm
     */
    public void setRaporttiAsti(LocalDate raporttiAsti) {
        this.raporttiAsti = raporttiAsti;
    }
}
