package com.example.ohjelmistotuotanto;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AsiakasLuokka {

    public Stage luoAsiakkaatIkkuna(){
        BorderPane rootPaneeli=new BorderPane();
        rootPaneeli.setPadding(new Insets(5,5,5,5));
        //tyhjä lista
        ObservableList<String> testi= FXCollections.observableArrayList("Testi","yippee","not the real list");
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

        Stage asiakasStage = new Stage();
        Scene asiakasScene = new Scene(rootPaneeli,500,500);
        asiakasStage.setScene(asiakasScene);
        asiakasStage.setTitle("Asiakkaat");
        return asiakasStage;
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

}
