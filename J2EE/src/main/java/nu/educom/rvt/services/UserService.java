package nu.educom.rvt.services;

import java.security.Key;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.security.auth.login.LoginException;

import org.hibernate.Session;
import org.mindrot.jbcrypt.BCrypt;

import io.jsonwebtoken.Jwts;
import nu.educom.rvt.models.Role;
import nu.educom.rvt.models.Location;
import nu.educom.rvt.models.User;
import nu.educom.rvt.models.UserLocation;
import nu.educom.rvt.models.view.UserSearch;
import nu.educom.rvt.models.view.UserSearchJson;
import nu.educom.rvt.repositories.DatabaseException;
import nu.educom.rvt.repositories.EntryNotFoundException;
import nu.educom.rvt.repositories.LocationRepository;
import nu.educom.rvt.repositories.RoleRepository;
import nu.educom.rvt.repositories.UserLocationRepository;
import nu.educom.rvt.repositories.UserRepository;
import nu.educom.rvt.rest.filter.Token;

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

	public User checkUser(User user) throws DatabaseException, LoginException {
		
		User dbUser = userRepo.readByEmail(user.getEmail());
	
		if (dbUser != null && BCrypt.checkpw(user.getPassword(), dbUser.getPassword())) {
			return dbUser;
		}
		else {
			throw new LoginException();
		}
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
		Location location = locationRepo.readByKnownId(id);
		return location;
	}
	
	public Role getRoleById(int id) throws DatabaseException {
	    Role role = roleRepo.readByKnownId(id);
		return role;
		
	}
	
	public boolean validateUser(User user) throws DatabaseException {
		User foundUser = userRepo.readByEmail(user.getEmail());
		if (foundUser == null) return true; 
		else return false;
	}
	
	public String issueToken(User user) throws Exception {
		Key key = Token.getSecretTokenKey();
		String jws = Jwts.builder()
				.setSubject(user.getName())
				.claim("currentLocations", user.getCurrentLocations())
				.claim("UserId", user.getId())
				.claim("Role", user.getRole())
				.signWith(key)
				.compact();
		return jws;
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
	
	public void addUserAndLocations(User user,List<Location> locations) throws DatabaseException
	{
		user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
		User createdUser = userRepo.create(user);
		userRepo.updateLocations(createdUser,locations);
	}
	
	public void addUserLocation(UserLocation userLocation) throws DatabaseException
	{
		userLocationRepo.create(userLocation);
	}
	
	public User updateUser(User user,List<Location> locations) throws DatabaseException
	{
		User userToUpdate = userRepo.update(user);
		userRepo.updateLocations(userToUpdate,locations);
		return userToUpdate;
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
		List<User> filteredUsers = new ArrayList<User>();
		List<Integer> locationsIds = locations.stream().map(l -> l.getId()).collect(Collectors.toList());
		
		for (User user : allUsers) {
			List<Location> userCurrentLocations = user.getCurrentLocations();
			for (Location userCurrentLocation : userCurrentLocations) {
				if (locationsIds.contains(userCurrentLocation.getId()) && locationsIds.size()!=0) {
					filteredUsers.add(user);
					break;
				}
			}
		}
		
		filteredUsers=filteredUsers.stream()
				.filter(u -> u.getRole().getId() == role.getId() || role == null)
				.collect(Collectors.toList());
		
		if (criteria != null) {
			filteredUsers = filteredUsers.stream()
					.filter(u -> u.getName().contains(criteria) || u.getEmail().contains(criteria))
					.collect(Collectors.toList());
		}
		
		return filteredUsers;
	}
	// TODO move to a UserLogic class
	public UserSearchJson convertToUSJ(List<User> users)
	{
		List<UserSearch> userSearch = new ArrayList<>();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		for(User user : users)
		{
//			userSearch.add(new UserSearch(user.getId(), user.getName(), user.getEmail(), user.getRole(), user.getLocation(), user.getDateActive().format(formatter)));
			userSearch.add(new UserSearch(user.getId(), user.getName(), user.getEmail(), user.getRole(), user.getCurrentLocations(), user.getStartDate().format(formatter)));
		}		
		return new UserSearchJson(userSearch);
	}

    public User getUserById(int userId) throws EntryNotFoundException, DatabaseException {
      User user = userRepo.readByKnownId(userId);
    
      return user;
    }

    public List<User> getAllUsers() throws DatabaseException {
      List<User> users = userRepo.readAll();
      return users;
    }
}
