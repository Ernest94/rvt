package nu.educom.rvt.rest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import nu.educom.rvt.models.*;
import nu.educom.rvt.repositories.*;

public class Filler {
	
	public static Boolean isDatabaseEmpty() {
		RoleRepository rolesRepo = new RoleRepository();
		List<Role> roles = rolesRepo.readAll();
		return roles.size() <= 0 ;
	}
	
	public static void fillDatabase() {
		
	    LocalDate localDate = LocalDate.now();
		LocalDate endDate=null;

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
		UserRepository userRepo = new UserRepository();
		for (User user : users) {
			userRepo.create(user);
		}		
		
		//add themes to db
		List<Theme> themes = new ArrayList<Theme>();
		themes.add(new Theme("webserver","ws","Een webserver is een programma dat via een netwerk HTTP-verzoeken ontvangt en documenten naar de client stuurt."));
		themes.add(new Theme("IDE","IDE","Een integrated development environment of IDE is computersoftware die een softwareontwikkelaar ondersteunt bij het ontwikkelen van computersoftware."));
		themes.add(new Theme("front end","fe","Front end is de voorkant van de website en alles wat de gebruiker ziet."));
		themes.add(new Theme("coding","cod","Coding is het schrijven van een computerprogramma, een concrete reeks instructies die een computer kan uitvoeren."));
		themes.add(new Theme("Database (relationeel)","db","Een database is een grote hoeveelheid informatie welke met behulp van computertechniek gestructureerd is opgeslagen."));
		themes.add(new Theme("connecties (API)","API","Een API (Application Programming Interface) is een software-interface die het mogelijk maakt dat twee applicaties met elkaar kunnen communiceren."));
		themes.add(new Theme("programming paradigms","pp","In de informatica zijn programmeerparadigma's denkpatronen of uitgesproken concepten van programmeren, die voornamelijk verschillen in de wijze van aanpak om het gewenste resultaat te kunnen behalen."));
		themes.add(new Theme("ontwerp methodieken","om","Een ontwerppatroon of patroon (Engels: design pattern) in de informatica is een generiek opgezette softwarestructuur, die een bepaald veelvoorkomend type software-ontwerpprobleem oplost."));
		themes.add(new Theme("problem solving","ps","Probleem oplossing."));
		themes.add(new Theme("werken in/ aan projecten","proj.",""));
		themes.add(new Theme("beeldvorming","bv",""));
		ThemeRepository themeRepo = new ThemeRepository();
		for (Theme theme : themes) {
			themeRepo.create(theme);
		}
		
		//add concepts to db
		List<Concept> concepts = new ArrayList<Concept>();
		concepts.add(new Concept(themes.get(0),"webserver opzetten",null,2,localDate,endDate));
		concepts.add(new Concept(themes.get(0),"document root, local host",null,2,localDate,endDate));
		concepts.add(new Concept(themes.get(0),"request/response flow",null,2,localDate,endDate));
		
		concepts.add(new Concept(themes.get(1),"code omgeving basis",null,2,localDate,endDate));
		
		concepts.add(new Concept(themes.get(2),"documentopbouw",null,2,localDate,endDate));
		concepts.add(new Concept(themes.get(2),"pagina indeling (divisions, paragraphs, lists)",null,2,localDate,endDate));
		concepts.add(new Concept(themes.get(2),"interaction- anchors, forms/inputfields",null,2,localDate,endDate));
		concepts.add(new Concept(themes.get(2),"uiterlijk (colors, fonts, borders)",null,2,localDate,endDate));
		concepts.add(new Concept(themes.get(2),"weergave( inline, block, margin/padding)",null,2,localDate,endDate));
		
		concepts.add(new Concept(themes.get(3),"variabelen,data types",null,2,localDate,endDate));
		concepts.add(new Concept(themes.get(3),"iteraties (loops), Selecties (if/else, switch)",null,2,localDate,endDate));
		concepts.add(new Concept(themes.get(3),"Functies en Scope",null,2,localDate,endDate));
		concepts.add(new Concept(themes.get(3),"sequenties",null,2,localDate,endDate));
		concepts.add(new Concept(themes.get(3),"Post-afhandeling",null,2,localDate,endDate));
		concepts.add(new Concept(themes.get(3),"URL parameters",null,1,localDate,endDate));
		concepts.add(new Concept(themes.get(3),"File Handling(read/write)",null,2,localDate,endDate));
		concepts.add(new Concept(themes.get(3),"bron code organisatie",null,2,localDate,endDate));
		concepts.add(new Concept(themes.get(3),"herbruikbare code",null,2,localDate,endDate));
		concepts.add(new Concept(themes.get(3),"debuggen",null,2,localDate,endDate));
		concepts.add(new Concept(themes.get(3),"clean code",null,2,localDate,endDate));
		concepts.add(new Concept(themes.get(3),"dry code",null,2,localDate,endDate));
	
		concepts.add(new Concept(themes.get(4),"normalisatie en databasebouw",null,2,localDate,endDate));
		concepts.add(new Concept(themes.get(4),"queries (Select, where, group by)",null,2,localDate,endDate));
		
		concepts.add(new Concept(themes.get(5),"OOP (objecten, methods, properties)",null,2,localDate,endDate));
		
		concepts.add(new Concept(themes.get(7),"algorithme ontwerp",null,2,localDate,endDate));
		concepts.add(new Concept(themes.get(7),"clean code",null,2,localDate,endDate));
		
		concepts.add(new Concept(themes.get(8),"coding styles ",null,2,localDate,endDate));
		concepts.add(new Concept(themes.get(8),"file structuur",null,2,localDate,endDate));
		concepts.add(new Concept(themes.get(8),"programma structuur",null,2,localDate,endDate));
		
		concepts.add(new Concept(themes.get(9),"verschilllen en overeenkomsten talen",null,2,localDate,endDate));
		
		ConceptRepository conceptRepo = new ConceptRepository();
		for (Concept concept : concepts) {
			conceptRepo.create(concept);
		}	
	}
}
