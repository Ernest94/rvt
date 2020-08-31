package nu.educom.rvt.repositories;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import nu.educom.rvt.models.ReviewStatus;

public class ReviewStatusRepository {

	protected SessionFactory sessionFactory;
	
	public void create(ReviewStatus reviewStatus) {
		Session session = HibernateSession.getSessionFactory().openSession();
	    session.beginTransaction();
	 
	    session.save(reviewStatus); 
	 
	    session.getTransaction().commit();
	    session.close();
	}
	
	public ReviewStatus readById(int id) {
		Session session = null;
		try {
			session = HibernateSession.getSessionFactory().openSession();
			return session.get(ReviewStatus.class, id);
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
