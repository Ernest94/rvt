package nu.educom.rvt.repositories;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import nu.educom.rvt.models.User;

public class UserRepository {
	protected SessionFactory sessionFactory;
	
	public UserRepository() {
	}

	public void create(User user) {
		Session session = HibernateSession.getSessionFactory().openSession();
	    session.beginTransaction();
	 
	    session.save(user); 
	 
	    session.getTransaction().commit();
	    session.close();
	}
	
	public User readById(int id) {
		Session session = null;
		try {
			session = HibernateSession.getSessionFactory().openSession();
			return session.get(User.class, id);
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
