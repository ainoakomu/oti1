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

import java.util.Optional;

import static com.example.ohjelmistotuotanto.LaskutData.haeLaskut;
import static com.example.ohjelmistotuotanto.MokkiData.haeMokit;

/**
 * luodaan laskun luonnissa ja sen tilan paivittamisessa kaytettavia ikkunoita
 */
public class LaskuLuokka {
    private int laskuid;
    private int varausid;
    private boolean laskutettu;
    private boolean maksettu;
    /**
     * luodaan ikkuna jossa nakyvilla laskuja ja mahdollisuus paivittaa niita
     * @return
     */
    public int getLaskuid() {
        return laskuid;
    }

    public void setLaskuid(int laskuid) {
        this.laskuid = laskuid;
    }

    public int getVarausid() {
        return varausid;
    }

    public void setVarausid(int varausid) {
        this.varausid = varausid;
    }

    public boolean isLaskutettu() {
        return laskutettu;
    }

    public void setLaskutettu(boolean laskutettu) {
        this.laskutettu = laskutettu;
    }

    public boolean isMaksettu() {
        return maksettu;
    }

    public void setMaksettu(boolean maksettu) {
        this.maksettu = maksettu;
    }

    public Stage luoLaskuIkkuna(){
        Stage laskutStage = new Stage();

        //sql yhteys
        Yhteysluokka yhteysolio=new Yhteysluokka();
        LaskutData laskutData=new LaskutData();
        BorderPane rootPaneeli=new BorderPane();
        rootPaneeli.setPadding(new Insets(5,5,5,5));
        //observable lista, joka lisätään list view
        //käytetään datanhaku metodia
        ObservableList<String> laskuData = FXCollections.observableArrayList(haeLaskut(yhteysolio));
        ListView<String> laskulista= new ListView<>(laskuData);
        laskulista.setMaxSize(450,300);
        rootPaneeli.setCenter(laskulista);
        rootPaneeli.setBackground(Taustakuvat.TaustakuvaAsettaminen.luoKolmasTausta());

        //buttonit ja action eventit
        Button laskunTila =new Button("Päivitä laskun tila");
        Button suljeBt=new Button("Sulje");

        laskunTila.setOnAction(e->{
            //metodi, jolla tarkistetaan että lasku on valittuna listalta
            String data=laskulista.getSelectionModel().getSelectedItem();
                if (data != null) {
                    String[] kentat = data.split(", ");
                    int laskuID = -1;
                    int varausID = -1;
                    boolean laskutettu = false;
                    boolean maksettu = false;

                    for (String kentta : kentat) {
                        String[] avainArvo = kentta.split(": ");
                        if (avainArvo.length < 2) continue;

                        String avain = avainArvo[0].trim();
                        String arvo = avainArvo[1].trim();

                        switch (avain) {
                            case "Lasku ID":
                                laskuID = Integer.parseInt(arvo);
                                setLaskuid(laskuID);
                                break;
                            case "Varaus ID":
                                varausID = Integer.parseInt(arvo);
                                setVarausid(varausID);
                                break;
                            case "Laskutettu":
                                laskutettu = Boolean.parseBoolean(arvo);
                                setLaskutettu(laskutettu);
                                break;
                            case "Maksettu":
                                maksettu = Boolean.parseBoolean(arvo);
                                setMaksettu(maksettu);
                                break;
                        }
                    }
                }
                luoPaivitaLaskuIkkuna(laskuData).show();
        });

        suljeBt.setOnAction(e->{
            // varmista ifillä, suljetaanko
            Alert alert4 = new Alert(Alert.AlertType.CONFIRMATION);
            alert4.setTitle("Varoitus");
            alert4.setHeaderText("Poistutaanko");
            alert4.setContentText("Poistu ?");
            Optional<ButtonType> valinta = alert4.showAndWait();
            if (valinta.isPresent() && valinta.get() == ButtonType.OK) {
                laskutStage.close();
            } else {
                e.consume();
            }
        });

        HBox nappulaBoksi=new HBox(laskunTila,suljeBt);
        nappulaBoksi.setSpacing(20);
        rootPaneeli.setBottom(nappulaBoksi);
        nappulaBoksi.setAlignment(Pos.CENTER);
        rootPaneeli.setPadding(new Insets(5,5,5,5));


        Scene laskutScene = new Scene(rootPaneeli,800,500);
        laskutStage.setScene(laskutScene);
        laskutStage.setTitle("Laskut");
        return laskutStage;

    }


