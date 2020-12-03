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
	
	protected void update() {
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
