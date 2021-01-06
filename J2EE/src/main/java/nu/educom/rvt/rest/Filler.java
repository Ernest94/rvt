package nu.educom.rvt.rest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

import nu.educom.rvt.models.Bundle;
import nu.educom.rvt.models.BundleConcept;
import nu.educom.rvt.models.BundleTrainee;
import nu.educom.rvt.models.Concept;
import nu.educom.rvt.models.ConceptRating;
import nu.educom.rvt.models.Location;
import nu.educom.rvt.models.Review;
import nu.educom.rvt.models.Role;
import nu.educom.rvt.models.Theme;
import nu.educom.rvt.models.User;
import nu.educom.rvt.models.UserLocation;
import nu.educom.rvt.repositories.BundleConceptRepository;
import nu.educom.rvt.repositories.BundleRepository;
import nu.educom.rvt.repositories.BundleTraineeRepository;
import nu.educom.rvt.repositories.ConceptRatingRepository;
import nu.educom.rvt.repositories.ConceptRepository;
import nu.educom.rvt.repositories.DatabaseException;
import nu.educom.rvt.repositories.HibernateSession;
import nu.educom.rvt.repositories.LocationRepository;
import nu.educom.rvt.repositories.ReviewRepository;
import nu.educom.rvt.repositories.RoleRepository;
import nu.educom.rvt.repositories.ThemeRepository;
import nu.educom.rvt.services.UserService;

public class Filler {
    
	private static final Logger LOG = LogManager.getLogger();
	/* JH: Gebruik geen <tab> karakters  */
    public static Boolean isDatabaseEmpty() {
        try (Session session = HibernateSession.getSessionFactory().openSession()) {
            RoleRepository rolesRepo = new RoleRepository(session);
            List<Role> roles = rolesRepo.readAll();
            return roles.size() <= 0 ;    
        } catch (DatabaseException e) {
            // TODO LOG
            e.printStackTrace();
            return false;
        }
    }
    
