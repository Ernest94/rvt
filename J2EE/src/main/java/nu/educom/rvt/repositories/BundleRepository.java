package nu.educom.rvt.repositories;


import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import nu.educom.rvt.models.Bundle;
import nu.educom.rvt.models.ConceptRating;

public class BundleRepository {

protected SessionFactory sessionFactory;
	
	public Bundle create(Bundle bundle) {
		Session session = null;
		try {
			session = HibernateSession.getSessionFactory().openSession();
		    session.beginTransaction();
		    int bundleId = (int)session.save(bundle);
		    session.getTransaction().commit();
		    bundle.setId(bundleId);
			return bundle;
		} catch (Exception e) { //TO DO: catch all the different exceptions: {f.e. HibernateException,RollbackException} 
			return null;
		} finally {		   
			if (session != null) {
				session.close();
			}
		}
	}
	
	public List<Bundle> readAll() {
		Session session = null;
		try {
			session = HibernateSession.getSessionFactory().openSession();
			return HibernateSession.loadAllData(Bundle.class, session);
			
		} catch (Exception e) {//TO DO: catch all the different exceptions: {f.e. HibernateException} 
			return null;
		}
		finally {
//			if (session != null) {
//				session.close();
//			}
		}
	}
	
	public Bundle readById(int id) {
		Session session = null;
//		try {
			session = HibernateSession.getSessionFactory().openSession();
//			session.createQuery("FROM Bundle WHERE id=:id ").parameter
			return session.get(Bundle.class, id);
//		}
//		finally {
//			if (session != null) {
//				session.close();
//			}
//		}
	}
	
	protected void update(Bundle bundle) {
		Session session = null;
		try {
			session = HibernateSession.getSessionFactory().openSession();
		    session.beginTransaction();
		    
		    session.saveOrUpdate(bundle);
		    
		    session.getTransaction().commit();
		} catch (Exception e) {
			session.getTransaction().rollback();
			//TO DO: catch all the different exceptions: {f.e. HibernateException,RollbackException} 
			
		} finally {		   
			if (session != null) {
				session.close();
			}
		}
	}
	
	protected void delete() {
	}
}
