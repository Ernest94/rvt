<<<<<<< HEAD
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
=======
package nu.educom.rvt.repositories;

import java.util.List;
import org.hibernate.Session;
import nu.educom.rvt.models.TraineeActive;

public class TraineeActiveRepository {
	public TraineeActiveRepository() {
	}

	public void create(TraineeActive traineeActive) {
		Session session = HibernateSession.getSessionFactory().openSession();
	    session.beginTransaction();
	 
	    session.save(traineeActive); 
	 
	    session.getTransaction().commit();
	    session.close();
	}
	
	public TraineeActive readById(int id) {
		Session session = null;
		try {
			session = HibernateSession.getSessionFactory().openSession();
			return session.get(TraineeActive.class, id);
		}
		finally {
			if (session != null) {
				session.close();
			}
		}
	}
	public TraineeActive findActiveByUserIdAndConceptId(int userId, int conceptId) {
		Session session = null;
		try {
			session = HibernateSession.getSessionFactory().openSession();
			TraineeActive result = session
			.createQuery("from TraineeActive where user_id =:userId and concept_id=:conceptId and enddate is null", TraineeActive.class)
			.setParameter("userId", userId)
			.setParameter("conceptId", conceptId)
			.getSingleResult();
			return result;
		}
		catch (Exception e) {
			return null;
		}
		finally {
			if (session != null) {
				session.close();
			}
		}		
	}
	
	public void update(TraineeActive traineeActive) {
		Session session = null;
		try {
			session = HibernateSession.getSessionFactory().openSession();
		    session.beginTransaction();
		    session.saveOrUpdate(traineeActive);
		    session.getTransaction().commit();
		} catch (Exception e) { //TO DO: catch all the different exceptions: {f.e. HibernateException,RollbackException} 
			
		} finally {		   
			if (session != null) {
				session.close();
			}
		}
	}
	
	protected void delete() {	
	}
	
	public List<TraineeActive> readAll() {
		Session session = null;
		try {
			session = HibernateSession.getSessionFactory().openSession();
			return HibernateSession.loadAllData(TraineeActive.class, session);
		}
		finally {
			if (session != null) {
				session.close();
			}
		}
	}
}
>>>>>>> origin/development
