package com.example.ohjelmistotuotanto;


import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.util.Duration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.ohjelmistotuotanto.MokkiData.haeMokit;
import static com.example.ohjelmistotuotanto.VarausData.haeVaraukset;


public class VarausLuokka {

    private int valitunMokinID;
    private int varauksenHinta;
    private int asiakasID;
    private int sekuntti = 0;
    private int varausID;
    private StringProperty valittuMokki=new SimpleStringProperty();

    public Stage luoVarauksetIkkuna(){
        Stage varausStage = new Stage();

        //sql yhteys
        Yhteysluokka olio=new Yhteysluokka();
        BorderPane rootPaneeli=new BorderPane();
        rootPaneeli.setPadding(new Insets(5,5,5,5));
        rootPaneeli.setBackground(Taustakuvat.TaustakuvaAsettaminen.luoKolmasTausta());

        //käytetään datanhaku metodia datan luokasta
        ObservableList<String> varausData = FXCollections.observableArrayList(haeVaraukset(olio));
        ListView<String> varauslista = new ListView<>(varausData);
        varauslista.setMaxSize(900,350);
        rootPaneeli.setCenter(varauslista);

        //buttonit ja action eventit
        Button addLasku=new Button("Tee lasku");
        Button muokkaaVarausta =new Button("Muokkaa varausta");
        Button suljeBt=new Button("Sulje");

        addLasku.setOnAction(e-> luoLisaaLaskuIkkuna().show());

        muokkaaVarausta.setOnAction(e-> luoMuokkaaVaraustaIkkuna(varausData).show());

        suljeBt.setOnAction(e-> varausStage.close());

        HBox nappulaBoksi=new HBox(addLasku, muokkaaVarausta,suljeBt);
        nappulaBoksi.setSpacing(20);
        rootPaneeli.setBottom(nappulaBoksi);
        nappulaBoksi.setAlignment(Pos.CENTER);
        rootPaneeli.setPadding(new Insets(5,5,5,5));

        Scene varausScene = new Scene(rootPaneeli,1000,800);
        varausStage.setScene(varausScene);
        varausStage.setTitle("Varaukset");
        return varausStage;
    }

    public Stage luoMuokkaaVaraustaIkkuna(ObservableList<String> lista){
        Stage muokkausStage = new Stage();
        GridPane rootPaneeli=new GridPane();
        rootPaneeli.setAlignment(Pos.CENTER);
        rootPaneeli.setVgap(20);
        rootPaneeli.setHgap(20);

        rootPaneeli.setPadding(new Insets(10));
        rootPaneeli.setStyle("-fx-background-color: #bfddf2;");

        Label mokkilb =new Label("Mökki");
        Label asiakasLb =new Label("Asiakas");
        Label alkulb=new Label("Varaus alkaa");
        Label loppulb =new Label("Varaus päättyy");

        TextField mokkiTxt=new TextField();
        TextField asiakasTxt =new TextField();
        TextField alkuTxt =new TextField();
        TextField loppuTxt =new TextField();
        HBox row1=new HBox(mokkilb,mokkiTxt);
        row1.setSpacing(50);
        HBox row2=new HBox(asiakasLb, asiakasTxt);
        row2.setSpacing(43);
        HBox row3=new HBox(alkulb, alkuTxt);
        row3.setSpacing(17);
        HBox row4=new HBox(loppulb, loppuTxt);
        row4.setSpacing(5);
        VBox sarake=new VBox(row1,row2,row3,row4);
        sarake.setSpacing(20);

        sarake.setSpacing(15);
        sarake.setAlignment(Pos.CENTER);

        //buttonit ja action eventit
        Button tallennaBt=new Button("Tallenna");
        Button poistaBt=new Button("Poista varaus");
        Button suljeBt=new Button("Sulje");

        Yhteysluokka yhteysluokka = new Yhteysluokka();

        tallennaBt.setOnAction(e->{
            //kysy tallennetaanko muutokse
            //tallenna muutokset sqlään
            //ilmoita että tallennettu
            lista.setAll(FXCollections.observableArrayList(haeVaraukset(yhteysluokka)));
            muokkausStage.close();
        });

        poistaBt.setOnAction(e->{
            //kysy poistetaanko mökki
            //poista mökki sqlästä
            //ilmoita että poistettu
            lista.setAll(FXCollections.observableArrayList(haeVaraukset(yhteysluokka)));
            muokkausStage.close();
        });

        suljeBt.setOnAction(e->{
            //kysy suljetaanko ikkuna
            lista.setAll(FXCollections.observableArrayList(haeVaraukset(yhteysluokka)));
            muokkausStage.close();
        });

        VBox buttons=new VBox(tallennaBt,poistaBt,suljeBt);
        buttons.setSpacing(20);
        rootPaneeli.add(buttons,2,2);

        VBox keskikohta=new VBox(sarake);
        keskikohta.setSpacing(20);
        rootPaneeli.add(keskikohta,1,0);

        Scene muokkausScene = new Scene(rootPaneeli,500,500);
        muokkausStage.setScene(muokkausScene);
        muokkausStage.setTitle("Muokkaa varausta");
        return muokkausStage;
    }

