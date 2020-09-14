package nu.educom.rvt.repositories;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import nu.educom.rvt.models.Location;

public class LocationRepository {
	protected SessionFactory sessionFactory;
	
	public void create(Location location) {
		Session session = HibernateSession.getSessionFactory().openSession();
	    session.beginTransaction();
	 
	    session.save(location); 
	 
	    session.getTransaction().commit();
	    session.close();
	}
	
	public Location readById(int id) {
		Session session = null;
		try {
			session = HibernateSession.getSessionFactory().openSession();
			return session.get(Location.class, id);
		}
		finally {
			if (session != null) {
				session.close();
			}
		}
	}
	
	public List<Location> readAll() {
		Session session = null;
		try {
			session = HibernateSession.getSessionFactory().openSession();
			return HibernateSession.loadAllData(Location.class, session);
		}
		finally {
			if (session != null) {
				session.close();
			}
		}
	}
	
	protected void update() {
		
	}
	
	protected void delete() {
		
	}
	
}
