DROP DATABASE IF EXISTS mokkikodit;
CREATE DATABASE mokkikodit;
USE mokkikodit;

CREATE TABLE `kayttajat`(
	`kayttaja_id` INT PRIMARY KEY,
    `nimi` VARCHAR(250) NOT NULL,
    `kayttaja_tunnus` VARCHAR(250) NOT NULL,
    `salasana` VARCHAR(250) NOT NULL,
    `kayttaja_taso` VARCHAR(250) NOT NULL,
    `onko_anniskelu_oikeus` BOOLEAN,
    `onko_hygieniapassi` BOOLEAN
);

CREATE TABLE `asiakkaat`(
	`asiakas_id` INT PRIMARY KEY,
    `asiakkaan_nimi` VARCHAR(250) NOT NULL,
    `asiakkaan_sahkoposti` VARCHAR(250) NOT NULL,
    `puhelinnumero` VARCHAR(250) NOT NULL,
    `koti_osoite` VARCHAR(250) NOT NULL
);

CREATE TABLE `mokit`(
    `mokki_id` INT PRIMARY KEY,
    `hinta_per_yo` DECIMAL,
    `osoite` VARCHAR(250) NOT NULL,
    `neliot` INT NOT NULL,
    `vuodepaikat` INT NOT NULL,
    `onko_rantasauna` BOOLEAN,
    `onko_oma_ranta` BOOLEAN,
    `onko_wifi` BOOLEAN,
    `onko_sisa_wc` BOOLEAN,
    `onko_palju` BOOLEAN
);

CREATE TABLE `varaukset`(
    `varaus_id` INT PRIMARY KEY,
    `varausalku_date` DATETIME NOT NULL,
    `varausloppu_date` DATETIME NOT NULL,
    `hinta` INT NOT NULL,
    `kayttaja_id` INT NOT NULL,
    `asiakas_id` INT NOT NULL,
    `mokki_id` INT NOT NULL,
    FOREIGN KEY (kayttaja_id) REFERENCES kayttajat(kayttaja_id),
    FOREIGN KEY (asiakas_id) REFERENCES asiakkaat(asiakas_id),
    FOREIGN KEY (mokki_id) REFERENCES mokit(mokki_id)
);
    

CREATE TABLE `arvostelut`(
	`arvostelu_id` INT PRIMARY KEY,
    `varaus_id` INT NOT NULL,
    `arvosana` DECIMAL,
    `arvostelu` TEXT,
    FOREIGN KEY (varaus_id) REFERENCES varaukset(varaus_id)
);

CREATE TABLE `laskut` (
    `lasku_id` INT PRIMARY KEY,
    `varaus_id` INT NOT NULL,
    `onko_laskutettu` BOOLEAN,
    `onko_maksettu` BOOLEAN,
    FOREIGN KEY (varaus_id) REFERENCES varaukset(varaus_id)
);

CREATE TABLE `kayttaja_muokkaa_mokkia` (
`kayttaja_id` INT NOT NULL,
`mokki_id` INT NOT NULL,
PRIMARY KEY (kayttaja_id, mokki_id),
FOREIGN KEY (kayttaja_id) REFERENCES kayttajat(kayttaja_id),
FOREIGN KEY (mokki_id) REFERENCES mokit(mokki_id)
);

CREATE TABLE `kayttaja_tekee_varaus` (
`kayttaja_id` INT NOT NULL,
`varaus_id` INT NOT NULL,
PRIMARY KEY (kayttaja_id, varaus_id),
FOREIGN KEY (kayttaja_id) REFERENCES kayttajat(kayttaja_id),
FOREIGN KEY (varaus_id) REFERENCES varaukset(varaus_id)
);


INSERT INTO kayttajat (kayttaja_id, nimi, kayttaja_tunnus, salasana, kayttaja_taso, onko_anniskelu_oikeus, onko_hygieniapassi) VALUES
(3887, "Maisa Kaasinen", "MaiKaa", "Salasana123", "perus", true, true),
(4459, "Sulo Karhu", "Karhunsulo", "Talviunet<3", "perus", true, true),
(7866, "Kaarina Kananen", "KaKana", "BroilerLover22", "admin", true, true),
(2644, "Lassi Lapanen", "LaLap", "Icecold909", "perus", false, true);

