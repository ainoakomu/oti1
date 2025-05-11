package com.example.ohjelmistotuotanto;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;


public class AdminLuokka {

    private String valittuRaportti;
    private int kayID = 0;
    private String kayNimi = "";
    private String kayTun= "";
    private String salaSana= "";
    private String kayTaso= "";
    private int annOikeus = 0;
    private int hygPassi = 0;



    public Stage luoAdminToiminnotIkkuna(){
        Stage adminStage = new Stage();
        GridPane rootPaneeli=new GridPane();
        rootPaneeli.setAlignment(Pos.CENTER);
        rootPaneeli.setHgap(50);
        rootPaneeli.setVgap(100);
        rootPaneeli.setPadding(new Insets(10,10,10,10));

        //buttonit ja action eventit
        Button raporttibt=new Button("Raportit");
        Button kayttajanhallintabt=new Button("Käyttäjänhallinta");
        Button suljebt=new Button("Sulje");

        raporttibt.setOnAction(e->{
            luoRaportitIkkuna().show();
        });

        kayttajanhallintabt.setOnAction(e->{
            luoKayttajanhallintaIkkuna().show();
        });

        suljebt.setOnAction(e->{
            adminStage.close();
        });

        rootPaneeli.add(raporttibt,2,1);
        rootPaneeli.add(kayttajanhallintabt,0,1);
        rootPaneeli.add(suljebt,2,2);

        Scene adminScene = new Scene(rootPaneeli,400,400);
        adminStage.setScene(adminScene);
        adminStage.setTitle("Admin");
        return adminStage;
    }

    public Stage luoKayttajanhallintaIkkuna(){
        Stage kayttajatStage = new Stage();
        BorderPane rootPaneeli=new BorderPane();

        Yhteysluokka olio=new Yhteysluokka();
        KayttajaData kayttajaData = new KayttajaData();
        //tyhjä lista
        ObservableList<String> kayttajat= FXCollections.observableArrayList(kayttajaData.haeKayttajat(olio));
        ListView<String> kayttajalista =new ListView<>(kayttajat);

        kayttajalista.setMaxSize(600,250);
        rootPaneeli.setCenter(kayttajalista);


        // KÄYTTÄJÄN VALINTA LISTALTA

        kayttajalista.getSelectionModel().selectedItemProperty().addListener((obs, vanha, uusi) -> {
            if (uusi != null) {
                String[] kentat = uusi.split(", ");

                for (String kentta : kentat) {
                    String[] avainArvo = kentta.split(": ");
                    if (avainArvo.length < 2) continue;

                    String avain = avainArvo[0].trim();
                    String arvo = avainArvo[1].trim();

                    switch (avain) {
                        case "ID":
                            setKayID(Integer.valueOf(arvo));
                            break;
                        case "Nimi":
                            setKayNimi(arvo);
                            break;
                        case "Käyttäjätunnus":
                            setKayTun(arvo);
                            break;
                        case "Salasana":
                            setSalaSana(arvo);
                            break;
                        case "Käyttäjätaso":
                            setKayTaso(arvo);
                            break;
                        case "Anniskeluoikeus":
                            if (arvo.equals("Kyllä")) {
                                setAnnOikeus(1);
                            } else if (arvo.equals("Ei")) {
                                setAnnOikeus(0);
                            }
                            break;
                        case "Hygieniapassi":
                            if (arvo.equals("Kyllä")) {
                                setHygPassi(1);
                            } else if (arvo.equals("Ei")) {
                                setHygPassi(0);
                            }
                            break;
                    }
                }
            }
        });



        //buttonit ja action eventit
        Button muokkaaKayttajaa =new Button("Muokkaa käyttäjää");
        Button lisaaKayttaja =new Button("Lisää uusi käyttäjä");
        Button suljeBt=new Button("Sulje");

        muokkaaKayttajaa.setOnAction(e->{
            //tarkista että käyttäjä on valittu listalta
            luoMuokkaaKayttajaIkkuna(kayttajat).show();
        });

        lisaaKayttaja.setOnAction(e->{
            setKayID(0);
            setKayNimi("");
            setKayTun("");
            setSalaSana("");
            setKayTaso("");
            setAnnOikeus(0);
            setHygPassi(0);

            luoUusiKayttajaIkkuna(kayttajat).show();
        });

        suljeBt.setOnAction(e->{
            kayttajatStage.close();
        });

        HBox nappulaBoksi=new HBox(lisaaKayttaja,muokkaaKayttajaa,suljeBt);
        nappulaBoksi.setSpacing(50);
        rootPaneeli.setBottom(nappulaBoksi);
        nappulaBoksi.setAlignment(Pos.CENTER);
        rootPaneeli.setPadding(new Insets(15,15,15,15));

        Scene kayttajatScene = new Scene(rootPaneeli,650,500);
        kayttajatStage.setScene(kayttajatScene);
        kayttajatStage.setTitle("Käyttäjähallinta");
        return kayttajatStage;
    }

