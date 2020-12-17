package nu.educom.rvt.repositories;

import java.util.List;

import javax.persistence.NoResultException;

import org.hibernate.Session;
import nu.educom.rvt.models.TraineeActive;

public class TraineeActiveRepository {
	private final Session session;
	
	public TraineeActiveRepository(Session session) {
		super();
		this.session = session;
	}

	public void create(TraineeActive traineeActive) throws DatabaseException {
		if (!session.isOpen() || !session.getTransaction().isActive()) {
			throw new DatabaseException("Create called on an DB transaction that is not open");
		}
		session.save(traineeActive); 
	}
	
	public List<TraineeActive> readAll() throws DatabaseException {
		return HibernateSession.loadAllData(TraineeActive.class, session);
	}
	
	public TraineeActive readById(int id) throws DatabaseException {
		return session.get(TraineeActive.class, id);
	}

	public TraineeActive findActiveByUserIdAndConceptId(int userId, int conceptId) {
		TraineeActive result;
		try {
			result = session
				.createQuery("from TraineeActive where user_id =:userId and concept_id=:conceptId and end_date is null", TraineeActive.class)
				.setParameter("userId", userId)
				.setParameter("conceptId", conceptId)
				.getSingleResult();
		}catch(NoResultException nre) {
			result = null;
		}
		return result;
	}
	
	public void update(TraineeActive traineeActive) throws DatabaseException {
		if (!session.isOpen() || !session.getTransaction().isActive()) {
			throw new DatabaseException("Create called on an DB transaction that is not open");
		}
		// TODO use this with records
	    session.saveOrUpdate(traineeActive);
	}

	
	protected void delete() throws DatabaseException {	
	}	
}
