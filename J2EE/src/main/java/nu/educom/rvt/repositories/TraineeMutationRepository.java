package nu.educom.rvt.repositories;

import java.util.List;

import org.hibernate.Session;

import nu.educom.rvt.models.TraineeMutation;

public class TraineeMutationRepository {
	public TraineeMutationRepository() {
	}

	public void create(TraineeMutation traineeMutation) {
		Session session = HibernateSession.getSessionFactory().openSession();
	    session.beginTransaction();
	 
	    session.save(traineeMutation); 
	 
	    session.getTransaction().commit();
	    session.close();
	}
	
	public TraineeMutation readById(int id) {
		Session session = null;
		try {
			session = HibernateSession.getSessionFactory().openSession();
			return session.get(TraineeMutation.class, id);
		}
		finally {
			if (session != null) {
				session.close();
			}
		}
	}
	
	public TraineeMutation findWeekMutationByUserIdAndConceptId(int userId, int conceptId) {
		Session session = null;
		try {
			session = HibernateSession.getSessionFactory().openSession();
			TraineeMutation result = session
					.createQuery("from TraineeMutation where user_id =:userId and concept_id=:conceptId and enddate is null", TraineeMutation.class)
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
	
	public void update(TraineeMutation traineeMutation) {
		Session session = null;
		try {
			session = HibernateSession.getSessionFactory().openSession();
		    session.beginTransaction();
		    session.saveOrUpdate(traineeMutation);
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
	
	public List<TraineeMutation> readAll() {
		Session session = null;
		try {
			session = HibernateSession.getSessionFactory().openSession();
			return HibernateSession.loadAllData(TraineeMutation.class, session);
		}
		finally {
			if (session != null) {
				session.close();
			}
		}
	}
}
