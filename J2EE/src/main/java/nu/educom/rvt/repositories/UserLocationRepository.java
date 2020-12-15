package nu.educom.rvt.repositories;

import java.util.List;

import org.hibernate.Session;

import nu.educom.rvt.models.UserLocation;

public class UserLocationRepository {

	protected Session session;
	
	public UserLocationRepository(Session session) {
		super();
		this.session = session;
	}

	public void create(UserLocation userLocation) throws DatabaseException {
		if (!session.isOpen() || !session.getTransaction().isActive()) {
			throw new DatabaseException("Create called on an DB transaction that is not open");
		}
		session.save(userLocation);
	}
	
	
	public List<UserLocationRepository> readAll() throws DatabaseException {
		return HibernateSession.loadAllData(UserLocationRepository.class, session);
	}
	
	public UserLocationRepository readById(int id) throws DatabaseException {
		return session.get(UserLocationRepository.class, id);
	}

	protected void update(UserLocationRepository userLocation) throws DatabaseException {
		session.saveOrUpdate(userLocation);
	}
	
	protected void delete() throws DatabaseException {
		
	}
}
