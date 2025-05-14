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

import static com.example.ohjelmistotuotanto.AsiakasData.haeAsiakkaat;
import static com.example.ohjelmistotuotanto.VarausData.haeVaraukset;

/**
 * luodaan asiakkaiden yllapitoon ikkuna seka mahdollisuus muokata asiakkaita
 */
public class AsiakasLuokka {
    /**
     * asiakkaan identifioiva numerotunniste
     */
    private int asiakasID;
    /**
     * asiakkaan nimi
     */
    private String asiakkaanNimi;
    /**
     * asiakkaan sahkopostiosoite
     */
    private String asiakkaanSposti;
    /**
     * asiakkaan puhelinnumero
     */
    private String puhelinnumero;
    /**
     * asiakkaan kotiosoite
     */
    private String kotiosoite;

    /**
     * luodaan ikkuna, johon tuodaan esille tietokannasta kaikkien asiakkaiden lista
     * @return palautetaan haluttu stage
     */
    public Stage luoAsiakkaatIkkuna(){
        Stage asiakasStage = new Stage();
        BorderPane rootPaneeli=new BorderPane();
        rootPaneeli.setPadding(new Insets(5,5,5,5));

        //sql yhteys
        Yhteysluokka olio=new Yhteysluokka();

        //observable lista, joka lisätään list view
        //käytetään datanhaku metodia
        ObservableList<String> asiakkaidenData = FXCollections.observableArrayList(haeAsiakkaat(olio));
        ListView<String> asiakaslista = new ListView<>(asiakkaidenData);
        asiakaslista.setMaxSize(900,350);
        rootPaneeli.setCenter(asiakaslista);
        rootPaneeli.setBackground(Taustakuvat.TaustakuvaAsettaminen.luoKolmasTausta());


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
            luoMuokkaaAsiakastaIkkuna(asiakkaidenData).show();
        });

        suljeBt.setOnAction(e->{
            asiakasStage.close();
        });

        HBox nappulaBoksi=new HBox(muokkaaAsiakasta,suljeBt);
        nappulaBoksi.setSpacing(20);
        rootPaneeli.setBottom(nappulaBoksi);
        nappulaBoksi.setAlignment(Pos.CENTER);
        rootPaneeli.setPadding(new Insets(5,5,5,5));

        Scene asiakasScene = new Scene(rootPaneeli,1000,600);
        asiakasStage.setScene(asiakasScene);
        asiakasStage.setTitle("Asiakkaat");
        return asiakasStage;
    }

    /**
     * luodaan asiakkaan muokkaamista kasitteleva ikkuna, joka paivittaa muutokset asiakkaat tauluun tietokannassa
     * @param lista annettu lista asiakkaat taulusta
     * @return palauttaa halutun stagen
     */
    public Stage luoMuokkaaAsiakastaIkkuna(ObservableList<String> lista){
        Stage muokkausStage = new Stage();
        GridPane rootPaneeli=new GridPane();
        rootPaneeli.setAlignment(Pos.CENTER);
        rootPaneeli.setVgap(20);
        rootPaneeli.setHgap(20);

        rootPaneeli.setPadding(new Insets(10));
        rootPaneeli.setStyle("-fx-background-color: #bfddf2;");

        Label nimilb =new Label("Nimi");
        Label spostiLb =new Label("Sähköpostiosoite");
        Label puhnrolb =new Label("Puhelinnumero");
        Label katuosoitelb =new Label("Katuosoite");

        TextField nimiTxt=new TextField();
        TextField spostiTxt =new TextField();
        TextField puhTxt =new TextField();
        TextField osoiteTxt =new TextField();
        HBox row1=new HBox(nimilb,nimiTxt);
        row1.setSpacing(69);
        HBox row2=new HBox(spostiLb, spostiTxt);
        row2.setSpacing(5);
        HBox row3=new HBox(puhnrolb, puhTxt);
        row3.setSpacing(13);
        HBox row4=new HBox(katuosoitelb, osoiteTxt);
        row4.setSpacing(37);
        VBox sarake=new VBox(row1,row2,row3,row4);
        sarake.setSpacing(20);

        sarake.setSpacing(15);
        sarake.setAlignment(Pos.CENTER);

        // setataan asiakkaan tiedot kenttiin
        nimiTxt.setText(getAsiakkaanNimi());
        spostiTxt.setText(getAsiakkaanSposti());
        puhTxt.setText(getPuhelinnumero());
        osoiteTxt.setText(getKotiosoite());

        //buttonit ja action eventit
        Button tallennaBt=new Button("Tallenna muutokset");
        Button poistaBt=new Button("Poista asiakas");
        Button suljeBt=new Button("Sulje");

        AsiakasData asiakasData = new AsiakasData();
        Yhteysluokka yhteysluokka = new Yhteysluokka();

        tallennaBt.setOnAction(e->{
            if(!nimiTxt.getText().isEmpty()){

                //TARVITAAN kysy tallennetaanko muutokset
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Tallennus");
                alert.setHeaderText("Tallennetaanko muutokset?");
                alert.setContentText("Tallenna ja sulje?");
                Optional<ButtonType> sulje = alert.showAndWait();

                if (sulje.isPresent() && sulje.get() == ButtonType.OK) {
                    //otetaan tiedot kentistä
                    setAsiakkaanNimi(nimiTxt.getText());
                    setAsiakkaanSposti(spostiTxt.getText());
                    setPuhelinnumero(puhTxt.getText());
                    setKotiosoite(osoiteTxt.getText());

                    //tallenna muutokset sqlään
                    asiakasData.muokkaaAsiakasta(yhteysluokka,getAsiakasID(),getAsiakkaanNimi(),getAsiakkaanSposti(),getPuhelinnumero(),getKotiosoite());
                    //TARVITAAN ilmoita että tallennettu
                    Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                    alert2.setTitle("Tallennus");
                    alert2.setHeaderText("Tallennettu tietokantaan");
                    alert2.setContentText("Tallennus onnistui");
                    //päivitä lista
                    lista.setAll(FXCollections.observableArrayList(haeAsiakkaat(yhteysluokka)));

                    muokkausStage.close();
                }


            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Tallennus");
                alert.setHeaderText("Kaikkia tietoja ei ole täytetty!");
                alert.setContentText("Täytä kaikki kohdat, jotta voit tallentaa.");
                alert.showAndWait();
                e.consume();
            }
        });

        poistaBt.setOnAction(e->{
            //kysy poistetaaanko asiakas varmasti
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Käyttäjätietojen poisto");
            alert.setHeaderText("Poistetaanko asiakastiedot tietokannasta?");
            alert.setContentText("Tätä toimintoa ei voi enää peruuttaa."+
                    "\n Asiakastietojen poistaminen voi aiheuttaa odottamattomia" +
                    "\nongelmia varausten tarkasteluun ja raportointiin.");
            Optional<ButtonType> valinta = alert.showAndWait();

            // jos painaa ok, poistetaan, jos painaa cancel, ei poisteta
            if (valinta.isPresent() && valinta.get() == ButtonType.OK) {
                asiakasData.poistaAsiakas(yhteysluokka,getAsiakasID());
                lista.setAll(FXCollections.observableArrayList(haeAsiakkaat(yhteysluokka)));

                // TARVITAAN ilmoita että asiakastiedot poistettu
                Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                alert2.setTitle("Asiakkaan poisto onnistui");
                alert2.setHeaderText("Tietojen poisto");
                alert2.setContentText("Asiakkaan tiedot poistettu onnistuneesti");
                muokkausStage.close();
            } else {
                Alert alert45 = new Alert(Alert.AlertType.ERROR);
                alert45.setTitle("Pakollisia tietoja puuttuu");
                alert45.setHeaderText("Pakollisia tietoja puuttuu.");
                alert45.setContentText("Täytä kaikki kentät.");
                alert45.showAndWait();
                e.consume();
            }
        });

        suljeBt.setOnAction(e->{
            //kysy suljetaanko ikkuna
            //kysy poistetaaanko varaus varmasti
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Poistu");
            alert.setHeaderText("Poistutaanko tallentamatta?");
            alert.setContentText("Tätä toimintoa ei voi enää peruuttaa.");
            Optional<ButtonType> valinta = alert.showAndWait();

            // jos painaa ok, poistetaan, jos painaa cancel, ei poisteta
            if (valinta.isPresent() && valinta.get() == ButtonType.OK) {
                muokkausStage.close();
            } else {
                e.consume();
            }
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

    /**
     * haetaan asiakkaan id
     * @return halutun asiakkan id numero
     */
    public int getAsiakasID() {
        return asiakasID;
    }

    /**
     * asetetaan asiakkaan uusi id numero
     * @param asiakasID uusi id numero
     */
    public void setAsiakasID(int asiakasID) {
        this.asiakasID = asiakasID;
    }

    /**
     * haetaan asiakkaan nimi
     * @return palautetaan loydetty nimi
     */
    public String getAsiakkaanNimi() {
        return asiakkaanNimi;
    }

    /**
     * asetetaan asiakkaan nimi
      * @param asiakkaanNimi haluttu nimi
     */
    public void setAsiakkaanNimi(String asiakkaanNimi) {
        this.asiakkaanNimi = asiakkaanNimi;
    }

    /**
     * haetaan asiakkaan sahkopostiosoite
     * @return palautetaan sahkopostiosoite
     */
    public String getAsiakkaanSposti() {
        return asiakkaanSposti;
    }

    /**
     * asetetaan asiakkaalle sahkopostiosoite
     * @param asiakkaanSposti haluttu sahkopostiosoite
     */
    public void setAsiakkaanSposti(String asiakkaanSposti) {
        this.asiakkaanSposti = asiakkaanSposti;
    }

    /**
     * haetaan asiakkaan puhelinnumero
     * @return palautetaan puhelinnumero
     */
    public String getPuhelinnumero() {
        return puhelinnumero;
    }

    /**
     * asetetaan asiakkaalle puhelinnumero
     * @param puhelinnumero haluttu puhelinnumero
     */
    public void setPuhelinnumero(String puhelinnumero) {
        this.puhelinnumero = puhelinnumero;
    }

    /**
     * haetaan asiakkaan kotiosoite
     * @return palautetaan kotiosoite
     */
    public String getKotiosoite() {
        return kotiosoite;
    }

    /**
     * asetetaan asiakkalle kotiosoite
     * @param kotiosoite haluttu kotiosoite
     */
    public void setKotiosoite(String kotiosoite) {
        this.kotiosoite = kotiosoite;
    }
}
