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
		
		
		User admin = new User("Admin", "admin@educom.nu", "$2a$12$jj0hte6Vh9thhjsZEMeiLebqN.QryPRru6KcGXNjWRX0xc3v3zhVq", role1);
		UserRepository userRepo = new UserRepository();
		userRepo.create(admin);
	}

}