    public Stage luoLisaaLaskuIkkuna(){
        Stage lisaaLaskuStage = new Stage();

        GridPane rootPaneeli=new GridPane();
        rootPaneeli.setAlignment(Pos.CENTER);
        rootPaneeli.setVgap(20);
        rootPaneeli.setHgap(20);

        rootPaneeli.setPadding(new Insets(10));
        rootPaneeli.setStyle("-fx-background-color: #bfddf2;");

        Label mokkilb =new Label("Mökki");
        Label asiakasLb =new Label("Asiakas");
        Label alkulb=new Label("Varaus alkaa");
        Label loppulb =new Label("Varaus päättyy");

        TextField mokkiTxt=new TextField();
        TextField asiakasTxt =new TextField();
        TextField alkuTxt =new TextField();
        TextField loppuTxt =new TextField();
        HBox row1=new HBox(mokkilb,mokkiTxt);
        row1.setSpacing(50);
        HBox row2=new HBox(asiakasLb, asiakasTxt);
        row2.setSpacing(43);
        HBox row3=new HBox(alkulb, alkuTxt);
        row3.setSpacing(17);
        HBox row4=new HBox(loppulb, loppuTxt);
        row4.setSpacing(5);
        VBox sarake=new VBox(row1,row2,row3,row4);
        sarake.setSpacing(20);

        sarake.setSpacing(15);
        sarake.setAlignment(Pos.CENTER);

        //buttonit ja action eventit
        Button laskutaBt=new Button("Luo lasku");
        Button suljeBt=new Button("Sulje");

        laskutaBt.setOnAction(e->{
            LaskunLuonti laskunLuonti =new LaskunLuonti();
            laskunLuonti.luoLasku(1);
            laskuValmis(laskunLuonti.getLaskuNro()).show();
            lisaaLaskuStage.close();
        });

        suljeBt.setOnAction(e->{
            //kysy suljetaanko ikkuna luomatta uutta laskua
            lisaaLaskuStage.close();
        });

        VBox buttons=new VBox(laskutaBt,suljeBt);
        buttons.setSpacing(20);
        rootPaneeli.add(buttons,2,2);

        VBox keskikohta=new VBox(sarake);
        keskikohta.setSpacing(20);
        rootPaneeli.add(keskikohta,1,0);

        Scene lisaalaskuScene = new Scene(rootPaneeli,500,500);
        lisaaLaskuStage.setScene(lisaalaskuScene);
        lisaaLaskuStage.setTitle("Uusi lasku");
        return lisaaLaskuStage;
    }

