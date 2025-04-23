package com.example.ohjelmistotuotanto;

import javafx.application.Application;

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
import javafx.stage.Stage;

public class IkkunaMetodeja extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        //saadaan esille ikkuna
        primaryStage.setScene(luoUusiMokkiIkkuna());
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
        GridPane rootPaneeli=new GridPane();
        //tyhjä lista
        ObservableList<String>testi= FXCollections.observableArrayList("Testi","yippee","not the real list");
        ListView<String> mokkiLista=new ListView<>(testi);
        mokkiLista.setPrefWidth(250);
        mokkiLista.setPrefHeight(150);
        //buttonit
        Button addUusiMokki=new Button("Lisää uusi mökki");
        Button muokkaaMokkia=new Button("Muokkaa mökkiä");
        Button suljeBt=new Button("Sulje");
        //asettelu
        rootPaneeli.setAlignment(Pos.CENTER);
        rootPaneeli.setVgap(10);
        rootPaneeli.setHgap(10);
        rootPaneeli.setPadding(new Insets(5,5,5,5));
        //lisäys
        rootPaneeli.add(mokkiLista,1,0);
        rootPaneeli.add(addUusiMokki,0,2);
        rootPaneeli.add(muokkaaMokkia,1,2);
        rootPaneeli.add(suljeBt,2,2);

        return new Scene(rootPaneeli,500,500);
    }

    public static Scene luoUusiMokkiIkkuna(){
        BorderPane rootPaneeli=new BorderPane();

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
        row1.setSpacing(5);
        HBox row2=new HBox(hintaLb,hintaTxt);
        row2.setSpacing(5);
        HBox row3=new HBox(neliotlb,nelioTxt);
        row3.setSpacing(5);
        HBox row4=new HBox(vuodepaikatlb,vuodeTxt);
        row4.setSpacing(5);
        VBox sarake=new VBox(row1,row2,row3,row4);
        sarake.setSpacing(5);
        sarake.setAlignment(Pos.CENTER);
        rootPaneeli.setTop(sarake);
        rootPaneeli.setPadding(new Insets(70,10,10,70));
        CheckBox rantasauna=new CheckBox("Rantasauna");
        CheckBox ranta=new CheckBox("Oma ranta");
        CheckBox wifi=new CheckBox("Wi-Fi");
        CheckBox sisawc=new CheckBox("Sisä-WC");
        CheckBox palju=new CheckBox("Palju");
        Button tallennaBt=new Button("Tallenna uusi mökki");
        Button suljeBt=new Button("Sulje");
        VBox buttons=new VBox(tallennaBt,suljeBt);
        buttons.setAlignment(Pos.BOTTOM_RIGHT);
        VBox checkBox=new VBox(ominaisuudetlb,rantasauna,ranta,wifi,sisawc,palju);
        checkBox.setSpacing(10);
        checkBox.setAlignment(Pos.CENTER);
        rootPaneeli.setCenter(checkBox);
        rootPaneeli.setBottom(buttons);


        return new Scene(rootPaneeli,500,500);
    }
}
