package nu.educom.rvt.repositories;

import java.util.List;

import org.hibernate.Session;

import nu.educom.rvt.models.ConceptRating;
/* JH: Voor link tabellen is doorgaans geen aparte repository, maar dit wordt in de andere repositories opgelost */
public class ConceptUserMutationsRepository {
	protected Session session;
	
	public ConceptUserMutationsRepository(Session session) {
		super();
		this.session = session;
	}
	
	public ConceptRating create(ConceptRating conceptRating) throws DatabaseException {
		if (!session.isOpen() || !session.getTransaction().isActive()) {
			throw new DatabaseException("Create called on an DB transaction that is not open");
		}
	    int conceptId = (int)session.save(conceptRating);
	    conceptRating.setId(conceptId);
	    return conceptRating;
	}
	
	public List<ConceptRating> readAll() throws DatabaseException {
		return HibernateSession.loadAllData(ConceptRating.class, session);
	}
	
	public List<ConceptRating> readByReviewId(int review_id) throws DatabaseException {
		return (List<ConceptRating>) session
				.createQuery("from concept_rating where review_id =:review_id", ConceptRating.class)
				.setParameter("review_id", review_id)
				.getResultList();
	}
	
	protected void update() throws DatabaseException {
	}
	
	protected void delete() throws DatabaseException {
	}


}