INSERT INTO asiakkaat (asiakas_id, asiakkaan_nimi, asiakkaan_sahkoposti, puhelinnumero, koti_osoite) VALUES
(166, "Pekka Makkonen", "p.make@luukku.fi", "+358455675678", "Mongokuja 16 40100, JYVÄSKYLÄ"),
(464, "Toivo Valkonen", "Valkoinen.monsteri@hotmail.com", "+358449763133", "Kongolianraitti 90, 28220 PORI"),
(727, "Nikolas Häkki", "Häkki.nikolas@gmail.com", "+358503230091", "Tsekkilänkatu 87, 27600 HINNERJOKI"),
(224, "Topias Tiskari", "Topias.tiskari@wippies.fi", "+358443459007", "Vastarannantie 56, 23140 PYHE"),
(243, "Pirkko Kesonen", "Pirkko.kesonen@luukku.fi", "+358406742218", "Jaavakatu 21, 00560 HELSINKI"),
(113, "Cristiano Ronaldo", "Ronaldo.Manager@celebhouse.com", "+358912345678", "Rua da Liberdade 123, 7100-014 PORTUGAL");

INSERT INTO mokit (mokki_id, hinta_per_yo, osoite, neliot, vuodepaikat, onko_rantasauna, onko_oma_ranta, onko_wifi, onko_sisa_wc, onko_palju) VALUES
(1, 130, "Vaaratie 16", 54, 4, true, true, true, true, true),
(2, 560, "Loimukuja 564", 98, 8, false, true, true, true, true),
(3, 87, "Loimukuja 542", 24, 2, true, false, false, false, false),
(4, 43, "Loimukuja 687", 15, 1, false, true, false, true, false),
(5, 340, "Vaaratie 17", 75, 5, true, true, true, true, false);

INSERT INTO varaukset (varaus_id, asiakas_id, mokki_id, varausalku_date, varausloppu_date, hinta, kayttaja_id) VALUES
(4466, 166, 1, "2025-01-01", "2025-01-04", 390, 3887),
(5573, 224, 2, "2025-02-12", "2025-02-16", 2240, 4459),
(2198, 464, 3, "2025-04-30", "2025-05-02", 174, 4459),
(9865, 166, 4, "2025-04-30", "2025-05-02", 86, 7866),
(6655, 243, 5, "2024-06-20", "2024-06-23", 1020, 2644),
(1243, 166, 2, "2024-11-15", "2024-11-17", 1120, 2644),
(3309, 113, 2, "2024-12-30", "2025-01-11", 6720, 3887);

INSERT INTO arvostelut (arvostelu_id, varaus_id, arvosana, arvostelu) VALUES
(287, 4466, 4.2, "Ihana paikka, mutta saunaan oli jäänyt edellisen asukkaan boxerit hyi"),
(765, 5573, 3.8, "Mukava viikonloppu takana! Mitä hyötyä on tilaa aamupalaa, jos sitä ei saa ikinä?"),
(332, 2198, 5, "Ihan paras reissu ikinä! Wabu ei lopu!"),
(122, 9865, 2.5, "Mökki oli hyvä, mutta naapurin asukkaat häiritsivät lomaani. Poliisit eivät ikinä löytäneet mökkikylää."),
(433, 6655, 3, "Näin hintavalta mökiltä olisin odottanut, että sauna edes toimisi kunnolla. Ja vifikin oli ihan surkea!"),
(657, 3309, 5, "As Ronaldo's manager, I’m speaking on his behalf — the accommodation facilities were absolutely amazing. This was our first time visiting Eastern Finland. Lapland doesn’t even come close!");


INSERT INTO laskut (lasku_id, varaus_id, onko_laskutettu, onko_maksettu) VALUES
(25, 3309, true, true),
(26, 4466, true, true),
(27, 5573, true, true),
(28, 2198, true, true),
(29, 9865, true, true),
(30, 6655, true, false),
(31, 1243, false, false);
