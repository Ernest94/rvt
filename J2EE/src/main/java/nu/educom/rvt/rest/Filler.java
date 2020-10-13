package nu.educom.rvt.rest;

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
		
		Role role1 = new Role("Admin");
		Role role2 = new Role("Docent");
		Role role3 = new Role("Sales");
		Role role4 = new Role("Office");
		Role role5 = new Role("Trainee");
		
		RoleRepository rolesRepo = new RoleRepository();
		rolesRepo.create(role1);
		rolesRepo.create(role2);
		rolesRepo.create(role3);
		rolesRepo.create(role4);
		rolesRepo.create(role5);
		
		
		Location location1 = new Location("Utrecht");
		LocationRepository locationRepo = new LocationRepository();
		locationRepo.create(location1);
		
		
		User admin = new User("Admin", "admin@educom.nu", "$2a$12$jj0hte6Vh9thhjsZEMeiLebqN.QryPRru6KcGXNjWRX0xc3v3zhVq", role1, location1);
		UserRepository userRepo = new UserRepository();
		userRepo.create(admin);
		
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
		
	}
}
