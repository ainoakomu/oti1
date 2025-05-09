package com.example.ohjelmistotuotanto;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import static com.example.ohjelmistotuotanto.LaskutData.haeLaskut;
import static com.example.ohjelmistotuotanto.MokkiData.haeMokit;

public class LaskuLuokka {

    public Stage luoLaskuIkkuna(){
        Stage laskutStage = new Stage();

        //sql yhteys
        Yhteysluokka olio=new Yhteysluokka();
        BorderPane rootPaneeli=new BorderPane();
        rootPaneeli.setPadding(new Insets(5,5,5,5));
        //observable lista, joka lisätään list view
        //käytetään datanhaku metodia
        ObservableList<String> mokkiData = FXCollections.observableArrayList(haeLaskut(olio));
        ListView<String> laskulista= new ListView<>(mokkiData);
        laskulista.setMaxSize(350,250);
        rootPaneeli.setCenter(laskulista);

        //buttonit ja action eventit
        Button laskunTila =new Button("Päivitä laskun tila");
        Button suljeBt=new Button("Sulje");

        laskunTila.setOnAction(e->{
            //metodi, jolla tarkistetaan että lasku on valittuna listalta
            luoPaivitaLaskuIkkuna().show();
        });

        suljeBt.setOnAction(e->{
            laskutStage.close();
        });

        HBox nappulaBoksi=new HBox(laskunTila,suljeBt);
        nappulaBoksi.setSpacing(20);
        rootPaneeli.setBottom(nappulaBoksi);
        nappulaBoksi.setAlignment(Pos.CENTER);
        rootPaneeli.setPadding(new Insets(5,5,5,5));


        Scene laskutScene = new Scene(rootPaneeli,500,500);
        laskutStage.setScene(laskutScene);
        laskutStage.setTitle("Laskut");
        return laskutStage;

    }

    public Stage luoPaivitaLaskuIkkuna(){
        Stage paivitysStage = new Stage();
        GridPane rootPaneeli=new GridPane();

        rootPaneeli.setAlignment(Pos.CENTER);
        rootPaneeli.setVgap(20);
        rootPaneeli.setHgap(20);
        rootPaneeli.setPadding(new Insets(20));
        CheckBox laskuLahetetty =new CheckBox("Lasku lähetetty");
        CheckBox maskuVastaanotto=new CheckBox("Maksu vastaanotettu");

        //buttonit ja action eventit
        Button tallennaBt=new Button("Tallenna");
        Button suljeBt=new Button("Sulje");

        tallennaBt.setOnAction(e->{
            //metodi, jolla tallennetaan annetut muutokset
        });

        suljeBt.setOnAction(e->{
            // varmista ifillä, suljetaanko
            paivitysStage.close();
        });

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


        Scene paivitysScene = new Scene(rootPaneeli,500,500);
        paivitysStage.setScene(paivitysScene);
        paivitysStage.setTitle("Päivitä laskun tiedot");
        return paivitysStage;
    }

}
