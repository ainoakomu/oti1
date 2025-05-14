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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static com.example.ohjelmistotuotanto.AsiakasData.haeAsiakkaat;
import static com.example.ohjelmistotuotanto.VarausData.haeVaraukset;

/**
 * luodaan kayttajahallintaan ja managerin toimintaa liittyvia ikkunoita ja yhditellaan niiden toimintoja
 * kayttajahallinnassa kasitellaan tyontekijoiden tietoja seka raporttien tekoa
 */
public class AdminLuokka {
    /**
     * haluttu raportin valinnan muuttuja
     */
    private String valittuRaportti;
    /**
     * kayttajan identifioiva numero
     */
    private int kayID = 0;
    /**
     * kayttajan nimi
     */
    private String kayNimi = "";
    /**
     * kayttajan oma tunnus jarjestelmaan
     */
    private String kayTun= "";
    /**
     * kayttajan oma salasana jarjestelmaan
     */
    private String salaSana= "";
    /**
     * maarittelee kayttajan admin tai perusoikeuden
     */
    private String kayTaso= "";
    /**
     * kertoo onko kayttajalla anniskelupassi
     */
    private int annOikeus = 0;
    /**
     * kertoo onko kayttajalla hygienipassi
     */
    private int hygPassi = 0;

    private int arvosteluID = 0;
    private int varausID = 0;
    private Double arvosana = 0.0;
    private String arvostelu = "";

    /**
     * luodaan kayttajanhallinnan ikkuna, jossa voidaan kasitella tyontekijoita tai luoda erilaisia raportteja
     * @return palauttaa halutun stagen
     */
    public Stage luoAdminToiminnotIkkuna(){
        Stage adminStage = new Stage();
        GridPane rootPaneeli=new GridPane();
        rootPaneeli.setAlignment(Pos.CENTER);
        rootPaneeli.setHgap(50);
        rootPaneeli.setVgap(100);
        rootPaneeli.setPadding(new Insets(10,10,10,10));
        rootPaneeli.setBackground(Taustakuvat.TaustakuvaAsettaminen.luoToinenTausta());

        //buttonit ja action eventit
        Button raporttibt=new Button("Raportit");
        Button kayttajanhallintabt=new Button("Käyttäjänhallinta");
        Button arvostelutbt = new Button("Arvostelut");
        Button suljebt=new Button("Sulje");

        raporttibt.setOnAction(e->{
            luoRaportitIkkuna().show();
        });

        kayttajanhallintabt.setOnAction(e->{
            luoKayttajanhallintaIkkuna().show();
        });

        arvostelutbt.setOnAction(e->{
            luoArvostelutIkkuna().show();
        });

        suljebt.setOnAction(e->{
            adminStage.close();
        });

        rootPaneeli.add(raporttibt,2,1);
        rootPaneeli.add(arvostelutbt,1,1);
        rootPaneeli.add(kayttajanhallintabt,0,1);
        rootPaneeli.add(suljebt,2,2);

        Scene adminScene = new Scene(rootPaneeli,400,400);
        adminStage.setScene(adminScene);
        adminStage.setTitle("Admin");
        return adminStage;
    }

    /**
     * luodaan lista kayttajista, joita voi muokata, lisata
     * @return palauttaa halutun stagen
     */
    public Stage luoKayttajanhallintaIkkuna(){
        Stage kayttajatStage = new Stage();
        BorderPane rootPaneeli=new BorderPane();

        Yhteysluokka olio=new Yhteysluokka();
        KayttajaData kayttajaData = new KayttajaData();
        //tyhjä lista
        ObservableList<String> kayttajat= FXCollections.observableArrayList(kayttajaData.haeKayttajat(olio));
        ListView<String> kayttajalista =new ListView<>(kayttajat);

        kayttajalista.setMaxSize(900,250);
        rootPaneeli.setCenter(kayttajalista);
        rootPaneeli.setBackground(Taustakuvat.TaustakuvaAsettaminen.luoToinenTausta());


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

        Scene kayttajatScene = new Scene(rootPaneeli,1000,500);
        kayttajatStage.setScene(kayttajatScene);
        kayttajatStage.setTitle("Käyttäjähallinta");
        return kayttajatStage;
    }

