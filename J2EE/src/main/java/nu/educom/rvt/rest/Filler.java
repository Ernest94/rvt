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
		
		User trainee1 = new User("Trainee", "trainee1@educom.nu", "3vDOqHO*B%5i6O@HlW", roles.get(2), locations.get(0),now,endDate);
		User trainee2 = new User("Trainee", "trainee2@educom.nu", "3vDOqHO*B%5i6O@HlW", roles.get(2), locations.get(0),now,endDate);
		User trainee3 = new User("Trainee", "trainee3@educom.nu", "3vDOqHO*B%5i6O@HlW", roles.get(2), locations.get(0),now,endDate);
		
		//add users to db
		List<User> users = new ArrayList<User>();
		users.add(new User("Admin", "admin@educom.nu", "AyW0BdSKojK^Uw4LRQ", roles.get(0), locations.get(0),now,endDate));
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
		concepts.add(new Concept(themes.get(0),"webserver opzetten",null,2,now,endDate));
		concepts.add(new Concept(themes.get(0),"document root, local host",null,2,now,endDate));
		concepts.add(new Concept(themes.get(0),"request/response flow",null,2,now,endDate));
		
		concepts.add(new Concept(themes.get(1),"code omgeving basis",null,2,now,endDate));
		
		concepts.add(new Concept(themes.get(2),"documentopbouw",null,2,now,endDate));
		concepts.add(new Concept(themes.get(2),"pagina indeling (divisions, paragraphs, lists)",null,2,now,endDate));
		concepts.add(new Concept(themes.get(2),"interaction- anchors, forms/inputfields",null,2,now,endDate));
		concepts.add(new Concept(themes.get(2),"uiterlijk (colors, fonts, borders)",null,2,now,endDate));
		concepts.add(new Concept(themes.get(2),"weergave( inline, block, margin/padding)",null,2,now,endDate));
		
		concepts.add(new Concept(themes.get(3),"variabelen,data types",null,2,now,endDate));
		concepts.add(new Concept(themes.get(3),"iteraties (loops), Selecties (if/else, switch)",null,2,now,endDate));
		concepts.add(new Concept(themes.get(3),"Functies en Scope",null,2,now,endDate));
		concepts.add(new Concept(themes.get(3),"sequenties",null,2,now,endDate));
		concepts.add(new Concept(themes.get(3),"Post-afhandeling",null,2,now,endDate));
		concepts.add(new Concept(themes.get(3),"URL parameters",null,1,now,endDate));
		concepts.add(new Concept(themes.get(3),"File Handling(read/write)",null,2,now,endDate));
		concepts.add(new Concept(themes.get(3),"bron code organisatie",null,2,now,endDate));
		concepts.add(new Concept(themes.get(3),"herbruikbare code",null,2,now,endDate));
		concepts.add(new Concept(themes.get(3),"debuggen",null,2,now,endDate));
		concepts.add(new Concept(themes.get(3),"clean code",null,2,now,endDate));
		concepts.add(new Concept(themes.get(3),"dry code",null,2,now,endDate));
	
		concepts.add(new Concept(themes.get(4),"normalisatie en databasebouw",null,2,now,endDate));
		concepts.add(new Concept(themes.get(4),"queries (Select, where, group by)",null,2,now,endDate));
		
		concepts.add(new Concept(themes.get(5),"OOP (objecten, methods, properties)",null,2,now,endDate));
		
		concepts.add(new Concept(themes.get(7),"algorithme ontwerp",null,2,now,endDate));
		concepts.add(new Concept(themes.get(7),"clean code",null,2,now,endDate));
		
		concepts.add(new Concept(themes.get(8),"coding styles ",null,2,now,endDate));
		concepts.add(new Concept(themes.get(8),"file structuur",null,2,now,endDate));
		concepts.add(new Concept(themes.get(8),"programma structuur",null,2,now,endDate));
		
		concepts.add(new Concept(themes.get(9),"verschilllen en overeenkomsten talen",null,2,now,endDate));
		
		ConceptRepository conceptRepo = new ConceptRepository();
		for (Concept concept : concepts) {
			conceptRepo.create(concept);
		}	
		
		//fill database with mock review data {Review, ConceptRating}
		
		//REVIEW	
		ReviewRepository reviewRepo = new ReviewRepository();
		Review review1 = new Review(weekAgo, "Matig bezig trainee1", "Deze trainee is meh bezig", Review.Status.COMPLETED, trainee1);
		Review review2 = new Review(dayAgo, "Redelijk bezig trainee1", "Deze trainee is voldoende bezig", Review.Status.COMPLETED, trainee1);
		Review review3 = new Review(now, "Goed bezig trainee1", "Deze trainee is prima bezig", Review.Status.PENDING, trainee1);
		Review review4 = new Review(dayAgo, "Goed bezig trainee", "Deze trainee is prima bezig", Review.Status.COMPLETED, trainee2);
		reviewRepo.create(review1);
		reviewRepo.create(review2);
		reviewRepo.create(review3);
		reviewRepo.create(review4);
		
		
		//CONCEPTRATING
		List<ConceptRating> conceptsRatings = new ArrayList<ConceptRating>();
		conceptsRatings.add(new ConceptRating(review1,concepts.get(0),2));
		conceptsRatings.add(new ConceptRating(review1,concepts.get(1),2));
		conceptsRatings.add(new ConceptRating(review1,concepts.get(2),2));
		conceptsRatings.add(new ConceptRating(review1,concepts.get(3),2));
		conceptsRatings.add(new ConceptRating(review1,concepts.get(4),2));
		
		
		conceptsRatings.add(new ConceptRating(review2,concepts.get(1),3));
		conceptsRatings.add(new ConceptRating(review2,concepts.get(2),3));
		conceptsRatings.add(new ConceptRating(review2,concepts.get(3),3));
		conceptsRatings.add(new ConceptRating(review2,concepts.get(4),3));
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
