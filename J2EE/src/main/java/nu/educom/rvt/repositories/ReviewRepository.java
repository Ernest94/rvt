package nu.educom.rvt.repositories;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import nu.educom.rvt.models.Review;

public class ReviewRepository {

	protected SessionFactory sessionFactory;
	
	public void create(Review review) {
		Session session = HibernateSession.getSessionFactory().openSession();
	    session.beginTransaction();
	 
	    session.save(review); 
	 
	    session.getTransaction().commit();
	    session.close();
	}
	
	public Review readById(int id) {
		Session session = null;
		try {
			session = HibernateSession.getSessionFactory().openSession();
			return session.get(Review.class, id);
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
