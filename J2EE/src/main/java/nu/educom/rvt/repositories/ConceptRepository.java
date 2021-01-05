package nu.educom.rvt.repositories;

import java.util.List;

import org.hibernate.Session;

import nu.educom.rvt.models.Concept;

public class ConceptRepository {

	protected Session session;
	
	public ConceptRepository(Session session) {
		super();
		this.session = session;
	}

	public Concept create(Concept concept) throws DatabaseException {
		if (!session.isOpen() || !session.getTransaction().isActive()) {
			throw new DatabaseException("Create called on an DB transaction that is not open");
		}
	    int conceptId = (int)session.save(concept);
	    concept.setId(conceptId);
		return concept;
	}
	
	public List<Concept> readAll() throws DatabaseException {
		return HibernateSession.loadAllData(Concept.class, session);
	}
	
	public Concept readByKnownId(int id) throws EntryNotFoundException, DatabaseException {
		return HibernateSession.loadByKnownId(Concept.class, id, session);
	}
	
	public Concept readByName(String name) throws DatabaseException {
		if (!session.isOpen()) {
			throw new DatabaseException("Create called on an session that is not open");
		}
		return (Concept) session
				.createQuery("from Concept where name =:name", Concept.class)
				.setParameter("name", name)
				.uniqueResultOptional().orElse(null);
	}
	
	public boolean isConceptInUserBundle(int id, int userId)  throws DatabaseException {
		boolean result = session.createNativeQuery("SELECT name FROM concept c "
												+ "LEFT JOIN bundle_concept AS bc "
												+ "ON c.id = bc.concept_id "
												+ "LEFT JOIN bundle_trainee AS btr "
												+ "ON bc.bundle_id = btr.bundle_id "
												+ "WHERE bc.concept_id =:conceptId "
												+ "AND btr.user_id =:userId "
												+ "AND btr.end_date IS NULL "
												+ "AND bc.end_date IS NULL")
					.setParameter("userId", userId)
					.setParameter("conceptId", id)
					.getResultList().isEmpty();
		
		return !result;
	}
	
	protected void update() throws DatabaseException {
	}
	
	protected void delete() throws DatabaseException {
	}
}
