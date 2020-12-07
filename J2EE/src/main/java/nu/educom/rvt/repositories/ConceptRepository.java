package nu.educom.rvt.repositories;


import java.util.List;

import javax.persistence.NoResultException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import nu.educom.rvt.models.Concept;
import nu.educom.rvt.models.Theme;

public class ConceptRepository {

protected SessionFactory sessionFactory;
	
	public Concept create(Concept concept) {
		Session session = null;
		try {
			session = HibernateSession.getSessionFactory().openSession();
		    session.beginTransaction();
		    int conceptId = (int)session.save(concept);
		    session.getTransaction().commit();
		    concept.setId(conceptId);
			return concept;
		} catch (Exception e) { //TO DO: catch all the different exceptions: {f.e. HibernateException,RollbackException} 
			return null;
		} finally {		   
			if (session != null) {
				session.close();
			}
		}
	}
	
	public List<Concept> readAll() {
		Session session = null;
		try {
			session = HibernateSession.getSessionFactory().openSession();
			return HibernateSession.loadAllData(Concept.class, session);
		} catch (Exception e) {//TO DO: catch all the different exceptions: {f.e. HibernateException} 
			return null;
		}
		finally {
			if (session != null) {
				session.close();
			}
		}
	}
	
	public Concept readById(int id) {
		Session session = null;
		try {
			session = HibernateSession.getSessionFactory().openSession();
			return session.get(Concept.class, id);
		}
		finally {
			if (session != null) {
				session.close();
			}
		}
	}
	
	public Concept readByName(String name) {
		Session session = null;
		try {
			session = HibernateSession.getSessionFactory().openSession();
			return (Concept) session
					.createQuery("from Concept where name =:name", Concept.class)
					.setParameter("name", name)
					.getSingleResult();
		} catch (NoResultException ex) {
			return null;
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}
	
	protected void update() {
	}
	
	protected void delete() {
	}
}
