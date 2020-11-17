package nu.educom.rvt.repositories;

import java.util.List;

import javax.persistence.NoResultException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import nu.educom.rvt.models.Location;

public class LocationRepository {
	protected SessionFactory sessionFactory;
	
	public int create(Location location) {
		Session session = HibernateSession.getSessionFactory().openSession();
	    session.beginTransaction();
	 
	    int generated = (int) session.save(location); 
	 
	    session.getTransaction().commit();
	    session.close();
	    
	    return generated;
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
	public Location readByName(String name) {
		Session session = null;
		try {
			session = HibernateSession.getSessionFactory().openSession();
			return (Location) session
					.createQuery("from Location where name =:name", Location.class)
					.setParameter("email", name)
					.getSingleResult();
		} catch (NoResultException ex) {
			return null;
		} finally {
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
