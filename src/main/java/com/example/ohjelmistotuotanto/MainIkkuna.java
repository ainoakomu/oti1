package com.example.ohjelmistotuotanto;

import javafx.application.Application;
import javafx.stage.Stage;

public class MainIkkuna extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        AlkuIkkunat alkuIkkunat = new AlkuIkkunat();
        stage.setScene(alkuIkkunat.ohjelmaAukeaa(stage));
        stage.setTitle("VillaBOOK");
        stage.show();
    }
}
