package nu.educom.rvt.repositories;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import nu.educom.rvt.models.Review;
import nu.educom.rvt.models.Review.Status;

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
	
	public List<Review> readAll() {
		Session session = null;
		try {
			session = HibernateSession.getSessionFactory().openSession();
			return HibernateSession.loadAllData(Review.class, session);
		}
		finally {
			if (session != null) {
				session.close();
			}
		}
	}
	
	public void update(Review review) {
		if (review.getReviewStatus() != Status.PENDING) {
			throw new IllegalStateException("Modifying an existing Review");
		}
        Session session = HibernateSession.getSessionFactory().openSession();
	    session.beginTransaction();
	 
	    session.update(review); 
	 
	    session.getTransaction().commit();
	    session.close();
	}
	
	protected void delete() {
		
	}
}
