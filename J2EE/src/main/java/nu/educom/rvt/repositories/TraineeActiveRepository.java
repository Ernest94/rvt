package nu.educom.rvt.repositories;

import java.util.List;
import org.hibernate.Session;
import nu.educom.rvt.models.TraineeActive;

public class TraineeActiveRepository {
	private final Session session;
	
	public TraineeActiveRepository(Session session) {
		super();
		this.session = session;
	}

	public void create(TraineeActive traineeActive) throws DatabaseException {
		session.save(traineeActive); 
	}
	
	public List<TraineeActive> readAll() throws DatabaseException {
		return HibernateSession.loadAllData(TraineeActive.class, session);
		
	}
	
	public TraineeActive readById(int id) throws DatabaseException {
		return session.get(TraineeActive.class, id);
	}
	
	protected void update() throws DatabaseException {
	}
	
	protected void delete() throws DatabaseException {	
	}
	
	
}
