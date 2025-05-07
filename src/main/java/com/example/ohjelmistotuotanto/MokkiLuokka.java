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



import static com.example.ohjelmistotuotanto.MokkiData.haeMokit;

public class MokkiLuokka{


    public Stage luoMokitIkkuna() {
        //sql yhteys
        Yhteysluokka olio=new Yhteysluokka();

        BorderPane rootPaneeli = new BorderPane();
        rootPaneeli.setPadding(new Insets(5, 5, 5, 5));
        //observable lista, joka lisätään list view
        //käytetään datanhaku metodia
        ObservableList<String> mokkiData = FXCollections.observableArrayList(haeMokit(olio));
        ListView<String> mokkiLista = new ListView<>(mokkiData);

        mokkiLista.setMaxSize(450, 250);
        rootPaneeli.setCenter(mokkiLista);

        //buttonit ja action eventit
        Button addUusiMokki = new Button("Lisää uusi mökki");
        Button muokkaaMokkia = new Button("Muokkaa mökkiä");
        Button suljeBt = new Button("Sulje");

        addUusiMokki.setOnAction(e->{

        });

        muokkaaMokkia.setOnAction(e->{

        });

        suljeBt.setOnAction(e->{

        });


        HBox nappulaBoksi = new HBox(addUusiMokki, muokkaaMokkia, suljeBt);
        nappulaBoksi.setSpacing(20);
        rootPaneeli.setBottom(nappulaBoksi);
        nappulaBoksi.setAlignment(Pos.CENTER);
        rootPaneeli.setPadding(new Insets(5, 5, 5, 5));

        Stage mokkiStage = new Stage();
        Scene mokkiScene = new Scene(rootPaneeli, 500, 500);
        mokkiStage.setScene(mokkiScene);
        mokkiStage.setTitle("Mökit");
        return mokkiStage;
    }

    public static Scene luoUusiMokkiIkkuna() {
        GridPane rootPaneeli = new GridPane();
        rootPaneeli.setAlignment(Pos.CENTER);
        rootPaneeli.setVgap(10);
        rootPaneeli.setHgap(10);

        rootPaneeli.setPadding(new Insets(10));

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

        CheckBox rantasauna = new CheckBox("Rantasauna");
        CheckBox ranta = new CheckBox("Oma ranta");
        CheckBox wifi = new CheckBox("Wi-Fi");
        CheckBox sisawc = new CheckBox("Sisä-WC");
        CheckBox palju = new CheckBox("Palju");
        Button tallennaBt = new Button("Tallenna");
        Button suljeBt = new Button("Sulje");
        VBox buttons = new VBox(tallennaBt, suljeBt);
        buttons.setSpacing(15);
        buttons.setAlignment(Pos.TOP_CENTER);
        rootPaneeli.add(buttons, 2, 2);
        VBox checkBox = new VBox(ominaisuudetlb, rantasauna, ranta, wifi, sisawc, palju);
        checkBox.setSpacing(15);
        checkBox.setAlignment(Pos.CENTER_LEFT);
        VBox keskikohta = new VBox(sarake, checkBox);
        keskikohta.setSpacing(20);
        rootPaneeli.add(keskikohta, 1, 0);


        return new Scene(rootPaneeli, 500, 500);
    }

    public static Scene luoMuokkausMokkiIkkuna() {
        GridPane rootPaneeli = new GridPane();
        rootPaneeli.setAlignment(Pos.CENTER);
        rootPaneeli.setVgap(10);
        rootPaneeli.setHgap(10);
        rootPaneeli.setPadding(new Insets(10));

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

        CheckBox rantasauna = new CheckBox("Rantasauna");
        CheckBox ranta = new CheckBox("Oma ranta");
        CheckBox wifi = new CheckBox("Wi-Fi");
        CheckBox sisawc = new CheckBox("Sisä-WC");
        CheckBox palju = new CheckBox("Palju");
        Button tallennaBt = new Button("Tallenna");
        Button poistaBt = new Button("Poista mökki");
        Button suljeBt = new Button("Sulje");
        VBox buttons = new VBox(tallennaBt, poistaBt, suljeBt);
        buttons.setSpacing(15);
        buttons.setAlignment(Pos.TOP_CENTER);
        rootPaneeli.add(buttons, 2, 2);
        VBox checkBox = new VBox(ominaisuudetlb, rantasauna, ranta, wifi, sisawc, palju);
        checkBox.setSpacing(15);
        checkBox.setAlignment(Pos.CENTER_LEFT);
        VBox keskikohta = new VBox(sarake, checkBox);
        keskikohta.setSpacing(20);
        rootPaneeli.add(keskikohta, 1, 0);


        return new Scene(rootPaneeli, 500, 500);
    }


}
