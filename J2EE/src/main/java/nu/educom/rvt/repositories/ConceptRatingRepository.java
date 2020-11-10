package nu.educom.rvt.repositories;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;

import nu.educom.rvt.models.ConceptRating;

public class ConceptRatingRepository {

	public ConceptRating create(ConceptRating conceptRating) {
		Session session = null;
		try {
			session = HibernateSession.getSessionFactory().openSession();
		    session.beginTransaction();
		    int conceptId = (int)session.save(conceptRating);
		    session.getTransaction().commit();
		    conceptRating.setId(conceptId);
			return conceptRating;
		} catch (Exception e) { //TO DO: catch all the different exceptions: {f.e. HibernateException,RollbackException} 
			return null;
		} finally {		   
			if (session != null) {
				session.close();
			}
		}
	}
	
	public List<ConceptRating> createMulti(List<ConceptRating> crs) {
		Session session = null;
		try {
			session = HibernateSession.getSessionFactory().openSession();
			Transaction tx = session.beginTransaction();
			for ( int i=0; i<crs.size(); i++ ) {
			  int conceptId = (int)session.save(crs.get(i));
			  session.save(crs.get(i));
	          crs.get(i).setId(conceptId);
		      if ( i % 20 == 0 ) { 
		        //flush a batch of inserts and release memory:
		        session.flush();
		        session.clear();
		    }
		}
		tx.commit();
		return crs;
		} catch (Exception e) { //TO DO: catch all the different exceptions: {f.e. HibernateException,RollbackException} 
			return null;
		} finally {		   
			if (session != null) {
				session.close();
			}
		}
		
	}
	
	public List<ConceptRating> readAll() {
		Session session = null;
		try {
			session = HibernateSession.getSessionFactory().openSession();
			return HibernateSession.loadAllData(ConceptRating.class, session);
		} catch (Exception e) {//TO DO: catch all the different exceptions: {f.e. HibernateException} 
			return null;
		}
		finally {
			if (session != null) {
				session.close();
			}
		}
	}
	
	public List<ConceptRating> readByReviewId(int review_id) {
		Session session = null;
		try {
			session = HibernateSession.getSessionFactory().openSession();
			return (List<ConceptRating>) session
					.createQuery("from concept_rating where review_id =:review_id", ConceptRating.class)
					.setParameter("review_id", review_id)
					.getResultList();
		} catch (Exception e) {//TO DO: catch all the different exceptions: {f.e. HibernateException} 
			return null;
		} finally {
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
