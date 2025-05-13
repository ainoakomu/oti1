package com.example.dao;

public class User {

    private int kayttajaId;
    private String nimi;
    private String kayttajaTunnus;
    private String salasana;
    private String kayttajaTaso;
    private boolean onkoAnniskeluOikeus;
    private boolean onkoHygieniapassi;

    // Konstruktori ja getterit / setterit

    public User(int kayttajaId, String nimi, String kayttajaTunnus, String salasana, String kayttajaTaso, boolean onkoAnniskeluOikeus, boolean onkoHygieniapassi) {
        this.kayttajaId = kayttajaId;
        this.nimi = nimi;
        this.kayttajaTunnus = kayttajaTunnus;
        this.salasana = salasana;
        this.kayttajaTaso = kayttajaTaso;
        this.onkoAnniskeluOikeus = onkoAnniskeluOikeus;
        this.onkoHygieniapassi = onkoHygieniapassi;
    }

    public int getKayttajaId() {
        return kayttajaId;
    }

    public String getName() {
        return nimi;
    }

    // Lisää getterit muille kentille tarvittaessa...
}