    public Stage luoUusiKayttajaIkkuna(ObservableList<String> lista){
        Stage uusiKayttajaStage = new Stage();
        GridPane rootPaneeli=new GridPane();
        rootPaneeli.setAlignment(Pos.CENTER);
        rootPaneeli.setVgap(10);
        rootPaneeli.setHgap(10);
        rootPaneeli.setPadding(new Insets(10));

        Label idlb =new Label("ID");
        Label nimiLb =new Label("Nimi");
        Label kayttajatunnuslb =new Label("Käyttäjätunnus");
        Label salasanalb =new Label("Salasana");
        Label kayttooikeuslb=new Label("Käyttöoikeus");
        Label passitlb =new Label("Passit");
        TextField idTxt=new TextField();
        TextField nimiTxt =new TextField();
        TextField kayttajaTxt =new TextField();
        TextField ssTxt=new TextField();
        HBox row1=new HBox(idlb,idTxt);
        row1.setSpacing(70);
        HBox row2=new HBox(nimiLb, nimiTxt);
        row2.setSpacing(57);
        HBox row3=new HBox(kayttajatunnuslb, kayttajaTxt);
        row3.setSpacing(5);
        HBox row4=new HBox(salasanalb,ssTxt);
        row4.setSpacing(38);
        VBox sarake=new VBox(row1,row2,row3,row4);
        sarake.setSpacing(15);
        sarake.setAlignment(Pos.CENTER);

        RadioButton peruskayttajaRbtn=new RadioButton("Peruskäyttäjä");
        RadioButton adminRbtn=new RadioButton("Admin-käyttäjä");
        CheckBox anniskeluChbx=new CheckBox("Anniskelupassi");
        CheckBox hygieniaChbx=new CheckBox("Hygieniapassi");

        ToggleGroup oikeusGrp = new ToggleGroup();
        oikeusGrp.getToggles().addAll(peruskayttajaRbtn,adminRbtn);

        //käyttäjän tiedot
        peruskayttajaRbtn.setOnAction(e->{
            setKayTaso("perus");
        });
        adminRbtn.setOnAction(e->{
            setKayTaso("admin");
        });

        //buttonit ja action eventit
        Button tallennaBt=new Button("Tallenna uusi käyttäjä");
        Button suljeBt=new Button("Sulje");

        tallennaBt.setOnAction(e->{
            setKayID(Integer.parseInt(idTxt.getText()));
            setKayNimi(nimiTxt.getText());
            setKayTun(kayttajaTxt.getText());
            setSalaSana(ssTxt.getText());
            if(anniskeluChbx.isSelected()){
                setAnnOikeus(1);
            } else if (!anniskeluChbx.isSelected()){
                setAnnOikeus(0);
            }
            if(hygieniaChbx.isSelected()){
                setHygPassi(1);
            } else if (!hygieniaChbx.isSelected()){
                setHygPassi(0);
            }

            //TARVITAAN: metodi jolla tarkistetaan onko kaikki tarvittavat tiedot täytetty
            // jos ei ole kaikkia tarvittavia tietoja, pitää tulla kehote täydentää

            // tallenna tiedot tietokantaaan
            Yhteysluokka yhteysluokka = new Yhteysluokka();
            KayttajaData kayttajaData = new KayttajaData();
            kayttajaData.lisaaKayttaja(yhteysluokka, getKayID(), getKayNimi(), getKayTun(), getSalaSana(), getKayTaso(), getAnnOikeus(), getHygPassi());

            //päivitä listviewin lista
            lista.setAll(FXCollections.observableArrayList(kayttajaData.haeKayttajat(yhteysluokka)));

            // TARVITAAN ilmoitus että tiedot tallennettu

            uusiKayttajaStage.close();
        });

        suljeBt.setOnAction(e->{
            //kysy suljetaanko
            uusiKayttajaStage.close();
        });

        VBox buttons=new VBox(tallennaBt,suljeBt);
        buttons.setSpacing(15);
        buttons.setAlignment(Pos.TOP_CENTER);
        rootPaneeli.add(buttons,2,2);
        VBox checkBox=new VBox(kayttooikeuslb,peruskayttajaRbtn,adminRbtn,passitlb,anniskeluChbx,hygieniaChbx);
        checkBox.setSpacing(15);
        checkBox.setAlignment(Pos.CENTER_LEFT);
        VBox keskikohta=new VBox(sarake,checkBox);
        keskikohta.setSpacing(20);
        rootPaneeli.add(keskikohta,1,0);

        Scene uusikayttajaScene = new Scene(rootPaneeli,500,500);
        uusiKayttajaStage.setScene(uusikayttajaScene);
        uusiKayttajaStage.setTitle("Uusi käyttäjä");


        return uusiKayttajaStage;
    }

