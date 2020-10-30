package nu.educom.rvt.rest;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import nu.educom.rvt.models.*;
import nu.educom.rvt.repositories.*;
import nu.educom.rvt.services.UserService;

public class Filler {
	/* JH: Gebruik geen <tab> karakters in de database */
	public static Boolean isDatabaseEmpty() {
		RoleRepository rolesRepo = new RoleRepository();
		List<Role> roles = rolesRepo.readAll();
		return roles.size() <= 0 ;
	}
	
	public static void fillDatabase() {
		
		LocalDate weekAgo = LocalDate.now().minus(7, ChronoUnit.DAYS);
		LocalDate dayAgo = LocalDate.now().minus(1, ChronoUnit.DAYS);
	    LocalDate now = LocalDate.now();
		LocalDate endDate = null;
		
		//FILL THE USER TABLE
		List<Role> roles = new ArrayList<Role>();
		roles.add(new Role("Admin"));
		roles.add(new Role("Docent"));
		roles.add(new Role("Trainee"));
		roles.add(new Role("Sales"));
		roles.add(new Role("Office"));
		RoleRepository roleRepo = new RoleRepository();
		for (Role role : roles) {
			roleRepo.create(role);
		}
		
		//FILL THE LOCATION TABLE
		List<Location> locations = new ArrayList<Location>();
		locations.add(new Location("Utrecht"));
		locations.add(new Location("Arnhem"));
		locations.add(new Location("Sittard"));
		locations.add(new Location("Eindhoven"));
		LocationRepository locationRepo = new LocationRepository();
		for (Location location : locations) {
			locationRepo.create(location);
		}
		

		User trainee1 = new User("Trainee", "trainee1@educom.nu", "3vDOqHO*B%5i6O@HlW", roles.get(2), locations.get(0),now,endDate);
		User trainee2 = new User("Trainee", "trainee2@educom.nu", "3vDOqHO*B%5i6O@HlW", roles.get(2), locations.get(0),now,endDate);
		User trainee3 = new User("Trainee", "trainee3@educom.nu", "3vDOqHO*B%5i6O@HlW", roles.get(2), locations.get(0),now,endDate);
		
		//add users to db
		List<User> users = new ArrayList<User>();
		users.add(new User("Admin", "admin@educom.nu", "AyW0BdSKojK^Uw4LRQ", roles.get(0), locations.get(0),now,endDate));
		users.add(new User("Admin", "jem@edu-deta.com", "a5G&36wOfL644ZJ!2y", roles.get(4), locations.get(0),now,endDate));
		users.add(new User("Docent", "docent@educom.nu", "5^mBejfdV0Rt509x$n", roles.get(1), locations.get(0),now,endDate));
		users.add(new User("Sales", "sales@educom.nu", "xA8PF&0yN*Ye5#2Vnz", roles.get(3), locations.get(0),now,endDate));
		users.add(new User("Office", "office@educom.nu", "eYOPEzEDq^YMlJ7$9D", roles.get(4), locations.get(0),now,endDate));
		
		users.add(trainee1);
		users.add(trainee2);
		users.add(trainee3);
		UserService userService = new UserService();
		for (User user : users) {
			userService.addUser(user);
		}		
		
		//FILL THE THEME TABLE
		List<Theme> themes = new ArrayList<Theme>();
		themes.add(new Theme("Webserver","WS","werking en configuratie van een HTTP server opzetten, Request - response principe, HTTP en HTTPS en limitaties op ongeoorloofde toegang."));
		themes.add(new Theme("IDE","IDE","werken met een geavanceerde editor of geïntegreerde ontwikkelomgeving, bouwen vanuit deze omgeving, gebruik shortcuts en debuggen."));
		themes.add(new Theme("Front end","FE","maken van een frontend deel van een applicatie met HTML, CSS, en Javascript. Met aandacht voor hoe het er voor de gebruiker netjes en overzichtelijk uitziet."));
		themes.add(new Theme("Coding","CD","principes van een de programmeertaal de syntax, gebruik van datatypen, D.R.Y. & clean code, decompositie van functies, afhandelen van URL verzoeken, werken met bestanden."));
		themes.add(new Theme("Database (relationeel)","DB","gebruik van SQL om zaken in de database gedaan te krijgen, database normalisatie en opbouwen van database diagrammen."));
		themes.add(new Theme("Connecties (API)","API","gebruik van een RESTfull API of er zelf een aanbieden."));
		themes.add(new Theme("Programming paradigms","PP","bekend zijn met zaken die in meerdere object georiënteerde talen terugkomen zoals design patronen als Factory en Singelton, encapulatie, static en abstract."));
		themes.add(new Theme("Ontwerp methodieken","OM","ontwerptechnieken die taal overschrijdend zijn zoals MVC, MVP, MVVM."));
		themes.add(new Theme("Problem solving","PS","maken van een ontwerp, code goed gescheiden houden, oplossen van bug en de voorspelbaarheid van de code."));
		themes.add(new Theme("Werken in/ aan projecten","PRJ.","technieken die belangrijk zijn voor het werken aan grote projecten zoals het overzichtelijk structureren van je bestanden werken met Agile/Scrum methodiek, versiebeheer etc."));
		themes.add(new Theme("Beeldvorming","BV","hoever kan je geleerde technieken meenemen naar nieuwe talen, wat zijn de overeenkomsten en de verschillen."));
		ThemeRepository themeRepo = new ThemeRepository();
		for (Theme theme : themes) {
			themeRepo.create(theme);
		}

		//FILL THE CONCEPT TABLE
		List<Concept> concepts = new ArrayList<Concept>();
		//thema webserver
		//week 1&2
		concepts.add(new Concept(themes.get(0),"webserver opzetten",null,1,now,endDate));
		concepts.add(new Concept(themes.get(0),"document root, local host",null,1,now,endDate));
		concepts.add(new Concept(themes.get(0),"request/response flow",null,1,now,endDate));
		//thema IDE
		//week 1&2
		concepts.add(new Concept(themes.get(1),"code omgeving basis",null,2,now,endDate));
		//thema front end
		//week 1&2
		concepts.add(new Concept(themes.get(2),"documentopbouw",null,1,now,endDate));
		concepts.add(new Concept(themes.get(2),"pagina indeling (divisions, paragraphs, lists)",null,1,now,endDate));
		concepts.add(new Concept(themes.get(2),"interaction- anchors, forms/inputfields",null,1,now,endDate));
		concepts.add(new Concept(themes.get(2),"uiterlijk (colors, fonts, borders)",null,1,now,endDate));
		concepts.add(new Concept(themes.get(2),"weergave( inline, block, margin/padding)",null,1,now,endDate));
		//week 3&4
		concepts.add(new Concept(themes.get(2),"wireframe voor GUI design",null,3,now,endDate));
		concepts.add(new Concept(themes.get(2),"framework",null,3,now,endDate));		
		//week5&6
		concepts.add(new Concept(themes.get(2),"Template Enige",null,6,now,endDate));
		concepts.add(new Concept(themes.get(2),"JavaScript Front End",null,5,now,endDate));
		concepts.add(new Concept(themes.get(2),"AJAX",null,5,now,endDate));
		//week 7 t/m 9
		concepts.add(new Concept(themes.get(2),"Advanced UI",null,8,now,endDate));
		//week 10 t/m 12
		concepts.add(new Concept(themes.get(2),"Front-end Frameworks",null,11,now,endDate));
		//thema coding
		//week 1&2
		concepts.add(new Concept(themes.get(3),"variabelen,data types",null,1,now,endDate));
		concepts.add(new Concept(themes.get(3),"iteraties (loops), Selecties (if/else, switch)",null,1,now,endDate));
		concepts.add(new Concept(themes.get(3),"Functies en Scope",null,1,now,endDate));
		concepts.add(new Concept(themes.get(3),"sequenties",null,1,now,endDate));
		concepts.add(new Concept(themes.get(3),"Post-afhandeling",null,1,now,endDate));
		concepts.add(new Concept(themes.get(3),"URL parameters",null,2,now,endDate));
		concepts.add(new Concept(themes.get(3),"File Handling(read/write)",null,2,now,endDate));
		concepts.add(new Concept(themes.get(3),"bron code organisatie",null,2,now,endDate));
		concepts.add(new Concept(themes.get(3),"herbruikbare code",null,2,now,endDate));
		concepts.add(new Concept(themes.get(3),"debuggen",null,2,now,endDate));
		concepts.add(new Concept(themes.get(3),"clean code",null,2,now,endDate));
		concepts.add(new Concept(themes.get(3),"dry code",null,2,now,endDate));
		//week 3&4
		concepts.add(new Concept(themes.get(3),"multiparts forms",null,4,now,endDate));
		concepts.add(new Concept(themes.get(3),"$_post + $_files",null,4,now,endDate));
		concepts.add(new Concept(themes.get(3),"sessions en connections",null,4,now,endDate));
		concepts.add(new Concept(themes.get(3),"defensief programmeren",null,4,now,endDate));
		concepts.add(new Concept(themes.get(3),"map en dictionary",null,4,now,endDate));
		concepts.add(new Concept(themes.get(3),"error handling (try, catch & finally)",null,4,now,endDate));
		concepts.add(new Concept(themes.get(3),"error logging",null,4,now,endDate));
		//week 5&6
		concepts.add(new Concept(themes.get(3),"frameworks",null,6,now,endDate));
		//week 7 t/m 9
		concepts.add(new Concept(themes.get(3),"2e taal al het voorgaande & OOP",null,7,now,endDate));
		//week 10 t/m 12
		concepts.add(new Concept(themes.get(3),"2e taal verdieping",null,10,now,endDate));		
		//thema Database (relationeel)
		//week 1&2
		concepts.add(new Concept(themes.get(4),"normalisatie en databasebouw",null,2,now,endDate));
		concepts.add(new Concept(themes.get(4),"queries (Select, where, group by)",null,2,now,endDate));
		//week 3&4
		concepts.add(new Concept(themes.get(4),"queries (join, having)",null,3,now,endDate));
		concepts.add(new Concept(themes.get(4),"nested / sub- queries",null,3,now,endDate));
		//week 7 t/m 9
		concepts.add(new Concept(themes.get(4),"transactions (ROLLBACK en COMMIT)",null,9,now,endDate));
		//week 10 t/m 12
		concepts.add(new Concept(themes.get(4),"kennis van no-sql",null,11,now,endDate));
		//thema connecties (API)
		//week 3&4
		concepts.add(new Concept(themes.get(5),"API's gebruiken",null,4,now,endDate));		
		//week 5&6
		concepts.add(new Concept(themes.get(5),"API's bouwen",null,5,now,endDate));		
		//week 7 t/m 9
		concepts.add(new Concept(themes.get(5),"API's bouwen",null,9,now,endDate));
		//thema programming paradigms
		//week 1&2
		concepts.add(new Concept(themes.get(6),"OOP (objecten, methods, properties)",null,2,now,endDate));
		//week 3&4
		concepts.add(new Concept(themes.get(6),"OOP (encapsulation, polymorphism)",null,4,now,endDate));
		concepts.add(new Concept(themes.get(6),"OOP (abstract Methods en classes)",null,4,now,endDate));
		concepts.add(new Concept(themes.get(6),"OOP (static Methods en properties)",null,4,now,endDate));
		//week 5&6
		concepts.add(new Concept(themes.get(6),"Design Patterns (PHP)",null,6,now,endDate));
		//week 7 t/m 9
		concepts.add(new Concept(themes.get(6),"Design Patterns (JS)",null,7,now,endDate));
		//thema ontwerp methodieken
		//week 7 t/m 9
		concepts.add(new Concept(themes.get(7),"MVC",null,7,now,endDate));
		concepts.add(new Concept(themes.get(7),"MVVM",null,9,now,endDate));
		concepts.add(new Concept(themes.get(7),"MPC",null,9,now,endDate));
		//thema problem solving
		//week 1&2
		concepts.add(new Concept(themes.get(8),"algorithme ontwerp",null,2,now,endDate));
		concepts.add(new Concept(themes.get(8),"clean code",null,1,now,endDate));
		//week 3&4
		concepts.add(new Concept(themes.get(8),"algorithme ontwerp",null,3,now,endDate));
		concepts.add(new Concept(themes.get(8),"algorithme ontwerp",null,3,now,endDate));
		//thema werken in/ aan projecten
		//week 1&2
		concepts.add(new Concept(themes.get(9),"coding styles ",null,1,now,endDate));
		concepts.add(new Concept(themes.get(9),"file structuur",null,1,now,endDate));
		concepts.add(new Concept(themes.get(9),"programma structuur",null,1,now,endDate));
		//week 3&4
		concepts.add(new Concept(themes.get(9),"project management",null,4,now,endDate));
		concepts.add(new Concept(themes.get(9),"versiebeheer",null,4,now,endDate));
		//week 7 t/m 9
		concepts.add(new Concept(themes.get(9),"deployment",null,8,now,endDate));
		//thema beeldvorming
		//week 1&2
		concepts.add(new Concept(themes.get(10),"verschilllen en overeenkomsten talen",null,2,now,endDate));
		ConceptRepository conceptRepo = new ConceptRepository();
		for (Concept concept : concepts) {
			conceptRepo.create(concept);
		}	

		//MOCK DATA	
		
		//FILL THE REVIEW TABLE
		ReviewRepository reviewRepo = new ReviewRepository();
		Review review1 = new Review(weekAgo, "Matig bezig trainee1", "Deze trainee is meh bezig", Review.Status.COMPLETED, trainee1);
		Review review2 = new Review(dayAgo, "Redelijk bezig trainee1", "Deze trainee is voldoende bezig", Review.Status.COMPLETED, trainee1);
		Review review3 = new Review(now, "Goed bezig trainee1", "Deze trainee is prima bezig", Review.Status.PENDING, trainee1);
		Review review4 = new Review(dayAgo, "Goed bezig trainee", "Deze trainee is prima bezig", Review.Status.COMPLETED, trainee2);
		reviewRepo.create(review1);
		reviewRepo.create(review2);
		reviewRepo.create(review3);
		reviewRepo.create(review4);
		
		//FILL THE CONCEPT-RATING TABLE
		List<ConceptRating> conceptsRatings = new ArrayList<ConceptRating>();
		conceptsRatings.add(new ConceptRating(review1,concepts.get(0),2, "Kan beter"));
		conceptsRatings.add(new ConceptRating(review1,concepts.get(1),2, "Kan beter"));
		conceptsRatings.add(new ConceptRating(review1,concepts.get(2),2, "Kan beter"));
		conceptsRatings.add(new ConceptRating(review1,concepts.get(3),2, "Kan beter"));
		conceptsRatings.add(new ConceptRating(review1,concepts.get(4),2, "Kan beter"));
				
		conceptsRatings.add(new ConceptRating(review2,concepts.get(1),3, "Goed, maar komop"));
		conceptsRatings.add(new ConceptRating(review2,concepts.get(2),3, "Je komt er wel"));
		conceptsRatings.add(new ConceptRating(review2,concepts.get(3),3, "Je moet iets meer letten op je stijl"));
		conceptsRatings.add(new ConceptRating(review2,concepts.get(4),3, "no comment"));
		conceptsRatings.add(new ConceptRating(review2,concepts.get(5),3));
		
		conceptsRatings.add(new ConceptRating(review3,concepts.get(0),4));
		conceptsRatings.add(new ConceptRating(review3,concepts.get(1),4));
		conceptsRatings.add(new ConceptRating(review3,concepts.get(2),4));
		conceptsRatings.add(new ConceptRating(review3,concepts.get(3),4));
		conceptsRatings.add(new ConceptRating(review3,concepts.get(4),4));
		conceptsRatings.add(new ConceptRating(review3,concepts.get(5),4));
		
		conceptsRatings.add(new ConceptRating(review4,concepts.get(0),4));
		conceptsRatings.add(new ConceptRating(review4,concepts.get(1),4));
		conceptsRatings.add(new ConceptRating(review4,concepts.get(2),4));
		conceptsRatings.add(new ConceptRating(review4,concepts.get(3),4));
		conceptsRatings.add(new ConceptRating(review4,concepts.get(4),4));
		conceptsRatings.add(new ConceptRating(review4,concepts.get(5),4));
		
		
		ConceptRatingRepository conceptRatingRepo = new ConceptRatingRepository();
		for (ConceptRating conceptRating : conceptsRatings) {
			conceptRatingRepo.create(conceptRating);
		}	
		
	}
}
