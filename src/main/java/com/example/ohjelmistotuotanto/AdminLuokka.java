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

public class AdminLuokka {

    public Stage luoAdminToiminnotIkkuna(){
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


        Stage adminStage = new Stage();
        Scene adminScene = new Scene(rootPaneeli,400,400);
        adminStage.setScene(adminScene);
        adminStage.setTitle("Admin");
        return adminStage;
    }

    public static Scene luoKattajanhallintaIkkuna(){
        BorderPane rootPaneeli=new BorderPane();

        //tyhjä lista
        ObservableList<String> testi= FXCollections.observableArrayList("Testi","yippee","not the real list");
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
