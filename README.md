# EDUCOM REGISTRATIE VOORTGANG TRAINEES (RVT) #

Dit document bevat informatie over het Educom Registratie Voortgang Trainees platform dat ontwikkelt wordt door trainees van Educom.

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
Kloon deze repository met deze line in opdrachtprompt in een folder waar je de bestanden wil hebben staan:

`git clone https://educomq@bitbucket.org/educom_utrecht/rvt.git`

#### Backend Java
Om de backend te runnen wordt er gebruik gemaakt van Eclipse voor Java Enterprise. 
Eclipse kan [hier](https://www.eclipse.org/downloads/packages/) gedownload worden.

Start na het downloaden Eclipse op en import de project folder via `file/open project from file system`
Alle bestanden zullen nu worden ingeladen en kunnen worden geopend in de editor

Om de code uiteindelijk te runnen wordt er gebruik gemaakt van een tomcat server. Deze kan [hier](https://tomcat.apache.org/download-90.cgi) gedownload worden.
Om de code runnen op de tomcat server ga naar `run\ run configuration` en selecteer de tomcat server. Als dit is gebeurt wordt de code gerund op de tomcat server als je de run knop gebruikt.

Een request kan vervolgens gedaan worden door in Postman/browser een request te doen naar `localhost:[tomcat port]/J2EE/webapi/{path}`

#### Database
De java code bevat files die de database aanmaken met behulp van hibernate. Om de database inderdaad correct te laten aanmaken wordt er gebruik gemaakt van de xampp sql server (phpmyadmin).
Zorg ervoor dat de mysql en apache is gestart in Xampp. Als ook de backend server draait (zie vorige sectie) en er wordt een request gedaan, hibernate zal dan checken of de database bestaat en hem aanmaken als dat niet zo is. Ook wordt de database gelijk gevuld met de nodige tabelen.

#### Frontend React
Voor de frontend is Nodejs nodig. Dit kan [hier](https://nodejs.org/en/download/) gedownload worden.
Met node js kan frontend gedraait worden door met opdrachtpromp naar de react_app folder en `npm start` te runnen.
Vervolgens wordt de development server gestart en zal na enkele minuten de standaard browser de start pagina openen (`localhost:3000/login`).

### Hoe zit de repository in elkaar? 

In deze sectie hoe de folder structuur van deze repository in elkaar zit.
Dit gaat dan over drie main folders: 

1. [Documents](#markdown-header-Documents)
2. [J2EE](#markdown-header-J2EE)
3. [react_app](#markdown-header-react_app)
 
Bij vragen zie [deze sectie](#markdown-header-Help! Wie moet ik contacten?).

#### Documents

Deze folder bevat alle belangrijke documenten over het project. Ook de ERD die de database structuur aangeven staan hier opgeslagen.


#### J2EE

Deze folder bevat alle bestanden voor het runnen van de backend. Deze folder heeft zijn eigen `.gitignore` bestand die alle bestanden die niet door de repository moet worden meegenomen.
Naast dit bestand is er de `pom.xml`. Dit bestand bevat alle dependencies die nodig zijn voor de backend. Met behulp van Maven worden deze dependecies opgehaald en geïnstaleerd.

De rest van de files staan in de `src/main/java/nu/educom/rvt` folder. In deze folder zijn de bestanden in 4 folders opgedeeld:

1. [models](#markdown-header-models)
2. [repositories] (#markdown-header-repositories)
3. [rest] (#markdown-header-rest)
4. [services] (#markdown-header-services)

Deze folders zijn zo opgesteld dat er een MVC structuur is in de backed. Hieronder is uitgelegd wat voor bestanden er in elke folder zit.

###### models

Deze folder bestaat uit classes die de tabelen representeren van de database en door de applicatie gebruikt kunnen worden op informatie over te dragen tussen functies.
Ook staan hier classes die een object structuur hebben die handig is voor communicatie is tussen frontend en backend. (bijv. RoleJson.java)

###### repositories

Deze folder bevat classes die met behulp van de models het mogelijk maken om de nodige informatie van de database kan aanvullen, veranderen of ophalen. 
Voor elk model in de models folder die een tabel voorstelt in de database heeft zijn eigen repository.

Ook bevat deze folder een bestand dat heet `HibernateSession.java`. Dit bestand bevat de details voor het connecten met de database. Hier moet ook gedefineerd worden van welke models een tabel moet komen in de database.

###### rest

Het bestand `MyApp.java` is het startpunt van de api applicatie. Hier staat ook de api path gedefineerd.
Vervolgens kijkt de applicatie naar de resources die in deze folder staan. De resources bevatten de specifieke paden waarop een request gedaan kan worden. Dit zijn in termen van MVC de controllers.

Ook is er een `Filler.java` bestand. Dit bestand checkt of de database leeg is, en als dat zo is dat de database gevuld wordt met data.

Als laatste is er het `CORSFilter.java` bestand. Dit bestand zorgt ervoor dat de communicatie tussen back en frontend goed verloopt. Zonder bestand worden de requests vanuit de frontend niet correct beantwoord, omdat beide servers op dezelfde host draaien (in development localhost)

###### services

In deze folder zijn de services die de business logica bevatten van de backend. Hier vinden berekeningen en modificaties van data plaats.

#### react_app

Ook deze folder heeft een eigen `.gitignore` bestand. De andere losse bestande zijn `package.json` en `package-lock.json`.
`package.json` bevat de depencies en andere React applicatie specificaties. In `package-lock.json` bevat details over elke dependencie van de applicatie.

Naast de losse bestanden zijn er twee folder: 

1. public
2. src

In de `public ` folder staat de `index.html` die de standaard structuur van de html pagina bevat. Alle specifieke elementen gemaakt met react worden hieraan toegevoegd.

##### src folder
De src folder bevat de componenten waaruit een pagina is opgebouwd. De applicatie begint bij het bestand `index.js`. Dit bestand start met het renderen van de App component. Dit component staat in `App.js`. Dit bestand bevat een function die html code return met die andere componeten opvraagt.
Ook bevat de folder styling bestanden voor index en app (`index.css, app.css`). Naast deze losse bestanden zijn er twee folders: 

1. [components] (#markdown-header-components)
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

De `routes` folder bevatten functies die met behulp van een conditie bepalen of een route component gerenderd moet worden of dat de gebruiker geen toegang heeft tot deze pagina en naar andere pagina wordt verwezen. Bijvoorbeeld de `privateRoute.js` geeft alleen toegang tot een pagina als de gebruiker is ingelogd.

###### constraints
Deze folder bevatten de validaties op de verschillende formulieren in de applicatie zoals bijvoorbeeld het inloggen.

### Contribution guidelines ###

Als je code hebt geschreven kan dit gepusht worden naar deze repository (`git push`). 
Om de code quality hoog te houden is het handig om commits te laten controleren door mede ontwikkelaars. 
Nog beter is om te werken in branches en zodra een ontwikkelaar denkt klaar te zijn en de branch wil merchen met de master branch (de branch die uiteindelijk in ontwikkeling gaat) om een pull request aan te maken en anderen ontwikkelaars te vragen om deze pull request te bevesetigen. Op deze manier is iedereen op de hoogte van de veranderingen in de code.


### Help! Wie moet ik contacten? ###

* Neem contact op met een van de ontwikkelaars
* Neem contact op met de docent Jeroen van locatie Utrecht _jeroen.heemskerk@educom.nu_