    /**
     * luodaan ikkuna jossa voi lisata kokonaan uuden kayttajan jarjestelmaan ja tietokantaan
     * @param lista annettu kayttajat tietokanna taulun tiedot lista muodossa
     * @return palauttaa halutun stagen
     */
    public Stage luoUusiKayttajaIkkuna(ObservableList<String> lista){
        Stage uusiKayttajaStage = new Stage();
        GridPane rootPaneeli=new GridPane();
        rootPaneeli.setAlignment(Pos.CENTER);
        rootPaneeli.setVgap(10);
        rootPaneeli.setHgap(10);
        rootPaneeli.setPadding(new Insets(10));
        rootPaneeli.setStyle("-fx-background-color: #eeccfc;");

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

            // jos ei ole kaikkia tarvittavia tietoja, pitää tulla kehote täydentää

            if((getKayID()==0)||(getKayNimi()=="")||(getKayTun()=="")||(getSalaSana()=="")||(getKayTaso()=="")){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Varoitus");
                alert.setHeaderText("Tietoja puuttuu");
                alert.setContentText("Täytä kaikki tiedot ennen tallentamista");
                alert.showAndWait();
                e.consume();
            } else {
                Yhteysluokka yhteysluokka = new Yhteysluokka();
                KayttajaData kayttajaData = new KayttajaData();
                kayttajaData.lisaaKayttaja(yhteysluokka, getKayID(), getKayNimi(), getKayTun(), getSalaSana(), getKayTaso(), getAnnOikeus(), getHygPassi());

                //päivitä listviewin lista
                lista.setAll(FXCollections.observableArrayList(kayttajaData.haeKayttajat(yhteysluokka)));

                // ilmoitus että tiedot tallennettu
                Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                alert2.setTitle("Tallennettu");
                alert2.setHeaderText("Uusi käyttäjä luotu");
                alert2.setContentText("Tallennettu onnistuneesti tietokantaan");
                alert2.showAndWait();

                uusiKayttajaStage.close();
            }
        });

        suljeBt.setOnAction(e->{
            //kysy suljetaanko
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Poistu");
            alert.setHeaderText("Poistutaanko uuden käyttäjän lisäyksestä?");
            alert.setContentText("Tietoja ei tallenneta.");
            Optional<ButtonType> valinta = alert.showAndWait();
            if (valinta.isPresent() && valinta.get() == ButtonType.OK) {
                uusiKayttajaStage.close();
            }
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

    /**
     * luodaan ikkuna jossa on mahdollista muokata listalta valittua kayttajaa ja sen tietoja
     * @param lista kayttajat taulu tietokannasta listamuotona
     * @return palauttaa halutun stagen
     */
    public Stage luoMuokkaaKayttajaIkkuna(ObservableList<String> lista){
        Stage muokkausStage = new Stage();
        GridPane rootPaneeli=new GridPane();
        rootPaneeli.setAlignment(Pos.CENTER);
        rootPaneeli.setVgap(10);
        rootPaneeli.setHgap(10);
        rootPaneeli.setPadding(new Insets(10));
        rootPaneeli.setStyle("-fx-background-color: #eeccfc;");

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
        Button tallennaBt=new Button("Tallenna muutokset");
        Button poistaBt=new Button("Poista käyttäjä");
        Button suljeBt=new Button("Sulje");

        KayttajaData kayttajaData = new KayttajaData();
        Yhteysluokka yhteysluokka = new Yhteysluokka();

        tallennaBt.setOnAction(e->{
            if((!idTxt.getText().isEmpty())&&(!nimiTxt.getText().isEmpty())&&(!kayttajaTxt.getText().isEmpty())&&(!ssTxt.getText().isEmpty())){

                // kysy tallennetaanko muutokset
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Tallennus");
                alert.setHeaderText("Tallennetaanko muutokset?");
                alert.setContentText("Poistu ja tallenna");
                Optional<ButtonType> valinta = alert.showAndWait();
                if (valinta.isPresent() && valinta.get() == ButtonType.OK) {
                    setKayNimi(nimiTxt.getText());
                    setKayTun(kayttajaTxt.getText());
                    setSalaSana(ssTxt.getText());
                    if (anniskeluChbx.isSelected()) {
                        setAnnOikeus(1);
                    } else if (!anniskeluChbx.isSelected()) {
                        setAnnOikeus(0);
                    }
                    if (hygieniaChbx.isSelected()) {
                        setHygPassi(1);
                    } else if (!hygieniaChbx.isSelected()) {
                        setHygPassi(0);
                    }

                    //tallenna muutokset sqlään
                    kayttajaData.muokkaaKayttajaa(yhteysluokka, getKayID(), getKayNimi(), getKayTun(), getSalaSana(), getKayTaso(), getAnnOikeus(), getHygPassi());

                    //päivitetään lista
                    lista.setAll(FXCollections.observableArrayList(kayttajaData.haeKayttajat(yhteysluokka)));
                    //ilmoita että tallennettu
                    Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                    alert2.setTitle("Tallennettu");
                    alert2.setHeaderText("Muutokset tallennettu");
                    alert2.setContentText("Tallennettu onnistuneesti tietokantaan");
                    alert2.showAndWait();

                    muokkausStage.close();
                }
            } else {
                // anna warning että jottain puuttuu
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Varoitus");
                alert.setHeaderText("Tietoja puuttuu");
                alert.setContentText("Täytä kaikki tiedot ennen tallentamista");
                alert.showAndWait();
                e.consume();
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
                    Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                    alert2.setTitle("Poistettu");
                    alert2.setHeaderText("Käyttäjätiedot poistettu");
                    alert2.setContentText("Poisto ok.");
                    alert2.showAndWait();
                    muokkausStage.close();
                } else {
                    e.consume();
                }

            } else {
                // anna warning että jottain puuttuu
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Pakollisia tietoja puuttuu");
                alert.setHeaderText("Pakollisia tietoja puuttuu.");
                alert.setContentText("Virhe käyttäjätietojen poistossa.");
                alert.showAndWait();
                e.consume();
                System.out.println("kayttajaid tyhjä");
            }
        });

        suljeBt.setOnAction(e->{
            //kysy suljetaanko ikkuna
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Poistuminen");
            alert.setHeaderText("Poistu tallentamatta");
            alert.setContentText("Poistutaanko ilman muutoksia?");
            alert.showAndWait();
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

    /**
     * luodaan ikkuna jossa voidaan valita aiheittan mista halutaan luoda pdf raportti
     * @return halutun stagen
     */
    public Stage luoArvostelutIkkuna(){
        Stage arvosteluStage = new Stage();
        BorderPane rootPaneeli=new BorderPane();
        rootPaneeli.setBackground(Taustakuvat.TaustakuvaAsettaminen.luoToinenTausta());

        Yhteysluokka yhteysluokka = new Yhteysluokka();
        VarausData varausData = new VarausData();

        ObservableList<String> arvosteluRaporttidata = FXCollections.observableArrayList(FXCollections.observableArrayList(varausData.haeArvostetlut((yhteysluokka))));

        ListView<String> lista=new ListView<>(arvosteluRaporttidata);
        lista.setMaxSize(600,350);
        lista.setPadding(new Insets(10,10,10,10));
        rootPaneeli.setCenter(lista);

        lista.getSelectionModel().selectedItemProperty().addListener((obs, vanha, uusi) -> {
            if (uusi != null) {
                String[] kentat = uusi.split(", ");

                for (String kentta : kentat) {
                    String[] avainArvo = kentta.split(": ");
                    if (avainArvo.length < 2) continue;

                    String avain = avainArvo[0].trim();
                    String arvo = avainArvo[1].trim();

                    switch (avain) {
                        case "Arvostelu ID":
                            setArvosteluID(Integer.valueOf(arvo));
                            break;
                        case "Varaus ID":
                            setVarausID(Integer.valueOf(arvo));
                            break;
                        case "arvosana":
                            setArvosana(Double.parseDouble(arvo));
                            break;
                        case "arvostelu":
                            setArvostelu(arvo);
                            break;
                    }
                }
            }
        });

        //buttonit ja action eventit
        Button lisaaArvostelubt=new Button("Lisää uusi arvostelu");
        Button poistaArvostelu=new Button("Poista arvostelu");
        Button sulje=new Button("Sulje");

        lisaaArvostelubt.setOnAction(e->{
            luoUusiArvosteluIkkuna(arvosteluRaporttidata).show();
        });

        poistaArvostelu.setOnAction(e->{

                if(getArvosteluID()>0){
                    //kysy poistetaaanko arvostelu varmasti
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Arvostelun poisto");
                    alert.setHeaderText("Poistetaanko arvostelu tietokannasta?");
                    alert.setContentText("Tätä toimintoa ei voi enää peruuttaa.");
                    Optional<ButtonType> valinta = alert.showAndWait();

                    // jos painaa ok, poistetaan, jos painaa cancel, ei poisteta
                    if (valinta.isPresent() && valinta.get() == ButtonType.OK) {
                        varausData.poistaArvostelu(yhteysluokka,getArvosteluID());
                        arvosteluRaporttidata.setAll(FXCollections.observableArrayList(FXCollections.observableArrayList(varausData.haeArvostetlut((yhteysluokka)))));

                        // TARVITAAN ilmoita että käyttäjätiedot poistettu
                        Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                        alert2.setTitle("Poistettu");
                        alert2.setHeaderText("Arvostelu poistettu");
                        alert2.setContentText("Poisto ok.");
                        alert2.showAndWait();
                    } else {
                        e.consume();
                    }

                } else {
                    // anna warning että jottain puuttuu
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Pakollisia tietoja puuttuu");
                    alert.setHeaderText("Ei valittua arvostelua");
                    alert.setContentText("Valitse arvostelu listalta ja yritä uudestaan.");
                    alert.showAndWait();
                    e.consume();
                }
        });

        sulje.setOnAction(e->{
            arvosteluStage.close();
        });

        HBox alaosa=new HBox(lisaaArvostelubt,poistaArvostelu,sulje);
        alaosa.setPadding(new Insets(10,10,10,10));
        alaosa.setSpacing(10);
        alaosa.setAlignment(Pos.CENTER);
        rootPaneeli.setBottom(alaosa);

        Scene arvosteluScene = new Scene(rootPaneeli,700,610);
        arvosteluStage.setScene(arvosteluScene);
        arvosteluStage.setTitle("Arvostelut");
        return arvosteluStage;
    }

    public Stage luoUusiArvosteluIkkuna(ObservableList<String> lista){
        Stage uusiArvosteluStage = new Stage();
        GridPane rootPaneeli=new GridPane();
        rootPaneeli.setAlignment(Pos.CENTER);
        rootPaneeli.setVgap(10);
        rootPaneeli.setHgap(10);
        rootPaneeli.setPadding(new Insets(10));
        rootPaneeli.setStyle("-fx-background-color: #eeccfc;");




        Yhteysluokka yhteysluokka = new Yhteysluokka();
        VarausData varausData = new VarausData();

        //listataan varausid:t
        ObservableList<Integer> varaustenlista = FXCollections.observableArrayList();
        try {
            Connection yhteys = yhteysluokka.getYhteys();
            if (yhteys == null) {
                System.err.println("Tietokantayhteys epäonnistui.");
            }
            String sql = "SELECT varaus_id FROM varaukset";
            PreparedStatement stmt = yhteys.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                varaustenlista.add(rs.getInt("varaus_id"));
            };

        } catch (Exception e) {
            e.printStackTrace();
        }

        ComboBox<Integer> varausComboBox = new ComboBox<>();
        varausComboBox.setItems(varaustenlista);

        Label varausLb =new Label("Varaus ID");
        Label arvosanalb =new Label("Arvosana");
        Label arvostelulb =new Label("Arvostelu");

        TextField varausTxt =new TextField();
        TextField arvosanaTxt =new TextField();
        TextArea arvosteluTxtArea = new TextArea();
        arvosteluTxtArea.setPrefHeight(200);
        arvosteluTxtArea.setPrefWidth(200);

        HBox row2=new HBox(varausLb, varausComboBox);
        row2.setSpacing(57);
        HBox row3=new HBox(arvosanalb, arvosanaTxt);
        row3.setSpacing(5);
        HBox row4=new HBox(arvostelulb,arvosteluTxtArea);
        row4.setSpacing(38);
        VBox sarake=new VBox(row2,row3,row4);
        sarake.setSpacing(15);
        sarake.setAlignment(Pos.CENTER);

        //buttonit ja action eventit
        Button tallennaBt=new Button("Tallenna uusi arvostelu");
        Button suljeBt=new Button("Sulje");



        tallennaBt.setOnAction(e->{

            if ((varausComboBox.getValue()==null) || (arvosanaTxt.getText() == "") || (arvosteluTxtArea.getText() == "")) {
                Alert alert3 = new Alert(Alert.AlertType.ERROR);
                alert3.setTitle("Varoitus");
                alert3.setHeaderText("Tietoja puuttuu");
                alert3.setContentText("Täytä kaikki tiedot ennen tallentamista");
                alert3.showAndWait();
                e.consume();
            } else {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Uusi arvostelu");
                alert.setHeaderText("Lisätäänkö uusi arvostelu?");
                Optional<ButtonType> valinta = alert.showAndWait();

                // jos painaa ok, poistetaan, jos painaa cancel, ei poisteta
                if (valinta.isPresent() && valinta.get() == ButtonType.OK) {

                    setArvosteluID(varausData.arvostelunID(yhteysluokka));
                    setVarausID(varausComboBox.getValue());
                    setArvosana(Double.parseDouble(arvosanaTxt.getText()));
                    setArvostelu(arvosteluTxtArea.getText());

                    varausData.lisaaArvostelu(yhteysluokka, getArvosteluID(), getVarausID(), getArvosana(), getArvostelu());

                    //päivitä listviewin lista
                    lista.setAll(FXCollections.observableArrayList(FXCollections.observableArrayList(FXCollections.observableArrayList(varausData.haeArvostetlut((yhteysluokka))))));

                    // ilmoitus että tiedot tallennettu
                    Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                    alert2.setTitle("Tallennettu");
                    alert2.setHeaderText("Uusi arvostelu lisätty.");
                    alert2.setContentText("Tallennettu onnistuneesti tietokantaan");
                    alert2.showAndWait();

                    uusiArvosteluStage.close();
                }
            }
        });

        suljeBt.setOnAction(e->{
            //kysy suljetaanko
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Poistu");
            alert.setHeaderText("Poistutaanko uuden käyttäjän lisäyksestä?");
            alert.setContentText("Tietoja ei tallenneta.");
            Optional<ButtonType> valinta = alert.showAndWait();
            if (valinta.isPresent() && valinta.get() == ButtonType.OK) {
                uusiArvosteluStage.close();
            }
        });

        VBox buttons=new VBox(tallennaBt,suljeBt);
        buttons.setSpacing(15);
        buttons.setAlignment(Pos.TOP_CENTER);
        rootPaneeli.add(buttons,2,2);
        VBox keskikohta=new VBox(sarake);
        keskikohta.setSpacing(20);
        rootPaneeli.add(keskikohta,1,0);

        Scene uusiArvosteluScene = new Scene(rootPaneeli,500,500);
        uusiArvosteluStage.setScene(uusiArvosteluScene);
        uusiArvosteluStage.setTitle("Uusi arvostelu");

        return uusiArvosteluStage;
    }



    /**
     * luodaan ikkuna jossa voidaan valita aiheittan mista halutaan luoda pdf raportti
     * @return halutun stagen
     */
    public Stage luoRaportitIkkuna(){
        Stage raporttiStage = new Stage();
        BorderPane rootPaneeli=new BorderPane();
        rootPaneeli.setBackground(Taustakuvat.TaustakuvaAsettaminen.luoToinenTausta());

        //dates
        DatePicker alkupaiva=new DatePicker();
        alkupaiva.setValue(LocalDate.now());
        alkupaiva.setPrefWidth(100);
        alkupaiva.setPrefHeight(20);
        DatePicker loppupaiva=new DatePicker();
        loppupaiva.setValue(LocalDate.now());
        loppupaiva.setPrefWidth(100);
        loppupaiva.setPrefHeight(20);
        HBox datebox=new HBox(alkupaiva,loppupaiva);
        datebox.setAlignment(Pos.TOP_RIGHT);

        Yhteysluokka yhteysluokka = new Yhteysluokka();
        VarausData varausData = new VarausData();

        // tähän pitää laaittaa et päivittää valittavan listaan valitun raportin perusteella
        ObservableList<String> raporttidata = FXCollections.observableArrayList("Valitse ensin tarkasteltava raportti");
        ObservableList<String> varausRaporttidata = FXCollections.observableArrayList(FXCollections.observableArrayList(haeVaraukset(yhteysluokka)));
        ObservableList<String> talousRaporttidata = FXCollections.observableArrayList(FXCollections.observableArrayList(varausData.haeTaloustiedot(yhteysluokka)));
        ObservableList<String> asiakasRaporttidata = FXCollections.observableArrayList(FXCollections.observableArrayList(haeAsiakkaat(yhteysluokka)));
        ObservableList<String> arvosteluRaporttidata = FXCollections.observableArrayList(FXCollections.observableArrayList(varausData.haeArvostetlut((yhteysluokka))));

        ListView<String> lista=new ListView<>(raporttidata);
        lista.setMaxSize(600,350);
        lista.setPadding(new Insets(10,10,10,10));
        rootPaneeli.setCenter(lista);

        HBox rapsaBox =new HBox();
        rapsaBox.setSpacing(10);
        rapsaBox.setAlignment(Pos.TOP_LEFT);
        rapsaBox.setPadding(new Insets(25,5,5,5));
        ToggleButton asiakasRaporttiBtn =new ToggleButton("Asiakasraportti");
        ToggleButton varausRaporttiBtn =new ToggleButton("Varausraportti");
        ToggleButton talousRaporttiBtn =new ToggleButton("Talousraportti");
        ToggleButton arvosteluRaporttiBtn =new ToggleButton("Arvosteluraportti");


        rapsaBox.getChildren().addAll(asiakasRaporttiBtn,varausRaporttiBtn,talousRaporttiBtn, arvosteluRaporttiBtn);

        ToggleGroup btnGroup = new ToggleGroup();
        btnGroup.getToggles().addAll(asiakasRaporttiBtn,varausRaporttiBtn,talousRaporttiBtn);

        asiakasRaporttiBtn.setOnAction(e->{
            valittuRaportti="Asiakasraportti";
            napitReset(asiakasRaporttiBtn,varausRaporttiBtn,talousRaporttiBtn,arvosteluRaporttiBtn);
            asiakasRaporttiBtn.setStyle(
                    "-fx-background-color: green;" +
                            "-fx-text-fill: white");
            raporttidata.setAll(asiakasRaporttidata);
        });
        varausRaporttiBtn.setOnAction(e->{
            valittuRaportti="Varausraportti";
            napitReset(asiakasRaporttiBtn,varausRaporttiBtn,talousRaporttiBtn,arvosteluRaporttiBtn);
            varausRaporttiBtn.setStyle(
                    "-fx-background-color: green;" +
                            "-fx-text-fill: white");
            raporttidata.setAll(varausRaporttidata);
        });
        talousRaporttiBtn.setOnAction(e->{
            valittuRaportti="Talousraportti";
            napitReset(asiakasRaporttiBtn,varausRaporttiBtn,talousRaporttiBtn,arvosteluRaporttiBtn);
            talousRaporttiBtn.setStyle(
                    "-fx-background-color: green;" +
                            "-fx-text-fill: white");
            raporttidata.setAll(talousRaporttidata);
        });
        arvosteluRaporttiBtn.setOnAction(e->{
            valittuRaportti="Arvosteluraportti";
            napitReset(asiakasRaporttiBtn,varausRaporttiBtn,talousRaporttiBtn,arvosteluRaporttiBtn);
            arvosteluRaporttiBtn.setStyle(
                    "-fx-background-color: green;" +
                            "-fx-text-fill: white");
            raporttidata.setAll(arvosteluRaporttidata);
        });

        HBox ylaosa =new HBox(rapsaBox,datebox);
        ylaosa.setAlignment(Pos.TOP_CENTER);
        ylaosa.setSpacing(10);
        ylaosa.setPadding(new Insets(10,10,10,10));
        rootPaneeli.setTop(ylaosa);

        //buttonit ja action eventit
        Button viePDF=new Button("Vie PDF-tiedostoon");
        Button sulje=new Button("Sulje");

        viePDF.setOnAction(e->{

            // jos varaus- tai talousraportti ja päivämäärätiedot puuttuu tai on väärin päin (asiakasraporttiin ei tule pvm)
            if(((valittuRaportti=="Varausraportti")||(valittuRaportti=="Talousraportti"))&&
                    ((alkupaiva.getValue()==null)||(loppupaiva.getValue()==null)||
                    (alkupaiva.getValue().isAfter(loppupaiva.getValue()))||
                    (loppupaiva.getValue().isBefore(alkupaiva.getValue())))){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Varoitus");
                alert.setHeaderText("Valitse raportin aikaväli");
                alert.setContentText("Valitse raportointiväli.\n" +
                        "Varmista, että alkupäivä on ennen loppupäivää.");
                alert.showAndWait();
                e.consume();
            } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Viedäänkö PDF");
            alert.setHeaderText("Viedäänkö PDF tiedosto");
            alert.setContentText("Olet luomassa PDF tiedostoa, jatketaanko");
            Optional<ButtonType> valinta = alert.showAndWait();

            if (valinta.isPresent() && valinta.get() == ButtonType.OK) {
                RaportinLuonti raportinLuonti = new RaportinLuonti();
                raportinLuonti.luoRaportti(valittuRaportti, alkupaiva.getValue(), loppupaiva.getValue());
                raporttiValmis().show();
            }}
        });

        sulje.setOnAction(e->{
            raporttiStage.close();
        });

        HBox alaosa=new HBox(viePDF,sulje);
        alaosa.setPadding(new Insets(10,10,10,10));
        alaosa.setSpacing(10);
        alaosa.setAlignment(Pos.BASELINE_RIGHT);
        rootPaneeli.setBottom(alaosa);

        Scene raporttiScene = new Scene(rootPaneeli,700,610);
        raporttiStage.setScene(raporttiScene);
        raporttiStage.setTitle("Raportit");
        return raporttiStage;
    }

    /**
     * luodaan ikkuna jossa kerrotaan kayttajalle etta haluttu raportti on luotu onnistuneesti
     * @return
     */
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
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Raportti valmis");
            alert.setHeaderText("Raportti valmis.");
            alert.setContentText("Raportti on valmis.");
            alert.showAndWait();
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

    /**
     * tyhjennetaan valittujen nappien tiedot jos joku on valittu
     * @param a tyhjennettava valinta
     * @param b tyhjennettava valinta
     * @param c tyhjennettava valinta
     */
    public void napitReset(ToggleButton a, ToggleButton b, ToggleButton c,ToggleButton d){
        a.setStyle(null);
        b.setStyle(null);
        c.setStyle(null);
        d.setStyle(null);
    }

    /**
     * haetaan kayttajan id numero
     * @return palauttaa id numeron
     */
    public int getKayID() {
        return kayID;
    }
    /**
     * aseteaan kayttajalle id numero
     */
    public void setKayID(int kayID) {
        this.kayID = kayID;
    }

    /**
     * haetaan kayttajan nimi
     * @return kayttajan nimi
     */
    public String getKayNimi() {
        return kayNimi;
    }

    /**
     * asetetaan kayttajalle nimi
     * @param kayNimi haluttu nimi
     */
    public void setKayNimi(String kayNimi) {
        this.kayNimi = kayNimi;
    }

    /**
     * haetaan kayttajan tunnus
     * @return palauttaa kayttajan tunnuksen
     */
    public String getKayTun() {
        return kayTun;
    }

    /**
     * aseteaan kayttajan tunnus
     * @param kayTun haluttu tunnus
     */
    public void setKayTun(String kayTun) {
        this.kayTun = kayTun;
    }

    /**
     * haetaan kayttajan salasana
     * @return kayttajan salasana
     */
    public String getSalaSana() {
        return salaSana;
    }

    /**
     * asetetaan kayttajan salasana
     * @param salaSana haluttu salasana
     */
    public void setSalaSana(String salaSana) {
        this.salaSana = salaSana;
    }

    /**
     * haetaan kayttajan taso
     * @return palautetaan kayttajan taso
     */
    public String getKayTaso() {
        return kayTaso;
    }

    /**
     * asetetaan kayttajan taso
     * @param kayTaso haluttu taso
     */
    public void setKayTaso(String kayTaso) {
        this.kayTaso = kayTaso;
    }

    /**
     * haetaan kayttajan anniskelupassin olemassaolo
     * @return anniskelupassioikeus
     */
    public int getAnnOikeus() {
        return annOikeus;
    }

    /**
     * aseteaan anniskelupassin oikeus
     * @param annOikeus annettu oikeus
     */
    public void setAnnOikeus(int annOikeus) {
        this.annOikeus = annOikeus;
    }

    /**
     * haetaan kayttajan hygieniapassin olemassaoloa
     * @return hygieniapassin olemassaolo
     */
    public int getHygPassi() {
        return hygPassi;
    }

    /**
     * asetetaan kayttajalle hygienipassioikeus
     * @param hygPassi haluttu oikeus passista
     */
    public void setHygPassi(int hygPassi) {
        this.hygPassi = hygPassi;
    }

    public int getArvosteluID() {
        return arvosteluID;
    }

    public void setArvosteluID(int arvosteluID) {
        this.arvosteluID = arvosteluID;
    }

    public int getVarausID() {
        return varausID;
    }

    public void setVarausID(int varausID) {
        this.varausID = varausID;
    }

    public Double getArvosana() {
        return arvosana;
    }

    public void setArvosana(Double arvosana) {
        this.arvosana = arvosana;
    }

    public String getArvostelu() {
        return arvostelu;
    }

    public void setArvostelu(String arvostelu) {
        this.arvostelu = arvostelu;
    }
}
