package com.example.ohjelmistotuotanto;

import javafx.scene.layout.*;
import javafx.scene.image.Image;

/**
 * luodaan ikkunoiden ulkoasulle taustakuvia
 * /kuvalähteet:
 * <a href="https://www.flickr.com/photos/charlie_cravero/101604127/">...</a>
 * //<a href="https://www.flickr.com/photos/canoarias/15046278708">...</a>
 * //<a href="https://www.pexels.com/fi-fi/kuva/puu-vesi-kesa-rentoutuminen-8844584/">...</a>
 */
public class Taustakuvat {
    /**
     * luodaan ikkunoiden taustalle taustakuva
     */
    public class TaustakuvaAsettaminen {

        /**
         * luodaan taustakuva kuvalla kuva2.png
         * @return haluttu tausta
         */
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

        /**
         * luodaan taustakuva kuvalla kuva3.png
         * @return haluttu tausta
         */
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

        /**
         * luodaan taustakuva kuvalla taustakuva1.png
         * @return luotu tausta
         */
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