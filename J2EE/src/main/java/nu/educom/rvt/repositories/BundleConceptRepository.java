package nu.educom.rvt.repositories;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import nu.educom.rvt.models.BundleConcept;

public class BundleConceptRepository {


	public void create(BundleConcept bundleConcept) {
		Session session = HibernateSession.getSessionFactory().openSession();
	    session.beginTransaction();
	 
	    session.save(bundleConcept); 
	 
	    session.getTransaction().commit();
	    session.close();
	}
	
	public List<BundleConcept> createMulti(List<BundleConcept> crs) {
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
	
	public List<BundleConcept> readAll() {
		Session session = null;
		try {
			session = HibernateSession.getSessionFactory().openSession();
			return HibernateSession.loadAllData(BundleConcept.class, session);
		} catch (Exception e) {//TO DO: catch all the different exceptions: {f.e. HibernateException} 
			return null;
		}
		finally {
			if (session != null) {
				session.close();
			}
		}
	}
	
	public BundleConcept readById(int id) {
		Session session = null;
		try {
			session = HibernateSession.getSessionFactory().openSession();
			return session.get(BundleConcept.class, id);
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
	
	protected void update(BundleConcept bundleConcept) {
		Session session = null;
		try {
			session = HibernateSession.getSessionFactory().openSession();
		    session.beginTransaction();
		    session.saveOrUpdate(bundleConcept);
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
	
	

