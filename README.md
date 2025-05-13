Kevään 2025 Ohjelmistotuotanto I- kurssin lopputyö. Projektia on työstänyt Aino Komulainen, Henna Korpelainen, Evita Romppainen ja Jenni Simonen. (UEF)

Projektin aihe on erään kuvitteellisen mokkiyrityksen varausjärjestelmä. 
Projektin teossa on käytetty JetBrains IntellJ Community-versiota sekä MySQL Community Workbench, Server and Shell- applikaatioita tietojärjestelmän ylläpitoon.

**HUOMIO:**
Tämän projektin ajaminen edellyttää MySQL Workbench, Server and Shell toimintoja. Voit ladata ne täältä [https://dev.mysql.com/downloads/installer/] Projektissa hyödynnetään 8.0 versiota.

# OHJEET:
 
1- Lataa mokkikodit.sql-tiedosto omalle koneelle GitHubin src- kansion sisällä olevasta sql-kansiosta ja valitse mokkikodit.sql ladattavaksi.
2- Avaa MySQL Workbench, luo uusi yhteys (connection) ja ota talteen yhteydessä käytetävä käyttäjänimi, salasana, host ja port.
3- Avaa nyt luotu uusi yhteys ja valitse ylhäältä palkista kuvake  “open a sql script file in a new query” ja avaa mokkikodit.sql jonka latasit.
4- Kun scripti on ladannut, aja se salama-nappulasta

## Kun olet kopioinut repon omaan IDE:n

5- Vaihda koodissa olevat lähteet sinun oman yhteyden vastaavaksi luokassa *YhteysLuokka*
Lopputuloksen tulee näyttää tältä:
String url = "jdbc:mysql://host:port/mokkikodit"; 
String user = "root"; 
String password ="salasana"; 

**Dependency huomio:**
Pom.xml on lisätty tuki mysql-connector-java versiolla 8.0.32 josta on tiedetty haavoittovuus. 
Projektia ajetaan kuitenkin aina lokaalisti, eikä siinä ole vaaraa tässä yhteydessä.
