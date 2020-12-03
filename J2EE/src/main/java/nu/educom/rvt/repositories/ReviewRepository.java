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
        Session session = HibernateSession.getSessionFactory().openSession();
	    session.beginTransaction();
	    
	    Review oldReview = session.get(Review.class, review.getId());
		if (oldReview.getReviewStatus() != Status.PENDING) {
			throw new IllegalStateException("Modifying an existing Review");
		}
		oldReview.setReviewStatus(review.getReviewStatus());
		oldReview.setCommentOffice(review.getCommentOffice());
		oldReview.setCommentStudent(review.getCommentStudent());
		oldReview.setDate(review.getDate());
	 
	    session.getTransaction().commit();
	    session.close();
	}
	
	protected void delete() {
		
	}
}
