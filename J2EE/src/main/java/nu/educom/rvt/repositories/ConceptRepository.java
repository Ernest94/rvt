package nu.educom.rvt.repositories;


import org.hibernate.Session;
import org.hibernate.SessionFactory;

import nu.educom.rvt.models.Concept;

public class ConceptRepository {

protected SessionFactory sessionFactory;
	
	public void create(Concept concept) {
		Session session = HibernateSession.getSessionFactory().openSession();
	    session.beginTransaction();
	 
	    session.save(concept); 
	 
	    session.getTransaction().commit();
	    session.close();
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
	
	protected void update() {
		
	}
	
	protected void delete() {
		
	}
}
