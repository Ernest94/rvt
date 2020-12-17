package nu.educom.rvt.repositories;

import java.time.LocalDate;
import java.util.List;
import org.hibernate.Session;

import nu.educom.rvt.models.Location;
import nu.educom.rvt.models.User;
import nu.educom.rvt.models.Role;
import nu.educom.rvt.models.UserLocation;

public class UserRepository {
	protected Session session;
	
	public UserRepository(Session session) {
		this.session = session;
	}

	public User create(User user) throws DatabaseException {
		if (!session.isOpen() || !session.getTransaction().isActive()) {
			throw new DatabaseException("Create called on an DB transaction that is not open");
		}
		user.setRole(session.get(Role.class, user.getRole().getId()));
	    session.save(user);
	    return user;
	}
	
	public User readById(int id) throws DatabaseException {
		return session.get(User.class, id);
	}
	
	public User update(User user) throws DatabaseException {
		User toUpdate = this.readById(user.getId());
		toUpdate.setName(user.getName());
		toUpdate.setEmail(user.getEmail());
		toUpdate.setRole(session.get(Role.class, user.getRole().getId()));
		toUpdate.setStartDate(user.getStartDate());
		toUpdate.setEndDate(user.getEndDate());
		return toUpdate;
	}
	
	public void updateLocations(User user, List<Location> newLocations) throws DatabaseException {
		HibernateSession.updateReadOnlyEntries(user.getAllUserLocations(), newLocations,
				         (ul, l) -> ul.getLocation().equals(l),
				         l -> new UserLocation(user, l, LocalDate.now(), null),
				         session);
	}
	
	protected void delete() throws DatabaseException {	
	}
	
	public List<User> readAll() throws DatabaseException {
		return HibernateSession.loadAllData(User.class, session);
	}

	public User readByEmail(String email) throws DatabaseException {
		return (User) session
			.createQuery("from User where email =:email", User.class) /* JH TODO: Check endDate of user */
			.setParameter("email", email)
			.uniqueResultOptional().orElse(null);
	}
	
	public User updatePassword(User user, String password) throws DatabaseException {
		if (!session.isOpen() || !session.getTransaction().isActive()) {
			throw new DatabaseException("Create called on an DB transaction that is not open");
		}
		user.setPassword(password);
		return user;
	}
}
