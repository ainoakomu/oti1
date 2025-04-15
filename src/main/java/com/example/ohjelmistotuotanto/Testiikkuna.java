package com.example.ohjelmistotuotanto;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Testiikkuna extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        BorderPane rootPaneeli=new BorderPane();
        Scene ruutu=new Scene(rootPaneeli,400,400);

        //hbox for logging in
        VBox kirjautumisLaatikko=new VBox();
        kirjautumisLaatikko.setSpacing(10);
        kirjautumisLaatikko.setAlignment(Pos.CENTER);
        Text nimi=new Text("VillaBOOK");
        Text login=new Text("Kirjaudu sisään");
        TextField kayttajatunnus=new TextField("Käyttäjätunnus");
        kayttajatunnus.setPrefWidth(50);
        kayttajatunnus.setPrefHeight(20);
        PasswordField salasana=new PasswordField();
        salasana.setPrefWidth(50);
        salasana.setPrefHeight(20);
        Button kirjautumisButton=new Button("Kirjaudu sisään");
        kirjautumisLaatikko.getChildren().addAll(nimi,login,kayttajatunnus,salasana,kirjautumisButton);
        rootPaneeli.setCenter(kirjautumisLaatikko);





        primaryStage.setScene(ruutu);
        primaryStage.show();


    }
}
