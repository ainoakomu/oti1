Projektin ajaminen edellyttää MySQL Workbenchiä 

 
1- Lataa mokkikodit.sql-tiedosto omalle koneelle GitHubin src- kansio ja sielt sql kansio ja mokkikodit.sql
2- Luo uusi connection SQLWorkbenchissä ja ota talteen username ja password sekä hostname ja port number 
3- Avaa nyt luotu uusi connection ja valitse ylhäältä palkista kuvake  “open a sql script file in a new query” ja avaa mokkikodit.sql jonka latasit 
Muista “ajaa” sql jotta database rakentuu (salama-nappula) 
(SQL-serveri pitää olla pystyssä, jotta yhteys intellj IDEAan onnistuu.) 

4. Vaihda koodissa olevat lähteet sinun oman workbenchin vastaavaksi luokassa YhteysLuokka

-jdbc:myseql://host:port/mokkikodit

-rootin tilalla on sun connection/workbench username 

-salasanan tilalla on sun connection/workbench salasana  

 
String url = "jdbc:mysql://tähän tulee host:tähän tulee port/mokkikodit"; 
String user = "root"; 
String password ="salasana"; 

 

TROUBLESHOOT: 

Jos dependency heittää punasta
Käy klikkaa Mavenia oikeassa laidassa ja paina Refresh ikonia, ja rebuild all maven projects. 

Jos ei vieläkään tottele, rebuild project IntellJ ja  sen pitäisi yrittää syncata Maven kirjastot jos ei muuten toiminut. 

Dependency huomio: 

Pom.xml on lisätty tuki mysql-connector-java versiolla 8.0.32 
