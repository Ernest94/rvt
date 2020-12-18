package nu.educom.rvt.repositories;

import java.util.List;


import org.hibernate.Session;

import nu.educom.rvt.models.Location;

public class LocationRepository {
	protected Session session;
	
	public LocationRepository(Session session) {
		super();
		this.session = session;
	}

	public int create(Location location) throws DatabaseException {
		int generated = (int) session.save(location); 
		return generated;
	}
	
	public Location readById(int id) throws DatabaseException {
		return session.get(Location.class, id);
	}
	public Location readByName(String name) throws DatabaseException {
		return (Location) session
					.createQuery("from Location where name =:name", Location.class)
					.setParameter("name", name)
					.uniqueResultOptional().orElse(null);		
	}	
	
	public List<Location> readAll() throws DatabaseException {
		return HibernateSession.loadAllData(Location.class, session);
	}
	
	protected void update() throws DatabaseException {
		
	}
	
	protected void delete() throws DatabaseException {
		
	}
	
}
