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
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.ohjelmistotuotanto.MokkiData.haeMokit;
import static com.example.ohjelmistotuotanto.VarausData.haeVaraukset;

/**
 * luodaan ikkunoita varauksille
 * kasitellaan varauksia ja muokataan niita
 */
public class VarausLuokka {
    /**
     * listasta valintun mokin id
     */
    private int valitunMokinID;
    /**
     * muokatun mokin id
     */
    private int muutosMokkiID;
    /**
     * varauksen hinta
     */
    private int varauksenHinta;
    /**
     * varauksessa olevan kayttajan id
     */
    private int varauksenKayttajanID;
    /**
     * asiakkaan id
     */
    private int asiakasID;
    /**
     * varauksen id
     */
    private int varausID;
    /**
     * laskulla olevan varauksen id
     */
    private int laskulleVarID = 0;
    /**
     * laskulla oleva alkupvm
     */
    private LocalDate alkuPVM;
    /**
     * laskulla oleva loppupvm
     */
    private LocalDate loppuPVM;
    /**
     * listalta valitun mokin kaikki tiedot
     */
    private StringProperty valittuMokki=new SimpleStringProperty();

    /**
     * luodaan ikkuna, jossa nakyy varaukset
     * varauksia voi muokata ja lisata
     * @return luotu stage
     */
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

        varauslista.getSelectionModel().selectedItemProperty().addListener((obs, vanha, uusi) -> {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");

            if (uusi != null) {
                String[] kentat = uusi.split(", ");

                for (String kentta : kentat) {
                    String[] avainArvo = kentta.split(": ");
                    if (avainArvo.length < 2) continue;

                    String avain = avainArvo[0].trim();
                    String arvo = avainArvo[1].trim();

                    switch (avain) {
                        case "Varaus ID":
                            setVarauksenID(Integer.valueOf(arvo));
                            this.laskulleVarID = Integer.parseInt(arvo);
                            break;
                        case "Asiakas ID":
                            setAsiakasID(Integer.parseInt(arvo));
                            break;
                        case "Alku":
                            LocalDateTime pelkkaAlkuPaiva = LocalDateTime.parse(arvo, formatter);
                            setAlkuPVM(pelkkaAlkuPaiva.toLocalDate());
                            break;
                        case "Loppu":
                            LocalDateTime pelkkaLoppuPaiva = LocalDateTime.parse(arvo, formatter);
                            setLoppuPVM(pelkkaLoppuPaiva.toLocalDate());
                            break;
                        case "Hinta":
                            setVarauksenHinta(Integer.parseInt(arvo));
                            break;
                        case "Mökki ID":
                            muutosMokkiID = Integer.parseInt(arvo);
                            break;
                    }
                }
            }
        });

        //buttonit ja action eventit
        Button addLasku=new Button("Tee lasku");
        Button muokkaaVarausta =new Button("Muokkaa varausta");
        Button suljeBt=new Button("Sulje");

        addLasku.setOnAction(e-> luoLisaaLaskuIkkuna(olio).show());

        muokkaaVarausta.setOnAction(e-> luoMuokkaaVaraustaIkkuna(varausData).show());

        suljeBt.setOnAction(e-> varausStage.close());

        HBox nappulaBoksi=new HBox(addLasku, muokkaaVarausta,suljeBt);
        nappulaBoksi.setSpacing(20);
        rootPaneeli.setBottom(nappulaBoksi);
        nappulaBoksi.setAlignment(Pos.CENTER);
        rootPaneeli.setPadding(new Insets(5,5,5,5));

