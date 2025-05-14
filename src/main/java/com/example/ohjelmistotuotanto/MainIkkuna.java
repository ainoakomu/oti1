package com.example.ohjelmistotuotanto;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Ohjelman paaikkuna, jolla ohjelmaa suoritetaan.
 * Kaynnistys tapahtuu tasta luokasta johon muut ovat linkitetyina omissa luokissaan
 */
public class MainIkkuna extends Application {
    /**
     * Avaa ohjelman ja luo kirjautumisikkunan
     *
     * @param stage annetaan ilmenyta joka avautuu
     * @throws Exception kasitellaan virhetilanne
     */
    @Override
    public void start(Stage stage) throws Exception {
        AlkuIkkunat alkuIkkunat = new AlkuIkkunat();
        stage.setScene(alkuIkkunat.ohjelmaAukeaa(stage));
        stage.setTitle("VillaBOOK");
        stage.show();
    }
}
