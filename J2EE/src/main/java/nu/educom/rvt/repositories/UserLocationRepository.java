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
	public List<UserLocation> createMulti(List<UserLocation> userLocs) throws DatabaseException {
		for ( int i=0; i<userLocs.size(); i++ ) {
			int userId = (int)session.save(userLocs.get(i));
			session.save(userLocs.get(i));
			userLocs.get(i).setId(userId);
			if ( i % 20 == 0 ) { 
				//flush a batch of inserts and release memory:
				session.flush();
				session.clear();
			}
		}
		return userLocs;
	}
	
	public List<UserLocation> readAll() throws DatabaseException {
		return HibernateSession.loadAllData(UserLocation.class, session);
	}
	
	public UserLocation readByKnownId(int id) throws EntryNotFoundException, DatabaseException {
		return HibernateSession.loadByKnownId(UserLocation.class, id, session);
	}

	protected void update(UserLocationRepository userLocation) throws DatabaseException {
		session.saveOrUpdate(userLocation);
	}
	
	protected void delete() throws DatabaseException {
		
	}
}