        Scene varausScene = new Scene(rootPaneeli,1000,600);
        varausStage.setScene(varausScene);
        varausStage.setTitle("Varaukset");
        return varausStage;
    }

    /**
     * luodaan varauksen muokkaus-ikkuna, jossa varauksen tietoja voi muuttaa
     * @param lista varauksien lista
     * @return luotu stage
     */
    public Stage luoMuokkaaVaraustaIkkuna(ObservableList<String> lista){
        Stage muokkausStage = new Stage();
        GridPane rootPaneeli=new GridPane();
        rootPaneeli.setAlignment(Pos.CENTER);
        rootPaneeli.setVgap(20);
        rootPaneeli.setHgap(20);

        rootPaneeli.setPadding(new Insets(10));
        rootPaneeli.setStyle("-fx-background-color: #bfddf2;");

        Label asiakasLb =new Label("Asiakas");
        Label alkulb=new Label("Varaus alkaa");
        Label loppulb =new Label("Varaus päättyy");
        Label hintalb =new Label("Hinta");
        Label kayttajalb=new Label("Käyttäjän ID");
        Label mokkilb =new Label("Mökki");
        Label varauslb=new Label("Varauksen ID");

        TextField mokkiTxt=new TextField();
        mokkiTxt.setEditable(false);
        TextField asiakasTxt =new TextField();
        asiakasTxt.setEditable(false);
        TextField hintaTextField = new TextField();
        hintaTextField.setEditable(false);
        TextField kayttajaTextField = new TextField();
        kayttajaTextField.setEditable(false);
        TextField varausTextField = new TextField();
        varausTextField.setEditable(false);

        DatePicker checkInDatePicker = new DatePicker(LocalDate.now());
        DatePicker checkOutDatePicker = new DatePicker();

        //kuuntelija
        HBox row1=new HBox(mokkilb,mokkiTxt);
        row1.setSpacing(50);
        HBox row2=new HBox(asiakasLb, asiakasTxt);
        row2.setSpacing(43);
        HBox row3=new HBox(alkulb, checkInDatePicker);
        row3.setSpacing(17);
        HBox row4=new HBox(loppulb, checkOutDatePicker);
        row4.setSpacing(5);
        HBox row5=new HBox(hintalb,hintaTextField);

        HBox row6=new HBox(kayttajalb,kayttajaTextField);

        HBox row7=new HBox(varauslb,varausTextField);

        VBox sarake=new VBox(row1,row2,row3,row4,row5,row6,row7);
        sarake.setSpacing(20);

        sarake.setSpacing(15);
        sarake.setAlignment(Pos.CENTER);

        //kaikille käytettävät instanssit
        Yhteysluokka yhteysluokka = new Yhteysluokka();
        VarausData varausData = new VarausData();
        //kutsutaan saman yhteysluokan instanssin varauksen id
        annaVarauksenKayttajanID(yhteysluokka);
        //asetetaan arvot
        mokkiTxt.setText(String.valueOf(muutosMokkiID));
        asiakasTxt.setText(String.valueOf(getAsiakasID()));
        checkInDatePicker.setValue(getAlkuPVM());
        checkOutDatePicker.setValue(getLoppuPVM());
        hintaTextField.setText(String.valueOf(getVarauksenHinta()));
        int placeholderHinta=getVarauksenHinta();
        asiakasTxt.setText(String.valueOf(getAsiakasID()));
        kayttajaTextField.setText(String.valueOf(getVarauksenKayttajanID()));
        varausTextField.setText(String.valueOf(getVarauksenID()));

        checkInDatePicker.valueProperty().addListener(new muokkauksenKuuntelija(checkInDatePicker,checkOutDatePicker,mokkiTxt,hintaTextField,kayttajaTextField,asiakasTxt,varausTextField,placeholderHinta));
        checkOutDatePicker.valueProperty().addListener(new muokkauksenKuuntelija(checkInDatePicker,checkOutDatePicker,mokkiTxt,hintaTextField,kayttajaTextField,asiakasTxt,varausTextField,placeholderHinta));

        //buttonit ja action eventit
        Button tallennaBt=new Button("Tallenna muutokset");
        Button poistaBt=new Button("Poista varaus");
        Button suljeBt=new Button("Sulje");

        tallennaBt.setOnAction(e->{

            if ((mokkiTxt.getText().isEmpty())||
                    (checkInDatePicker.getValue()==null)||(checkOutDatePicker.getValue()==null)||
                    (checkInDatePicker.getValue().isAfter(checkOutDatePicker.getValue()))||
                    (checkOutDatePicker.getValue().isBefore(checkInDatePicker.getValue()))){
                Alert alert3 = new Alert(Alert.AlertType.WARNING);
                alert3.setTitle("Varoitus");
                alert3.setHeaderText("Kaikkia tietoja ei ole täytetty!");
                alert3.setContentText("Täytä kaikki kohdat, jotta voit tallentaa.\n" +
                        "Varmista, että varauksen loppupäivä on alkupäivän jälkeen.");
                alert3.showAndWait();
                e.consume();
            } else {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Tallennus");
                alert.setHeaderText("Tallennus");
                alert.setContentText("Tallennetaanko muutokset tietokantaan?");
                Optional<ButtonType> valinta = alert.showAndWait();
                if (valinta.isPresent() && valinta.get() == ButtonType.OK) {
                    //tallennetaan
                    varausData.muokkaaVarausta(yhteysluokka,getVarauksenID(),checkInDatePicker.getValue(),checkOutDatePicker.getValue(),hintaTextField,kayttajaTextField,mokkiTxt,asiakasTxt);

                    lista.setAll(FXCollections.observableArrayList(haeVaraukset(yhteysluokka)));

                    Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                    alert2.setTitle("Tallennettu");
                    alert2.setHeaderText("Muutokset tallennettu");
                    alert2.setContentText("Tallennus onnistuneesti tietokantaan");
                    alert2.showAndWait();

                    muokkausStage.close();
                }
            }
        });

        poistaBt.setOnAction(e->{
            //kysy poistetaaanko varaus varmasti
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Varauksen poisto");
            alert.setHeaderText("Poistetaanko varaus tietokannasta?");
            alert.setContentText("Tätä toimintoa ei voi enää peruuttaa.");
            Optional<ButtonType> valinta = alert.showAndWait();

            // jos painaa ok, poistetaan, jos painaa cancel, ei poisteta
            if (valinta.isPresent() && valinta.get() == ButtonType.OK) {
                varausData.poistaVaraus(yhteysluokka,getVarauksenID());
                lista.setAll(FXCollections.observableArrayList(haeVaraukset(yhteysluokka)));

                // TARVITAAN ilmoita että asiakastiedot poistettu
                Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                alert2.setTitle("Varauksen poisto onnistui");
                alert2.setHeaderText("Tietojen poisto");
                alert2.setContentText("Tiedot poistettu onnistuneesti");
                muokkausStage.close();
            } else {
                e.consume();
            }

        });

        suljeBt.setOnAction(e->{
            //TARVITAAN kysy suljetaanko ikkuna
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Poistu");
            alert.setHeaderText("Olet poistumassa");
            alert.setContentText("Poistutaanko varmasti?");
            Optional<ButtonType> varmistus = alert.showAndWait();
            if(varmistus.isPresent()&&varmistus.get()==ButtonType.OK){
                lista.setAll(FXCollections.observableArrayList(haeVaraukset(yhteysluokka)));
                muokkausStage.close();
            }
            else{
                e.consume();
            }
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

    /**
     * luodaan ikkuna milla voidaan tehda lasku valitun mokin perusteella
     * @param yhteysluokka yhteys tietokantaan
     * @return luotu stage
     */
    public Stage luoLisaaLaskuIkkuna(Yhteysluokka yhteysluokka){
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

        mokkiTxt.setText(String.valueOf(muutosMokkiID));
        asiakasTxt.setText(String.valueOf(getAsiakasID()));
        alkuTxt.setText(getAlkuPVM().toString());
        loppuTxt.setText(getLoppuPVM().toString());

        //buttonit ja action eventit
        Button laskutaBt=new Button("Luo lasku");
        Button suljeBt=new Button("Sulje");

        laskutaBt.setOnAction(e->{
            LaskunLuonti laskunLuonti =new LaskunLuonti();
            laskunLuonti.luoLasku(yhteysluokka,laskulleVarID);
            laskuValmis(laskunLuonti.getLaskuNro()).show();
            lisaaLaskuStage.close();
        });

        suljeBt.setOnAction(e->{
            //kysy suljetaanko ikkuna luomatta uutta laskua
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Poistu");
            alert.setHeaderText("Olet poistumassa.");
            alert.setContentText("Poistu luomatta uutta laskua?");
            Optional<ButtonType> varmistus = alert.showAndWait();
            if(varmistus.isPresent()&&varmistus.get()==ButtonType.OK){
                lisaaLaskuStage.close();
            }
            else{
                e.consume();
            }
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

    /**
     * kun lasku on luotu, naytetaan tassa rakennettu ikkuna
     * @param laskunro tehdyn laskun numero
     * @return luotu stage
     */
    public Stage laskuValmis(int laskunro){
        Stage valmisStage = new Stage();

        Text teksti = new Text("Lasku luotu\nonnistuneesti!\n\n" +
                "Laskun numero: "+laskunro+"\n\n" +
                "Lasku tallennettu Laskut-\nkansioon pdf-tiedostona.");

        teksti.setTextAlignment(TextAlignment.CENTER);

        Button okBt = new Button("OK");
        okBt.setOnAction(e-> valmisStage.close());

        BorderPane pane = new BorderPane();
        pane.setStyle("-fx-background-color: #bfddf2;");
        pane.setCenter(teksti);
        pane.setBottom(okBt);

        Scene scene = new Scene(pane, 300,300);
        valmisStage.setScene(scene);
        valmisStage.setTitle("Lasku tallennettu");

        return valmisStage;
    }

    /**
     * luodaan uuden varauksen tekemiseen ikkuna, jossa listassa valitulle mokille luodaan uusi varaus
     * @return luotu stage
     */
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

    // ListView valinta -> päivittää valittuMokki & mokkiTextField
    mokkiLista.setOnMouseClicked(e -> {
        String valittu = mokkiLista.getSelectionModel().getSelectedItem();

        if (valittu != null && !valittu.isEmpty()) {

            valittuMokki.set(valittu);
            System.out.println(valittuMokki);
            mokkiTextField.setText(valittu);
        } else {

            mokkiTextField.setText("et valinnut mökkiä");
        }
    });

    //alustetaan mökin hinnan laskua hakemalla metodilla sql id+hintaperyö
    Map<Double, Integer> hintaToMokkiId = MokkiData.haeMokinHinta(yhteys);

    // Kalenterin kuuntelijat ja muutettavat labelit sekä hinnan lasku ja textfieldin päivitys päivämäärän mukaan
    ChangeListener<LocalDate> yleinenkuuntelija = new PaivamaaraListener(
            checkInDatePicker, checkOutDatePicker,
            varausLabel, hintaLabel,
            alkuVarausTextField, paattyVarausTextField,
            hintaToMokkiId, valittuMokki
    );
    //lisätään luotu kuuntelija
    checkInDatePicker.valueProperty().addListener(yleinenkuuntelija);
    checkOutDatePicker.valueProperty().addListener(yleinenkuuntelija);

    AsiakasData asiakasData = new AsiakasData();
    VarausData varausData = new VarausData();


    // Painikkeet
    Button tallennaButton = new Button("Tallenna");
    tallennaButton.setOnAction(e -> {

        // jos kaikki kentät täytetty, tallennetaan ja suljetaan
        if ((!mokkiTextField.getText().isEmpty()) && (!alkuVarausTextField.getText().isEmpty()) && (!paattyVarausTextField.getText().isEmpty()) &&
                (!nimiTextField.getText().isEmpty()) && (!emailTextField.getText().isEmpty()) && (!puhelinTextField.getText().isEmpty()) && (!osoiteTextField.getText().isEmpty())) {
            int tarkID = asiakasData.tarkistaAsiakas(yhteys, nimiTextField.getText());

            if (tarkID > 0) {
                // Asiakas löytyy
                setAsiakasID(tarkID);
            } else {
                // Uusi asiakas
                int uusiAsiakasID = luoAsiakasID.asiakasID(yhteys, asiakasData);
                setAsiakasID(uusiAsiakasID);
                lisaaUusiAsiakas(yhteys, getAsiakasID(), nimiTextField.getText(), emailTextField.getText(),puhelinTextField.getText(), osoiteTextField.getText());
            }

            int uusiVarausID = VarausID.luoVarausID(yhteys, varausData);
            setVarauksenID(uusiVarausID);
            System.out.println(hintaLabel);
            Pattern pricePattern = Pattern.compile("(\\d+)\\s*€\\s*'$");
            Matcher priceMatcher = pricePattern.matcher(hintaLabel.toString());
            if(priceMatcher.find()){
                int hinta =  Integer.parseInt(priceMatcher.group(1));
                System.out.println(hinta);
                setVarauksenHinta(hinta);
            }
            else{
                System.out.println("ei käsitelty mitää stringiä");
            }

            lisaaVaraus(yhteys, getVarauksenID(), checkInDatePicker.getValue(),
                    checkOutDatePicker.getValue(), getVarauksenHinta(),
                    annaKayttajaID(), getAsiakasID(), getValitunMokinID());

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Varaus lisätty");
            alert.setHeaderText("Varaus lisättiin onnistuneesti!");
            alert.showAndWait();

        valmisStage.close();

        } else {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Pakollisia tietoja puuttuu");
        alert.setHeaderText("Pakollisia varaustietoja puuttuu.");
        alert.setContentText("Täytä kaikki kentät.");
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

    /**
     * haetaan listalta valitun mokin id
     * @return id numero
     */
    public int getValitunMokinID() {
        String mokkiString = valittuMokki.get();

        // onko empty
        if (mokkiString == null || mokkiString.isEmpty()) {
            throw new IllegalStateException("Valittu mökki on null tai tyhjä.");
        }

        //
        Pattern idPattern = Pattern.compile("ID: (\\d)");
        Matcher idMatcher = idPattern.matcher(mokkiString);

        //
        if (idMatcher.find()) {
            int id = Integer.parseInt(idMatcher.group(1));
            System.out.println(id);
            setValitunMokinID(id);
            return id;
        }

        // If no match is found, throw an exception
        throw new RuntimeException("Ei löytynyt vapaata varausnumeroa.");
    }

    /**
     * asetetaan listasta valitulle mokille id
     * @param valitunMokinID haluttu id numero
     */
    public void setValitunMokinID(int valitunMokinID) {
        this.valitunMokinID = valitunMokinID;
    }

    /**
     * haetaan varauksen id
     * @return id numero
     */
    public int getVarauksenHinta() {
        return varauksenHinta;
    }

    /**
     * asetetaan varaukselle hinta
     * @param varauksenHinta hinta numeroina
     */
    public void setVarauksenHinta(int varauksenHinta) {
        this.varauksenHinta = varauksenHinta;
    }

    /**
     * haetaan asiakkan id numero
     * @return id numero
     */
    public int getAsiakasID(){
        return asiakasID;
    }

    /**
     * asetetaan asiakkan id
     * @param idnumero haluttu id
     */
    public void setAsiakasID(int idnumero){
        this.asiakasID=idnumero;
    }

    /**
     * haetaan varauksen id
     * @return id numero
     */
    public int getVarauksenID(){
        return varausID;
        }

    /**
     * asetetaan varaukselle id
     * @param varausnumero haluttu id
     */
    public void setVarauksenID(Integer varausnumero){
        this.varausID=varausnumero;
    }

    /**
     * haetaan varauksen alkupvm
     * @return paivamaara
     */
    public LocalDate getAlkuPVM() {
        return alkuPVM;
    }

    /**
     * asetetaan varauksen alkupvm
     * @param alkuPVM haluttu paivamaara
     */
    public void setAlkuPVM(LocalDate alkuPVM) {
        this.alkuPVM = alkuPVM;
    }

    /**
     * haetaan varauksen loppupvm
     * @return paivamaara
     */
    public LocalDate getLoppuPVM() {
        return loppuPVM;
    }

    /**
     * asetetaan varauksen loppupvm
     * @param loppuPVM paivamaara
     */
    public void setLoppuPVM(LocalDate loppuPVM) {
        this.loppuPVM = loppuPVM;
    }

    /**
     *haetaan varauksella olevan kayttajan id
     * @return id numero
     */
    public int getVarauksenKayttajanID() {
        return varauksenKayttajanID;
    }

    /**
     * asetetaan varauksella olevan kayttajan id
     * @param varauksenKayttajanID haluttu id
     */
    public void setVarauksenKayttajanID(int varauksenKayttajanID) {
        this.varauksenKayttajanID = varauksenKayttajanID;
    }

    /**
     * luodaan sisaluokka, jossa kuuntelijat, jotka huomioivat uuden varauksen kaikkien kohtien muuttumista
     * lasketaan koknaishinta seka varauksen kokonaiskesto
     */
    public static class PaivamaaraListener implements ChangeListener<LocalDate> {
            private DatePicker checkIn;
            private DatePicker checkOut;
            private Label varaus;
            private Label hinta;
            private TextField alku;
            private TextField loppu;
            private Map<Double, Integer> hintaToMokkiId;
            private StringProperty valittuMokki2;

        /**
         * alustaja kuuntelijalle paivamaarissa
         * @param checkIn datepicker alkuvpm
         * @param checkOut datepicker loppupvm
         * @param varaus varauksen id
         * @param hinta kokonaishinta
         * @param alku varauksen alku tekstikenttaan
         * @param loppu varauksen loppu tekstikenttaan
         * @param hintaToMokkiId varauksen hinta per yo
         * @param valittuMokki listasta valitun mokin tiedot
         */
            public PaivamaaraListener(DatePicker checkIn, DatePicker checkOut, Label varaus, Label hinta,
                                      TextField alku, TextField loppu, Map<Double, Integer> hintaToMokkiId,
                                      StringProperty valittuMokki) {
                this.checkIn = checkIn;
                this.checkOut = checkOut;
                this.varaus = varaus;
                this.hinta = hinta;
                this.alku = alku;
                this.loppu = loppu;
                this.hintaToMokkiId = hintaToMokkiId;
                this.valittuMokki2 = valittuMokki;
            }

        /**
         * paivamaarien muutoksilla lasketaan varauksen kesto seka kokonaishinta
         * laskut ovat riippuvaisia mokista, silla jokaisen mokin hinta per yon on eri
         * @param observable datepickers
         * @param oldValue alkupaivaaman valitsija
         * @param newValue loppupaivamaaran valitsija
         */
            @Override
            public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {

                LocalDate checkInDate = this.checkIn.getValue();
                LocalDate checkOutDate = this.checkOut.getValue();

                // eihän oo empty
                if (checkInDate != null && checkOutDate != null && !checkOutDate.isBefore(checkInDate)) {
                    // valipaivaat
                    long daysBetween = ChronoUnit.DAYS.between(checkInDate, checkOutDate);
                    varaus.setText("Mökkivaraus on: " + daysBetween + " vuorokautta");

                    //tieto textfieldiin
                    alku.setText(checkInDate.toString());
                    loppu.setText(checkOutDate.toString());

                    // valitun mokin info
                    String mokki = valittuMokki2.get();
                    System.out.println(mokki);

                    if (mokki != null && !mokki.isEmpty()) {
                        //etsitään raha/yö pattern
                        Pattern pricePattern = Pattern.compile("Hinta/yö: (\\d+(\\.\\d+)?)");
                        Matcher priceMatcher = pricePattern.matcher(mokki);

                        // lasketaan hinta
                        if (priceMatcher.find()) {
                            double pricePerNight = Double.parseDouble(priceMatcher.group(1));
                            System.out.println("Hinta per yö: " + pricePerNight);

                            // hinta per yö totaali
                            int totalPrice = (int) (daysBetween * pricePerNight);
                            hinta.setText("Hinta yhteensä: " + totalPrice + " €");
                        } else {
                            // If no price is found, show an error message or do nothing
                            hinta.setText("Hinta ei löytynyt.");
                        }
                    } else {
                        // If mokki information is missing, show an error or default message
                        hinta.setText("Mökki ei ole valittu.");
                    }
                } else {
                    // If dates are invalid, reset the labels and text fields
                    varaus.setText("Virheelliset päivämäärät.");
                    hinta.setText("Hinta ei saatavilla.");
                    alku.clear();
                    loppu.clear();
                }
            }
        }


    /**
     * lisataan tietokantaan kokonaan uusi varaus
     * @param yhteysluokka yhteys tietokantaan
     * @param varaus_id varauksen id
     * @param varausalku_date varauksen alku pvm
     * @param varausloppu_date varauksen loppu pvm
     * @param hinta varauksen hinta
     * @param kayttaja_id kayttajan id
     * @param asiakas_id asiakkaan id
     * @param mokki_id mokin id
     */
    public void lisaaVaraus(Yhteysluokka yhteysluokka,Integer varaus_id, LocalDate varausalku_date, LocalDate varausloppu_date, Integer hinta, Integer kayttaja_id,Integer asiakas_id, Integer mokki_id){


        if (varausalku_date == null || varausloppu_date == null) {
            throw new IllegalArgumentException("Päivämäärä ei saa olla null");
        }
        VarausData tarkistaid = new VarausData();
        boolean exists = tarkistaid.tarkistaVarausID(yhteysluokka, varaus_id);

        if (exists) {
            System.out.println("varaus_id on jo olemassa!");
            return;
        } else {

            System.out.println("Lisätään uusi varaus"+varaus_id);
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

    /**
     * lisataan tietokantaan kokonaan uusi asiakas
     * @param yhteysluokka yhteys tietokantaan
     * @param asiakas_id asiakkaan id
     * @param asiakkaan_nimi asiakkan nimi
     * @param asiakkaan_sahkoposti asiakkan sahkopostiosoite
     * @param puhelinnumero asiakkan puhelinnumero
     * @param koti_osoite asiakkaan kotiosoite
     */
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

    /**
     * generoi kayttajan id numeron
     * @return id numero
     */
    public Integer annaKayttajaID() {
        //jos aikaa, huomio että jos tulee uusia käyttäjiä
        int[] kayttajat = {3887, 4459, 7866, 2644};
        Random random = new Random();
        int annaNumero=random.nextInt(kayttajat.length);
        return kayttajat[annaNumero];
    }

    /**
     * tietyn varauksen kayttajan olemassaoleva id numero
     * @param yhteysluokka yhteys tietokantaan
     */
    public void annaVarauksenKayttajanID(Yhteysluokka yhteysluokka){
        String list= String.valueOf(haeVaraukset(yhteysluokka));
        if (list!=null){
            Pattern KayttajaIDPattern = Pattern.compile("Käyttäjä ID: (\\d{4})");
            Matcher IDmatcher = KayttajaIDPattern.matcher(list);
            if (IDmatcher.find()){
                int id=Integer.parseInt(IDmatcher.group(1));
                System.out.println("Kayttajan ID "+id);
                setVarauksenKayttajanID(id);
            }
            else{
                System.out.println("EI löytynyt kayttajan id");
            }
        }
    }

    /**
     * luokalla luodaan metodi uuden varauksen id numeron luomiseen
     */
    public static class VarausID {
        /**
         * luodaan kokonaan uusi varauksen id ja tarkistetaan sen olemassa olo tietokannassa
         * @param yhteysolio yhteys tietokantaan
         * @param varausData varauksen tiedot toisesta luokasta
         * @return id
         */
        public static int luoVarausID(Yhteysluokka yhteysolio, VarausData varausData) {
            Random random = new Random();

            for (int i = 0; i < 1000; i++) {
                int id = 1000 + random.nextInt(9000);
                if (!varausData.tarkistaVarausID(yhteysolio, id)) {
                    return id;
                }
            }

            throw new RuntimeException("Ei löytynyt vapaata varausnumeroa.");
        }
    }

    /**
     * luodaan asiakkaalle id numero
     */
    public static class luoAsiakasID {
        /**
         * luodaan kokonaan uusi asiakas id jos sita ei loydy tietokannasta
         * @param yhteysolio yhteys tietokantaan
         * @param asiakasData asiakkaan tiedot toisesta luokasta
         * @return id
         */
        public static int asiakasID(Yhteysluokka yhteysolio, AsiakasData asiakasData) {
            Random random = new Random();

            for (int i = 0; i < 100; i++) {
                int asiakasnumero = 100 + random.nextInt(900); // 3-digit number

                int exists = asiakasData.tarkistaAsiakasID(yhteysolio, asiakasnumero);

                if (exists == 0) {
                    System.out.println("Löytyi uniikki asiakas ID: " + asiakasnumero);
                    return asiakasnumero;
                } else {
                    System.out.println("Asiakas ID " + asiakasnumero + " on jo olemassa.");
                }
            }

            throw new RuntimeException("Ei löytynyt vapaata asiakasnumeroa 100 yrityksen jälkeen.");
        }
    }

    /**
     * kuuntelija joka kuuntelee muokkauksessa tieutekenttien muutoksia ja laskee hinnan
     */
    public static class muokkauksenKuuntelija implements ChangeListener<LocalDate> {
        /**
         * varauksen alkupvm
         */
        private DatePicker checkIn;
        /**
         * varauksen loppupvm
         */
        private DatePicker checkOut;
        /**
         * mokin tiedot tekstikentassa
         */
        private TextField mokki;
        /**
         * hinnan tiedot tekstikentassa
         */
        private TextField hinta;
        /**
         * asiakkan tiedot tekstikentassa
         */
        private TextField asiakas;
        /**
         * asiakkan tiedot tekstikentassa
         */
        private TextField kayttaja;
        /**
         * varauksen tiedot tekstikentassa
         */
        private TextField varaus;
        /**
         * luetaan hinta mokin tiedoista jotta voidaan laskea kokonaishinta
         */
        private int placeholder;

        public muokkauksenKuuntelija(DatePicker checkIn, DatePicker checkOut, TextField mokkiID, TextField hinta, TextField kayttaja, TextField asiakas,TextField varausID, Integer placeholder) {
            this.checkIn = checkIn;
            this.checkOut = checkOut;
            this.mokki=mokkiID;
            this.hinta = hinta;
            this.asiakas=asiakas;
            this.kayttaja=kayttaja;
            this.varaus=varausID;
            this.placeholder=placeholder;
        }

        /**
         * lasketaan muutettujen paivien perusteella uusi hinta
         * @param observable datepickers
         * @param oldValue varauksen alkupvm
         * @param newValue varauksen loppupvm
         */
        @Override
        public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
            LocalDate checkIn=this.checkIn.getValue();
            LocalDate checkOut=this.checkOut.getValue();


            if (checkIn != null && checkOut != null && !checkOut.isBefore(checkIn)) {
                // valipaivaat
                long valipaivat = ChronoUnit.DAYS.between(checkIn, checkOut);
                int kokonaishinta= (int) (valipaivat*placeholder);
                hinta.setText(String.valueOf(kokonaishinta));
            }
            else {
                hinta.clear();
            }
        }
    }
}
