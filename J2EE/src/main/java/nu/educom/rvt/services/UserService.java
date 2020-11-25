package nu.educom.rvt.services;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.mindrot.jbcrypt.BCrypt;

import nu.educom.rvt.models.Role;
import nu.educom.rvt.models.Location;
import nu.educom.rvt.models.User;
import nu.educom.rvt.models.UserRelation;
import nu.educom.rvt.models.LinkedUsers;
import nu.educom.rvt.models.view.UserSearch;
import nu.educom.rvt.models.view.UserSearchJson;
import nu.educom.rvt.repositories.LocationRepository;
import nu.educom.rvt.repositories.RoleRepository;
import nu.educom.rvt.repositories.UserRelationRepository;
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
	
	public User makeUser(String name, String email, String password, Role role, Location location, LocalDate dateActive)
	{
		String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
		User user = new User(name, email, hashedPassword, role, location, dateActive, null);
		return user;
	}
	
	public void addUser(User user)
	{
		UserRepository userRepo = new UserRepository();
		user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
		userRepo.create(user);
	}
	public void updateUser(User user)
	{
		UserRepository userRepo = new UserRepository();		
		userRepo.update(user);
	}
	
	public List<Role> getRoles() {
		RoleRepository roleRepo = new RoleRepository();
		return roleRepo.readAll(); 
	}
	
	public List<Location> getLocations()
	{
		LocationRepository locRepo = new LocationRepository();
		return locRepo.readAll();
	}
	
	public int addLocation(Location location) {
		LocationRepository locationRepo = new LocationRepository();
		int success = locationRepo.create(location);
		return success;
	}
	
	public boolean validateLocation(Location location) {	
		
		if(location.getName().trim().isEmpty() || !Pattern.matches("^.*\\p{L}.*$", location.getName())) {
			return false;
		}
		else {
			return this.doesLocationExist(location);
		}
	}
	public boolean doesLocationExist(Location location) {
		LocationRepository locationRepo = new LocationRepository();
		Location duplicate = locationRepo.readByName(location.getName());		
		return duplicate==null;
	}
	
	public List<User> getFilteredUsers(String criteria, Role role, Location location)
	{
		String[] words = criteria.split(" ");
		List<User> foundUsers = new ArrayList<>();	
		
		if (!criteria.isEmpty()) {
			for(String word : words)
			{
				if(word != "")
	            {
	                foundUsers.addAll(findUsersByCriteria(word, role, location));
	            } 
			}
		} 
		else {
			foundUsers.addAll(findUsersByCriteria(null, role, location));
		}
		
		foundUsers.stream().distinct().collect(Collectors.toList());
		return foundUsers;
	}
	
	private List<User> findUsersByCriteria(String criteria, Role role, Location location)
	{
		UserRepository userRepo = new UserRepository();
		List<User> allUsers = userRepo.readAll();
		List<User> filterdUsers = new ArrayList<User>();
		
		filterdUsers.addAll(allUsers.stream()
				.filter(u -> u.getRole().getId() == role.getId() || role == null)
				.collect(Collectors.toList()));
		
		filterdUsers = filterdUsers.stream()
				.filter(u -> u.getLocation().getId() == location.getId() || location == null)
				.collect(Collectors.toList());
		if (criteria != null) {
			filterdUsers = filterdUsers.stream()
					.filter(u -> u.getName().contains(criteria) || u.getEmail().contains(criteria))
					.collect(Collectors.toList());
		}
		
		
		
//		filterdUsers.stream().filter(u -> u.getRole().equals(role) || role == null)
//							 .filter(u -> u.getLocation().equals(location) || location == null)
//							 .filter(u -> u.getName().contains(criteria) || u.getEmail().contains(criteria))
//							 .collect(Collectors.toList());
		
		return filterdUsers;
	}
	
	public UserSearchJson convertToUSJ(List<User> users)
	{
		List<UserSearch> userSearch = new ArrayList<>();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		for(User user : users)
		{
			userSearch.add(new UserSearch(user.getId(), user.getName(), user.getEmail(), user.getRole(), user.getLocation(), user.getDateActive().format(formatter)));
		}		
		return new UserSearchJson(userSearch);
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
	
	public List<User> getPossibleRelations(User user) 
	{
		UserRepository userRepo = new UserRepository();
		List<User> allUsers = userRepo.readAll();
		List<User> filteredUsers = new ArrayList<User>();
		String userRole = user.getRole().getName();
		
		switch (userRole) {
			case "Admin":
				filteredUsers.addAll(allUsers);
				break;
			case "Docent":
				filteredUsers.addAll(allUsers.stream()
						.filter(u -> u.getRole().getName().equals("Trainee"))
						.collect(Collectors.toList()));
				filteredUsers.addAll(allUsers.stream()
						.filter(u -> u.getRole().getName().equals("Docent"))
						.collect(Collectors.toList()));
				break;
			case "Sales":
				filteredUsers.addAll(allUsers.stream()
						.filter(u -> !(u.getRole().getName().equals("Admin")))
						.collect(Collectors.toList()));
				break;
			case "Office":
				filteredUsers.addAll(allUsers.stream()
						.filter(u -> !(u.getRole().getName().equals("Admin")))
						.collect(Collectors.toList()));
				break;
			case "Trainee":
				break;
		}
		
		return filteredUsers;
	}
	
	public List<LinkedUsers> combineUsers(User currentUser, List<User> connectedUsers, List<User> possibleRelatedUsers)
	{
		List<LinkedUsers> linkedUsers = new ArrayList<LinkedUsers>();
		
		connectedUsers.forEach((user) -> {
			linkedUsers.add(new LinkedUsers(
					user.getId(),
					user.getName(),
					user.getRole(),
					user.getLocation(),
					true));
		});
		
		possibleRelatedUsers.forEach(user -> {
			boolean hasAlreadyRelation = connectedUsers.stream().anyMatch(u -> u.getId() == (user.getId()));
			if (user.getId() != currentUser.getId() && !hasAlreadyRelation) {
				linkedUsers.add(new LinkedUsers(
						user.getId(),
						user.getName(),
						user.getRole(),
						user.getLocation(),
						false));
			}
		});
		
		return linkedUsers;
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

    public List<User> getAllUsers() {
      UserRepository userRepo= new UserRepository();
      List<User> users = userRepo.readAll();
      return users;
    }
}
