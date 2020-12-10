<<<<<<< HEAD
package nu.educom.rvt.repositories;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import nu.educom.rvt.models.BundleTrainee;

public class BundleTraineeRepository {

	private final Session session;
	
	public BundleTraineeRepository(Session session) {
		super();
		this.session = session;
	}

	public void create(BundleTrainee bundleTrainee) throws DatabaseException {
		session.save(bundleTrainee); 
	}
	
	public List<BundleTrainee> createMulti(List<BundleTrainee> crs) throws DatabaseException {
		for ( int i=0; i<crs.size(); i++ ) {
			  int bundleId = (int)session.save(crs.get(i));
			  session.save(crs.get(i));
	          crs.get(i).setId(bundleId);
		      if ( i % 20 == 0 ) { 
		        //flush a batch of inserts and release memory:
		        session.flush();
		        session.clear();
		    }
		}
		return crs;
	}
	
	public List<BundleTrainee> readAll() throws DatabaseException {
		return HibernateSession.loadAllData(BundleTrainee.class, session);
	}
	
	public BundleTrainee readById(int id) throws DatabaseException {
		return session.get(BundleTrainee.class, id);
	}
	
//	public List<BundleConcept> readByConceptId(int concept_id) throws DatabaseException {
//		return (List<BundleConcept>) session
//					.createQuery("from bundle_concept where concept_id =:concept_id", BundleConcept.class)
//					.setParameter("concept_id", concept_id)
//					.getResultList();
//		
//	} This function isn't necessary yet, but I believe it will be in the future so it's already built. 
	
	protected void update(BundleTrainee bundleTrainee) throws DatabaseException {
		// TODO fix this met record based
		session.saveOrUpdate(bundleTrainee);
	}
	
	
	protected void delete() throws DatabaseException {
		
	}
	
}
	
	

=======
package nu.educom.rvt.repositories;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import nu.educom.rvt.models.BundleTrainee;

public class BundleTraineeRepository {


	public void create(BundleTrainee bundleTrainee) {
		Session session = HibernateSession.getSessionFactory().openSession();
	    session.beginTransaction();
	 
	    session.save(bundleTrainee); 
	 
	    session.getTransaction().commit();
	    session.close();
	}
	
	public List<BundleTrainee> createMulti(List<BundleTrainee> crs) {
		Session session = null;
		try {
			session = HibernateSession.getSessionFactory().openSession();
			Transaction tx = session.beginTransaction();
			for ( int i=0; i<crs.size(); i++ ) {
			  int bundleId = (int)session.save(crs.get(i));
			  session.save(crs.get(i));
	          crs.get(i).setId(bundleId);
		      if ( i % 20 == 0 ) { 
		        //flush a batch of inserts and release memory:
		        session.flush();
		        session.clear();
		    }
		}
		tx.commit();
		return crs;
		} catch (Exception e) { //TO DO: catch all the different exceptions: {f.e. HibernateException,RollbackException} 
			return null;
		} finally {		   
			if (session != null) {
				session.close();
			}
		}
		
	}
	
	public List<BundleTrainee> readAll() {
		Session session = null;
		try {
			session = HibernateSession.getSessionFactory().openSession();
			return HibernateSession.loadAllData(BundleTrainee.class, session);
		} catch (Exception e) {//TO DO: catch all the different exceptions: {f.e. HibernateException} 
			return null;
		}
		finally {
//			if (session != null) {
//				session.close();
//			}
		}
	}
	
	public BundleTrainee readById(int id) {
		Session session = null;
		try {
			session = HibernateSession.getSessionFactory().openSession();
			return session.get(BundleTrainee.class, id);
		}
		finally {
			if (session != null) {
				session.close();
			}
		}
	}
	
	public List<BundleTrainee> readByUserId(int userId) {
		Session session = null;
		try {
			session = HibernateSession.getSessionFactory().openSession();
			return (List<BundleTrainee>) session
			.createQuery("from BundleTrainee BT where BT.user.id =:user_id", BundleTrainee.class)
			.setParameter("user_id", userId)
			.getResultList();
		}
		finally {
			if (session != null) {
				session.close();
			}
		}		
	}
	
//	public List<BundleConcept> readByConceptId(int concept_id) {
//		Session session = null;
//		try {
//			session = HibernateSession.getSessionFactory().openSession();
//			return (List<BundleConcept>) session
//					.createQuery("from bundle_concept where concept_id =:concept_id", BundleConcept.class)
//					.setParameter("concept_id", concept_id)
//					.getResultList();
//		} catch (Exception e) {//TO DO: catch all the different exceptions: {f.e. HibernateException} 
//			return null;
//		} finally {
//			if (session != null) {
//				session.close();
//			}
//		}
//	} This function isn't necessary yet, but I believe it will be in the future so it's already built. 
	
//=======
//					.createQuery("from bundle_trainee where user_id =:userId", BundleTrainee.class)
//					.setParameter("userId", userId)
//					.getResultList();
//		} catch (Exception e) {//TO DO: catch all the different exceptions: {f.e. HibernateException} 
//			return null;
//		} finally {
//			if (session != null) {
//				session.close();
//			}
//		}
//	} 
//>>>>>>> dbe9b30caaf93afe00897c6326269369b0f8ed6f
	
	public void update(BundleTrainee bundleTrainee) {
		Session session = null;
		try {
			session = HibernateSession.getSessionFactory().openSession();
		    session.beginTransaction();
		    session.saveOrUpdate(bundleTrainee);
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
	
}
	
	

>>>>>>> origin/development
