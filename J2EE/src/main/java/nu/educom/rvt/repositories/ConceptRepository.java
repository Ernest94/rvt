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
	
	public Concept readById(int id) throws DatabaseException {
		if (!session.isOpen()) {
			throw new DatabaseException("Create called on an session that is not open");
		}
		return session.get(Concept.class, id);
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
	
	protected void update() throws DatabaseException {
	}
	
	protected void delete() throws DatabaseException {
	}
}
