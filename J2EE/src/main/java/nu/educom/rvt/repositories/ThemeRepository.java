package nu.educom.rvt.repositories;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import nu.educom.rvt.models.Theme;

public class ThemeRepository {
protected SessionFactory sessionFactory;
	
	public void create(Theme theme) {
		Session session = HibernateSession.getSessionFactory().openSession();
	    session.beginTransaction();
	 
	    session.save(theme); 
	 
	    session.getTransaction().commit();
	    session.close();
	}
	
	public Theme readById(int id) {
		Session session = null;
		try {
			session = HibernateSession.getSessionFactory().openSession();
			return session.get(Theme.class, id);
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
