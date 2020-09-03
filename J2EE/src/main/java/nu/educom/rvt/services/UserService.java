package nu.educom.rvt.services;

import org.mindrot.jbcrypt.BCrypt;

import nu.educom.rvt.models.User;
import nu.educom.rvt.repositories.UserRepository;

public class UserService {

	public User checkUser(User user) {
		UserRepository userRepo = new UserRepository();
		User dbUser = userRepo.readByEmail(user.getEmail());
		
		if (dbUser != null && BCrypt.checkpw(user.getPassword(), dbUser.getPassword())) {
//			Boolean validPassword = BCrypt.checkpw(user.getPassword(), dbUser.getPassword());
			return dbUser;
		}
		
		return null;
	}
}