    public Stage luoMuokkaaKayttajaIkkuna(ObservableList<String> lista){
        Stage muokkausStage = new Stage();
        GridPane rootPaneeli=new GridPane();
        rootPaneeli.setAlignment(Pos.CENTER);
        rootPaneeli.setVgap(10);
        rootPaneeli.setHgap(10);
        rootPaneeli.setPadding(new Insets(10));

        Label idlb =new Label("ID");
        Label nimiLb =new Label("Nimi");
        Label kayttajatunnuslb =new Label("Käyttäjätunnus");
        Label salasanalb =new Label("Salasana");
        Label kayttooikeuslb=new Label("Käyttöoikeus");
        Label passitlb =new Label("Passit");
        TextField idTxt=new TextField();
        TextField nimiTxt =new TextField();
        TextField kayttajaTxt =new TextField();
        TextField ssTxt=new TextField();
        HBox row1=new HBox(idlb,idTxt);
        row1.setSpacing(70);
        HBox row2=new HBox(nimiLb, nimiTxt);
        row2.setSpacing(57);
        HBox row3=new HBox(kayttajatunnuslb, kayttajaTxt);
        row3.setSpacing(5);
        HBox row4=new HBox(salasanalb,ssTxt);
        row4.setSpacing(38);
        VBox sarake=new VBox(row1,row2,row3,row4);
        sarake.setSpacing(15);
        sarake.setAlignment(Pos.CENTER);

        RadioButton peruskayttajaRbtn=new RadioButton("Peruskäyttäjä");
        RadioButton adminRbtn=new RadioButton("Admin-käyttäjä");
        CheckBox anniskeluChbx=new CheckBox("Anniskelupassi");
        CheckBox hygieniaChbx=new CheckBox("Hygieniapassi");

        ToggleGroup oikeusGrp = new ToggleGroup();
        oikeusGrp.getToggles().addAll(peruskayttajaRbtn,adminRbtn);

        // setataan valitun käyttäjän tiedot
        idTxt.setText(String.valueOf(getKayID()));
        nimiTxt.setText(getKayNimi());
        kayttajaTxt.setText(getKayTaso());
        ssTxt.setText(getSalaSana());

        if (getKayTaso().equals("perus")) {
            peruskayttajaRbtn.setSelected(true);
        } else if (getKayTaso().equals("admin")) {
            adminRbtn.setSelected(true);
        }
        anniskeluChbx.setSelected(getAnnOikeus() == 1);
        hygieniaChbx.setSelected(getHygPassi() == 1);



        //buttonit ja action eventit
        Button tallennaBt=new Button("Tallenna uusi käyttäjä");
        Button poistaBt=new Button("Poista käyttäjä");
        Button suljeBt=new Button("Sulje");

        KayttajaData kayttajaData = new KayttajaData();
        Yhteysluokka yhteysluokka = new Yhteysluokka();

        tallennaBt.setOnAction(e->{
            if(!idTxt.getText().isEmpty()){
                //kysy tallennetaanko muutokset

                //tallenna muutokset sqlään


                //ilmoita että tallennettu
                muokkausStage.close();
            } else {
                // anna warning että jottain puuttuu
            }
        });

        poistaBt.setOnAction(e->{
            if(!idTxt.getText().isEmpty()){
                //kysy poistetaaanko käyttäjä varmasti
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Käyttäjätietojen poisto");
                alert.setHeaderText("Poistetaanko käyttäjätiedot tietokannasta?");
                alert.setContentText("Tätä toimintoa ei voi enää peruuttaa.");
                Optional<ButtonType> valinta = alert.showAndWait();

                // jos painaa ok, poistetaan, jos painaa cancel, ei poisteta
                if (valinta.isPresent() && valinta.get() == ButtonType.OK) {
                    kayttajaData.poistaKayttaja(yhteysluokka,getKayID());
                    lista.setAll(FXCollections.observableArrayList(kayttajaData.haeKayttajat(yhteysluokka)));

                    // TARVITAAN ilmoita että käyttäjätiedot poistettu
                    muokkausStage.close();
                } else {
                    e.consume();
                }

            } else {
                // anna warning että jottain puuttuu
                e.consume();
                System.out.println("kayttajaid tyhjä");
            }
        });

        suljeBt.setOnAction(e->{
            //kysy suljetaanko ikkuna
            muokkausStage.close();
        });

        VBox buttons=new VBox(tallennaBt,poistaBt,suljeBt);
        buttons.setSpacing(15);
        buttons.setAlignment(Pos.TOP_CENTER);
        rootPaneeli.add(buttons,2,2);
        VBox checkBox=new VBox(kayttooikeuslb,peruskayttajaRbtn,adminRbtn,passitlb,anniskeluChbx,hygieniaChbx);
        checkBox.setSpacing(15);
        checkBox.setAlignment(Pos.CENTER_LEFT);
        VBox keskikohta=new VBox(sarake,checkBox);
        keskikohta.setSpacing(20);
        rootPaneeli.add(keskikohta,1,0);

        Scene muokkausScene = new Scene(rootPaneeli,500,500);
        muokkausStage.setScene(muokkausScene);
        muokkausStage.setTitle("Muokkaa käyttäjää");
        return muokkausStage;
    }

