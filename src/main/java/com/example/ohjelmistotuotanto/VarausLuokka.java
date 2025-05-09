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
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.ohjelmistotuotanto.MokkiData.haeMokinHinta;
import static com.example.ohjelmistotuotanto.MokkiData.haeMokit;
import static com.example.ohjelmistotuotanto.VarausData.haeVaraukset;

public class VarausLuokka {
    private final StringProperty valittuMokki = new SimpleStringProperty();
    private Map<Double, Integer> hintaToMokkiId;



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
            luoMuokkaaVaraustaIkkuna().show();
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

    public Stage luoMuokkaaVaraustaIkkuna(){
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

        tallennaBt.setOnAction(e->{
            //kysy tallennetaanko muutokse
            //tallenna muutokset sqlään
            //ilmoita että tallennettu
            muokkausStage.close();
        });

        poistaBt.setOnAction(e->{
            //kysy poistetaanko mökki
            //poista mökki sqlästä
            //ilmoita että poistettu
            muokkausStage.close();
        });

        suljeBt.setOnAction(e->{
            //kysy suljetaanko ikkuna
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
/**
    public Stage luoUusiVarausIkkuna(){
        Stage valmisStage=new Stage();
        BorderPane rootPaneeli = new BorderPane();
        rootPaneeli.setPadding(new Insets(10,10,10,10));

        //sql yhteys
        Yhteysluokka olio=new Yhteysluokka();

        //observable lista, joka lisätään list view
        //käytetään datanhaku metodia
        ObservableList<String> mokkiData = FXCollections.observableArrayList(haeMokit(olio));
        ListView<String> mokkiLista = new ListView<>(mokkiData);
        mokkiLista.setMaxSize(450, 250);


        //vasen
        VBox vasenpuoli=new VBox();
        vasenpuoli.setAlignment(Pos.CENTER);
        vasenpuoli.setSpacing(10);

        DatePicker checkInDatePicker = new DatePicker();
        Label alku=new Label("Check in päivämäärä");
        DatePicker checkOutDatePicker = new DatePicker();
        Label loppu=new Label("Check out päivämäärä");
        GridPane kalenteriPane = new GridPane();
        kalenteriPane.setHgap(10);
        kalenteriPane.setVgap(10);
        kalenteriPane.add(alku, 0, 0);
        kalenteriPane.add(checkInDatePicker, 0, 1);
        kalenteriPane.add(loppu, 0, 2);
        kalenteriPane.add(checkOutDatePicker, 0, 3);
        kalenteriPane.setAlignment(Pos.CENTER);

        checkInDatePicker.setValue(LocalDate.now());

        vasenpuoli.getChildren().addAll(mokkiLista,kalenteriPane);
        rootPaneeli.setLeft(vasenpuoli);




        //right side
        VBox oikeapuoli=new VBox();
        oikeapuoli.setSpacing(10);
        oikeapuoli.setAlignment(Pos.CENTER);
        oikeapuoli.setPadding(new Insets(5,5,5,5));
        //buttons
        Button tallennaBt=new Button("Tallenna");
        Button suljeBt=new Button("Sulje");
        // labels
        Label mokkilb=new Label("Valitse mökki");
        Label varausalkulb=new Label("Varaus alkaa");
        Label varauspaattyylb=new Label("Varaus päättyy");
        Label asiakasnimilb=new Label("Asiakkaan nimi");
        Label asiakasspostilb=new Label("Asiakkaan säköpostiosoite");
        Label puhelinnrolb=new Label("Asiakkaan puhelinnumero");
        Label osoitelb=new Label("Asiakkaan kotiosoite");

        //textfields
        TextField mokkitxt=new TextField();
        mokkitxt.setEditable(false);
        mokkiLista.setOnMouseClicked(e->{
            String valittumokki=mokkiLista.getSelectionModel().getSelectedItem();
            valittuMokki.set(valittumokki);

            mokkitxt.setText(valittumokki);
        });
        TextField alkvaraustxt =new TextField();
        alkvaraustxt.setEditable(false);
        TextField paatvaraustxt=new TextField();
        paatvaraustxt.setEditable(false);
        TextField nimitxt=new TextField();
        TextField spostitxt=new TextField();
        TextField puhnrotxt=new TextField();
        TextField osoitetxt=new TextField();

        //rows
        VBox rowBox=new VBox();
        HBox row1=new HBox(mokkilb,mokkitxt);
        HBox row2=new HBox(varausalkulb,alkvaraustxt);
        HBox row3=new HBox(varauspaattyylb,paatvaraustxt);
        HBox row4=new HBox(asiakasnimilb,nimitxt);
        HBox row5=new HBox(asiakasspostilb,spostitxt);
        HBox row6=new HBox(puhelinnrolb,puhnrotxt);
        HBox row7=new HBox(osoitelb,osoitetxt);

        //spacing and adding
        rowBox.setSpacing(10);
        rowBox.setAlignment(Pos.CENTER);
        rowBox.getChildren().addAll(row1,row2,row3,row4,row5,row6,row7);

        //kk ja rahamäärä labels
        Label varaus=new Label();
        Label hinta=new Label();
        //listener for changes
        checkInDatePicker.valueProperty().addListener(
                new paivamaara(checkInDatePicker, checkOutDatePicker, varaus, hinta, alkvaraustxt, paatvaraustxt, hintaToMokkiId, valittuMokki)
        );

        checkOutDatePicker.valueProperty().addListener(
                new paivamaara(checkInDatePicker, checkOutDatePicker, varaus, hinta, alkvaraustxt, paatvaraustxt, hintaToMokkiId, valittuMokki)
        );
        //right side
        oikeapuoli.getChildren().addAll(rowBox,varaus,hinta,tallennaBt,suljeBt);
        oikeapuoli.setSpacing(10);
        //aadding all
        rootPaneeli.setLeft(vasenpuoli);
        rootPaneeli.setRight(oikeapuoli);

        Scene scene = new Scene(rootPaneeli, 600,600);
        valmisStage.setScene(scene);
        valmisStage.setTitle("Luo uusi varaus");
        return valmisStage;
    }

    public static class paivamaara implements ChangeListener<LocalDate> {
        private DatePicker checkIn;
        private DatePicker checkOut;
        private Label varaus;
        private Label hinta;
        private TextField alku;
        private TextField loppu;
        private Map<Double, Integer> hintaToMokkiId;
        private StringProperty valittuMokki;


        //alustaja
        public paivamaara(DatePicker checkInDatePicker, DatePicker checkOutDatePicker, Label varaus, Label hinta, TextField alku,TextField loppu, Map<Double, Integer> hintaToMokkiId, StringProperty valittuMokki) {
            this.checkIn = checkInDatePicker;
            this.checkOut = checkOutDatePicker;
            this.varaus = varaus;
            this.hinta=hinta;
            this.alku=alku;
            this.loppu=loppu;
            this.hintaToMokkiId = hintaToMokkiId;
            this.valittuMokki=valittuMokki;
        }

        //change listener interfacen changed metodi
        @Override
        public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
            Yhteysluokka olio=new Yhteysluokka();
            // onhan valittu
            LocalDate checkIn = this.checkIn.getValue();
            LocalDate checkOut = this.checkOut.getValue();
            Map<Double, Integer> hintaToMokkiId = (Map<Double, Integer>) MokkiData.haeMokinHinta(olio);

            if (checkIn != null && checkOut != null) {
                // Calculate the number of days between check-in and check-out
                long daysBetween = ChronoUnit.DAYS.between(checkIn, checkOut);
                varaus.setText("Mökkivaraus on: " + daysBetween+ " vuorokautta");
                alku.setText(checkIn.toString());
                loppu.setText(checkOut.toString());

                String mokkiString = valittuMokki.get();

                if (mokkiString != null && !mokkiString.isEmpty()) {
                    Pattern pattern = Pattern.compile("Hinta/yö: ([\\d.]+) €");
                    Matcher matcher = pattern.matcher(mokkiString);
                    if (matcher.find()) {
                        double hinta = Double.parseDouble(matcher.group(1));
                        if (hintaToMokkiId.containsKey(hinta)) {
                            int mokkiId = hintaToMokkiId.get(hinta);
                            System.out.println("Mökki ID: " + mokkiId);
                        }
                    }
                }

            }
        }
    }*/

    public Stage luoUusiVarausIkkuna() {
        Stage valmisStage = new Stage();
        BorderPane rootPaneeli = new BorderPane();
        rootPaneeli.setPadding(new Insets(10));

        Yhteysluokka olio = new Yhteysluokka();

        // UI Components
        ObservableList<String> mokkiData = FXCollections.observableArrayList(haeMokit(olio));
        ListView<String> mokkiLista = new ListView<>(mokkiData);
        mokkiLista.setMaxSize(450, 250);

        // Left side: Mökki + Calendar
        VBox vasenpuoli = new VBox(10);
        vasenpuoli.setAlignment(Pos.CENTER);

        Label alku = new Label("Check in päivämäärä");
        Label loppu = new Label("Check out päivämäärä");
        DatePicker checkInDatePicker = new DatePicker(LocalDate.now());
        DatePicker checkOutDatePicker = new DatePicker();

        GridPane kalenteriPane = new GridPane();
        kalenteriPane.setHgap(10);
        kalenteriPane.setVgap(10);
        kalenteriPane.setAlignment(Pos.CENTER);
        kalenteriPane.add(alku, 0, 0);
        kalenteriPane.add(checkInDatePicker, 0, 1);
        kalenteriPane.add(loppu, 0, 2);
        kalenteriPane.add(checkOutDatePicker, 0, 3);

        vasenpuoli.getChildren().addAll(mokkiLista, kalenteriPane);

        // Right side components
        VBox oikeapuoli = new VBox(10);
        oikeapuoli.setAlignment(Pos.CENTER);
        oikeapuoli.setPadding(new Insets(5));

        // Labels and TextFields
        TextField mokkitxt = new TextField(); mokkitxt.setEditable(false);
        TextField alkvaraustxt = new TextField(); alkvaraustxt.setEditable(false);
        TextField paatvaraustxt = new TextField(); paatvaraustxt.setEditable(false);
        TextField nimitxt = new TextField();
        TextField spostitxt = new TextField();
        TextField puhnrotxt = new TextField();
        TextField osoitetxt = new TextField();

        // Info fields layout
        VBox rowBox = new VBox(10);
        rowBox.setAlignment(Pos.CENTER);
        rowBox.getChildren().addAll(
                new HBox(new Label("Valitse mökki"), mokkitxt),
                new HBox(new Label("Varaus alkaa"), alkvaraustxt),
                new HBox(new Label("Varaus päättyy"), paatvaraustxt),
                new HBox(new Label("Asiakkaan nimi"), nimitxt),
                new HBox(new Label("Asiakkaan säköpostiosoite"), spostitxt),
                new HBox(new Label("Asiakkaan puhelinnumero"), puhnrotxt),
                new HBox(new Label("Asiakkaan kotiosoite"), osoitetxt)
        );

        // Varaus duration and price
        Label varaus = new Label();
        Label hinta = new Label();

        // Value bindings

        StringProperty valittuMokki = new SimpleStringProperty();

        // ListView selection -> updates valittuMokki & mokkitxt
        mokkiLista.setOnMouseClicked(e -> {
            String valittu = mokkiLista.getSelectionModel().getSelectedItem();
            valittuMokki.set(valittu);
            mokkitxt.setText(valittu);
        });

        // Calendar listeners
        ChangeListener<LocalDate> listener = new paivamaara(
                checkInDatePicker, checkOutDatePicker,
                varaus, hinta,
                alkvaraustxt, paatvaraustxt,
                hintaToMokkiId, valittuMokki
        );

        checkInDatePicker.valueProperty().addListener(listener);
        checkOutDatePicker.valueProperty().addListener(listener);

        // Buttons
        Button tallennaBt = new Button("Tallenna");
        Button suljeBt = new Button("Sulje");

        oikeapuoli.getChildren().addAll(rowBox, varaus, hinta, tallennaBt, suljeBt);

        rootPaneeli.setLeft(vasenpuoli);
        rootPaneeli.setRight(oikeapuoli);

        Scene scene = new Scene(rootPaneeli, 600, 600);
        valmisStage.setScene(scene);
        valmisStage.setTitle("Luo uusi varaus");
        return valmisStage;
    }

    public static class paivamaara implements ChangeListener<LocalDate> {
        private DatePicker checkIn;
        private DatePicker checkOut;
        private Label varaus;
        private Label hinta;
        private TextField alku;
        private TextField loppu;
        private Map<Double, Integer> hintaToMokkiId;
        private StringProperty valittuMokki;

        public paivamaara(DatePicker checkIn, DatePicker checkOut, Label varaus, Label hinta,
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

        @Override
        public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
            LocalDate checkIn = this.checkIn.getValue();
            LocalDate checkOut = this.checkOut.getValue();

            if (checkIn != null && checkOut != null && !checkOut.isBefore(checkIn)) {
                long daysBetween = ChronoUnit.DAYS.between(checkIn, checkOut);
                varaus.setText("Mökkivaraus on: " + daysBetween + " vuorokautta");
                alku.setText(checkIn.toString());
                loppu.setText(checkOut.toString());

                String mokkiString = valittuMokki.get();
                if (mokkiString != null && !mokkiString.isEmpty()) {
                    Pattern pattern = Pattern.compile("Hinta/yö: ([\\d.]+) €");
                    Matcher matcher = pattern.matcher(mokkiString);
                    if (matcher.find()) {
                        double price = Double.parseDouble(matcher.group(1));
                        double total = daysBetween * price;
                        hinta.setText("Hinta yhteensä: " + total + " €");

                        if (hintaToMokkiId.containsKey(price)) {
                            int mokkiId = hintaToMokkiId.get(price);
                            System.out.println("Mökki ID: " + mokkiId);
                        }
                    }
                }
            }
        }
    }

}
