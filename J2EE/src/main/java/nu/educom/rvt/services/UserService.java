package nu.educom.rvt.services;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.hibernate.Session;
import org.mindrot.jbcrypt.BCrypt;

import nu.educom.rvt.models.Role;
import nu.educom.rvt.models.Location;
import nu.educom.rvt.models.User;
import nu.educom.rvt.models.UserLocation;
import nu.educom.rvt.models.view.UserSearch;
import nu.educom.rvt.models.view.UserSearchJson;
import nu.educom.rvt.repositories.DatabaseException;
import nu.educom.rvt.repositories.LocationRepository;
import nu.educom.rvt.repositories.RoleRepository;
import nu.educom.rvt.repositories.UserLocationRepository;
import nu.educom.rvt.repositories.UserRepository;

public class UserService {
	private final UserRepository userRepo;
	private LocationRepository locationRepo;
	private UserLocationRepository userLocationRepo;
	private RoleRepository roleRepo;

	public UserService(Session session) {
		userRepo = new UserRepository(session);
		roleRepo = new RoleRepository(session);
		locationRepo = new LocationRepository(session);
		userLocationRepo = new UserLocationRepository(session);
	}

	public User checkUser(User user) throws DatabaseException {
		User dbUser = userRepo.readByEmail(user.getEmail());
		
		if (dbUser != null && BCrypt.checkpw(user.getPassword(), dbUser.getPassword())) {
			return dbUser;
		}
		
		return null;
	}
	
	public User checkUserPasswordById(int id, String password) throws DatabaseException {
		User dbUser = userRepo.readById(id);
		
		if (dbUser != null && BCrypt.checkpw(password, dbUser.getPassword())) {
			return dbUser;
		}
		
		return null;
	}
	
	public User changePassword(User user, String password) throws DatabaseException {
		String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
		User changedUser = userRepo.updatePassword(user, hashedPassword);
		if (changedUser != null) {
			return changedUser;
		}
		return null;
	}
	
	public Location getLocationById(int id) throws DatabaseException {
		Location location = locationRepo.readById(id);
		return location;
	}
	
	public Role getRoleById(int id) throws DatabaseException {
	    Role role = roleRepo.readById(id);
		return role;
		
	}
	
	public boolean validateUser(User user) throws DatabaseException {
		User foundUser = userRepo.readByEmail(user.getEmail());
		if (foundUser == null) return true; 
		else return false;
	}
	
	// TODO move to a UserLogic class om beter te kunnen testen
	public User makeUser(String name, String email, String password, Role role, Location location, LocalDate dateActive)
	{
		String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt()); /* JH QUESTION: Lijkt of het password 2x wordt gehashed */
//		User user = new User(name, email, hashedPassword, role, location, dateActive, null);
		User user = new User(name, email, hashedPassword, role, dateActive, null);
		return user;
	}
	
	public void addUser(User user) throws DatabaseException
	{
		user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
		userRepo.create(user);
	}	
	public void addUserLocation(UserLocation userLocation) throws DatabaseException
	{
		userLocationRepo.create(userLocation);
	}
	public void updateUser(User user) throws DatabaseException
	{
		userRepo.update(user);
	}
	
	public List<Role> getRoles() throws DatabaseException {
		return roleRepo.readAll(); 
	}
	
	public List<Location> getLocations() throws DatabaseException
	{
		return locationRepo.readAll();
	}
	
	public int addLocation(Location location) throws DatabaseException {
		int success = locationRepo.create(location);
		return success;
	}
	public boolean validateLocation(Location location) throws DatabaseException {	
		
		if(location.getName().trim().isEmpty() || !Pattern.matches("^.*\\p{L}.*$", location.getName())) {
			return false;
		}
		else {
			return this.doesLocationExist(location);
		}
	}
	public boolean doesLocationExist(Location location) throws DatabaseException {
		Location duplicate = locationRepo.readByName(location.getName());		
		return duplicate==null;
	}
	
	public List<User> getFilteredUsers(String criteria, Role role, List<Location> locations) throws DatabaseException
	{
		String[] words = criteria.split(" ");
		List<User> foundUsers = new ArrayList<>();	
		
		if (!criteria.isEmpty()) {
			for(String word : words)
			{
				if(word != "")
	            {
	                foundUsers.addAll(findUsersByCriteria(word, role, locations));
	            } 
			}
		} 
		else {
			foundUsers.addAll(findUsersByCriteria(null, role, locations));
		}
		
		foundUsers.stream().distinct().collect(Collectors.toList());
		return foundUsers;
	}
	
	
	public List<User> findUsersByCriteria(String criteria, Role role, List<Location> locations) throws DatabaseException
	{
		List<User> allUsers = userRepo.readAll();
		List<User> filterdUsers = new ArrayList<User>();
		
		filterdUsers.addAll(allUsers.stream()
				.filter(u -> u.getRole().getId() == role.getId() || role == null)
				.collect(Collectors.toList()));
		
		
		filterdUsers = filterdUsers.stream()
				.filter(u -> locations.contains(u.getCurrentLocations().get(0)) || locations.size()==0)
				.collect(Collectors.toList());	
//		filterdUsers = filterdUsers.stream()
//				.filter(u -> locations.contains(u.getLocation()) || locations.size()==0)
//				.collect(Collectors.toList());	
		
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
	// TODO move to a UserLogic class
	public UserSearchJson convertToUSJ(List<User> users)
	{
		List<UserSearch> userSearch = new ArrayList<>();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		for(User user : users)
		{
//			userSearch.add(new UserSearch(user.getId(), user.getName(), user.getEmail(), user.getRole(), user.getLocation(), user.getDateActive().format(formatter)));
			userSearch.add(new UserSearch(user.getId(), user.getName(), user.getEmail(), user.getRole(), user.getCurrentLocations(), user.getDateActive().format(formatter)));
		}		
		return new UserSearchJson(userSearch);
	}
	
	

    public User getUserById(int userId) throws DatabaseException {
      User user = userRepo.readById(userId);
    
      return user;
    }

    public List<User> getAllUsers() throws DatabaseException {
      List<User> users = userRepo.readAll();
      return users;
    }
}
