package com.example.ohjelmistotuotanto;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import static com.example.ohjelmistotuotanto.AsiakasData.haeAsiakkaat;


public class AsiakasLuokka {

    private int asiakasID;
    private String asiakkaanNimi;
    private String asiakkaanSposti;
    private String puhelinnumero;
    private String kotiosoite;

    public Stage luoAsiakkaatIkkuna(){
        Stage asiakasStage = new Stage();
        BorderPane rootPaneeli=new BorderPane();
        rootPaneeli.setPadding(new Insets(5,5,5,5));

        //sql yhteys
        Yhteysluokka olio=new Yhteysluokka();

        //observable lista, joka lisätään list view
        //käytetään datanhaku metodia
        ObservableList<String> asiakasData = FXCollections.observableArrayList(haeAsiakkaat(olio));
        ListView<String> asiakaslista = new ListView<>(asiakasData);
        asiakaslista.setMaxSize(350,250);
        rootPaneeli.setCenter(asiakaslista);


        asiakaslista.getSelectionModel().selectedItemProperty().addListener((obs, vanha, uusi) -> {
            if (uusi != null) {
                String[] kentat = uusi.split(", ");

                for (String kentta : kentat) {
                    String[] avainArvo = kentta.split(": ");
                    if (avainArvo.length < 2) continue;

                    String avain = avainArvo[0].trim();
                    String arvo = avainArvo[1].trim();

                    switch (avain) {
                        case "ID":
                            setAsiakasID(Integer.valueOf(arvo));
                            break;
                        case "Nimi":
                            setAsiakkaanNimi(arvo);
                            break;
                        case "Sähköposti":
                            setAsiakkaanSposti(arvo);
                            break;
                        case "Puhelin":
                            setPuhelinnumero(arvo);
                            break;
                        case "Osoite":
                            setKotiosoite(arvo);
                            break;
                    }
                }
            }
        });


        //buttonit ja action eventit
        Button muokkaaAsiakasta =new Button("Muokkaa asiakasta");
        Button suljeBt=new Button("Sulje");

        muokkaaAsiakasta.setOnAction(e->{
            luoMuokkaaAsiakastaIkkuna(asiakasData).show();
        });

        suljeBt.setOnAction(e->{
            asiakasStage.close();
        });

        HBox nappulaBoksi=new HBox(muokkaaAsiakasta,suljeBt);
        nappulaBoksi.setSpacing(20);
        rootPaneeli.setBottom(nappulaBoksi);
        nappulaBoksi.setAlignment(Pos.CENTER);
        rootPaneeli.setPadding(new Insets(5,5,5,5));

        Scene asiakasScene = new Scene(rootPaneeli,500,500);
        asiakasStage.setScene(asiakasScene);
        asiakasStage.setTitle("Asiakkaat");
        return asiakasStage;
    }

    public Stage luoMuokkaaAsiakastaIkkuna(ObservableList<String> lista){
        Stage muokkausStage = new Stage();
        GridPane rootPaneeli=new GridPane();
        rootPaneeli.setAlignment(Pos.CENTER);
        rootPaneeli.setVgap(20);
        rootPaneeli.setHgap(20);

        rootPaneeli.setPadding(new Insets(10));

        Label nimilb =new Label("Nimi");
        Label spostiLb =new Label("Sähköpostiosoite");
        Label puhnrolb =new Label("Puhelinnumero");
        Label katuosoitelb =new Label("Katuosoite");

        TextField mokkiTxt=new TextField();
        TextField asiakasTxt =new TextField();
        TextField alkuTxt =new TextField();
        TextField loppuTxt =new TextField();
        HBox row1=new HBox(nimilb,mokkiTxt);
        row1.setSpacing(69);
        HBox row2=new HBox(spostiLb, asiakasTxt);
        row2.setSpacing(5);
        HBox row3=new HBox(puhnrolb, alkuTxt);
        row3.setSpacing(13);
        HBox row4=new HBox(katuosoitelb, loppuTxt);
        row4.setSpacing(37);
        VBox sarake=new VBox(row1,row2,row3,row4);
        sarake.setSpacing(20);

        sarake.setSpacing(15);
        sarake.setAlignment(Pos.CENTER);

        //buttonit ja action eventit
        Button tallennaBt=new Button("Tallenna muutokset");
        Button poistaBt=new Button("Poista asiakas");
        Button suljeBt=new Button("Sulje");

        AsiakasData asiakasData = new AsiakasData();
        Yhteysluokka yhteysluokka = new Yhteysluokka();

        tallennaBt.setOnAction(e->{
            //kysy tallennetaanko muutokse
            //tallenna muutokset sqlään
            // asiakasData.muokkaaAsiakasta(yhteysluokka,);

            //ilmoita että tallennettu
            lista.setAll(FXCollections.observableArrayList(haeAsiakkaat(yhteysluokka)));

            muokkausStage.close();
        });

        poistaBt.setOnAction(e->{
            //kysy poistetaanko asiakas
            //poista asiakas sqlästä
            // asiakasData.poistaAsiakas(yhteysluokka,);

            //ilmoita että poistettu

            lista.setAll(FXCollections.observableArrayList(haeAsiakkaat(yhteysluokka)));

            muokkausStage.close();
        });

        suljeBt.setOnAction(e->{
            //kysy suljetaanko ikkuna
            muokkausStage.close();
        });

        VBox buttons=new VBox(tallennaBt,poistaBt,suljeBt);
        buttons.setSpacing(20);
        rootPaneeli.add(buttons,2,2);

        VBox keskikohta=new VBox(sarake);
        keskikohta.setSpacing(20);
        rootPaneeli.add(keskikohta,1,0);


        Scene muokkausScene = new Scene(rootPaneeli,500,500);
        muokkausStage.setScene(muokkausScene);
        muokkausStage.setTitle("Muokkaa asiakasta");
        return muokkausStage;
    }

    public int getAsiakasID() {
        return asiakasID;
    }

    public void setAsiakasID(int asiakasID) {
        this.asiakasID = asiakasID;
    }

    public String getAsiakkaanNimi() {
        return asiakkaanNimi;
    }

    public void setAsiakkaanNimi(String asiakkaanNimi) {
        this.asiakkaanNimi = asiakkaanNimi;
    }

    public String getAsiakkaanSposti() {
        return asiakkaanSposti;
    }

    public void setAsiakkaanSposti(String asiakkaanSposti) {
        this.asiakkaanSposti = asiakkaanSposti;
    }

    public String getPuhelinnumero() {
        return puhelinnumero;
    }

    public void setPuhelinnumero(String puhelinnumero) {
        this.puhelinnumero = puhelinnumero;
    }

    public String getKotiosoite() {
        return kotiosoite;
    }

    public void setKotiosoite(String kotiosoite) {
        this.kotiosoite = kotiosoite;
    }
}
