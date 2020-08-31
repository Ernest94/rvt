package nu.educom.rvt.rest;

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
		
		Star star1 = new Star("Geen score", 0);
		Star star2 = new Star("Matig", 1);
		Star star3 = new Star("Redelijk", 2);
		Star star4 = new Star("Voldoende", 3);
		Star star5 = new Star("Goed", 4);
		Star star6 = new Star("Uitstekend", 5);
		
		StarRepository starRepo = new StarRepository();
		starRepo.create(star1);
		starRepo.create(star2);
		starRepo.create(star3);
		starRepo.create(star4);
		starRepo.create(star5);
		starRepo.create(star6);
		
		Weekblock block1 = new Weekblock("Week 1 en 2", 1);
		Weekblock block2 = new Weekblock("Week 1 t/m 3", 1);
		Weekblock block3 = new Weekblock("Week 3 en 4", 2);
		Weekblock block4 = new Weekblock("Week 4 t/m 6", 2);
		Weekblock block5 = new Weekblock("Week 5 en 6", 3);
		Weekblock block6 = new Weekblock("Week 7 en 8", 4);
		Weekblock block7 = new Weekblock("Week 7 t/m 9", 3);
		Weekblock block8 = new Weekblock("Na week 8", 5);
		Weekblock block9 = new Weekblock("Na week 9", 4);
		
		WeekblockRepository blockRepo = new WeekblockRepository();
		blockRepo.create(block1);
		blockRepo.create(block2);
		blockRepo.create(block3);
		blockRepo.create(block4);
		blockRepo.create(block5);
		blockRepo.create(block6);
		blockRepo.create(block7);
		blockRepo.create(block8);
		blockRepo.create(block9);
		
		UserRelationType relationType1 = new UserRelationType("Docent-Trainee");
		UserRelationType relationType2 = new UserRelationType("Kantoor-Docent");
		
		UserRelationTypeRepository relationTypeRepo = new UserRelationTypeRepository();
		relationTypeRepo.create(relationType1);
		relationTypeRepo.create(relationType2);
		
		User admin = new User("Admin", "admin@educom.nu", "password", rolesRepo.readById(1));
		UserRepository userRepo = new UserRepository();
		userRepo.create(admin);
	}

}
