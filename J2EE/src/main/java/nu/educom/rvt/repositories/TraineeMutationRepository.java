package nu.educom.rvt.repositories;

import java.util.List;

import org.hibernate.Session;

import nu.educom.rvt.models.TraineeMutation;

public class TraineeMutationRepository {
	private Session session;
	
	public TraineeMutationRepository(Session session) {
		this.session = session;
	}

	public void create(TraineeMutation traineeMutation) throws DatabaseException {
		if (!session.isOpen() || !session.getTransaction().isActive()) {
			throw new DatabaseException("Create called on an DB transaction that is not open");
		}
	    session.save(traineeMutation); 
	}
	
	public TraineeMutation readByKnownId(int id) throws EntryNotFoundException, DatabaseException {
		return HibernateSession.loadByKnownId(TraineeMutation.class, id, session);
	}
	
	public TraineeMutation findWeekMutationByUserIdAndConceptId(int userId, int conceptId) throws DatabaseException {
		TraineeMutation result = session
					.createQuery("from TraineeMutation where user_id =:userId and concept_id=:conceptId and end_date is null", TraineeMutation.class)
		 			.setParameter("userId", userId)
					.setParameter("conceptId", conceptId)
					.uniqueResultOptional().orElse(null);
		return result;
	}
	
	public void update(TraineeMutation traineeMutation) throws DatabaseException {
		if (!session.isOpen() || !session.getTransaction().isActive()) {
			throw new DatabaseException("Create called on an DB transaction that is not open");
		}
		// TODO user recordbase stratatgy
	    session.saveOrUpdate(traineeMutation);
	}
	
	protected void delete() throws DatabaseException {	
	}
	
	public List<TraineeMutation> readAll() throws DatabaseException {
		return HibernateSession.loadAllData(TraineeMutation.class, session);
	}
}
