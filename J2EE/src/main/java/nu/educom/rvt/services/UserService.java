package nu.educom.rvt.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.mindrot.jbcrypt.BCrypt;

import nu.educom.rvt.models.Role;
import nu.educom.rvt.models.Location;
import nu.educom.rvt.models.User;
import nu.educom.rvt.repositories.LocationRepository;
import nu.educom.rvt.repositories.RoleRepository;
import nu.educom.rvt.repositories.UserRepository;

public class UserService {

	public User checkUser(User user) {
		UserRepository userRepo = new UserRepository();
		User dbUser = userRepo.readByEmail(user.getEmail());
		
		if (dbUser != null && BCrypt.checkpw(user.getPassword(), dbUser.getPassword())) {
			return dbUser;
		}
		
		return null;
	}
	
	public User checkUserPasswordById(int id, String password) {
		UserRepository userRepo = new UserRepository();
		User dbUser = userRepo.readById(id);
		
		if (dbUser != null && BCrypt.checkpw(password, dbUser.getPassword())) {
			return dbUser;
		}
		
		return null;
	}
	
	public User changePassword(User user, String password) {
		String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
		UserRepository userRepo = new UserRepository();
		User changedUser = userRepo.updatePassword(user, hashedPassword);
		if (changedUser != null) {
			return changedUser;
		}
		return null;
	}
	
	public boolean validateUser(User user) {
		UserRepository userRepo = new UserRepository();
		User foundUser = userRepo.readByEmail(user.getEmail());
		if (foundUser == null) return true;
		else return false;
	}
	
	public User makeUser(String name, String email, String password, Role role, Location location, LocalDateTime datumActive)
	{
		String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
		User user = new User(name, email, hashedPassword, role, location, datumActive, null);
		return user;
	}
	
	public void addUser(User user)
	{
		UserRepository userRepo = new UserRepository();
		user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
		userRepo.create(user);
	}
	
	public List<Role> getRoles() {
		RoleRepository roleRepo = new RoleRepository();
		List<Role> roles = roleRepo.readAll(); 
		List<Role> rolesMinTra = new ArrayList<>();
		for(Role role: roles)
		{
			
			if(!role.getName().equals("Trainee"))
			{
				rolesMinTra.add(role);
			}		
		}	
		return rolesMinTra;
	}
	
	public List<Location> getLocations()
	{
		LocationRepository locRepo = new LocationRepository();
		return locRepo.readAll();
	}
	
	public List<User> getfilteredUsers(String criteria, Role role, Location location)
	{
		String[] words = criteria.split(" ");
		List<User> foundUsers = new ArrayList<>();	
		
		for(String word : words)
		{
			if(word != "")
            {
                foundUsers.addAll(findUsersByCriteria(word, role, location));
            } 
		}
		foundUsers.stream().distinct().collect(Collectors.toList());
		return foundUsers;
	}
	
	
	private List<User> findUsersByCriteria(String criteria, Role role, Location location)
	{
		UserRepository userRepo = new UserRepository();
		List<User> filterdUsers = userRepo.readAll();
		return filterdUsers.stream().filter(u -> u.getRole() == role || role == null)
									.filter(u -> u.getLocation() == location || location == null)
									.filter(u -> u.getName().contains(criteria) || u.getEmail().contains(criteria))
									.collect(Collectors.toList());
	}
	
	
	public List<User> getConnectedUsers(User user)
	{
		UserRelationRepository relaRepo = new UserRelationRepository();
		List<UserRelation> userRelation = relaRepo.readAll();
		List<User> returnedUsers = new ArrayList<>();		
		
		returnedUsers.addAll(userRelation.stream().filter(r -> r.getUser().getId() == user.getId())
												  .map(r -> r.getLinked()).collect(Collectors.toList()));
		returnedUsers.addAll(userRelation.stream().filter(r -> r.getLinked().getId() == user.getId())
				  								  .map(r -> r.getUser()).collect(Collectors.toList()));
		return returnedUsers;

	}
	
	public void addConnection(User base, User link)
	{
		UserRelationRepository relaRepo = new UserRelationRepository();
		UserRelation userRelation = new UserRelation(base, link);
		relaRepo.create(userRelation);
	}


    public User getUserById(int userId) {
      UserRepository userRepo = new UserRepository();
      User user = userRepo.readById(userId);
    
      return user;
    }
    
}
