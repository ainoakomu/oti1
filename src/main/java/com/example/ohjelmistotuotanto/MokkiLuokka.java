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
import javafx.stage.Stage;


import java.util.Optional;
import java.util.Random;

import static com.example.ohjelmistotuotanto.MokkiData.haeMokit;

public class MokkiLuokka{

    private int mokkiID;
    private double hintaPerYo;
    private String mokinOsoite;
    private int neliot;
    private int vuodepaikat;
    private boolean rantasauna;
    private boolean omaranta;
    private boolean wifi;
    private boolean sisavessa;
    private boolean palju;


    public Stage luoMokitIkkuna() {
        Stage mokkiStage = new Stage();
        BorderPane rootPaneeli = new BorderPane();
        rootPaneeli.setPadding(new Insets(5, 5, 5, 5));
        rootPaneeli.setBackground(Taustakuvat.TaustakuvaAsettaminen.luoKolmasTausta());

        //sql yhteys
        Yhteysluokka olio=new Yhteysluokka();

        //observable lista, joka lisätään list view
        //käytetään datanhaku metodia
        ObservableList<String> mokkitietoData = FXCollections.observableArrayList(haeMokit(olio));
        ListView<String> mokkiLista = new ListView<>(mokkitietoData);

        mokkiLista.setMaxSize(900, 400);
        rootPaneeli.setCenter(mokkiLista);


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
                            setMokkiID(Integer.valueOf(arvo));
                            break;
                        case "Osoite":
                            setMokinOsoite(arvo);
                            break;
                        case "Hinta/yö":
                            setHintaPerYo(Double.parseDouble(arvo));
                            break;
                        case "Neliöt":
                            setNeliot(Integer.valueOf(arvo));
                            break;
                        case "Vuodepaikat":
                            setVuodepaikat(Integer.valueOf(arvo));
                            break;
                        case "Rantasauna":
                            if (arvo.equals("Kyllä")) {
                            setRantasauna(true);
                        } else if (arvo.equals("Ei")) {
                            setRantasauna(false);
                        }
                            break;
                        case "Oma ranta":
                            if (arvo.equals("Kyllä")) {
                                setOmaranta(true);
                            } else if (arvo.equals("Ei")) {
                                setOmaranta(false);
                            }
                            break;
                        case "WiFi":
                            if (arvo.equals("Kyllä")) {
                                setWifi(true);
                            } else if (arvo.equals("Ei")) {
                                setWifi(false);
                            }
                            break;
                        case "Sisä-WC":
                            if (arvo.equals("Kyllä")) {
                                setSisavessa(true);
                            } else if (arvo.equals("Ei")) {
                                setSisavessa(false);
                            }
                            break;
                        case "Palju":
                            if (arvo.equals("Kyllä")) {
                                setPalju(true);
                            } else if (arvo.equals("Ei")) {
                                setPalju(false);
                            }
                            break;
                    }
                }
            }
        });


        //buttonit ja action eventit
        Button addUusiMokki = new Button("Lisää uusi mökki");
        Button muokkaaMokkia = new Button("Muokkaa mökkiä");
        Button suljeBt = new Button("Sulje");

        addUusiMokki.setOnAction(e->{
            luoUusiMokkiIkkuna(mokkitietoData).show();
        });

        muokkaaMokkia.setOnAction(e->{
            //tarkistat iffillä onko mökki valittu listalta (mokki-id > 0)
            luoMuokkausMokkiIkkuna(mokkitietoData).show();
        });

        suljeBt.setOnAction(e->{
            mokkiStage.close();
        });


        HBox nappulaBoksi = new HBox(addUusiMokki, muokkaaMokkia, suljeBt);
        nappulaBoksi.setSpacing(20);
        rootPaneeli.setBottom(nappulaBoksi);
        nappulaBoksi.setAlignment(Pos.CENTER);
        rootPaneeli.setPadding(new Insets(5, 5, 5, 5));


        Scene mokkiScene = new Scene(rootPaneeli, 1000, 600);
        mokkiStage.setScene(mokkiScene);
        mokkiStage.setTitle("Mökit");
        return mokkiStage;
    }

    public Stage luoUusiMokkiIkkuna(ObservableList<String> lista) {
        Stage uusiMokkiStage = new Stage();
        GridPane rootPaneeli = new GridPane();
        rootPaneeli.setAlignment(Pos.CENTER);
        rootPaneeli.setVgap(10);
        rootPaneeli.setHgap(10);

        rootPaneeli.setPadding(new Insets(10));
        rootPaneeli.setStyle("-fx-background-color: #bfddf2;");

        Label osoitelb = new Label("Osoite");
        Label hintaLb = new Label("Hinta per yö");
        Label neliotlb = new Label("Neliöt");
        Label vuodepaikatlb = new Label("Vuodepaikat");
        Label ominaisuudetlb = new Label("Ominaisuudet");
        TextField osoiteTxt = new TextField();
        TextField hintaTxt = new TextField();
        TextField nelioTxt = new TextField();
        TextField vuodeTxt = new TextField();
        HBox row1 = new HBox(osoitelb, osoiteTxt);
        row1.setSpacing(45);
        HBox row2 = new HBox(hintaLb, hintaTxt);
        row2.setSpacing(15);
        HBox row3 = new HBox(neliotlb, nelioTxt);
        row3.setSpacing(48);
        HBox row4 = new HBox(vuodepaikatlb, vuodeTxt);
        row4.setSpacing(15);
        VBox sarake = new VBox(row1, row2, row3, row4);
        sarake.setSpacing(20);
        sarake.setSpacing(15);
        sarake.setAlignment(Pos.CENTER);

        CheckBox rantasaunaChbx = new CheckBox("Rantasauna");
        CheckBox rantaChbx = new CheckBox("Oma ranta");
        CheckBox wifiChbx = new CheckBox("Wi-Fi");
        CheckBox sisawcChbx = new CheckBox("Sisä-WC");
        CheckBox paljuChbx = new CheckBox("Palju");

        //buttonit ja action eventit
        Button tallennaBt = new Button("Tallenna");
        Button suljeBt = new Button("Sulje");

        tallennaBt.setOnAction(e->{
            //TARVITAAN: metodi jolla tarkistetaan onko kaikki tarvittavat tiedot täytetty
            // jos ei ole kaikkia tarvittavia tietoja, pitää tulla kehote täydentää

            //luodaan uudelle mökille if, joka on välillä 6-99 (koska 1-5 on jo olemassa pohjadatassa)
            //tähän tarviis vielä sellasen metodin, joka tarkistaa, että uusi id ei ole joku muu olemassa oleva id
            int uusiID = new Random().nextInt(94)+6;
            setMokkiID(uusiID);

            //setataan annetut tiedot
            setMokinOsoite(osoiteTxt.getText());
            setHintaPerYo(Double.parseDouble(hintaTxt.getText()));
            setNeliot(Integer.valueOf(nelioTxt.getText()));
            setVuodepaikat(Integer.valueOf(vuodeTxt.getText()));

            if(rantasaunaChbx.isSelected()){
                setRantasauna(true);
            } else if (!rantasaunaChbx.isSelected()){
                setRantasauna(false);
            }
            if(rantaChbx.isSelected()){
                setRantasauna(true);
            } else if (!rantaChbx.isSelected()){
                setRantasauna(false);
            }
            if(wifiChbx.isSelected()){
                setWifi(true);
            } else if (!wifiChbx.isSelected()){
                setWifi(false);
            }
            if(sisawcChbx.isSelected()){
                setSisavessa(true);
            } else if (!sisawcChbx.isSelected()){
                setSisavessa(false);
            }
            if(paljuChbx.isSelected()){
                setPalju(true);
            } else if (!paljuChbx.isSelected()){
                setPalju(false);
            }

            // tallenna tiedot tietokantaaan
            Yhteysluokka yhteysluokka = new Yhteysluokka();
            MokkiData mokkiData = new MokkiData();
            mokkiData.lisaaMokki(yhteysluokka,getMokkiID(),getHintaPerYo(),getMokinOsoite(),getNeliot(),getVuodepaikat(),isRantasauna(),isOmaranta(),isWifi(),isSisavessa(),isPalju());

            setMokkiID(0);
            setHintaPerYo(0);
            setMokinOsoite("");
            setNeliot(0);
            setVuodepaikat(0);
            setRantasauna(false);
            setOmaranta(false);
            setWifi(false);
            setSisavessa(false);
            setPalju(false);


            //päivitä listviewin lista
            lista.setAll(FXCollections.observableArrayList(haeMokit(yhteysluokka)));

            // TARVITAAN ilmoitus että tiedot tallennettu

            //metodi joka tallentaa tiedot sqllään
            uusiMokkiStage.close();
        });

        suljeBt.setOnAction(e->{
            //kysy suljetaanko
            uusiMokkiStage.close();
        });


        VBox buttons = new VBox(tallennaBt, suljeBt);
        buttons.setSpacing(15);
        buttons.setAlignment(Pos.TOP_CENTER);
        rootPaneeli.add(buttons, 2, 2);
        VBox checkBox = new VBox(ominaisuudetlb, rantasaunaChbx,rantaChbx,wifiChbx,sisawcChbx,paljuChbx);
        checkBox.setSpacing(15);
        checkBox.setAlignment(Pos.CENTER_LEFT);
        VBox keskikohta = new VBox(sarake, checkBox);
        keskikohta.setSpacing(20);
        rootPaneeli.add(keskikohta, 1, 0);

        Scene uusiMokkiScene = new Scene(rootPaneeli, 500, 500);
        uusiMokkiStage.setScene(uusiMokkiScene);
        uusiMokkiStage.setTitle("Uusi mökki");
        return uusiMokkiStage;
    }

    public Stage luoMuokkausMokkiIkkuna(ObservableList<String> lista) {
        Stage muokkausStage = new Stage();
        GridPane rootPaneeli = new GridPane();
        rootPaneeli.setAlignment(Pos.CENTER);
        rootPaneeli.setVgap(10);
        rootPaneeli.setHgap(10);
        rootPaneeli.setPadding(new Insets(10));
        rootPaneeli.setStyle("-fx-background-color: #bfddf2;");

        Label osoitelb = new Label("Osoite");
        Label hintaLb = new Label("Hinta per yö");
        Label neliotlb = new Label("Neliöt");
        Label vuodepaikatlb = new Label("Vuodepaikat");
        Label ominaisuudetlb = new Label("Ominaisuudet");
        TextField osoiteTxt = new TextField();
        TextField hintaTxt = new TextField();
        TextField nelioTxt = new TextField();
        TextField vuodeTxt = new TextField();
        HBox row1 = new HBox(osoitelb, osoiteTxt);
        row1.setSpacing(45);
        HBox row2 = new HBox(hintaLb, hintaTxt);
        row2.setSpacing(15);
        HBox row3 = new HBox(neliotlb, nelioTxt);
        row3.setSpacing(48);
        HBox row4 = new HBox(vuodepaikatlb, vuodeTxt);
        row4.setSpacing(15);
        VBox sarake = new VBox(row1, row2, row3, row4);
        sarake.setSpacing(20);
        sarake.setSpacing(15);
        sarake.setAlignment(Pos.CENTER);

        CheckBox rantasaunaChbx = new CheckBox("Rantasauna");
        CheckBox rantaChbx = new CheckBox("Oma ranta");
        CheckBox wifiChbx = new CheckBox("Wi-Fi");
        CheckBox sisawcChbx = new CheckBox("Sisä-WC");
        CheckBox paljuChbx = new CheckBox("Palju");

        //setataan valitun mökin tiedot
        osoiteTxt.setText(getMokinOsoite());
        hintaTxt.setText(String.valueOf(getHintaPerYo()));
        nelioTxt.setText(String.valueOf(getNeliot()));
        vuodeTxt.setText(String.valueOf(getVuodepaikat()));
        rantasaunaChbx.setSelected(isRantasauna());
        rantaChbx.setSelected(isOmaranta());
        wifiChbx.setSelected(isWifi());
        sisawcChbx.setSelected(isSisavessa());
        paljuChbx.setSelected(isPalju());

        //buttonit ja action eventit
        Button tallennaBt = new Button("Tallenna muutokset");
        Button poistaBt = new Button("Poista mökki");
        Button suljeBt = new Button("Sulje");

        Yhteysluokka yhteysluokka = new Yhteysluokka();
        MokkiData mokkiData = new MokkiData();

        tallennaBt.setOnAction(e->{
            if(!osoiteTxt.getText().isEmpty()){

                //TARVITAAN kysy tallennetaanko muutokset

                setMokinOsoite(osoiteTxt.getText());
                setHintaPerYo(Double.parseDouble(hintaTxt.getText()));
                setNeliot(Integer.valueOf(nelioTxt.getText()));
                setVuodepaikat(Integer.valueOf(vuodeTxt.getText()));

                if(rantasaunaChbx.isSelected()){
                    setRantasauna(true);
                } else if (!rantasaunaChbx.isSelected()){
                    setRantasauna(false);
                }
                if(rantaChbx.isSelected()){
                    setOmaranta(true);
                } else if (!rantaChbx.isSelected()){
                    setOmaranta(false);
                }
                if(wifiChbx.isSelected()){
                    setWifi(true);
                } else if (!wifiChbx.isSelected()){
                    setWifi(false);
                }
                if(sisawcChbx.isSelected()){
                    setSisavessa(true);
                } else if (!sisawcChbx.isSelected()){
                    setSisavessa(false);
                }
                if(paljuChbx.isSelected()){
                    setPalju(true);
                } else if (!paljuChbx.isSelected()){
                    setPalju(false);
                }

                //tallenna muutokset sqlään
                mokkiData.muokkaaMokkia(yhteysluokka,getMokkiID(),getHintaPerYo(),getMokinOsoite(),getNeliot(),getVuodepaikat(),isRantasauna(),isOmaranta(),isWifi(),isSisavessa(),isPalju());

                //TARVITAAN ilmoita että tallennettu

                //päivitetään lista
                lista.setAll(FXCollections.observableArrayList(haeMokit(yhteysluokka)));

                //ilmoita että tallennettu
                muokkausStage.close();

            }else {
                // anna warning että jottain puuttuu
                e.consume();
            }
        });

        poistaBt.setOnAction(e->{
            if(!osoiteTxt.getText().isEmpty()){
                //kysy poistetaaanko mökki varmasti
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Mökkitiedon poisto");
                alert.setHeaderText("Poistetaanko mökin tiedot tietokannasta?");
                alert.setContentText("Tätä toimintoa ei voi enää peruuttaa.");
                Optional<ButtonType> valinta = alert.showAndWait();

                // jos painaa ok, poistetaan, jos painaa cancel, ei poisteta
                if (valinta.isPresent() && valinta.get() == ButtonType.OK) {
                    mokkiData.poistaMokki(yhteysluokka,getMokkiID());
                    //päivitetään lista
                    lista.setAll(FXCollections.observableArrayList(haeMokit(yhteysluokka)));

                    // TARVITAAN ilmoita että käyttäjätiedot poistettu
                    muokkausStage.close();
                } else {
                    e.consume();
                }

            } else {
                // anna warning että jottain puuttuu
                e.consume();
                System.out.println("mökin id tyhjä");
            }
        });

        suljeBt.setOnAction(e->{
            //kysy suljetaanko ikkuna
            muokkausStage.close();
        });

        VBox buttons = new VBox(tallennaBt, poistaBt, suljeBt);
        buttons.setSpacing(15);
        buttons.setAlignment(Pos.TOP_CENTER);
        rootPaneeli.add(buttons, 2, 2);
        VBox checkBox = new VBox(ominaisuudetlb, rantasaunaChbx,rantaChbx, wifiChbx, sisawcChbx,paljuChbx);
        checkBox.setSpacing(15);
        checkBox.setAlignment(Pos.CENTER_LEFT);
        VBox keskikohta = new VBox(sarake, checkBox);
        keskikohta.setSpacing(20);
        rootPaneeli.add(keskikohta, 1, 0);

        Scene muokkausScene = new Scene(rootPaneeli, 500, 500);
        muokkausStage.setScene(muokkausScene);
        muokkausStage.setTitle("Muokkaa mökkiä");
        return muokkausStage;
    }


    public int getMokkiID() {
        return mokkiID;
    }

    public void setMokkiID(int mokkiID) {
        this.mokkiID = mokkiID;
    }

    public double getHintaPerYo() {
        return hintaPerYo;
    }

    public void setHintaPerYo(double hintaPerYo) {
        this.hintaPerYo = hintaPerYo;
    }

    public String getMokinOsoite() {
        return mokinOsoite;
    }

    public void setMokinOsoite(String mokinOsoite) {
        this.mokinOsoite = mokinOsoite;
    }

    public int getNeliot() {
        return neliot;
    }

    public void setNeliot(int neliot) {
        this.neliot = neliot;
    }

    public int getVuodepaikat() {
        return vuodepaikat;
    }

    public void setVuodepaikat(int vuodepaikat) {
        this.vuodepaikat = vuodepaikat;
    }

    public boolean isRantasauna() {
        return rantasauna;
    }

    public void setRantasauna(boolean rantasauna) {
        this.rantasauna = rantasauna;
    }

    public boolean isOmaranta() {
        return omaranta;
    }

    public void setOmaranta(boolean omaranta) {
        this.omaranta = omaranta;
    }

    public boolean isWifi() {
        return wifi;
    }

    public void setWifi(boolean wifi) {
        this.wifi = wifi;
    }

    public boolean isSisavessa() {
        return sisavessa;
    }

    public void setSisavessa(boolean sisavessa) {
        this.sisavessa = sisavessa;
    }

    public boolean isPalju() {
        return palju;
    }

    public void setPalju(boolean palju) {
        this.palju = palju;
    }
}
