package nu.educom.rvt.services;

import java.time.LocalDateTime;
import java.util.List;

import org.mindrot.jbcrypt.BCrypt;

import nu.educom.rvt.models.Role;
import nu.educom.rvt.models.User;
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
	
	public User makeUser(String name, String email, String password, Role role, LocalDateTime datumActive)
	{
		User user = new User(name, email, password, role, datumActive, null);
		return user;
	}
	
	public void addUser(User user)
	{
		UserRepository userRepo = new UserRepository();
		userRepo.create(user);
	}
	
	public List<Role> getRoles() {
		RoleRepository roleRepo = new RoleRepository();
		return roleRepo.readAll(); 
	}
}
