# Mokkienvarausjärjestelmä VillaBook 

**Esitiedot:**

Kevään 2025 Ohjelmistotuotanto I- kurssin lopputyö. Projektia on työstänyt Itä-Suomen yliopiston tietojenkäsittelytieteen laitoksen opiskelijat Aino Komulainen, Henna Korpelainen, Evita Romppainen ja Jenni Simonen.  

Projektin aihe on erään kuvitteellisen mökkiyrityksen varausjärjestelmä. Projektin teossa on käytetty JetBrains IntellJ Community-versiota sekä MySQL Community  8.0 Workbench, Server and Shell- applikaatioita tietojärjestelmän ylläpitoon. Pystyt näillä ohjeilla ajamaan järjestelmän nimeltä VillaBook. 

## OHJEET:
 
**Projektin SQL-funktion toiminnallisuus ja asennus**

1. Koska projekti tarvitsee tietokantaa, on ominaisuus rakennettu MySQL:n tarjoamilla applikaatioilla. 

2. Lataa MySQL täältä. Valitse oman tietokoneesi käyttöjärjestelmän mukaan, mieluiten 8.0 ja alempi kohta jossa näkyy (x86, 32-bit), MSI Installer) jossa lukee (mysql-installer-community-8.0.42.0.msi) vielä alempana. Tässä esimerkissä OS on Windows 11. 

3. Seuraa Installerin ohjeita. Aseta itsellesi salasana, kun ohjelma pyytää ja kirjoita se ylös.  

4. Avaa MySQL Workbench. Paina plus-ikonista “MySQL Connections” otsikon vieressä 

5. Aseta yhteydelle nimi sekä käyttäjänimi. Jos käytät latauksessa luomasi “root” käyttäjää, käytä sitä salasanaa jonka loit ladatessasi sovellusta. Sovellus kysyy 
   sitä kun yhteys on luotu. Älä kirjoita vielä schemaan mitään. Paina OK 

6. Haluttu scripti löytyy GitHubin src-kansion sisältä sql-kansiosta nimeltään “mokkikodit.sql” Tallenna se haluaamasi paikkaan tietokoneella 

7. Avaa nyt luotu yhteys ja klikkaa vasemmalla ylhäällä olevista kuvakkeista “Open a SQL script file in a new query tab” ja valitse ladattu mokkikodit.sql 

8. Kun scripti on latautunut näkymään, paina vasemman puoleista salama-kuvakketta ajaaksesi scriptin ja luomaan tietokannan. 

9. Älä sammuta luodun yhteyden serveriä, sillä sen pitää olla päällä projektia ajettasi. Jos kohtaat ongelmia, voit löytää apua täältä. 

### Projektin lataaminen IDE ympäristöön ja käynnistys 

1. Valitse sivustolta vihreä <Code> painike ja ota talteen sivuston https url 

2. Avaa oma IDE. Tässä esimerkissä käytetään JetBrains IntellJ Community versiota. 

3. Valitse etusivulta kohta “Clone repository” ja paina sitä. Liitä HTTPS linkki kohtaa URL: ja valitse mihin haluat projektin tallentaa. 

4. Odota että projekti rakentuu. Microsoft Defender ei välttämättä anna kaikkia kansioita ja voi promptaa sinua hyväksymään ne, projektin suorittamisen vuoksi. 

5. Avaa Yhteysluokka src- main-c.example.ohjelmistotuotanto kansion sisällä. 

6. Vaihda tietueet oman workbenchin mukaisesti. Sinun käyttäjänimi ja salasana, sekä sinun //host:port/ url-tietueeseen.
7. Lopputuloksen tulee näyttää tältä:
   String url = "jdbc:mysql://host:port/mokkikodit"; 
   String user = "root"; 
   String password ="salasana" 

8. Nyt voit siirtyä MainIkkuna-luokkaan, josta projektia ajetaan. 
 

**Dependency huomio:**
Pom.xml on lisätty tuki mysql-connector-java versiolla 8.0.32 josta on tiedetty haavoittovuus. 
Projektia ajetaan kuitenkin aina lokaalisti, eikä siinä ole vaaraa tässä yhteydessä.
