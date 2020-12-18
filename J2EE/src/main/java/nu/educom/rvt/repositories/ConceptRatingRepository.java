package nu.educom.rvt.repositories;

import java.util.List;

import org.hibernate.Session;

import nu.educom.rvt.models.ConceptRating;
import nu.educom.rvt.models.Review.Status;
/* JH: Voor link tabellen is doorgaans geen aparte repository, maar dit wordt in de andere repositories opgelost */
public class ConceptRatingRepository {
	private final Session session;

	public ConceptRatingRepository(Session session) {
		super();
		this.session = session;
	}

	public ConceptRating create(ConceptRating conceptRating) throws DatabaseException {
		int conceptId = (int)session.save(conceptRating);
	    conceptRating.setId(conceptId);
		return conceptRating;
	}
	
	public List<ConceptRating> createMulti(List<ConceptRating> crs) throws DatabaseException {
		if (crs.stream().anyMatch(cr -> cr.getReview().getReviewStatus() != Status.PENDING)) {
			throw new IllegalStateException("Modifying an existing Review");
		}
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
		return crs;
	}
	
	public List<ConceptRating> readAll() throws DatabaseException {
		return HibernateSession.loadAllData(ConceptRating.class, session);
	}
	
	public ConceptRating readById(int id) throws DatabaseException {
		return session.get(ConceptRating.class, id);
	}
	
	public List<ConceptRating> readByReviewId(int review_id) throws DatabaseException {
		return (List<ConceptRating>) session
					.createQuery("from concept_rating where review_id =:review_id", ConceptRating.class)
					.setParameter("review_id", review_id)
					.getResultList();
	}
	
	public boolean update(ConceptRating conceptRating) throws DatabaseException {
		if (conceptRating.getReview().getReviewStatus() != Status.PENDING) {
			throw new IllegalStateException("Modifying an existing Review");
		}
		session.saveOrUpdate(conceptRating);
		return true;
	}
	
	
	protected void delete() throws DatabaseException {
	}
}
