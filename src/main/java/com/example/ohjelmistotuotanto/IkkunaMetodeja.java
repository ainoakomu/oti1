package com.example.ohjelmistotuotanto;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class IkkunaMetodeja extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        //saadaan esille ikkuna
        primaryStage.setScene(luoMokitIkkuna());
        primaryStage.show();
    }

    public static Scene luoKirjatumisIkkuna(){
        BorderPane rootPaneeli=new BorderPane();
        Scene ruutu=new Scene(rootPaneeli,400,400);

        //hbox for logging in
        VBox kirjautumisLaatikko=new VBox();
        kirjautumisLaatikko.setSpacing(10);
        kirjautumisLaatikko.setPadding(new Insets(5,50,5,50));
        kirjautumisLaatikko.setAlignment(Pos.CENTER);
        //kentät
        Text nimi=new Text("VillaBOOK");
        Text login=new Text("Kirjaudu sisään");
        TextField kayttajatunnus=new TextField();
        kayttajatunnus.setPromptText("Käyttäjätunnus");
        kayttajatunnus.setPrefWidth(10);
        PasswordField salasana=new PasswordField();
        salasana.setPromptText("Salasana");
        Button kirjautumisButton=new Button("Kirjaudu sisään");
        //lisäys
        kirjautumisLaatikko.getChildren().addAll(nimi,login,kayttajatunnus,salasana,kirjautumisButton);
        rootPaneeli.setCenter(kirjautumisLaatikko);
        //palautetaan luotu scene
        return ruutu;
    }

    public static Scene luoTervetuloIkkuna(){
        GridPane rootPaneeli=new GridPane();
        rootPaneeli.setAlignment(Pos.CENTER);
        rootPaneeli.setHgap(50);
        rootPaneeli.setVgap(50);
        rootPaneeli.setPadding(new Insets(10,10,10,10));
        //buttons
        Text terveTeksti=new Text("Tervetuloa");
        Button mokitBt=new Button("Mökit");
        Button laskutBt=new Button("Laskut");
        Button varauksetBT=new Button("Varaukset");
        Button uusiVarausBt=new Button("Uusi varaus");
        Button asiakkaatBt=new Button("Asiakkaat");
        Button adminOikeusBt=new Button("Admin-toiminnot");
        Button kirjauduUlosBt=new Button("Kirjaudu ulos");

        //lisäys
        rootPaneeli.add(terveTeksti,1,0);
        rootPaneeli.add(adminOikeusBt,2,0);
        rootPaneeli.add(mokitBt,0,1);
        rootPaneeli.add(varauksetBT,1,1);
        rootPaneeli.add(asiakkaatBt,2,1);
        rootPaneeli.add(laskutBt,0,2);
        rootPaneeli.add(uusiVarausBt,1,2);
        rootPaneeli.add(kirjauduUlosBt,2,2);

        //palautetaan luotu scene
        return new Scene(rootPaneeli,400,400);
    }

    public static Scene luoMokitIkkuna(){
        BorderPane rootPaneeli=new BorderPane();
        rootPaneeli.setPadding(new Insets(5,5,5,5));
        //tyhjä lista
        ObservableList<String>testi= FXCollections.observableArrayList("Testi","yippee","not the real list");
        ListView<String> mokkiLista=new ListView<>(testi);
        mokkiLista.setMaxSize(350,250);
        rootPaneeli.setCenter(mokkiLista);
        //buttonit
        Button addUusiMokki=new Button("Lisää uusi mökki");
        Button muokkaaMokkia=new Button("Muokkaa mökkiä");
        Button suljeBt=new Button("Sulje");
        HBox nappulaBoksi=new HBox(addUusiMokki,muokkaaMokkia,suljeBt);
        nappulaBoksi.setSpacing(20);
        rootPaneeli.setBottom(nappulaBoksi);
        nappulaBoksi.setAlignment(Pos.CENTER);
        rootPaneeli.setPadding(new Insets(5,5,5,5));

        return new Scene(rootPaneeli,500,500);
    }

    public static Scene luoUusiMokkiIkkuna(){
        GridPane rootPaneeli=new GridPane();
        rootPaneeli.setAlignment(Pos.CENTER);
        rootPaneeli.setVgap(10);
        rootPaneeli.setHgap(10);

        rootPaneeli.setPadding(new Insets(10));

        Label osoitelb=new Label("Osoite");
        Label hintaLb=new Label("Hinta per yö");
        Label neliotlb=new Label("Neliöt");
        Label vuodepaikatlb=new Label("Vuodepaikat");
        Label ominaisuudetlb=new Label("Ominaisuudet");
        TextField osoiteTxt=new TextField();
        TextField hintaTxt=new TextField();
        TextField nelioTxt=new TextField();
        TextField vuodeTxt=new TextField();
        HBox row1=new HBox(osoitelb,osoiteTxt);
        row1.setSpacing(45);
        HBox row2=new HBox(hintaLb,hintaTxt);
        row2.setSpacing(15);
        HBox row3=new HBox(neliotlb,nelioTxt);
        row3.setSpacing(48);
        HBox row4=new HBox(vuodepaikatlb,vuodeTxt);
        row4.setSpacing(15);
        VBox sarake=new VBox(row1,row2,row3,row4);
        sarake.setSpacing(20);


        sarake.setSpacing(15);
        sarake.setAlignment(Pos.CENTER);

        CheckBox rantasauna=new CheckBox("Rantasauna");
        CheckBox ranta=new CheckBox("Oma ranta");
        CheckBox wifi=new CheckBox("Wi-Fi");
        CheckBox sisawc=new CheckBox("Sisä-WC");
        CheckBox palju=new CheckBox("Palju");
        Button tallennaBt=new Button("Tallenna");
        Button suljeBt=new Button("Sulje");
        VBox buttons=new VBox(tallennaBt,suljeBt);
        buttons.setSpacing(15);
        buttons.setAlignment(Pos.TOP_CENTER);
        rootPaneeli.add(buttons,2,2);
        VBox checkBox=new VBox(ominaisuudetlb,rantasauna,ranta,wifi,sisawc,palju);
        checkBox.setSpacing(15);
        checkBox.setAlignment(Pos.CENTER_LEFT);
        VBox keskikohta=new VBox(sarake,checkBox);
        keskikohta.setSpacing(20);
        rootPaneeli.add(keskikohta,1,0);


        return new Scene(rootPaneeli,500,500);
    }

    public static Scene luoMuokkausMokkiIkkuna(){
        GridPane rootPaneeli=new GridPane();
        rootPaneeli.setAlignment(Pos.CENTER);
        rootPaneeli.setVgap(10);
        rootPaneeli.setHgap(10);
        rootPaneeli.setPadding(new Insets(10));

        Label osoitelb=new Label("Osoite");
        Label hintaLb=new Label("Hinta per yö");
        Label neliotlb=new Label("Neliöt");
        Label vuodepaikatlb=new Label("Vuodepaikat");
        Label ominaisuudetlb=new Label("Ominaisuudet");
        TextField osoiteTxt=new TextField();
        TextField hintaTxt=new TextField();
        TextField nelioTxt=new TextField();
        TextField vuodeTxt=new TextField();
        HBox row1=new HBox(osoitelb,osoiteTxt);
        row1.setSpacing(45);
        HBox row2=new HBox(hintaLb,hintaTxt);
        row2.setSpacing(15);
        HBox row3=new HBox(neliotlb,nelioTxt);
        row3.setSpacing(48);
        HBox row4=new HBox(vuodepaikatlb,vuodeTxt);
        row4.setSpacing(15);
        VBox sarake=new VBox(row1,row2,row3,row4);
        sarake.setSpacing(20);


        sarake.setSpacing(15);
        sarake.setAlignment(Pos.CENTER);

        CheckBox rantasauna=new CheckBox("Rantasauna");
        CheckBox ranta=new CheckBox("Oma ranta");
        CheckBox wifi=new CheckBox("Wi-Fi");
        CheckBox sisawc=new CheckBox("Sisä-WC");
        CheckBox palju=new CheckBox("Palju");
        Button tallennaBt=new Button("Tallenna");
        Button poistaBt=new Button("Poista mökki");
        Button suljeBt=new Button("Sulje");
        VBox buttons=new VBox(tallennaBt,poistaBt,suljeBt);
        buttons.setSpacing(15);
        buttons.setAlignment(Pos.TOP_CENTER);
        rootPaneeli.add(buttons,2,2);
        VBox checkBox=new VBox(ominaisuudetlb,rantasauna,ranta,wifi,sisawc,palju);
        checkBox.setSpacing(15);
        checkBox.setAlignment(Pos.CENTER_LEFT);
        VBox keskikohta=new VBox(sarake,checkBox);
        keskikohta.setSpacing(20);
        rootPaneeli.add(keskikohta,1,0);


        return new Scene(rootPaneeli,500,500);
    }

    public static Scene luoVarauksetIkkuna(){
        BorderPane rootPaneeli=new BorderPane();
        rootPaneeli.setPadding(new Insets(5,5,5,5));
        //tyhjä lista
        ObservableList<String>testi= FXCollections.observableArrayList("Testi","yippee","not the real list");
        ListView<String> varauslista =new ListView<>(testi);
        varauslista.setMaxSize(350,250);
        rootPaneeli.setCenter(varauslista);
        //buttonit
        Button addLasku=new Button("Tee lasku");
        Button muokkaaVarausta =new Button("Muokkaa varausta");
        Button suljeBt=new Button("Sulje");
        HBox nappulaBoksi=new HBox(addLasku, muokkaaVarausta,suljeBt);
        nappulaBoksi.setSpacing(20);
        rootPaneeli.setBottom(nappulaBoksi);
        nappulaBoksi.setAlignment(Pos.CENTER);
        rootPaneeli.setPadding(new Insets(5,5,5,5));

        return new Scene(rootPaneeli,500,500);
    }

    public static Scene luoAsiakkaatIkkuna(){
        BorderPane rootPaneeli=new BorderPane();
        rootPaneeli.setPadding(new Insets(5,5,5,5));
        //tyhjä lista
        ObservableList<String>testi= FXCollections.observableArrayList("Testi","yippee","not the real list");
        ListView<String> asiakaslista =new ListView<>(testi);
        asiakaslista.setMaxSize(350,250);
        rootPaneeli.setCenter(asiakaslista);
        //buttonit
        Button muokkaaAsiakasta =new Button("Muokkaa asiakasta");
        Button suljeBt=new Button("Sulje");
        HBox nappulaBoksi=new HBox(muokkaaAsiakasta,suljeBt);
        nappulaBoksi.setSpacing(20);
        rootPaneeli.setBottom(nappulaBoksi);
        nappulaBoksi.setAlignment(Pos.CENTER);
        rootPaneeli.setPadding(new Insets(5,5,5,5));

        return new Scene(rootPaneeli,500,500);
    }

    public static Scene luolaskuIkkuna(){
        BorderPane rootPaneeli=new BorderPane();
        rootPaneeli.setPadding(new Insets(5,5,5,5));
        //tyhjä lista
        ObservableList<String>testi= FXCollections.observableArrayList("Testi","yippee","not the real list");
        ListView<String> laskulista =new ListView<>(testi);
        laskulista.setMaxSize(350,250);
        rootPaneeli.setCenter(laskulista);
        //buttonit
        Button laskunTila =new Button("Päivitä laskun tila");
        Button suljeBt=new Button("Sulje");
        HBox nappulaBoksi=new HBox(laskunTila,suljeBt);
        nappulaBoksi.setSpacing(20);
        rootPaneeli.setBottom(nappulaBoksi);
        nappulaBoksi.setAlignment(Pos.CENTER);
        rootPaneeli.setPadding(new Insets(5,5,5,5));

        return new Scene(rootPaneeli,500,500);
    }

    public static Scene luoPaivitaLaskuIkkuna(){
        GridPane rootPaneeli=new GridPane();

        rootPaneeli.setAlignment(Pos.CENTER);
        rootPaneeli.setVgap(20);
        rootPaneeli.setHgap(20);
        rootPaneeli.setPadding(new Insets(20));
        CheckBox laskuLahetetty =new CheckBox("Lasku lähetetty");
        CheckBox maskuVastaanotto=new CheckBox("Maksu vastaanotettu");
        Button tallennaBt=new Button("Tallenna");
        Button suljeBt=new Button("Sulje");
        VBox buttons=new VBox(tallennaBt,suljeBt);
        buttons.setSpacing(15);
        buttons.setAlignment(Pos.TOP_CENTER);
        rootPaneeli.add(buttons,2,2);
        VBox checkBox=new VBox(laskuLahetetty,maskuVastaanotto);
        checkBox.setSpacing(15);
        checkBox.setAlignment(Pos.CENTER_LEFT);
        VBox keskikohta=new VBox(checkBox);
        keskikohta.setSpacing(20);
        rootPaneeli.add(keskikohta,1,0);
        return new Scene(rootPaneeli,500,500);
    }

    public static Scene luoMuokkaaVaraustaIkkuna(){
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

        Button tallennaBt=new Button("Tallenna");
        Button poistaBt=new Button("Poista varaus");
        Button suljeBt=new Button("Sulje");
        VBox buttons=new VBox(tallennaBt,poistaBt,suljeBt);
        buttons.setSpacing(20);
        rootPaneeli.add(buttons,2,2);

        VBox keskikohta=new VBox(sarake);
        keskikohta.setSpacing(20);
        rootPaneeli.add(keskikohta,1,0);


        return new Scene(rootPaneeli,500,500);
    }

    public static Scene luoMuokkaaAsiakastaIkkuna(){
        GridPane rootPaneeli=new GridPane();
        rootPaneeli.setAlignment(Pos.CENTER);
        rootPaneeli.setVgap(20);
        rootPaneeli.setHgap(20);

        rootPaneeli.setPadding(new Insets(10));

        Label nimilb =new Label("Nimi");
        Label spostiLb =new Label("Sähköpostiosoite");
        Label puhnrolb =new Label("Puhelinnumero");
        Label katuosoitelb =new Label("Katuosoite");

        TextField mokkiTxt=new TextField();
        TextField asiakasTxt =new TextField();
        TextField alkuTxt =new TextField();
        TextField loppuTxt =new TextField();
        HBox row1=new HBox(nimilb,mokkiTxt);
        row1.setSpacing(69);
        HBox row2=new HBox(spostiLb, asiakasTxt);
        row2.setSpacing(5);
        HBox row3=new HBox(puhnrolb, alkuTxt);
        row3.setSpacing(13);
        HBox row4=new HBox(katuosoitelb, loppuTxt);
        row4.setSpacing(37);
        VBox sarake=new VBox(row1,row2,row3,row4);
        sarake.setSpacing(20);


        sarake.setSpacing(15);
        sarake.setAlignment(Pos.CENTER);

        Button tallennaBt=new Button("Tallenna");
        Button poistaBt=new Button("Poista asiakas");
        Button suljeBt=new Button("Sulje");
        VBox buttons=new VBox(tallennaBt,poistaBt,suljeBt);
        buttons.setSpacing(20);
        rootPaneeli.add(buttons,2,2);

        VBox keskikohta=new VBox(sarake);
        keskikohta.setSpacing(20);
        rootPaneeli.add(keskikohta,1,0);


        return new Scene(rootPaneeli,500,500);
    }

    public static Scene luoAdminToiminnotIkkuna(){
        GridPane rootPaneeli=new GridPane();
        rootPaneeli.setAlignment(Pos.CENTER);
        rootPaneeli.setHgap(50);
        rootPaneeli.setVgap(100);
        rootPaneeli.setPadding(new Insets(10,10,10,10));

        Button raporttibt=new Button("Raportit");
        Button kayttajanhallintabt=new Button("Käyttäjänhallinta");
        Button suljebt=new Button("Sulje");



        rootPaneeli.add(raporttibt,2,1);
        rootPaneeli.add(kayttajanhallintabt,0,1);
        rootPaneeli.add(suljebt,2,2);
        return new Scene(rootPaneeli,400,400);
    }

    public static Scene luoKattajanhallintaIkkuna(){
        BorderPane rootPaneeli=new BorderPane();

        //tyhjä lista
        ObservableList<String>testi= FXCollections.observableArrayList("Testi","yippee","not the real list");
        ListView<String> kayttajalista =new ListView<>(testi);

        rootPaneeli.setCenter(kayttajalista);
        //buttonit
        Button muokkaaAsiakasta =new Button("Muokkaa käyttäjää");
        Button lisaaAsiakas =new Button("Lisää uusi käyttäjä");
        Button suljeBt=new Button("Sulje");
        HBox nappulaBoksi=new HBox(lisaaAsiakas,muokkaaAsiakasta,suljeBt);
        nappulaBoksi.setSpacing(50);
        rootPaneeli.setBottom(nappulaBoksi);
        nappulaBoksi.setAlignment(Pos.CENTER);
        rootPaneeli.setPadding(new Insets(15,15,15,15));

        return new Scene(rootPaneeli,500,500);
    }

    public static Scene luoUusiKayttajaIkkuna(){
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
        TextField vuodeTxt=new TextField();
        HBox row1=new HBox(idlb,idTxt);
        row1.setSpacing(70);
        HBox row2=new HBox(nimiLb, nimiTxt);
        row2.setSpacing(57);
        HBox row3=new HBox(kayttajatunnuslb, kayttajaTxt);
        row3.setSpacing(5);
        HBox row4=new HBox(salasanalb,vuodeTxt);
        row4.setSpacing(38);
        VBox sarake=new VBox(row1,row2,row3,row4);
        sarake.setSpacing(15);
        sarake.setAlignment(Pos.CENTER);

        CheckBox peruskayttaja=new CheckBox("Peruskäyttäjä");
        CheckBox admin=new CheckBox("Admin-käyttäjä");
        CheckBox anniskelu=new CheckBox("Anniskelupassi");
        CheckBox hygienia=new CheckBox("Hygieniapassi");

        Button tallennaBt=new Button("Tallenna uusi käyttäjä");
        Button suljeBt=new Button("Sulje");
        VBox buttons=new VBox(tallennaBt,suljeBt);
        buttons.setSpacing(15);
        buttons.setAlignment(Pos.TOP_CENTER);
        rootPaneeli.add(buttons,2,2);
        VBox checkBox=new VBox(kayttooikeuslb,peruskayttaja,admin,passitlb,anniskelu,hygienia);
        checkBox.setSpacing(15);
        checkBox.setAlignment(Pos.CENTER_LEFT);
        VBox keskikohta=new VBox(sarake,checkBox);
        keskikohta.setSpacing(20);
        rootPaneeli.add(keskikohta,1,0);


        return new Scene(rootPaneeli,500,500);
    }

    public static Scene luoMuokkaaKayttajaIkkuna(){
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
        TextField vuodeTxt=new TextField();
        HBox row1=new HBox(idlb,idTxt);
        row1.setSpacing(70);
        HBox row2=new HBox(nimiLb, nimiTxt);
        row2.setSpacing(57);
        HBox row3=new HBox(kayttajatunnuslb, kayttajaTxt);
        row3.setSpacing(5);
        HBox row4=new HBox(salasanalb,vuodeTxt);
        row4.setSpacing(38);
        VBox sarake=new VBox(row1,row2,row3,row4);
        sarake.setSpacing(15);
        sarake.setAlignment(Pos.CENTER);

        CheckBox peruskayttaja=new CheckBox("Peruskäyttäjä");
        CheckBox admin=new CheckBox("Admin-käyttäjä");
        CheckBox anniskelu=new CheckBox("Anniskelupassi");
        CheckBox hygienia=new CheckBox("Hygieniapassi");

        Button tallennaBt=new Button("Tallenna uusi käyttäjä");
        Button poistaBt=new Button("Poista käyttäjä");
        Button suljeBt=new Button("Sulje");
        VBox buttons=new VBox(tallennaBt,poistaBt,suljeBt);
        buttons.setSpacing(15);
        buttons.setAlignment(Pos.TOP_CENTER);
        rootPaneeli.add(buttons,2,2);
        VBox checkBox=new VBox(kayttooikeuslb,peruskayttaja,admin,passitlb,anniskelu,hygienia);
        checkBox.setSpacing(15);
        checkBox.setAlignment(Pos.CENTER_LEFT);
        VBox keskikohta=new VBox(sarake,checkBox);
        keskikohta.setSpacing(20);
        rootPaneeli.add(keskikohta,1,0);


        return new Scene(rootPaneeli,500,500);
    }

    public static Scene luoRaportitIkkuna(){
        BorderPane rootPaneeli=new BorderPane();

        HBox kirjautumisLaatikko=new HBox();
        kirjautumisLaatikko.setSpacing(10);
        kirjautumisLaatikko.setAlignment(Pos.TOP_LEFT);
        kirjautumisLaatikko.setPadding(new Insets(25,5,5,5));
        Button testi=new Button(" testi");
        Button testi2=new Button("testi");
        Button testi3=new Button("testi");
        Button testi4=new Button("testi");
        kirjautumisLaatikko.getChildren().addAll(testi2,testi4,testi3,testi);

        //dates
        DatePicker alkupaiva=new DatePicker();
        alkupaiva.setPrefWidth(40);
        alkupaiva.setPrefHeight(20);
        DatePicker loppupaiva=new DatePicker();
        loppupaiva.setPrefWidth(40);
        loppupaiva.setPrefHeight(20);
        HBox datebox=new HBox(alkupaiva,loppupaiva);
        datebox.setAlignment(Pos.TOP_RIGHT);

        HBox ylaosa =new HBox(kirjautumisLaatikko,datebox);
        ylaosa.setAlignment(Pos.TOP_CENTER);
        ylaosa.setSpacing(10);
        ylaosa.setPadding(new Insets(10,10,10,10));
        rootPaneeli.setTop(ylaosa);

        Button viePDF=new Button("Vie PDF-tiedostoon");
        Button sulje=new Button("Sulje");
        HBox alaosa=new HBox(viePDF,sulje);
        alaosa.setPadding(new Insets(10,10,10,10));
        alaosa.setSpacing(10);
        alaosa.setAlignment(Pos.BASELINE_RIGHT);
        rootPaneeli.setBottom(alaosa);

        ObservableList<String> tyja = FXCollections.observableArrayList("Testi","yippee","not the real list");
        ListView<String> lista=new ListView<>(tyja);
        lista.setMaxSize(350,250);
        lista.setPadding(new Insets(10,10,10,10));
        rootPaneeli.setCenter(lista);

        //palautetaan luotu scene
        return new Scene(rootPaneeli,500,500);
    }
}