    public Stage luoRaportitIkkuna(){
        Stage raporttiStage = new Stage();
        BorderPane rootPaneeli=new BorderPane();

        HBox rapsaBox =new HBox();
        rapsaBox.setSpacing(10);
        rapsaBox.setAlignment(Pos.TOP_LEFT);
        rapsaBox.setPadding(new Insets(25,5,5,5));
        ToggleButton asiakasRaporttiBtn =new ToggleButton("Asiakasraportti");
        ToggleButton varausRaporttiBtn =new ToggleButton("Varausraportti");
        ToggleButton talousRaporttiBtn =new ToggleButton("Talousraportti");
        ToggleButton testi4=new ToggleButton("Testi");
        rapsaBox.getChildren().addAll(asiakasRaporttiBtn,varausRaporttiBtn,talousRaporttiBtn,testi4);

        ToggleGroup btnGroup = new ToggleGroup();
        btnGroup.getToggles().addAll(asiakasRaporttiBtn,varausRaporttiBtn,talousRaporttiBtn,testi4);

        asiakasRaporttiBtn.setOnAction(e->{
            valittuRaportti="Asiakasraportti";
            napitReset(asiakasRaporttiBtn,varausRaporttiBtn,talousRaporttiBtn,testi4);
            asiakasRaporttiBtn.setStyle(
                    "-fx-background-color: green;" +
                            "-fx-text-fill: white");
            // metodi jolla haetaan asiakastiedot listviewiin
        });
        varausRaporttiBtn.setOnAction(e->{
            valittuRaportti="Varausraportti";
            napitReset(asiakasRaporttiBtn,varausRaporttiBtn,talousRaporttiBtn,testi4);
            varausRaporttiBtn.setStyle(
                    "-fx-background-color: green;" +
                            "-fx-text-fill: white");
            // metodi jolla haetaan varaukset listviewiin
        });
        talousRaporttiBtn.setOnAction(e->{
            valittuRaportti="Talousraportti";
            napitReset(asiakasRaporttiBtn,varausRaporttiBtn,talousRaporttiBtn,testi4);
            talousRaporttiBtn.setStyle(
                    "-fx-background-color: green;" +
                            "-fx-text-fill: white");
            // metodi jolla haetaan taloustietoja?? listviewiin
            // taloustietoja voisi olla esim. aikaväliltä varaukset (esim. pelkkä tunnus tms) ja varauksen hintaa
        });
        testi4.setOnAction(e->{
            valittuRaportti="testiraportti";
            napitReset(asiakasRaporttiBtn,varausRaporttiBtn,talousRaporttiBtn,testi4);
            testi4.setStyle(
                    "-fx-background-color: green;" +
                            "-fx-text-fill: white");
            // metodi jolla haetaan xxxx listviewiin ???
        });

        //dates
        DatePicker alkupaiva=new DatePicker();
        alkupaiva.setPrefWidth(100);
        alkupaiva.setPrefHeight(20);
        DatePicker loppupaiva=new DatePicker();
        loppupaiva.setPrefWidth(100);
        loppupaiva.setPrefHeight(20);
        HBox datebox=new HBox(alkupaiva,loppupaiva);
        datebox.setAlignment(Pos.TOP_RIGHT);

        HBox ylaosa =new HBox(rapsaBox,datebox);
        ylaosa.setAlignment(Pos.TOP_CENTER);
        ylaosa.setSpacing(10);
        ylaosa.setPadding(new Insets(10,10,10,10));
        rootPaneeli.setTop(ylaosa);

        //buttonit ja action eventit
        Button viePDF=new Button("Vie PDF-tiedostoon");
        Button sulje=new Button("Sulje");

        viePDF.setOnAction(e->{
            RaportinLuonti raportinLuonti = new RaportinLuonti();
            raportinLuonti.luoRaportti(valittuRaportti,alkupaiva.getValue(),loppupaiva.getValue());
            raporttiValmis().show();
        });

        sulje.setOnAction(e->{
            raporttiStage.close();
        });


        HBox alaosa=new HBox(viePDF,sulje);
        alaosa.setPadding(new Insets(10,10,10,10));
        alaosa.setSpacing(10);
        alaosa.setAlignment(Pos.BASELINE_RIGHT);
        rootPaneeli.setBottom(alaosa);


        // tähän pitää laaittaa et päivittää valittavan listaan valitun raportin perusteella
        ObservableList<String> tyja = FXCollections.observableArrayList("Testi","yippee","not the real list");
        ListView<String> lista=new ListView<>(tyja);
        lista.setMaxSize(600,350);
        lista.setPadding(new Insets(10,10,10,10));
        rootPaneeli.setCenter(lista);

        Scene raporttiScene = new Scene(rootPaneeli,700,700);
        raporttiStage.setScene(raporttiScene);
        raporttiStage.setTitle("Raportit");
        return raporttiStage;
    }

