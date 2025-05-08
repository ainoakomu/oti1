package com.example.ohjelmistotuotanto;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class AlkuIkkunat {

    public Scene ohjelmaAukeaa(Stage stage){

        //luodaan kuva
        ImageView kuva = new ImageView(new Image(Taustakuvat.TaustakuvaAsettaminen.class.getResource("/taustakuva1.png").toExternalForm()));
        kuva.setFitHeight(400);
        kuva.setFitWidth(400);

        //luodaan pane
        Pane pane=new Pane();
        pane.getChildren().add(kuva);

        //feidataan sisään 2,5sek ajan ja sitten vaihdetaan scene kirjautumisikkunaan
        FadeTransition feidi = new FadeTransition(Duration.millis(2500), kuva);
        feidi.setFromValue(0);
        feidi.setToValue(1);
        feidi.setOnFinished(e->{
            stage.setScene(luoKirjatumisIkkuna(stage));
        });
        feidi.play();

        return new Scene(pane,400,400);
    }

    public Scene luoKirjatumisIkkuna(Stage stage){
        BorderPane rootPaneeli=new BorderPane();
        Scene ruutu=new Scene(rootPaneeli,400,400);

        //kutsutaan taustakuva
        rootPaneeli.setBackground(Taustakuvat.TaustakuvaAsettaminen.luoTausta());

        //otsikko kuva
        Image kuva1 = new Image(getClass().getResource("/otsikko3.png").toExternalForm());
        ImageView otsikko = new ImageView(kuva1);
        otsikko.setPreserveRatio(true);
        otsikko.setFitWidth(250);
        otsikko.setLayoutX(80);
        otsikko.setLayoutY(110);

        // Pane jossa otsikko sijaitsee
        Pane kuvaPane = new Pane();
        kuvaPane.setPrefHeight(150);
        kuvaPane.getChildren().add(otsikko);
        rootPaneeli.setTop(kuvaPane);

        //hbox for logging in
        VBox kirjautumisLaatikko=new VBox();
        kirjautumisLaatikko.setSpacing(10);
        kirjautumisLaatikko.setPadding(new Insets(5,50,5,50));
        kirjautumisLaatikko.setAlignment(Pos.CENTER);
        //kentät
        //Text nimi=new Text("VillaBOOK"); korvattu kuva otsikolla
        Text login=new Text("Kirjaudu sisään");
        TextField kayttajatunnus=new TextField();
        kayttajatunnus.setPromptText("Käyttäjätunnus");
        kayttajatunnus.setPrefWidth(10);
        PasswordField salasana=new PasswordField();
        salasana.setPromptText("Salasana");
        Button kirjautumisButton=new Button("Kirjaudu sisään");

        //kun painaa kirjaudu, tervetuloikkuna aukeaa.
        kirjautumisButton.setOnAction(e->{

            // tee tähän if & if, joilla tarkistetaan käyttäjätunnus ja salasana
            //jos jompi kumpi väärin, avaa herjaikkuna, jossa valitetaan että väärin.
            //jos oikein, suorita ao metodi.

            stage.setScene(luoTervetuloIkkuna(stage));
        });

        //lisäys
        kirjautumisLaatikko.getChildren().addAll(login,kayttajatunnus,salasana,kirjautumisButton);
        rootPaneeli.setCenter(kirjautumisLaatikko);
        //palautetaan luotu scene
        return ruutu;
    }

    public static Scene luoTervetuloIkkuna(Stage stage){

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


        //painikkeiden action eventit.

        //avaa mökki-ikkunan
        mokitBt.setOnAction(e->{
            MokkiLuokka mokkiIkkuna = new MokkiLuokka();
            mokkiIkkuna.luoMokitIkkuna().show();
        });

        //avaa laskuikkunan
        laskutBt.setOnAction(e->{
            LaskuLuokka laskuIkkuna = new LaskuLuokka();
            laskuIkkuna.luoLaskuIkkuna().show();
        });

        //avaa varausten ikkunan
        varauksetBT.setOnAction(e->{
            VarausLuokka varausIkkuna = new VarausLuokka();
            varausIkkuna.luoVarauksetIkkuna().show();
        });

        //avaa ikkunan, josta voi tehdä uuden varauksen
        uusiVarausBt.setOnAction(e->{
            VarausLuokka uusiVarausIkkuna = new VarausLuokka();
            // lisää metodi, joka avaa uuden varausikkunan
        });

        //avaa asiakkaiden ikkunan
        asiakkaatBt.setOnAction(e->{
            AsiakasLuokka asiakasIkkuna = new AsiakasLuokka();
            asiakasIkkuna.luoAsiakkaatIkkuna().show();
        });

        //avaa admintoimintojen ikkunan
        adminOikeusBt.setOnAction(e->{
            //tee tähän if, joka tarkistaa käyttäjän käyttöoikeuden.
            // jos ei admin, herjaa
            // jos on admin, suorita ao

            AdminLuokka adminIkkuna = new AdminLuokka();
            adminIkkuna.luoAdminToiminnotIkkuna().show();
        });

        //kirjaa ulos
        kirjauduUlosBt.setOnAction(e->{
            suljetaankoIkkuna(stage);
        });

        //palautetaan luotu scene
        return new Scene(rootPaneeli,400,400);
    }

    // metodilla avataan ikkuna, jolla varmistetaan uloskirjaus
    public static void suljetaankoIkkuna(Stage stage){
        Stage kysymysIkkuna = new Stage();
        Text kysymys = new Text("Kirjaudutaanko ulos?");

        Button kyllaBt = new Button("Kirjaudu ulos");
        Button eiBt = new Button("Peruuta");

        BorderPane pane = new BorderPane();
        HBox hBox = new HBox(kyllaBt,eiBt);
        pane.setCenter(kysymys);
        pane.setBottom(hBox);

        kyllaBt.setOnAction(e->{
            kysymysIkkuna.close();
            stage.close();
        });
        eiBt.setOnAction(e->{
            kysymysIkkuna.close();
        });

        Scene scene = new Scene(pane, 300,300);
        kysymysIkkuna.setScene(scene);
        kysymysIkkuna.setTitle("Uloskirjautuminen");
        kysymysIkkuna.show();
    }

}
