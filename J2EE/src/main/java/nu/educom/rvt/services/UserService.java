package nu.educom.rvt.services;

import org.mindrot.jbcrypt.BCrypt;

import nu.educom.rvt.models.User;
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
}