    public Stage raporttiValmis(){
        Stage valmisStage = new Stage();
        LocalDate date = LocalDate.now();
        String paivaNyt = date.format(DateTimeFormatter.BASIC_ISO_DATE);

        Text teksti = new Text(
                "Raportti valmis!\n\n" +
                "Raportti on tallennettu Raportit-kansioon\n" +
                "pdf-tiedostona nimellä \n\n" +
                valittuRaportti+"_"+ paivaNyt + ".pdf");

        teksti.setTextAlignment(TextAlignment.CENTER);

        Button okBt = new Button("OK");
        okBt.setOnAction(e->{
            valmisStage.close();
        });

        BorderPane pane = new BorderPane();
        pane.setCenter(teksti);
        pane.setBottom(okBt);

        Scene scene = new Scene(pane, 300,300);
        valmisStage.setScene(scene);
        valmisStage.setTitle("Raportti tallennettu");

        return valmisStage;
    }

    public void napitReset(ToggleButton a, ToggleButton b, ToggleButton c, ToggleButton d){
        a.setStyle(null);
        b.setStyle(null);
        c.setStyle(null);
        d.setStyle(null);
    }

    public int getKayID() {
        return kayID;
    }

    public void setKayID(int kayID) {
        this.kayID = kayID;
    }

    public String getKayNimi() {
        return kayNimi;
    }

    public void setKayNimi(String kayNimi) {
        this.kayNimi = kayNimi;
    }

    public String getKayTun() {
        return kayTun;
    }

    public void setKayTun(String kayTun) {
        this.kayTun = kayTun;
    }

    public String getSalaSana() {
        return salaSana;
    }

    public void setSalaSana(String salaSana) {
        this.salaSana = salaSana;
    }

    public String getKayTaso() {
        return kayTaso;
    }

    public void setKayTaso(String kayTaso) {
        this.kayTaso = kayTaso;
    }

    public int getAnnOikeus() {
        return annOikeus;
    }

    public void setAnnOikeus(int annOikeus) {
        this.annOikeus = annOikeus;
    }

    public int getHygPassi() {
        return hygPassi;
    }

    public void setHygPassi(int hygPassi) {
        this.hygPassi = hygPassi;
    }
}