    public static void fillDatabase() {
        try (Session session = HibernateSession.openSessionAndTransaction()) {
            
        	
        	//INITIALIZE DATES AND TIMES
        	LocalDate lastWeekLd = LocalDate.now().minus(1, ChronoUnit.WEEKS);
            LocalDate nowLD = LocalDate.now();
            LocalDate endDateLD = null;
            LocalDateTime weekAgo = LocalDateTime.now().minus(7, ChronoUnit.DAYS);
            LocalDateTime dayAgo = LocalDateTime.now().minus(1, ChronoUnit.DAYS);
            LocalDateTime nowLDT = LocalDateTime.now();    
            
            //FILL THE ROLE TABLE
            List<Role> roles = new ArrayList<Role>();
            Role admin = new Role("Admin");
            roles.add(admin);
            Role docent = new Role("Docent");
            roles.add(docent);
            Role trainee = new Role("Trainee");
            roles.add(trainee);
            Role sales = new Role("Sales");
            roles.add(sales);
        	Role office = new Role("Office");
            roles.add(office);
            RoleRepository roleRepo = new RoleRepository(session);
            for (Role role : roles) {
                roleRepo.create(role);
            }
            
            //FILL THE LOCATION TABLE
            List<Location> locations = new ArrayList<Location>();
            Location utrecht = new Location("Utrecht"); 
            locations.add(utrecht);
            Location arnhem = new Location("Arnhem"); 
            locations.add(arnhem);
            Location sittard = new Location("Sittard"); 
            locations.add(sittard);
            Location eindhoven = new Location("Eindhoven"); 
            locations.add(eindhoven);
            Location testLocation = new Location("TestLocation"); 
            locations.add(testLocation);
            LocationRepository locationRepo = new LocationRepository(session);
            for (Location location : locations) {
                locationRepo.create(location);
            }      
            
            //FILL THE USER TABLE   
            List<User> users = new ArrayList<User>();
            User trainee1 = new User("Trainee1", "trainee1@educom.nu", "3vDOqHO*B%5i6O@HlW", trainee, nowLD, endDateLD);
            users.add(trainee1);
            User trainee2 = new User("Trainee2", "trainee2@educom.nu", "3vDOqHO*B%5i6O@HlW", trainee, nowLD, endDateLD);
            users.add(trainee2);
            User trainee3 = new User("Trainee3", "trainee3@educom.nu", "3vDOqHO*B%5i6O@HlW", trainee, nowLD, endDateLD);
            users.add(trainee3);
            User docent1 = new User("Docent1", "docent1@educom.nu", "5^mBejfdV0Rt509x$n", docent,nowLD,endDateLD);
            users.add(docent1);
            User docent2 = new User("Docent2", "docent2@educom.nu", "5^mBejfdV0Rt509x$n", docent, nowLD,endDateLD);
            users.add(docent2);
            User userAdmin = new User("Admin", "admin@educom.nu", "AyW0BdSKojK^Uw4LRQ", admin, nowLD, endDateLD);
            users.add(userAdmin);
            User userSales = new User("Sales", "sales@educom.nu", "xA8PF&0yN*Ye5#2Vnz", sales, nowLD, endDateLD);
            users.add(userSales);
            User userOffice = new User("Office", "office@educom.nu", "eYOPEzEDq^YMlJ7$9D", office, nowLD, endDateLD);
            users.add(userOffice);    
            User adminJeffrey = new User("Jeffrey Manders", "jem@edu-deta.com", "a5G&36wOfL644ZJ!2y", admin, LocalDate.of(2017, 6, 5), endDateLD);
            users.add(adminJeffrey);
            User docentJeroen = new User("Jeroen Heemskerk", "jeroen.heemskerk@educom.nu", "Zc^CN3;qXttu[7Q{zu", docent, LocalDate.of(2018, 6, 4), endDateLD);
            users.add(docentJeroen);
            User docentGeert = new User("Geert Weggemans", "coach@man-kind.nl", "BH>C5}UXp{$y!S>E7D", docent, LocalDate.of(2015, 3, 2), endDateLD);
            users.add(docentGeert);
            User docentFrank = new User("Frank Hoens", "f.hoens@online.nl", "w%a\\EFk!#Z2y2aD6K~", docent, nowLD, endDateLD);
            users.add(docentFrank);
            User docentRene = new User("Rene Krewinkel", "rene.krewinkel@educom.nu", "BL!FF&+d2\"Bu*T*r\\R", docent, LocalDate.of(2013, 6, 3), endDateLD);
            users.add(docentRene);
            UserService userService = new UserService(session);
            for (User user : users) {
                userService.addUser(user);
            }       
            
        	//FILL THE USER-LOCATION TABLE            
			List<UserLocation> userLocations = new ArrayList<UserLocation>();
			
			UserLocation trainee1ArnhemOld = new UserLocation(trainee1, arnhem, lastWeekLd, nowLD);
			userLocations.add(trainee1ArnhemOld);		
			UserLocation trainee1Utrecht = new UserLocation(trainee1, testLocation, nowLD, endDateLD);
			userLocations.add(trainee1Utrecht);
			UserLocation trainee2ArnhemOld = new UserLocation(trainee2, arnhem, lastWeekLd, nowLD);
			userLocations.add(trainee2ArnhemOld);
			UserLocation trainee2Utrecht = new UserLocation(trainee2, testLocation, nowLD, endDateLD);
			userLocations.add(trainee2Utrecht);			
			UserLocation trainee3Utrecht = new UserLocation(trainee3, testLocation, nowLD, endDateLD);
			userLocations.add(trainee3Utrecht);			
			UserLocation docent1Testlocation = new UserLocation(docent1, testLocation, nowLD, endDateLD);
			userLocations.add(docent1Testlocation);			
			UserLocation docent2Sittard = new UserLocation(docent2, sittard, nowLD, endDateLD);
			userLocations.add(docent2Sittard);			
			UserLocation adminUtrecht = new UserLocation(userAdmin, utrecht, nowLD, endDateLD);
			userLocations.add(adminUtrecht);			
			UserLocation adminTestlocation = new UserLocation(userAdmin, testLocation, nowLD, endDateLD);
			userLocations.add(adminTestlocation);	
			UserLocation adminArnhem = new UserLocation(userAdmin, arnhem, nowLD, endDateLD);
			userLocations.add(adminArnhem);			
			UserLocation adminSittard = new UserLocation(userAdmin, sittard, nowLD, endDateLD);
			userLocations.add(adminSittard);			
			UserLocation adminEindhoven = new UserLocation(userAdmin, eindhoven, nowLD, endDateLD);
			userLocations.add(adminEindhoven);				
			UserLocation salesTestlocation = new UserLocation(userSales, testLocation, nowLD, endDateLD);
			userLocations.add(salesTestlocation);			
			UserLocation salesUtrecht = new UserLocation(userSales, utrecht, nowLD, endDateLD);
			userLocations.add(salesUtrecht);			
			UserLocation officeUtrecht = new UserLocation(userOffice, utrecht, nowLD, endDateLD);
			userLocations.add(officeUtrecht);    
			UserLocation officeArnhem = new UserLocation(userOffice, arnhem, nowLD, endDateLD);
			userLocations.add(officeArnhem);    
			UserLocation jeffreyUtrecht = new UserLocation(adminJeffrey, utrecht, nowLD, endDateLD);
			userLocations.add(jeffreyUtrecht);			
			UserLocation jeffreyArnhem = new UserLocation(adminJeffrey, arnhem, nowLD, endDateLD);
			userLocations.add(jeffreyArnhem);			
			UserLocation jeffreySittard = new UserLocation(adminJeffrey, sittard, nowLD, endDateLD);
			userLocations.add(jeffreySittard);			
			UserLocation jeffreyEindhoven = new UserLocation(adminJeffrey, eindhoven, nowLD, endDateLD);
			userLocations.add(jeffreyEindhoven);			
			UserLocation jeroenUtrecht = new UserLocation(docentJeroen, utrecht, nowLD, endDateLD);
			userLocations.add(jeroenUtrecht);			
			UserLocation geertArnhem = new UserLocation(docentGeert, arnhem, nowLD, endDateLD);
			userLocations.add(geertArnhem);			
			UserLocation reneSittard = new UserLocation(docentRene, sittard, nowLD, endDateLD);
			userLocations.add(reneSittard);			
			UserLocation frankEindhoven = new UserLocation(docentFrank, eindhoven, nowLD, endDateLD);
			userLocations.add(frankEindhoven);			
            for (UserLocation userLocation : userLocations) {
            	userService.addUserLocation(userLocation);
            }       
            
    
            //FILL THE THEME TABLE
            List<Theme> themes = new ArrayList<Theme>();
            Theme ws = new Theme("Webserver","WS","werking en configuratie van een HTTP server opzetten, Request - response principe, HTTP en HTTPS en limitaties op ongeoorloofde toegang.");
			themes.add(ws);
            Theme ide = new Theme("IDE","IDE","werken met een geavanceerde editor of geïntegreerde ontwikkelomgeving, bouwen vanuit deze omgeving, gebruik shortcuts en debuggen.");
			themes.add(ide);
            Theme fe = new Theme("Front end","FE","maken van een frontend deel van een applicatie met HTML, CSS, en Javascript. Met aandacht voor hoe het er voor de gebruiker netjes en overzichtelijk uitziet.");
			themes.add(fe);
            Theme cd = new Theme("Coding","CD","principes van een de programmeertaal de syntax, gebruik van datatypen, D.R.Y. & clean code, decompositie van functies, afhandelen van URL verzoeken, werken met bestanden.");
			themes.add(cd);
            Theme db = new Theme("Database (relationeel)","DB","gebruik van SQL om zaken in de database gedaan te krijgen, database normalisatie en opbouwen van database diagrammen.");
			themes.add(db);
            Theme api = new Theme("Connecties (API)","API","gebruik van een RESTfull API of er zelf een aanbieden.");
			themes.add(api);
            Theme pp = new Theme("Programming paradigms","PP","bekend zijn met zaken die in meerdere object georiënteerde talen terugkomen zoals design patronen als Factory en Singelton, encapulatie, static en abstract.");
			themes.add(pp);
            Theme om = new Theme("Ontwerp methodieken","OM","ontwerptechnieken die taal overschrijdend zijn zoals MVC, MVP, MVVM.");
			themes.add(om);
            Theme ps = new Theme("Problem solving","PS","maken van een ontwerp, code goed gescheiden houden, oplossen van bug en de voorspelbaarheid van de code.");
			themes.add(ps);
            Theme prj = new Theme("Werken in/aan projecten","PRJ","technieken die belangrijk zijn voor het werken aan grote projecten zoals het overzichtelijk structureren van je bestanden werken met Agile/Scrum methodiek, versiebeheer etc.");
			themes.add(prj);
            Theme bv = new Theme("Beeldvorming","BV","hoever kan je geleerde technieken meenemen naar nieuwe talen, wat zijn de overeenkomsten en de verschillen.");
			themes.add(bv);
            ThemeRepository themeRepo = new ThemeRepository(session);
            for (Theme theme : themes) {
                themeRepo.create(theme);
            }
            
            //FILL THE BUNDLE TABLE
            BundleRepository bundleRepo = new BundleRepository(session);
            
            Bundle basis = new Bundle("Basis", adminJeffrey, nowLD, endDateLD);
            bundleRepo.create(basis);
            Bundle oop = new Bundle("OOP", adminJeffrey, nowLD, endDateLD);
            bundleRepo.create(oop);
            Bundle agile = new Bundle("Agile", adminJeffrey, nowLD, endDateLD);
            bundleRepo.create(agile);
            Bundle sql = new Bundle("Advanced SQL", docentGeert, nowLD, endDateLD);
            bundleRepo.create(sql);
            Bundle oo_lang = new Bundle("2de OO-programmeertaal (Java/C#)", adminJeffrey, nowLD, endDateLD);
            bundleRepo.create(oo_lang);
            Bundle js = new Bundle("JavaScript", docentGeert, nowLD, endDateLD);
            bundleRepo.create(js);
            Bundle php = new Bundle("PHP Framework (Laravel/Symfony)", docentGeert, nowLD, endDateLD);
            bundleRepo.create(php);
            Bundle js_fw = new Bundle("JS Framework (Angular/React/Vue)", docentJeroen, nowLD, endDateLD);
            bundleRepo.create(js_fw);
            Bundle java = new Bundle("Java verdieping", docentFrank, nowLD, endDateLD);
            bundleRepo.create(java);
            Bundle csharp = new Bundle("C# verdieping", docentJeroen, nowLD, endDateLD);
            bundleRepo.create(csharp);
            Bundle app = new Bundle("App Development", docentRene, nowLD, endDateLD);
            bundleRepo.create(app);
            
            //FILL THE BUNDLECONCEPT TABLE
            BundleConceptRepository bundleConceptRepo = new BundleConceptRepository(session);
            List<BundleConcept> BundleConcepts = new ArrayList<BundleConcept>();
              
 
            //FILL THE CONCEPT TABLE
            ConceptRepository conceptRepo = new ConceptRepository(session);
            List<Concept> concepts = new ArrayList<Concept>();
            
            //week 1
            //thema webserver
            BundleConcepts.add(new BundleConcept(basis, new Concept(ws,"webserver opzetten","Het opzetten van een webserver zoals Apache",nowLD,endDateLD), 0, nowLD, endDateLD));
    		BundleConcepts.add(new BundleConcept(basis, new Concept(ws,"document root, local host","Werken met een rootfolder op een webserver",nowLD,endDateLD), 0, nowLD, endDateLD));
    		BundleConcepts.add(new BundleConcept(basis, new Concept(ws,"request/response flow","Concept van stateless request en response flow en het gebruik ervan",nowLD,endDateLD), 0, nowLD, endDateLD));
    		//thema front end
    		BundleConcepts.add(new BundleConcept(basis, new Concept(fe,"documentopbouw","Opbouw van een HTML document, gebruik van elementen, attributen etc.",nowLD,endDateLD), 0, nowLD, endDateLD));
    		BundleConcepts.add(new BundleConcept(basis, new Concept(fe,"pagina indeling","Gebruik van divisions, paragraven en lijsten",nowLD,endDateLD), 0, nowLD, endDateLD));
    		BundleConcepts.add(new BundleConcept(basis, new Concept(fe,"interaction- anchors, inputfields","Gebruik van interactievelden, anchor tags en forms with input fields",nowLD,endDateLD), 0, nowLD, endDateLD));
    		BundleConcepts.add(new BundleConcept(basis, new Concept(fe,"uiterlijk (colors, fonts, borders)","Gebruik van styling voor kleur en font in CSS",nowLD,endDateLD), 0, nowLD, endDateLD));
    		BundleConcepts.add(new BundleConcept(basis, new Concept(fe,"weergave( inline, block, margin/padding)","Gebruik van styling voor positie in CSS",nowLD,endDateLD), 0, nowLD, endDateLD));
    		//thema coding
    		BundleConcepts.add(new BundleConcept(basis, new Concept(cd,"variabelen,data types","Data omvat het opslaan, ophalen en bijwerken van waarden",nowLD,endDateLD), 0, nowLD, endDateLD));
    		BundleConcepts.add(new BundleConcept(basis, new Concept(cd,"sequenties","Een sleutelbegrip bij het programmeren is dat een bepaalde activiteit of taak wordt uitgedrukt als een reeks individuele stappen of instructies die door de computer kunnen worden uitgevoerd. Vergelijkbaar met een recept, specificeert een reeks van programmeerinstructies het gedrag of de actie die moet worden geproduceerd.",nowLD,endDateLD), 0, nowLD, endDateLD));
    		BundleConcepts.add(new BundleConcept(basis, new Concept(cd,"iteraties (loops)","Loops zijn een mechanisme die er voor zorgt dat een bepaalde sequentie meerdere keren doorlopen wordt",nowLD,endDateLD), 0, nowLD, endDateLD));
    		BundleConcepts.add(new BundleConcept(basis, new Concept(cd,"functies en scope","Delegeren van funcitionaliteit naar functies, het gebruik van beperkte scope van variabelen",nowLD,endDateLD), 0, nowLD, endDateLD));
    		BundleConcepts.add(new BundleConcept(basis, new Concept(cd,"conditionals (if/else, switch)","Conditionals beslaat het vermogen om beslissingen te nemen op basis van bepaalde voorwaarden. Hierdoor kan er afhankelijk van een voorwaarde verschillende resultaten uitgedrukt worden.",nowLD,endDateLD), 0, nowLD, endDateLD));
    		BundleConcepts.add(new BundleConcept(basis, new Concept(cd,"operators","Operators bieden ondersteuning om wiskundige, logische en reeksen te manipuleren.",nowLD,endDateLD), 0, nowLD, endDateLD));
    		//thema problem solving
    		BundleConcepts.add(new BundleConcept(basis, new Concept(ps,"clean code","Het opbreken van een probleem in een nette structuur van code files die door anderen te volgen is.",nowLD,endDateLD), 0, nowLD, endDateLD));
    		//thema werken in/ aan projecten
    		BundleConcepts.add(new BundleConcept(basis, new Concept(prj,"coding styles ","Consistente code style (hoofdlettergebruik, snake_case of camelCase, tabs of spaties etc.) voor al je bestanden",nowLD,endDateLD), 0, nowLD, endDateLD));
    		BundleConcepts.add(new BundleConcept(basis, new Concept(prj,"file structuur","Consistentie tussen de naam van een file en de functies/classen in de file",nowLD,endDateLD), 0, nowLD, endDateLD));
    		BundleConcepts.add(new BundleConcept(basis, new Concept(prj,"programma structuur","Is de structuur van het programma goed opgezet en volgt dit een voorspelbaar en logisch pad",nowLD,endDateLD), 0, nowLD, endDateLD));
    		
    		// week 2
    		//thema IDE
    		BundleConcepts.add(new BundleConcept(basis, new Concept(ide,"code omgeving basis","Het opzetten van een omgeving om binnen te kunnen programmeren",nowLD,endDateLD), 1, nowLD, endDateLD));
    		//thema coding
    		BundleConcepts.add(new BundleConcept(basis, new Concept(cd,"POST-afhandeling","Het veilig extracten van data van een POST request",nowLD,endDateLD), 1, nowLD, endDateLD));
    		BundleConcepts.add(new BundleConcept(basis, new Concept(cd,"URL parameters","Het veilig extracten van data van een GET request",nowLD,endDateLD), 1, nowLD, endDateLD));
    		BundleConcepts.add(new BundleConcept(basis, new Concept(cd,"file handling(read/write)","Het gebruiken van Input- en Outputfiles (I/O)",nowLD,endDateLD), 1, nowLD, endDateLD));
    		BundleConcepts.add(new BundleConcept(basis, new Concept(cd,"bron code organisatie","Het verdelen van de code over meerdere files en deze op het juiste moment includen",nowLD,endDateLD), 1, nowLD, endDateLD));
    		BundleConcepts.add(new BundleConcept(basis, new Concept(cd,"herbruikbare code","Het generiek opzetten van functies die bij meerdere projecten bruikbaar zijn",nowLD,endDateLD), 1, nowLD, endDateLD));
    		BundleConcepts.add(new BundleConcept(basis, new Concept(cd,"debuggen","De techniek van het zoeken naar fouten en het volgen van de flow van het programma",nowLD,endDateLD), 1, nowLD, endDateLD));
    		BundleConcepts.add(new BundleConcept(basis, new Concept(cd,"scheiden van code","Het scheiden van code in lagen (Tier-3)",nowLD,endDateLD), 1, nowLD, endDateLD));
    		BundleConcepts.add(new BundleConcept(basis, new Concept(cd,"dry code","Don't Repeat Yourself - codeblokken die (bijna) identiek zijn herkennen en op 1 plek in de code neerleggen",nowLD,endDateLD), 1, nowLD, endDateLD));
    		//thema Database (relationeel)
    		BundleConcepts.add(new BundleConcept(basis, new Concept(db,"SQL queries (Select, Insert, Update, Where)","Het opbouwen van SQL queries met commando's als SELECT FROM, INSERT INTO, DELETE, UPDATE & WHERE-clausules",nowLD,endDateLD), 1, nowLD, endDateLD));
    		BundleConcepts.add(new BundleConcept(basis, new Concept(db,"normalisatie en databasebouw","Het opbouwen van relaties in een SQL database en kennis van koppeltabellen",nowLD,endDateLD), 1, nowLD, endDateLD));
    		//thema beeldvorming
    		BundleConcepts.add(new BundleConcept(basis, new Concept(bv,"verschilllen en overeenkomsten talen","Herkennen van de overeenkomsten en de verschillen tussen programmeertalen",nowLD,endDateLD), 1, nowLD, endDateLD));
    		
    		// week 3
    		//thema frondend
    		BundleConcepts.add(new BundleConcept(basis, new Concept(fe,"wireframe voor GUI design","Maken van een wireframe van een GUI",nowLD,endDateLD), 2, nowLD, endDateLD));
    		BundleConcepts.add(new BundleConcept(basis, new Concept(fe,"framework","Werken met een framework (bootstrap, javascript)",nowLD,endDateLD), 2, nowLD, endDateLD));		
    		//thmema coding
    		BundleConcepts.add(new BundleConcept(basis, new Concept(cd,"multiparts forms","Advanced formulieren gebruik zoals uploaden van images",nowLD,endDateLD), 2, nowLD, endDateLD));
    		BundleConcepts.add(new BundleConcept(basis, new Concept(cd,"sessions en connections","Gebruik van sessies om te herinneren welke user welke actie doet",nowLD,endDateLD), 2, nowLD, endDateLD));
    		//thema problem solving
    		BundleConcepts.add(new BundleConcept(basis, new Concept(ps,"algorithme ontwerp","ontwerp van een complexer algoritme dat je niet meer in je hoofd kan doen",nowLD,endDateLD), 2, nowLD, endDateLD));
    		BundleConcepts.add(new BundleConcept(basis, new Concept(ps,"plan van aanpak (divide en concure)","Opbreken van een opdracht in sub-onderdelen en deze verder uitwerken",nowLD,endDateLD), 2, nowLD, endDateLD));
    		//thema Database (relationeel)
    		BundleConcepts.add(new BundleConcept(basis, new Concept(db,"SQL join, order en group by","Het maken van grotere queries met commando's als JOIN, GROUP BY, ORDER BY etc.",nowLD,endDateLD), 2, nowLD, endDateLD));

    		// week 4
    		//thema coding
    		BundleConcepts.add(new BundleConcept(basis, new Concept(cd,"defensief programmeren","Bescherming tegen cross-scripting (XSS) attacks, overflow attacks en database modificatie (SQL injection) aanvallen",nowLD,endDateLD), 3, nowLD, endDateLD));
    		BundleConcepts.add(new BundleConcept(basis, new Concept(cd,"associative arrays","gebruik van associative array's zoals map en dictionary",nowLD,endDateLD), 3, nowLD, endDateLD));
    		BundleConcepts.add(new BundleConcept(basis, new Concept(cd,"error handling (try, catch & finally)","Gebruik van excepties om foutsituaties zoals database problemen te tackelen",nowLD,endDateLD), 3, nowLD, endDateLD));
    		BundleConcepts.add(new BundleConcept(basis, new Concept(cd,"error logging","Een concistente manier van het loggen van foutsituaties",nowLD,endDateLD), 3, nowLD, endDateLD));
    		
    		// bundle OOP
    		//thema programming paradigms
    		BundleConcepts.add(new BundleConcept(oop, new Concept(pp,"OOP (objecten, methods, properties)","Kennis van verschil tussen objecten en classen; private, protected en public methoden en properties",nowLD,endDateLD), 0, nowLD, endDateLD));
    		BundleConcepts.add(new BundleConcept(oop, new Concept(pp,"OOP (encapsulation, polymorphism)","Encapsulatie van data, overerven van classen",nowLD,endDateLD), 0, nowLD, endDateLD));
    		BundleConcepts.add(new BundleConcept(oop, new Concept(pp,"OOP (abstract Methods en classes)","Abstracte methodes en classen maken en correct gebruiken",nowLD,endDateLD), 0, nowLD, endDateLD));
    		BundleConcepts.add(new BundleConcept(oop, new Concept(pp,"OOP (static Methods en properties)","Statische methodes en classen maken en correct gebruiken",nowLD,endDateLD), 0, nowLD, endDateLD));
    		//thema ontwerp methodieken
    		BundleConcepts.add(new BundleConcept(oop, new Concept(om,"MVC","Model-View-Controller structuur in de code aanbrengen",nowLD,endDateLD), 1, nowLD, endDateLD));
    		//thema programming paradigms
    		BundleConcepts.add(new BundleConcept(oop, new Concept(pp,"Design Patterns (Factory)","Kennis hebben van design patronen zoals het factory patroon",nowLD,endDateLD), 2, nowLD, endDateLD));
    		//thema coding
    		BundleConcepts.add(new BundleConcept(basis, new Concept(cd,"herbruikbare classen","Classen kunnen hergebruiken over projecten heen, bijv. generieke C.R.U.D. class",nowLD,endDateLD), 2, nowLD, endDateLD));
    		
    		// bundle agile
    		//thema werken in/ aan projecten
    		BundleConcepts.add(new BundleConcept(agile, new Concept(prj,"project management (agile/scrum)","Projectmatig werken, taken verdelen onder teamleden, overleggen met teamleden",nowLD,endDateLD), 0, nowLD, endDateLD));
    		BundleConcepts.add(new BundleConcept(agile, new Concept(prj,"versiebeheer (GIT/SVN)","Werken met een versiebeheersysteem om code te delen of meerdere branches te maken",nowLD,endDateLD), 0, nowLD, endDateLD));

    		// bundle SQL advanced
    		BundleConcepts.add(new BundleConcept(sql, new Concept(db,"SQL queries (join, having)","Gebruiken van meer advanced SQL commando's zoals inner- en outer- JOIN en HAVING",nowLD,endDateLD), 0, nowLD, endDateLD));
    		BundleConcepts.add(new BundleConcept(sql, new Concept(db,"nested / sub- queries","Gebruiken van meer advanced SQL commando's zoals nested queries of subqueries in de WHERE-clause",nowLD,endDateLD), 0, nowLD, endDateLD));
    		BundleConcepts.add(new BundleConcept(sql, new Concept(db,"SQL transactions (ROLLBACK en COMMIT)","Gebruik van transacties om een groep SQL commando's allemaal wel of allemaal niet uit te voeren",nowLD,endDateLD), 0, nowLD, endDateLD));

    		// Bundle OO-programmeertaal (zoals Java, C#, Laravel)
    		//thema connecties (API)
    		Concept api_use = new Concept(api,"API's gebruiken","Gebruik van applicatie programeer interfaces (API's)",nowLD,endDateLD);
    		Concept api_create = new Concept(api,"API's bouwen","Het bouwen van een (RestFull) API",nowLD,endDateLD);

    		BundleConcepts.add(new BundleConcept(oo_lang, api_use, 0, nowLD, endDateLD));		
    		BundleConcepts.add(new BundleConcept(oo_lang, api_create, 2, nowLD, endDateLD));		
    		//thema IDE
    		Concept ide_use = new Concept(ide,"IDE gebruiken","Code schrijven in een geintergreerde development environment als eclipse, netbeans of visual-studio",nowLD,endDateLD);
			BundleConcepts.add(new BundleConcept(oo_lang, ide_use, 0, nowLD, endDateLD));		
    		//thema coding
    		BundleConcepts.add(new BundleConcept(oo_lang, new Concept(cd,"2e taal al het voorgaande & OOP","Gebruik sequences, functies, operatoren, clean-code, scheiden van code etc. voor de tweede taal",nowLD,endDateLD), 0, nowLD, endDateLD));
    		BundleConcepts.add(new BundleConcept(oo_lang, new Concept(cd,"Event handling","Een actie of gebeurtenis die er voor zorgt dat er iets anders gebeurd. Bijvoorbeeld het downloaden van een file doormiddel van een knop.",nowLD,endDateLD), 1, nowLD, endDateLD));		
    		
			// Bundel JavaScript
    		BundleConcepts.add(new BundleConcept(js, api_use, 0, nowLD, endDateLD));		
    		//thema front end
    		BundleConcepts.add(new BundleConcept(js, new Concept(fe,"JavaScript Front End","Maken van scripts voor de frontend",nowLD,endDateLD), 0, nowLD, endDateLD));
    		BundleConcepts.add(new BundleConcept(js, new Concept(fe,"jQuery & AJAX","Vanuit frontend scripts calls maken naar API functies",nowLD,endDateLD), 0, nowLD, endDateLD));
    		//-----
			BundleConcepts.add(new BundleConcept(js, api_create, 1, nowLD, endDateLD));		
    		//thema front end
			BundleConcepts.add(new BundleConcept(js, new Concept(fe,"Template engine","Pagina's volgens 'templates' opbouwen en deze uitserveren",nowLD,endDateLD), 1, nowLD, endDateLD));
			//thema coding
			BundleConcepts.add(new BundleConcept(js, new Concept(cd,"Javascript verdieping","Gebruikt van zaken die specifiek voor JavaScript zijn zoals promise etc.",nowLD,endDateLD), 2, nowLD, endDateLD));
    		//thema programming paradigms
			BundleConcepts.add(new BundleConcept(js, new Concept(pp,"Design Patterns","Gebruik van design patterns in javascript",nowLD,endDateLD), 2, nowLD, endDateLD));

			// Bundel PHP
    		//thema coding
    		BundleConcepts.add(new BundleConcept(php, new Concept(cd,"PHP Framework (Laravel, Symfony)","Gebruik van een PHP framework zoals Zend, Laravel en Symfony",nowLD,endDateLD), 0, nowLD, endDateLD));
    		BundleConcepts.add(new BundleConcept(php, new Concept(cd,"ORM, Fluent en migratie-scripts","Gebruik van object relation model binnen framework",nowLD,endDateLD), 1, nowLD, endDateLD));
			
    		// Bundle JS Framework
			//thema front end
    		BundleConcepts.add(new BundleConcept(js_fw, new Concept(fe,"Front-end Frameworks","Gebruik van JavaScript frameworks zoals Angular, React, Vue",nowLD,endDateLD), 0, nowLD, endDateLD));

    		// Bundle C#
    		//thema coding
    		BundleConcepts.add(new BundleConcept(csharp, ide_use, 0, nowLD, endDateLD));
			BundleConcepts.add(new BundleConcept(csharp, new Concept(cd,"LINQ","Gebruikt van LINQ queries om data uit lijsten te filteren.",nowLD,endDateLD), 1, nowLD, endDateLD));
			BundleConcepts.add(new BundleConcept(csharp, new Concept(cd,"Entity Framework","Gebruikt van ORM om data uit de database te krijgen (i.c.m. LINQ queries).",nowLD,endDateLD), 1, nowLD, endDateLD));
    		BundleConcepts.add(new BundleConcept(csharp, new Concept(cd,"ASP.NET MVC","Gebruik van het ASP.NET framework om een MVC applicatie te maken",nowLD,endDateLD), 2, nowLD, endDateLD));

    		// Bundle Java
    		//thema coding
    		BundleConcepts.add(new BundleConcept(java, ide_use, 0, nowLD, endDateLD));
    		BundleConcepts.add(new BundleConcept(java, new Concept(cd,"Hibernate","Gebruikt van ORM om data uit de database te krijgen",nowLD,endDateLD), 1, nowLD, endDateLD));
			BundleConcepts.add(new BundleConcept(java, new Concept(cd,"Java Framework","Gebruik van Java frameworks zoals Java Spring of J2EE om een webapplicatie te maken",nowLD,endDateLD), 2, nowLD, endDateLD));

    		// Bundle App Development
			BundleConcepts.add(new BundleConcept(app, ide_use, 0, nowLD, endDateLD));
			BundleConcepts.add(new BundleConcept(app, new Concept(fe,"Advanced UI","Create a Responsive UI",nowLD,endDateLD), 0, nowLD, endDateLD));
			
    		// Extract all used concepts
			for (BundleConcept bundleConcept : BundleConcepts) {
				if (!concepts.contains(bundleConcept.getConcept())) {
                   concepts.add(bundleConcept.getConcept());
				}
            }
			
			// Concepten zonder bundel (los bij te vinken)
    		//thema Database
    		concepts.add(new Concept(db,"kennis van no-sql","Kennis van een NO-SQL database (niet relationeel) zoals MongoDB en hoe daar data in en uit te halen",nowLD,endDateLD));    		
    		//thema programming paradigms
    		concepts.add(new Concept(pp,"Design Patterns (Dependency Injection)","Kennis hebben van design pattronen zoals het factory patroon",nowLD,endDateLD));
    		concepts.add(new Concept(pp,"Design Patterns (ObjectPool)","Kennis hebben van design patronen zoals het object pool patroon",nowLD,endDateLD));
    		concepts.add(new Concept(pp,"Design Patterns (Builder)","Kennis hebben van design patronen zoals het builder patroon",nowLD,endDateLD));
    		concepts.add(new Concept(pp,"Design Patterns (Observer)","Kennis hebben van design patronen zoals het observer patroon",nowLD,endDateLD));
    		concepts.add(new Concept(pp,"Design Patterns (Singleton)","Kennis hebben van design patronen zoals het singleton patroon",nowLD,endDateLD));
    		concepts.add(new Concept(pp,"Design Patterns (Producer/Consumer)","Kennis hebben van design patronen zoals het producer/consumer patroon",nowLD,endDateLD));
    		//thema ontwerp methodieken
    		concepts.add(new Concept(om,"MVP","Model-View-Presentor structuur in de code aanbrengen",nowLD,endDateLD));
    		concepts.add(new Concept(om,"MVVM","Model-View-ViewModel structuur in de code aanbrengen",nowLD,endDateLD));
    		//thema werken in/ aan projecten
    		concepts.add(new Concept(prj,"deployment on Linux","Het kunnen deployen van een applicatie op een Linux server",nowLD,endDateLD));
             
    		for (Concept concept : concepts) {
    			conceptRepo.create(concept);
    		}
            for (BundleConcept bundleConcept : BundleConcepts) {
                bundleConceptRepo.create(bundleConcept);
            }  
            //MOCK DATA    
            
            //FILL THE REVIEW TABLE
            ReviewRepository reviewRepo = new ReviewRepository(session);
            Review review1 = new Review(weekAgo, "Matig bezig trainee1", "Deze trainee is meh bezig", Review.Status.COMPLETED, trainee1);
            Review review2 = new Review(dayAgo, "Redelijk bezig trainee1", "Deze trainee is voldoende bezig", Review.Status.COMPLETED, trainee1);
            Review review3 = new Review(nowLDT, "Goed bezig trainee1", "Deze trainee is prima bezig", Review.Status.PENDING, trainee1);
            Review review4 = new Review(dayAgo, "Goed bezig trainee", "Deze trainee is prima bezig", Review.Status.COMPLETED, trainee2);
            reviewRepo.create(review1);
            reviewRepo.create(review2);
            reviewRepo.create(review3);
            reviewRepo.create(review4);
            
            //FILL THE CONCEPT-RATING TABLE
            List<ConceptRating> conceptsRatings = new ArrayList<ConceptRating>();
            conceptsRatings.add(new ConceptRating(review1,concepts.get(0),2, "Kan beter",true));
            conceptsRatings.add(new ConceptRating(review1,concepts.get(1),2, "Kan beter",false));
            conceptsRatings.add(new ConceptRating(review1,concepts.get(2),2, "Kan beter",false));
            conceptsRatings.add(new ConceptRating(review1,concepts.get(3),2, "Kan beter",false));
            conceptsRatings.add(new ConceptRating(review1,concepts.get(4),2, "Kan beter",false));
                    
            conceptsRatings.add(new ConceptRating(review2,concepts.get(1),3, "Goed, maar komop",false));
            conceptsRatings.add(new ConceptRating(review2,concepts.get(2),3, "Je komt er wel",true));
            conceptsRatings.add(new ConceptRating(review2,concepts.get(3),3, "Je moet iets meer letten op je stijl",false));
            conceptsRatings.add(new ConceptRating(review2,concepts.get(4),3, "no comment",false));
            conceptsRatings.add(new ConceptRating(review2,concepts.get(5),3,false));
            
            conceptsRatings.add(new ConceptRating(review3,concepts.get(0),5,false));
            conceptsRatings.add(new ConceptRating(review3,concepts.get(1),5,false));
            conceptsRatings.add(new ConceptRating(review3,concepts.get(2),5,false));
            conceptsRatings.add(new ConceptRating(review3,concepts.get(3),5,false));
            conceptsRatings.add(new ConceptRating(review3,concepts.get(4),5,false));
            conceptsRatings.add(new ConceptRating(review3,concepts.get(5),5,false));
            
            conceptsRatings.add(new ConceptRating(review4,concepts.get(0),4,false));
            conceptsRatings.add(new ConceptRating(review4,concepts.get(1),4,false));
            conceptsRatings.add(new ConceptRating(review4,concepts.get(2),4,false));
            conceptsRatings.add(new ConceptRating(review4,concepts.get(3),4,false));
            conceptsRatings.add(new ConceptRating(review4,concepts.get(4),4,false));
            conceptsRatings.add(new ConceptRating(review4,concepts.get(5),4,true));
            ConceptRatingRepository conceptRatingRepo = new ConceptRatingRepository(session);
            for (ConceptRating conceptRating : conceptsRatings) {
                conceptRatingRepo.create(conceptRating);
            }    
          
            //FILL THE BUNDLETRAINEE TABLE
            BundleTraineeRepository bundleTraineeRepo = new BundleTraineeRepository(session);
    
            BundleTrainee basisTrainee1 = new BundleTrainee(trainee1, basis, 1, nowLD, endDateLD);
            BundleTrainee basisTrainee2 = new BundleTrainee(trainee2, basis, 1, nowLD, endDateLD);
            BundleTrainee oopTrainee2 = new BundleTrainee(trainee2, oop, 4, nowLD, endDateLD);
    
            bundleTraineeRepo.create(basisTrainee1);
            bundleTraineeRepo.create(basisTrainee2);
            bundleTraineeRepo.create(oopTrainee2);
            session.getTransaction().commit();
        } catch (DatabaseException e) {
            LOG.error("Filling database failed", e);
        }
    }
}