    public Stage laskuValmis(int laskunro){
        Stage valmisStage = new Stage();

        Text teksti = new Text("Lasku luotu\nonnistuneesti!\n\n" +
                "Laskun numero: "+laskunro+"\n\n" +
                "Lasku tallennettu Laskut-\nkansioon pdf-tiedostona.");

        teksti.setTextAlignment(TextAlignment.CENTER);

        Button okBt = new Button("OK");
        okBt.setOnAction(e-> valmisStage.close());

        BorderPane pane = new BorderPane();
        pane.setCenter(teksti);
        pane.setBottom(okBt);

        Scene scene = new Scene(pane, 300,300);
        valmisStage.setScene(scene);
        valmisStage.setTitle("Lasku tallennettu");

        return valmisStage;
    }

public Stage luoUusiVarausIkkuna() {
    Stage valmisStage = new Stage();
    BorderPane rootPaneeli = new BorderPane();
    rootPaneeli.setPadding(new Insets(10));
    //yhteys sql
    Yhteysluokka yhteys = new Yhteysluokka();

    VarausLuokka varausluokkaolio=new VarausLuokka();

    // mokkien lista
    ObservableList<String> mokkiData = FXCollections.observableArrayList(haeMokit(yhteys));
    ListView<String> mokkiLista = new ListView<>(mokkiData);
    mokkiLista.setMaxSize(450, 250);

    // Vasemmalle puolelle kalenteri ja mokkilista
    VBox vasenpuoli = new VBox(10);
    vasenpuoli.setAlignment(Pos.CENTER);
    Label alkuLabel = new Label("Check-in päivämäärä");
    Label loppuLabel = new Label("Check-out päivämäärä");
    
    //kalenteri pickerit
    DatePicker checkInDatePicker = new DatePicker(LocalDate.now());
    DatePicker checkOutDatePicker = new DatePicker();
    
    //asettelu
    GridPane kalenteriPane = new GridPane();
    kalenteriPane.setHgap(10);
    kalenteriPane.setVgap(10);
    kalenteriPane.setAlignment(Pos.CENTER);
    kalenteriPane.add(alkuLabel, 0, 0);
    kalenteriPane.add(checkInDatePicker, 0, 1);
    kalenteriPane.add(loppuLabel, 0, 2);
    kalenteriPane.add(checkOutDatePicker, 0, 3);
    //lisäys
    vasenpuoli.getChildren().addAll(mokkiLista, kalenteriPane);

    // Oikean puolen tekstit ja buttonit
    VBox oikeapuoli = new VBox(10);
    oikeapuoli.setAlignment(Pos.CENTER);
    oikeapuoli.setPadding(new Insets(5));

    // Tekstikentät ja Labelit
    TextField mokkiTextField = new TextField();
    mokkiTextField.setEditable(false);
    TextField alkuVarausTextField = new TextField();
    alkuVarausTextField.setEditable(false);
    TextField paattyVarausTextField = new TextField();
    paattyVarausTextField.setEditable(false);
    TextField nimiTextField = new TextField();
    TextField emailTextField = new TextField();
    TextField puhelinTextField = new TextField();
    TextField osoiteTextField = new TextField();

    // Info kentät johon tieto varauksesta
    VBox rowBox = new VBox(10);
    rowBox.setAlignment(Pos.CENTER);
    rowBox.getChildren().addAll(
            new HBox(new Label("Valitse mökki"), mokkiTextField),
            new HBox(new Label("Varaus alkaa"), alkuVarausTextField),
            new HBox(new Label("Varaus päättyy"), paattyVarausTextField),
            new HBox(new Label("Asiakkaan nimi"), nimiTextField),
            new HBox(new Label("Asiakkaan sähköpostiosoite"), emailTextField),
            new HBox(new Label("Asiakkaan puhelinnumero"), puhelinTextField),
            new HBox(new Label("Asiakkaan kotiosoite"), osoiteTextField)
    );

    // Varaus ja hinta
    Label varausLabel = new Label();
    Label hintaLabel = new Label();
    
    // ListView valinta -> päivittää valittuMokki & mokkiTextField
    mokkiLista.setOnMouseClicked(e -> {
        String valittu = mokkiLista.getSelectionModel().getSelectedItem();
        //halutaan tallettaa mikä mökki oli jotta voidaan laskee hinta
        valittuMokki.set(valittu);

        //jotta kenttään tulee oikea valittu tieto
        mokkiTextField.setText(valittu);

    });
    
    //alustetaan mökin hinnan laskua hakemalla metodilla sql id+hintaperyö
    Map<Double, Integer> hintaToMokkiId = MokkiData.haeMokinHinta(yhteys);

    // Kalenterin kuuntelijat ja muutettavat labelit sekä hinnan lasku ja textfieldin päivitys päivämäärän mukaan
    ChangeListener<LocalDate> paivamaaraListener = new PaivamaaraListener(
            checkInDatePicker, checkOutDatePicker,
            varausLabel, hintaLabel,
            alkuVarausTextField, paattyVarausTextField,
            hintaToMokkiId, valittuMokki,varausluokkaolio
    );
    //lisätään luotu kuuntelija
    checkInDatePicker.valueProperty().addListener(paivamaaraListener);
    checkOutDatePicker.valueProperty().addListener(paivamaaraListener);

    Yhteysluokka yhteysluokka = new Yhteysluokka();
    AsiakasData asiakasData = new AsiakasData();

    // Painikkeet
    Button tallennaButton = new Button("Tallenna");
    tallennaButton.setOnAction(e->{

        // jos kaikki kentät täytetty, tallennetaan ja suljetaan
        if((!mokkiTextField.getText().isEmpty())&&(!alkuVarausTextField.getText().isEmpty())&&(!paattyVarausTextField.getText().isEmpty())&&
                (!nimiTextField.getText().isEmpty())&&(!emailTextField.getText().isEmpty())&&(!puhelinTextField.getText().isEmpty())&&(!osoiteTextField.getText().isEmpty())){

            int tarkID = asiakasData.tarkistaAsiakas(yhteysluokka,nimiTextField.getText());

            if(tarkID>0){
                lisaaVaraus(yhteys,getVarauksenID(),checkInDatePicker.getValue(),checkOutDatePicker.getValue(),getVarauksenHinta(),annaKayttajaID(),tarkID,getValitunMokinID());
            } else if (tarkID==0){

                lisaaUusiAsiakas(yhteys,getAsiakasID(),nimiTextField.getText(),emailTextField.getText(),puhelinTextField.getText(),osoiteTextField.getText());


                Timeline timeline = new Timeline();
                timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1), eventti->{

                    if(sekuntti<2){
                        sekuntti+=1;
                    } else {
                        timeline.stop();
                        sekuntti = 0;
                        lisaaVaraus(yhteys,getVarauksenID(),checkInDatePicker.getValue(),checkOutDatePicker.getValue(),getVarauksenHinta(),annaKayttajaID(),getAsiakasID(),valitunMokinID);
                        System.out.println(valitunMokinID);
                    }
                }));

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Uusi asiakas lisätty!");
                alert.setHeaderText("Uusi asiakas lisätty!");
                alert.showAndWait();
            }

            valmisStage.close();

        } else {
            // Jos joku puuttuu, huomautetaan
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Pakollisia tietoja puuttuu");
            alert.setHeaderText("Pakollisia varaustietojaa puuttuu.");
            alert.setContentText("Vinkki!\nValitse mökki, check-in - ja check-out päivät." +
                    "\nTäytä asiakkaan nimi, sähköpostiosoite, puhelinnumero ja kotiosoite.");
            alert.showAndWait();
        }

    });

    Button suljeButton = new Button("Sulje");

    //sulje kun sulje
    suljeButton.setOnAction(e ->{

        //tarkistetaan että jos jossain kentässä on tekstiä, suljetaanko varmasti?
        if((!mokkiTextField.getText().isEmpty())||(!alkuVarausTextField.getText().isEmpty())||(!paattyVarausTextField.getText().isEmpty())||
                (!nimiTextField.getText().isEmpty())||(!emailTextField.getText().isEmpty())||(!puhelinTextField.getText().isEmpty())||(!osoiteTextField.getText().isEmpty())){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Varoitus");
            alert.setHeaderText("Varaustietoja ei ole tallennettu");
            alert.setContentText("Poistu tallentamatta varaustietoja?");
            Optional<ButtonType> valinta = alert.showAndWait();

            // jos painaa ok, suljetaan, jos painaa cancel, ei suljeta
            if (valinta.isPresent() && valinta.get() == ButtonType.OK) {
                valmisStage.close();
            } else {
                e.consume();
            }

        } else {
            valmisStage.close();
        }
    });

    //lisäys
    oikeapuoli.getChildren().addAll(rowBox, varausLabel, hintaLabel, tallennaButton, suljeButton);
    //asettelu
    rootPaneeli.setLeft(vasenpuoli);
    rootPaneeli.setRight(oikeapuoli);
    rootPaneeli.setStyle("-fx-background-color: #bfddf2;");

    Scene scene = new Scene(rootPaneeli, 700, 600);
    valmisStage.setScene(scene);
    valmisStage.setTitle("Luo uusi varaus");
    return valmisStage;
}

    public int getValitunMokinID() {
        String mokkiString = valittuMokki.get();
        Pattern idPattern=Pattern.compile("ID: ([\\d])");
        Matcher idMatcher= idPattern.matcher(mokkiString);
        int id = 1;
        //jos löytyy lisätään id valintunmokin id:ksi
        if(idMatcher.find()){
            id= Integer.parseInt((idMatcher.group(1)));
            System.out.println(id);
            setValitunMokinID(id);
            return id;
        }
        throw new RuntimeException("Ei löytynyt vapaata varausnumeroa.");
    }

    public void setValitunMokinID(int valitunMokinID) {
        this.valitunMokinID = valitunMokinID;
    }

    public int getVarauksenHinta() {
        return varauksenHinta;
    }

    public void setVarauksenHinta(int varauksenHinta) {
        this.varauksenHinta = varauksenHinta;
    }

    public int getAsiakasID(){
        Yhteysluokka yhteysolio=new Yhteysluokka();
        AsiakasData tarkistusOlio=new AsiakasData();
        Random random = new Random();

        //niinkauan generate uusi asiakasnumero, kunnes tulee sellainen joka ei ole jo databasessa
        for (int i = 0; i < 100; i++) {
            int asiakasnumero = 100 + random.nextInt(900); // 4-digit number
            int tarkistusnumero = tarkistusOlio.tarkistaAsiakasID(yhteysolio, asiakasnumero);

            // If tarkistusnumero == 0, the number is unique
            if (tarkistusnumero == 0) {
                setAsiakasID(asiakasnumero);
                System.out.println(asiakasnumero);
                return asiakasnumero;
            }
        }
        throw new RuntimeException("Ei löytynyt vapaata varausnumeroa.");
    }

    public void setAsiakasID(int idnumero){
        this.asiakasID=idnumero;
    }

    public int getVarauksenID(){
        Yhteysluokka yhteysolio=new Yhteysluokka();

        VarausData tarkistusvaraus=new VarausData();
        Random random = new Random();

        //niinkauan generate uusi varausnumero, kunnes tulee sellainen joka ei ole jo databasessa
        for (int i = 0; i < 1000; i++) {
            int uniikkivarausnumero = 1000 + random.nextInt(9000); // 4-digit number
            int tarkistusnumero = tarkistusvaraus.tarkistaVarausID(yhteysolio, uniikkivarausnumero);

            // If tarkistusnumero == 0, the number is unique
            if (tarkistusnumero == 0) {
                setVarauksenID(uniikkivarausnumero);
                System.out.println(uniikkivarausnumero);
                return uniikkivarausnumero;
            }
        }
        throw new RuntimeException("Ei löytynyt vapaata varausnumeroa.");
        }



    public void setVarauksenID(Integer varausnumero){
        this.varausID=varausnumero;
    }

    //sisäluokka päivämäärien ja muiden updatemiseen
    public static class PaivamaaraListener implements ChangeListener<LocalDate> {
        private DatePicker checkIn;
        private DatePicker checkOut;
        private Label varaus;
        private Label hinta;
        private TextField alku;
        private TextField loppu;
        private Map<Double, Integer> hintaToMokkiId;
        private StringProperty valittuMokki;
        private VarausLuokka ulkoluokka;

        //alustaja jossa kaikki mitä halutaan  muuttaa
        public PaivamaaraListener(DatePicker checkIn, DatePicker checkOut, Label varaus, Label hinta,
                                  TextField alku, TextField loppu,
                                  Map<Double, Integer> hintaToMokkiId, StringProperty valittuMokki, VarausLuokka yhteysUlkoLuokkan) {
            this.checkIn = checkIn;
            this.checkOut = checkOut;
            this.varaus = varaus;
            this.hinta = hinta;
            this.alku = alku;
            this.loppu = loppu;
            this.hintaToMokkiId = hintaToMokkiId;
            this.valittuMokki = valittuMokki;
            this.ulkoluokka=yhteysUlkoLuokkan;
        }

        //lister-interface toiminto, jolla päivät tunnistetaan
        @Override
        public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
            //eihän oo tyhjää
            LocalDate checkIn = this.checkIn.getValue();
            LocalDate checkOut = this.checkOut.getValue();

            // Onko valittu ja onko start ennen end päivää
            if (checkIn != null && checkOut != null && !checkOut.isBefore(checkIn)) {
                // Lasketaan päivät välissä chronoUnittilla
                long valipaivat = ChronoUnit.DAYS.between(checkIn, checkOut);
                varaus.setText("Mökkivaraus on: " + valipaivat + " vuorokautta");

                // Päivämäärät kenttiin
                alku.setText(checkIn.toString());
                loppu.setText(checkOut.toString());

                // Hinta per yö siitä valitusta mökistä johon varaus tehdään
                String mokki=valittuMokki.toString();
                //eihän oo tyhjää
                if (mokki != null && !mokki.isEmpty()) {

                    //laitetaan haluttu teksti, jota hintaPattern etsii (Hinta/yö: numero €) muodossa
                    //[] välissä hgyväksyy kaikki numerot doublena
                    Pattern hintaPattern = Pattern.compile("Hinta/yö: (\\d+(\\.\\d+)?) €");
                    //annetaan klikatun mökin string info, josta hintaPattern etsii yllä olevaa
                    Matcher hintaMatcher = hintaPattern.matcher(mokki);

                    //jos löytyy lasketaan hintta mökeille per yö
                    if (hintaMatcher.find()) {
                        //eka joka löytyy parsetaan string
                        double price = Double.parseDouble(hintaMatcher.group(1));
                        System.out.println(price);
                        //päivät kertaa hinta on koko hinta
                        double total = valipaivat * price;
                        ulkoluokka.setVarauksenHinta((int)total);
                        //asetetaan hinta -labelille arvoksi
                        hinta.setText("Hinta yhteensä: " + total + " €");
                    }
                    
                }
            }
        }
    }

    //lisää varaus
    public void lisaaVaraus(Yhteysluokka yhteysluokka,Integer varaus_id, LocalDate varausalku_date, LocalDate varausloppu_date, Integer hinta, Integer kayttaja_id,Integer asiakas_id, Integer mokki_id){


        if (varausalku_date == null || varausloppu_date == null) {
            throw new IllegalArgumentException("Päivämäärä ei saa olla null");
        }
        VarausData tarkistaid=new VarausData();
       int vastaus= tarkistaid.tarkistaVarausID(yhteysluokka,mokki_id);
        if (vastaus==0) {
            System.out.println("varaus_id already exists in the database!");
            return;  // Prevent insertion if the ID already exists
        }


        LocalDateTime lahtien = varausalku_date.atStartOfDay();
        Timestamp tsLahtien = Timestamp.valueOf(lahtien);
        LocalDateTime saakka = varausloppu_date.atStartOfDay();
        Timestamp tsSaakka = Timestamp.valueOf(saakka);

        try {
            Connection yhteys = yhteysluokka.getYhteys();
            if (yhteys == null) {
                System.err.println("Tietokantayhteys epäonnistui.");
            }
            String sql = "insert into varaukset values (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = yhteys.prepareStatement(sql);
            stmt.setInt(1, varaus_id);
            stmt.setTimestamp(2, tsLahtien);
            stmt.setTimestamp(3, tsSaakka);
            stmt.setInt(4, hinta);
            stmt.setInt(5, kayttaja_id);
            stmt.setInt(6, asiakas_id);
            stmt.setInt(7, mokki_id);

            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Uusi varaus lisätty!");
        alert.setHeaderText("Uusi varaus lisätty!");
        alert.showAndWait();
    }

    //lisää asiakas
    public void lisaaUusiAsiakas(Yhteysluokka yhteysluokka,Integer asiakas_id, String asiakkaan_nimi, String asiakkaan_sahkoposti, String puhelinnumero, String koti_osoite){

        try {
            Connection yhteys = yhteysluokka.getYhteys();
            if (yhteys == null) {
                System.err.println("Tietokantayhteys epäonnistui.");
            }
            String sql = "insert into asiakkaat values (?,?,?,?,?);";
            PreparedStatement stmt = yhteys.prepareStatement(sql);
            stmt.setInt(1, asiakas_id);
            stmt.setString(2, asiakkaan_nimi);
            stmt.setString(3, asiakkaan_sahkoposti);
            stmt.setString(4, puhelinnumero);
            stmt.setString(5, koti_osoite);
            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Uusi asiakas lisätty!");
        alert.setHeaderText("Uusi asiakas lisätty!");
        alert.showAndWait();
    }

    //kayttaja
    public Integer annaKayttajaID() {
        //jos aikaa, huomio että jos tulee uusia käyttäjiä
        int[] kayttajat = {3887, 4459, 7866, 2644};
        Random random = new Random();
        int annaNumero=random.nextInt(kayttajat.length);
        return kayttajat[annaNumero];
    }
}
