# EDUCOM REGISTRATIE VOORTGANG TRAINEES (RVT) #

Dit document bevat informatie over het Educom Registratie Voortgang Trainees platform dat ontwikkeld wordt door trainees van Educom.

### Waar is deze repository voor ###

#### Samenvatting ####
Het Educom Registratie Voortgang Trainees platform is een groepsopdracht voor trainees van Educom.
Het platform is bedoeld om alle beoordelingen van trainees door docenten gelijk aan elkaar te laten maken en op die manier een vergelijkbaar CV te maken onder alle Educom trainees.
#### Versie
0.1
#### Trello bord
Voor dit project is er een [Trello bord](https://trello.com/b/vjGU0IEs/educom-rvt) waar alle taken in staan beschreven.
Vraag aan een van de ontwikkelaars om toegang tot het bord.


### Hoe zet ik het project op? ###

#### Samenvatting
Het project bestaat uit een frontend in React en een backend in Java EE Jax-rs.
De backend bevat code die de database aanmaakt als deze niet bestaat.
In deze sectie staat beschreven hoe een ontwikkelaar het project werkend krijgt.

#### Verkrijgen van de repository
Kloon deze repository met deze line met behulp van git in een folder waar je de bestanden wil hebben staan:

`git clone https://educomq@bitbucket.org/educom_utrecht/rvt.git`

#### Backend Java
Om de backend te runnen wordt er gebruik gemaakt van Eclipse voor Java Enterprise.
Eclipse kan [hier](https://www.eclipse.org/downloads/packages/) gedownload worden.

Start na het downloaden Eclipse op en import de project folder via `file/open project from file system`
Alle bestanden zullen nu worden ingeladen en kunnen worden geopend in de editor

Om de code te runnen wordt een Grizzly Http server gebruikt die zichzelf opstart als de applicatie gedraaid wordt.
Om te starten klik met de rechtermuisknop op de `Main.java` (te vinden in src/main/java/nu/educom/rvt/rest) en `run as -> java application`.
De grizzly server wordt gestart en de backend is beschikbaar op `localhost:8081`.
De api kan aangeroepen worden door een request te doen naar `localhost:8081/webapi/{source path}`.

Een request kan vervolgens gedaan worden door in Postman (download [hier](https://www.postman.com/downloads/) )/browser een request te doen naar `localhost:8081/webapi/{path}`

#### Database
De java code bevat files die de database aanmaken met behulp van hibernate. Om de database inderdaad correct te laten aanmaken wordt er gebruik gemaakt van de xampp sql server (phpmyadmin).
Zorg ervoor dat de mysql en apache is gestart in Xampp. Als ook de backend server draait (zie vorige sectie) en er wordt een request gedaan, zal hibernate checken of de database bestaat en deze aanmaken als dat niet zo is. Ook wordt de database gelijk gevuld met de nodige tabellen.
Om de database aan te maken moet je in je MySQL een nieuwe user aanmaken 
Gebruikersnaam: usr_voortgang
Password: zie HibernateSession.java
Servernaam: 127.0.0.1
globale rechten: Data en Structuur


#### Frontend React
Voor de frontend is Nodejs nodig. Dit kan [hier](https://nodejs.org/en/download/) gedownload worden.
Met node js kan de frontend gedraaid worden door met opdrachtprompt naar de react_app folder en `npm install` en vervolgens`npm start` te runnen.
Vervolgens wordt de development server gestart en zal na enkele minuten de standaard browser de startpagina openen (`localhost:3000/login`).

### Hoe zit de repository in elkaar?

In deze sectie hoe de folder structuur van deze repository in elkaar zit.
Dit gaat dan over drie main folders:

1. [Documents](#markdown-header-Documents)
2. [J2EE](#markdown-header-J2EE)
3. [react_app](#markdown-header-react_app)

Bij vragen zie [deze sectie](#markdown-header-Help! Wie moet ik contacten?).

#### Documents

Deze folder bevat alle belangrijke documenten over het project. Hier valt onder andere de ERD en de wireframes. De ERD is een weergave van de databasestructuur zoals van te voren besproken. Deze ERD moet aangepast worden als de databasestructuur afwijkt van de huidige versie. De wireframes zijn voorbeeldpagina's zoals die geïmplementeerd gaan worden in de applicatie.


#### J2EE

Deze folder bevat alle bestanden voor het runnen van de backend. Deze folder heeft zijn eigen `.gitignore` bestand die alle bestanden beschrijft die niet door de repository moet worden meegenomen.
Naast dit bestand is er de `pom.xml`. Dit bestand bevat alle dependencies die nodig zijn voor de backend. Met behulp van Maven worden deze dependencies opgehaald en geïnstalleerd.

De rest van de files staan in de `src/main/java/nu/educom/rvt` folder. In deze folder zijn de bestanden in vier folders opgedeeld:

1. [models](#markdown-header-models)
2. [repositories](#markdown-header-repositories)
3. [rest](#markdown-header-rest)
4. [services](#markdown-header-services)

Deze folders zijn zo opgesteld dat er een MVC structuur is in de backend. Hieronder is uitgelegd wat voor bestanden elke folder bevat.

###### models

Deze folder bestaat uit classes die de tabellen representeren van de database en door de applicatie gebruikt kunnen worden om informatie over te dragen tussen functies.
Ook staan hier classes die een objectstructuur hebben die handig is voor communicatie tussen frontend en backend. (bijv. RoleJson.java)

###### repositories

Deze folder bevat classes die met behulp van de models het mogelijk maken om de nodige informatie van de database aan te vullen, veranderen of op te halen.
Voor elk model in de models folder die een tabel voorstelt in de database is er een repository aangemaakt.

Ook bevat deze folder het bestand `HibernateSession.java`. Dit bestand bevat de details voor het verbinden met de database. Hier moet ook gedefinieerd worden van welke models een tabel moet komen in de database.

###### rest

Het bestand `Main.java` bevat de code om de HTTP server te starten waarop de api gaat draaien.
Het bestand `MyApp.java` is het startpunt van de api applicatie. Hier staat ook de api path gedefinieerd.
Vervolgens kijkt de applicatie naar de resources die in deze folder staan. De resources bevatten de specifieke paden waarop een request gedaan kan worden. Dit zijn in termen van MVC de controllers.

Ook is er een `Filler.java` bestand. Dit bestand checkt of de database leeg is, en als dat zo is wordt de database gevuld met data.

Als laatste is er het `CORSFilter.java` bestand. Dit bestand zorgt ervoor dat de communicatie tussen back- en frontend goed verloopt. Zonder dit bestand worden de requests vanuit de frontend niet correct beantwoord, omdat beide servers op dezelfde host draaien (in development localhost).

###### services

In deze folder zijn de services die de business logica bevatten van de backend. Hier vinden berekeningen en modificaties van data plaats.

#### react_app

Ook deze folder heeft een eigen `.gitignore` bestand. De andere losse bestanden zijn `package.json` en `package-lock.json`.
`package.json` bevat de depencies en andere React applicatie specificaties. In `package-lock.json` bevat details over elke dependency van de applicatie.

Naast de losse bestanden zijn er twee folder:

1. public
2. src

In de `public ` folder staat de `index.html` die de standaard structuur van de html pagina bevat. Alle specifieke elementen gemaakt met react worden hieraan toegevoegd.
Ook staan hier de afbeeldingen die worden toegevoegd.

##### src folder
De src folder bevat de componenten waaruit een pagina is opgebouwd. De applicatie begint bij het bestand `index.js`. Dit bestand start met het renderen van de App component. Dit component staat in `App.js`. Dit bestand bevat een function die html code returned die andere componeten opvraagt.
Ook bevat de folder styling bestanden voor index en app (`index.css, app.css`). Naast deze losse bestanden zijn er twee folders:

1. [components](#markdown-header-components)
2. [constraints](#markdown-header-constraints)

###### components
Deze folder bevat bestanden voor elk component van de applicatie.  Elk component bevat een render functie die Html returned. Ook zijn er functies die de requests sturen, requests ontvangen en data structuren. De folder heeft de volgende subfolders:

1. footer
2. header
3. settings
4. main
5. routes

De `footer` en `header` folders bevatten respectievelijk de footer en header component voor een pagina. Ook zit hier een specifieke styling bij voor deze componenten.

De `settings` folder bevat de paginas die onderdeel van de instellingen van een gebruiker zoals het veranderen van een wachtwoord of het toevoegen van een gebruiker.

De `main` folder bevat standaard schermen in de applicatie zoals login en home. Het `main.js` bestand bevat een switch case op te bepalen welk component gerenderd moet worden.

De `routes` folder bevatten functies die met behulp van een conditie bepalen of een route component gerenderd moet worden of dat de gebruiker geen toegang heeft tot deze pagina en naar een andere pagina wordt verwezen. Bijvoorbeeld de `privateRoute.js` geeft alleen toegang tot een pagina als de gebruiker is ingelogd.

###### constraints
Deze folder bevat de validaties op de verschillende formulieren in de applicatie zoals bijvoorbeeld het inloggen.

### Contribution guidelines ###

Als je code hebt geschreven kan dit gepusht worden naar deze repository (`git push`).
Om de code quality hoog te houden is het handig om commits te laten controleren door mede ontwikkelaars.
De repository heeft twee main branches : master en development.
`master` bevat de laatste versie van de afgelopen sprint. Deze versie wordt dan ook in productie gebracht door deze op de host te zetten.
De `development` branch bevat gaat verder op de versie van de master branch. Hier worden alle aanpassingen naar toe gepusht. Om ook deze branch netjes te houden is het verstandig om op deze branches nieuwe branches aan te maken als er wordt gewerkt aan een bepaalde opdracht. Zodra de opdracht af is en de code is gecontroleerd kan de branch gemerged worden met development.
Voordat er gemerged wordt is het wellicht verstandig op te kijken of jouw veranderingen ook blijven werken met de laatste versie van de development branch. Dit kan gedaan worden door de development branch te merge in je eigen branch (`git merge`)


### Production build ###
Voor het draaien van de applicatie in productie moeten zowel de frontend React als de Java Enterprise backend gebuild worden.
Dit wordt apart van elkaar gedaan op de production branch. Deze branch zal altijd de laatste werkende versie bevatten.

##### React
1. Open command line en ga naar de react app folder (rvt/react_app)
2. (Zorg dat node js is geïnstalleerd met npm -version)
3. Voer het volgende commando uit: npm run build
4. De react app wordt nu gebouwd en in de "build" folder gestopt
5. Download een FTP client (zoals [FileZilla](https://filezilla-project.org/))
6. Log in met de FTP gegevens hieronder
7. Kopieer alle bestanden in de "build" folder in de www_root folder op de server
8. De react app is nu beschikbaar op voortgang.educom.nu 

##### Java Enterprise (JAX-rs)
1. Ga naar de J2EE folder (rvt/J2EE) en run "mvn install"
2. Een jar wordt gemaakt en in de "target" folder gezet
3. Kopieer de "J2EE.jar" naar de server met FTP (zie stap 5 en 6 React)
4. Let op: Kopieer de Jar niet in de "www_root folder"!!
5. Download een SSH client (zoals [PuTTY](https://www.putty.org/))
6. Login op de server met de SSH gegevens hieronder 
7. Start de server met dit commando : "java -jar J2EE.jar"
8. Controleer of de grizzly server is gestart (`INFO: [HttpServer] Started`)

##### Stop Java server
1. Login via SSH client zoals uitgelegd in stap 5 en 6 van Java Enterprise (Jax-rs)
2. Check welke service je moet killen met het commando "ps -aux | grep J2EE"
3. Kijk naar de regel met aan het einde "java -jar J2EE.jar" en onthoud het nummer in de 2de kolom
4. Kill de server met "kill {nummer}" 

##### De gegevens

URL: https://voortgang.educom.nu

###### FTP
server: voortgang.educom.nu
user: edu-voortgang
Ww: @j0v22nS


###### SSH
> ssh edu-voortgang@edu-deta.com
Ww: @j0v22nS


###### MySQL
DB: db_voortgang
User: usr_voortgang
Ww: ?120qhZl
Host: localhost

### Help! Wie moet ik contacten? ###

* Neem contact op met een van de ontwikkelaars
* Neem contact op met de docent Jeroen van locatie Utrecht _jeroen.heemskerk@educom.nu_