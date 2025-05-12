package com.example.ohjelmistotuotanto;

import javafx.scene.layout.*;
import javafx.scene.image.Image;

// Luokka missä luodaan taustakuva.
// Kutsutaan alku ikkunassa!
public class Taustakuvat {
    public class TaustakuvaAsettaminen {

        public static Background luoKolmasTausta() {
            Image taustakuva = new Image(TaustakuvaAsettaminen.class.getResource("/kuva2.png").toExternalForm());

            BackgroundSize taustaKoko = new BackgroundSize(
                    BackgroundSize.AUTO, //Ei skaalausta leveydelle
                    BackgroundSize.AUTO, //Ei skaalausta korkeudelle
                    false,               //Ei skaalausta vaakasuunnassa
                    false,               //Ei skaalausta pystysuunnassa
                    true,                //Säilyttää alkuperäiset mittasuhteet
                    true                 //Säilyttää alkuperäiset mittasuhteet
            );
            BackgroundImage tausta = new BackgroundImage(
                    taustakuva,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.DEFAULT,
                    taustaKoko
            );
            return new Background(tausta);
        }

        public static Background luoToinenTausta() {
            Image taustakuva = new Image(TaustakuvaAsettaminen.class.getResource("/kuva3.png").toExternalForm());

            BackgroundSize taustaKoko = new BackgroundSize(
                    BackgroundSize.AUTO, //Ei skaalausta leveydelle
                    BackgroundSize.AUTO, //Ei skaalausta korkeudelle
                    false,               //Ei skaalausta vaakasuunnassa
                    false,               //Ei skaalausta pystysuunnassa
                    true,                //Säilyttää alkuperäiset mittasuhteet
                    true                 //Säilyttää alkuperäiset mittasuhteet
            );
            BackgroundImage tausta = new BackgroundImage(
                    taustakuva,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.DEFAULT,
                    taustaKoko
            );
            return new Background(tausta);
        }

        public static Background luoTausta() {
            Image taustakuva = new Image(TaustakuvaAsettaminen.class.getResource("/taustakuva1.png").toExternalForm());

            BackgroundSize taustaKoko = new BackgroundSize(
                    BackgroundSize.AUTO, //Ei skaalausta leveydelle
                    BackgroundSize.AUTO, //Ei skaalausta korkeudelle
                    false,               //Ei skaalausta vaakasuunnassa
                    false,               //Ei skaalausta pystysuunnassa
                    true,                //Säilyttää alkuperäiset mittasuhteet
                    true                 //Säilyttää alkuperäiset mittasuhteet
            );
            BackgroundImage tausta = new BackgroundImage(
                    taustakuva,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.DEFAULT,
                    taustaKoko
            );
            return new Background(tausta);
        }
    }
}