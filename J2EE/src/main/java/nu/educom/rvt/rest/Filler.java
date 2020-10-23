package nu.educom.rvt.rest;

import java.time.LocalDate;
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
		
	    LocalDate localDate = LocalDate.now();
		LocalDate endDate = null;

		//add roles to db
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
		
		//add locations to db
		List<Location> locations = new ArrayList<Location>();
		locations.add(new Location("Utrecht"));
		locations.add(new Location("Arnhem"));
		locations.add(new Location("Sittard"));
		locations.add(new Location("Eindhoven"));
		LocationRepository locationRepo = new LocationRepository();
		for (Location location : locations) {
			locationRepo.create(location);
		}
		
		//add users to db
		List<User> users = new ArrayList<User>();
		users.add(new User("Admin", "admin@educom.nu", "AyW0BdSKojK^Uw4LRQ", roles.get(0), locations.get(0),localDate,endDate));
		users.add(new User("Docent", "docent@educom.nu", "5^mBejfdV0Rt509x$n", roles.get(1), locations.get(0),localDate,endDate));
		users.add(new User("Trainee", "trainee@educom.nu", "3vDOqHO*B%5i6O@HlW", roles.get(2), locations.get(0),localDate,endDate));
		users.add(new User("Sales", "sales@educom.nu", "xA8PF&0yN*Ye5#2Vnz", roles.get(3), locations.get(0),localDate,endDate));
		users.add(new User("Office", "office@educom.nu", "eYOPEzEDq^YMlJ7$9D", roles.get(4), locations.get(0),localDate,endDate));
		UserService userService = new UserService();
		for (User user : users) {
			userService.addUser(user);
		}		
		
		//add themes to db
		List<Theme> themes = new ArrayList<Theme>();
		themes.add(new Theme("webserver","WS","Werking en configuratie van een HTTP server opzetten, Request - response principe, HTTP en HTTPS en limitaties op ongeoorloofde toegang"));
		themes.add(new Theme("IDE","IDE","Werken met een geavanceerde editor of geïntegreerde ontwikkelomgeving, bouwen vanuit deze omgeving, gebruik shortcuts en debuggen"));
		themes.add(new Theme("front end","FE","Maken van een forntend deel van een applicatie met HTML, CSS en Javascript"));
		themes.add(new Theme("coding","CD","Coding is het schrijven van een computerprogramma, een concrete reeks instructies die een computer kan uitvoeren."));
		themes.add(new Theme("Database (relationeel)","DB","Een database is een grote hoeveelheid informatie welke met behulp van computertechniek gestructureerd is opgeslagen."));
		themes.add(new Theme("connecties (API)","API","Een API (Application Programming Interface) is een software-interface die het mogelijk maakt dat twee applicaties met elkaar kunnen communiceren."));
		themes.add(new Theme("programming paradigms","PP","In de informatica zijn programmeerparadigma's denkpatronen of uitgesproken concepten van programmeren, die voornamelijk verschillen in de wijze van aanpak om het gewenste resultaat te kunnen behalen."));
		themes.add(new Theme("ontwerp methodieken","OM","Een ontwerppatroon of patroon (Engels: design pattern) in de informatica is een generiek opgezette softwarestructuur, die een bepaald veelvoorkomend type software-ontwerpprobleem oplost."));
		themes.add(new Theme("problem solving","PS","Maken van een ontwerp, code goed gescheiden houden, oplossen van bug en de voorspelbaarheid van de code."));
		themes.add(new Theme("werken in/ aan projecten","PRJ.","Technieken die belangrijk zijn voor het werken aan grote projecten zoals het overzichtelijk structureren van je bestanden werken met Agile/Scrum methodiek, versiebeheer etc."));
		themes.add(new Theme("beeldvorming","BV","Hoever kan je geleerde technieken meenemen naar nieuwe talen, wat zijn de overeenkomsten en de verschillen."));
		ThemeRepository themeRepo = new ThemeRepository();
		for (Theme theme : themes) {
			themeRepo.create(theme);
		}
		
		//add concepts to db
		List<Concept> concepts = new ArrayList<Concept>();
		
		//thema webserver
		//week 1&2
		concepts.add(new Concept(themes.get(0),"webserver opzetten",null,1,localDate,endDate));
		concepts.add(new Concept(themes.get(0),"document root, local host",null,1,localDate,endDate));
		concepts.add(new Concept(themes.get(0),"request/response flow",null,1,localDate,endDate));
		
		//thema IDE
		//week 1&2
		concepts.add(new Concept(themes.get(1),"code omgeving basis",null,2,localDate,endDate));
		
		//thema front end
		//week 1&2
		concepts.add(new Concept(themes.get(2),"documentopbouw",null,1,localDate,endDate));
		concepts.add(new Concept(themes.get(2),"pagina indeling (divisions, paragraphs, lists)",null,1,localDate,endDate));
		concepts.add(new Concept(themes.get(2),"interaction- anchors, forms/inputfields",null,1,localDate,endDate));
		concepts.add(new Concept(themes.get(2),"uiterlijk (colors, fonts, borders)",null,1,localDate,endDate));
		concepts.add(new Concept(themes.get(2),"weergave( inline, block, margin/padding)",null,1,localDate,endDate));
		//week 3&4
		concepts.add(new Concept(themes.get(2),"wireframe voor GUI design",null,3,localDate,endDate));
		concepts.add(new Concept(themes.get(2),"framework",null,3,localDate,endDate));		
		//week5&6
		concepts.add(new Concept(themes.get(2),"Template Enige",null,6,localDate,endDate));
		concepts.add(new Concept(themes.get(2),"JavaScript Front End",null,5,localDate,endDate));
		concepts.add(new Concept(themes.get(2),"AJAX",null,5,localDate,endDate));
		//week 7 t/m 9
		concepts.add(new Concept(themes.get(2),"Advanced UI",null,8,localDate,endDate));
		//week 10 t/m 12
		concepts.add(new Concept(themes.get(2),"Front-end Frameworks",null,11,localDate,endDate));

		//thema coding
		//week 1&2
		concepts.add(new Concept(themes.get(3),"variabelen,data types",null,1,localDate,endDate));
		concepts.add(new Concept(themes.get(3),"iteraties (loops), Selecties (if/else, switch)",null,1,localDate,endDate));
		concepts.add(new Concept(themes.get(3),"Functies en Scope",null,1,localDate,endDate));
		concepts.add(new Concept(themes.get(3),"sequenties",null,1,localDate,endDate));
		concepts.add(new Concept(themes.get(3),"Post-afhandeling",null,1,localDate,endDate));
		concepts.add(new Concept(themes.get(3),"URL parameters",null,2,localDate,endDate));
		concepts.add(new Concept(themes.get(3),"File Handling(read/write)",null,2,localDate,endDate));
		concepts.add(new Concept(themes.get(3),"bron code organisatie",null,2,localDate,endDate));
		concepts.add(new Concept(themes.get(3),"herbruikbare code",null,2,localDate,endDate));
		concepts.add(new Concept(themes.get(3),"debuggen",null,2,localDate,endDate));
		concepts.add(new Concept(themes.get(3),"clean code",null,2,localDate,endDate));
		concepts.add(new Concept(themes.get(3),"dry code",null,2,localDate,endDate));
		//week 3&4
		concepts.add(new Concept(themes.get(3),"multiparts forms",null,4,localDate,endDate));
		concepts.add(new Concept(themes.get(3),"$_post + $_files",null,4,localDate,endDate));
		concepts.add(new Concept(themes.get(3),"sessions en connections",null,4,localDate,endDate));
		concepts.add(new Concept(themes.get(3),"defensief programmeren",null,4,localDate,endDate));
		concepts.add(new Concept(themes.get(3),"map en dictionary",null,4,localDate,endDate));
		concepts.add(new Concept(themes.get(3),"error handling (try, catch & finally)",null,4,localDate,endDate));
		concepts.add(new Concept(themes.get(3),"error logging",null,4,localDate,endDate));
		//week 5&6
		concepts.add(new Concept(themes.get(3),"frameworks",null,6,localDate,endDate));
		//week 7 t/m 9
		concepts.add(new Concept(themes.get(3),"2e taal al het voorgaande & OOP",null,7,localDate,endDate));
		//week 10 t/m 12
		concepts.add(new Concept(themes.get(3),"2e taal verdieping",null,10,localDate,endDate));		
		
		//thema Database (relationeel)
		//week 1&2
		concepts.add(new Concept(themes.get(4),"normalisatie en databasebouw",null,2,localDate,endDate));
		concepts.add(new Concept(themes.get(4),"queries (Select, where, group by)",null,2,localDate,endDate));
		//week 3&4
		concepts.add(new Concept(themes.get(4),"queries (join, having)",null,3,localDate,endDate));
		concepts.add(new Concept(themes.get(4),"nested / sub- queries",null,3,localDate,endDate));
		//week 7 t/m 9
		concepts.add(new Concept(themes.get(4),"transactions (ROLLBACK en COMMIT)",null,9,localDate,endDate));
		//week 10 t/m 12
		concepts.add(new Concept(themes.get(4),"kennis van no-sql",null,11,localDate,endDate));
		
		//thema connecties (API)
		//week 3&4
		concepts.add(new Concept(themes.get(5),"API's gebruiken",null,4,localDate,endDate));		
		//week 5&6
		concepts.add(new Concept(themes.get(5),"API's bouwen",null,5,localDate,endDate));		
		//week 7 t/m 9
		concepts.add(new Concept(themes.get(5),"API's bouwen",null,9,localDate,endDate));
		
		//thema programming paradigms
		//week 1&2
		concepts.add(new Concept(themes.get(6),"OOP (objecten, methods, properties)",null,2,localDate,endDate));
		//week 3&4
		concepts.add(new Concept(themes.get(6),"OOP (encapsulation, polymorphism)",null,4,localDate,endDate));
		concepts.add(new Concept(themes.get(6),"OOP (abstract Methods en classes)",null,4,localDate,endDate));
		concepts.add(new Concept(themes.get(6),"OOP (static Methods en properties)",null,4,localDate,endDate));
		//week 5&6
		concepts.add(new Concept(themes.get(6),"Design Patterns (PHP)",null,6,localDate,endDate));
		//week 7 t/m 9
		concepts.add(new Concept(themes.get(6),"Design Patterns (JS)",null,7,localDate,endDate));

		//thema ontwerp methodieken
		//week 7 t/m 9
		concepts.add(new Concept(themes.get(7),"MVC",null,7,localDate,endDate));
		concepts.add(new Concept(themes.get(7),"MVVM",null,9,localDate,endDate));
		concepts.add(new Concept(themes.get(7),"MPC",null,9,localDate,endDate));
		
		//thema problem solving
		//week 1&2
		concepts.add(new Concept(themes.get(8),"algorithme ontwerp",null,2,localDate,endDate));
		concepts.add(new Concept(themes.get(8),"clean code",null,1,localDate,endDate));
		//week 3&4
		concepts.add(new Concept(themes.get(8),"algorithme ontwerp",null,3,localDate,endDate));
		concepts.add(new Concept(themes.get(8),"algorithme ontwerp",null,3,localDate,endDate));

		//thema werken in/ aan projecten
		//week 1&2
		concepts.add(new Concept(themes.get(9),"coding styles ",null,1,localDate,endDate));
		concepts.add(new Concept(themes.get(9),"file structuur",null,1,localDate,endDate));
		concepts.add(new Concept(themes.get(9),"programma structuur",null,1,localDate,endDate));
		//week 3&4
		concepts.add(new Concept(themes.get(9),"project management",null,4,localDate,endDate));
		concepts.add(new Concept(themes.get(9),"versiebeheer",null,4,localDate,endDate));
		//week 7 t/m 9
		concepts.add(new Concept(themes.get(9),"deployment",null,8,localDate,endDate));
		
		//thema beeldvorming
		//week 1&2
		concepts.add(new Concept(themes.get(10),"verschilllen en overeenkomsten talen",null,2,localDate,endDate));
		
		ConceptRepository conceptRepo = new ConceptRepository();
		for (Concept concept : concepts) {
			conceptRepo.create(concept);
		}	
		
		//fill database with mock review data {Review, ConceptRating}
		
		//REVIEW	
		ReviewRepository reviewRepo = new ReviewRepository();
		Review review = new Review(localDate, "Goed bezig trainee", "Deze trainee is prima bezig", Review.Status.COMPLETED);
		reviewRepo.create(review);
		
		//CONCEPTRATING
		List<ConceptRating> conceptsRatings = new ArrayList<ConceptRating>();
		conceptsRatings.add(new ConceptRating(review,concepts.get(0),2));
		conceptsRatings.add(new ConceptRating(review,concepts.get(1),2));
		conceptsRatings.add(new ConceptRating(review,concepts.get(2),2));
		conceptsRatings.add(new ConceptRating(review,concepts.get(3),4));
		conceptsRatings.add(new ConceptRating(review,concepts.get(4),4));
		ConceptRatingRepository conceptRatingRepo = new ConceptRatingRepository();
		for (ConceptRating conceptRating : conceptsRatings) {
			conceptRatingRepo.create(conceptRating);
		}	
		
	}
}