    /**
     * luodaan ikkuna jolla voidaan paivittaa tietyn laskun tilaa
     * @return haluttu stage
     */
    public Stage luoPaivitaLaskuIkkuna(ObservableList<String> lista){
        Stage paivitysStage = new Stage();
        GridPane rootPaneeli=new GridPane();

        rootPaneeli.setAlignment(Pos.CENTER);
        rootPaneeli.setVgap(20);
        rootPaneeli.setHgap(20);
        rootPaneeli.setPadding(new Insets(20));
        CheckBox laskulaskutettu =new CheckBox("Lasku laskutettu");
        CheckBox maskuMaksettu =new CheckBox("Maksu maksettu");

        //buttonit ja action eventit
        Button tallennaBt=new Button("Tallenna");
        Button suljeBt=new Button("Sulje");
        Yhteysluokka yhteysluokka=new Yhteysluokka();
        LaskutData laskuolio=new LaskutData();

        tallennaBt.setOnAction(e->{
            //metodi, jolla tallennetaan annetut muutokset
            if(laskulaskutettu.isSelected()){
                setLaskutettu(true);
            }
            else{
                setLaskutettu(false);
            }
            if(maskuMaksettu.isSelected()){
                setMaksettu(true);
            }
            else {
                setMaksettu(false);
            }
            laskuolio.paivitaLaskua(yhteysluokka,getLaskuid(),getVarausid(),isLaskutettu(),isMaksettu());


            //päivitä listviewin lista

            lista.setAll(FXCollections.observableArrayList(haeLaskut(yhteysluokka)));
            // TARVITAAN ilmoitus että tiedot tallennettu
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Tallennus");
            alert.setHeaderText("Tallennetaanko varmsti?");
            alert.setContentText("Tallenna ja sulje?");
            Optional<ButtonType> sulje = alert.showAndWait();

            if (sulje.isPresent() && sulje.get() == ButtonType.OK) {
                paivitysStage.close();
                e.consume();
            }
        });

        suljeBt.setOnAction(e->{
            // varmista ifillä, suljetaanko
            Alert alert4 = new Alert(Alert.AlertType.CONFIRMATION);
            alert4.setTitle("Varoitus");
            alert4.setHeaderText("Poistutaanko");
            alert4.setContentText("Poistu ?");
            Optional<ButtonType> valinta = alert4.showAndWait();
            if (valinta.isPresent() && valinta.get() == ButtonType.OK) {
                paivitysStage.close();
            } else {
                e.consume();
            }
        });

        VBox buttons=new VBox(tallennaBt,suljeBt);
        buttons.setSpacing(15);
        buttons.setAlignment(Pos.TOP_CENTER);
        rootPaneeli.add(buttons,2,2);
        VBox checkBox=new VBox(laskulaskutettu, maskuMaksettu);
        checkBox.setSpacing(15);
        checkBox.setAlignment(Pos.CENTER_LEFT);
        VBox keskikohta=new VBox(checkBox);
        keskikohta.setSpacing(20);
        rootPaneeli.add(keskikohta,1,0);
        rootPaneeli.setStyle("-fx-background-color: #bfddf2;");


        Scene paivitysScene = new Scene(rootPaneeli,500,500);
        paivitysStage.setScene(paivitysScene);
        paivitysStage.setTitle("Päivitä laskun tiedot");
        return paivitysStage;
    }


}

