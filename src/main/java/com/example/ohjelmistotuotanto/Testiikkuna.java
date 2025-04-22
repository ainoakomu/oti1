package com.example.ohjelmistotuotanto;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Testiikkuna extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        //saadaan esille ikkuna
        primaryStage.setScene(luoTervetuloIkkuna());
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
}
