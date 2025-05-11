package com.example.ohjelmistotuotanto;


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

    public Stage luoVarauksetIkkuna(){
        Stage varausStage = new Stage();

        //sql yhteys
        Yhteysluokka olio=new Yhteysluokka();
        BorderPane rootPaneeli=new BorderPane();
        rootPaneeli.setPadding(new Insets(5,5,5,5));

        //käytetään datanhaku metodia datan luokasta
        ObservableList<String> varausData = FXCollections.observableArrayList(haeVaraukset(olio));
        ListView<String> varauslista = new ListView<>(varausData);
        varauslista.setMaxSize(350,250);
        rootPaneeli.setCenter(varauslista);

        //buttonit ja action eventit
        Button addLasku=new Button("Tee lasku");
        Button muokkaaVarausta =new Button("Muokkaa varausta");
        Button suljeBt=new Button("Sulje");

        addLasku.setOnAction(e->{
            luoLisaaLaskuIkkuna().show();
        });

        muokkaaVarausta.setOnAction(e->{
            luoMuokkaaVaraustaIkkuna(varausData).show();
        });

        suljeBt.setOnAction(e->{
            varausStage.close();
        });

        HBox nappulaBoksi=new HBox(addLasku, muokkaaVarausta,suljeBt);
        nappulaBoksi.setSpacing(20);
        rootPaneeli.setBottom(nappulaBoksi);
        nappulaBoksi.setAlignment(Pos.CENTER);
        rootPaneeli.setPadding(new Insets(5,5,5,5));

        Scene varausScene = new Scene(rootPaneeli,500,500);
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
        okBt.setOnAction(e->{
            valmisStage.close();
        });

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

    // Arvojen sitominen hinnan laskua varten
    StringProperty valittuMokki = new SimpleStringProperty();


    // ListView valinta -> päivittää valittuMokki & mokkiTextField
    mokkiLista.setOnMouseClicked(e -> {
        String valittu = mokkiLista.getSelectionModel().getSelectedItem();
        //halutaan tallettaa mikä mökki oli jotta voidaan laskee hinta
        valittuMokki.set(valittu);
        //jotta kenttään tulee oikea valittu tieto
        mokkiTextField.setText(valittu);

        mokkiLista.getSelectionModel().selectedItemProperty().addListener((obs, vanha, uusi) -> {
            if (uusi != null) {
                String[] kentat = uusi.split(", ");

                for (String kentta : kentat) {
                    String[] avainArvo = kentta.split(": ");
                    if (avainArvo.length < 2) continue;

                    String avain = avainArvo[0].trim();
                    String arvo = avainArvo[1].trim();

                    switch (avain) {
                        case "ID":
                            setValitunMokinID(Integer.parseInt(avain));
                            break;

                    }
                }
            }
        });
    });
    //alustetaan mökin hinnan laskua hakemalla metodilla sql id+hintaperyö
    Map<Double, Integer> hintaToMokkiId = MokkiData.haeMokinHinta(yhteys);

    // Kalenterin kuuntelijat ja muutettavat labelit sekä hinnan lasku ja textfieldin päivitys päivämäärän mukaan
    ChangeListener<LocalDate> paivamaaraListener = new PaivamaaraListener(
            checkInDatePicker, checkOutDatePicker,
            varausLabel, hintaLabel,
            alkuVarausTextField, paattyVarausTextField,
            hintaToMokkiId, valittuMokki
    );
    //lisätään luotu kuuntelija
    checkInDatePicker.valueProperty().addListener(paivamaaraListener);
    checkOutDatePicker.valueProperty().addListener(paivamaaraListener);

    Yhteysluokka yhteysluokka = new Yhteysluokka();

    // Painikkeet
    Button tallennaButton = new Button("Tallenna");
    tallennaButton.setOnAction(e->{

        // jos kaikki kentät täytetty, tallennetaan ja suljetaan
        if((!mokkiTextField.getText().isEmpty())&&(!alkuVarausTextField.getText().isEmpty())&&(!paattyVarausTextField.getText().isEmpty())&&
                (!nimiTextField.getText().isEmpty())&&(!emailTextField.getText().isEmpty())&&(!puhelinTextField.getText().isEmpty())&&(!osoiteTextField.getText().isEmpty())){
            String asiakasID= String.valueOf(annaAsiakasID());

            lisaaVaraus(yhteys,annaVarausID() ,annaAsiakasID(), getValitunMokinID(),checkInDatePicker.getValue() ,checkOutDatePicker.getValue(),getVarauksenHinta(),annaKayttajaID());
            lisaaUusiAsiakas(yhteys,asiakasID,nimiTextField.getText(),emailTextField.getText(),puhelinTextField.getText(),osoiteTextField.getText());

            /*
            Alert alert = new Alert(Alert.AlertType.NONE);
            ButtonType type = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);


            alert.setTitle("Uusi varaus tallennettu");
            alert.setHeaderText("Varaus tallennettu!");
            alert.setContentText("Mökki: "+ mokkiTextField.getText()+
                    "\nVaraus alkaa: " + alkuVarausTextField.getText()+
                    "\nVaraus päättyy: " + paattyVarausTextField.getText()+
                    "\nHinta: "+ // tähän joku millä näytetään varauksen hinta
                    "\nAsiakas: " + nimiTextField.getText()
            // plus tähän joku info, että jos asiakas oli uusi asiakas, että uusi asiakas on lisätty järjestlemään?
            );
            alert.showAndWait();
             */

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

    Scene scene = new Scene(rootPaneeli, 600, 600);
    valmisStage.setScene(scene);
    valmisStage.setTitle("Luo uusi varaus");
    return valmisStage;
}

    public int getValitunMokinID() {
        return valitunMokinID;
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

        //alustaja jossa kaikki mitä halutaan  muuttaa
        public PaivamaaraListener(DatePicker checkIn, DatePicker checkOut, Label varaus, Label hinta,
                                  TextField alku, TextField loppu,
                                  Map<Double, Integer> hintaToMokkiId, StringProperty valittuMokki) {
            this.checkIn = checkIn;
            this.checkOut = checkOut;
            this.varaus = varaus;
            this.hinta = hinta;
            this.alku = alku;
            this.loppu = loppu;
            this.hintaToMokkiId = hintaToMokkiId;
            this.valittuMokki = valittuMokki;
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
                String mokkiString = valittuMokki.get();
                //eihän oo tyhjää
                if (mokkiString != null && !mokkiString.isEmpty()) {
                    //laitetaan haluttu teksti, jota pattern etsii (Hinta/yö: numero €) muodossa
                    //[] välissä hgyväksyy kaikki numerot doublena
                    Pattern pattern = Pattern.compile("Hinta/yö: ([\\d.]+) €");
                    //annetaan klikatun mökin string info, josta pattern etsii yllä olevaa
                    Matcher matcher = pattern.matcher(mokkiString);
                    //jos löytyy lasketaan hintta mökeille per yö
                    if (matcher.find()) {
                        //eka joka löytyy parsetaan string
                        double price = Double.parseDouble(matcher.group(1));
                        //päivät kertaa hinta on koko hinta
                        double total = valipaivat * price;
                        VarausLuokka varausLuokka = new VarausLuokka();
                        int smallInt = 0;
                        varausLuokka.setVarauksenHinta(smallInt=(int)total);

                        //asetetaan hinta -labelille arvoksi
                        hinta.setText("Hinta yhteensä: " + total + " €");
                    }
                }
            }
        }
    }
    //lisää varaus
    public void lisaaVaraus(Yhteysluokka yhteysluokka,Integer varaus_id, Integer asiakas_id, int mokki_id, LocalDate varausalku_date, LocalDate varausloppu_date, int hinta, Integer kayttaja_id){

        LocalDateTime lahtien = varausalku_date.atStartOfDay();
        Timestamp tsLahtien = Timestamp.valueOf(lahtien);
        LocalDateTime saakka = varausloppu_date.atStartOfDay();
        Timestamp tsSaakka = Timestamp.valueOf(saakka);

        try {

            Connection yhteys = yhteysluokka.getYhteys();
            if (yhteys == null) {
                System.err.println("Tietokantayhteys epäonnistui.");
            }
            String sql = "insert into varaukset values (?,?,?,?,?,?,?);";
            PreparedStatement stmt = yhteys.prepareStatement(sql);
            stmt.setInt(1, varaus_id);
            stmt.setInt(2, asiakas_id);
            stmt.setInt(3, mokki_id);
            stmt.setTimestamp(4, tsLahtien);
            stmt.setTimestamp(5, tsSaakka);
            stmt.setInt(6, hinta);
            stmt.setInt(7, kayttaja_id);
            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //lisää asiakas
    public void lisaaUusiAsiakas(Yhteysluokka yhteysluokka,String asiakas_id, String asiakkaan_nimi, String asiakkaan_sahkoposti, String puhelinnumero, String koti_osoite){

        int asiakkaanID = Integer.valueOf(asiakas_id);

        try {
            Connection yhteys = yhteysluokka.getYhteys();
            if (yhteys == null) {
                System.err.println("Tietokantayhteys epäonnistui.");
            }
            String sql = "insert into asiakkaat values (?,?,?,?,?);";
            PreparedStatement stmt = yhteys.prepareStatement(sql);
            stmt.setInt(1, asiakkaanID);
            stmt.setString(2, asiakkaan_nimi);
            stmt.setString(3, asiakkaan_sahkoposti);
            stmt.setString(4, puhelinnumero);
            stmt.setString(5, koti_osoite);
            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public Integer annaVarausID() {
        Random random = new Random();
        int varausID = 0;
        for (int i = 0; i < 4; i++) {
            varausID = random.nextInt(100);
            return varausID;
        }
        return varausID;
    }

    public Integer annaAsiakasID() {

        // tää on hyvä näin, vielä lisätään, että checkataan, ettei se ID ole jo olemassa asiakastiedoissa
        Random random = new Random();
        int asiakasID = 0;
        for (int i = 0; i < 3; i++) {
            asiakasID = random.nextInt(100);
            return asiakasID;
        }
        return asiakasID;
    }

    public Integer annaKayttajaID() {
        int[] kayttajat = {3887, 4459, 7866, 2644};
        Random random = new Random();
        int annaNumero=random.nextInt(kayttajat.length);
        int valitseNumero=kayttajat[annaNumero];
        return valitseNumero;
    }
}
