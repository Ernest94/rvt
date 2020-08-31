package nu.educom.rvt.repositories;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import nu.educom.rvt.models.UserRelationType;

public class UserRelationTypeRepository {

	protected SessionFactory sessionFactory;
	
	public void create(UserRelationType type) {
		Session session = HibernateSession.getSessionFactory().openSession();
	    session.beginTransaction();
	 
	    session.save(type); 
	 
	    session.getTransaction().commit();
	    session.close();
	}
	
	public UserRelationType readById(int id) {
		Session session = null;
		try {
			session = HibernateSession.getSessionFactory().openSession();
			return session.get(UserRelationType.class, id);
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
