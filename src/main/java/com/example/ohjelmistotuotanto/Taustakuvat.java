package com.example.ohjelmistotuotanto;

import javafx.scene.layout.*;
import javafx.scene.image.Image;

// Luokka missä luodaan taustakuvat
// Kutsutaan kuvien metodeja ikkunoissa muualla koodissa
//kuvalähteet: https://www.flickr.com/photos/charlie_cravero/101604127/
//https://www.flickr.com/photos/canoarias/15046278708
//https://www.pexels.com/fi-fi/kuva/puu-vesi-kesa-rentoutuminen-8844584/
public class Taustakuvat {
    public class TaustakuvaAsettaminen {


        public static Background luoKolmasTausta() {
            Image taustakuva = new Image(TaustakuvaAsettaminen.class.getResource("/Kuva2.png").toExternalForm());


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
            Image taustakuva = new Image(TaustakuvaAsettaminen.class.getResource("/Kuva3.png").toExternalForm());

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