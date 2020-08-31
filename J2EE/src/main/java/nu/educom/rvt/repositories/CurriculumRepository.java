package nu.educom.rvt.repositories;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import nu.educom.rvt.models.Curriculum;

public class CurriculumRepository {
	
protected SessionFactory sessionFactory;
	
	public void create(Curriculum curriculum) {
		Session session = HibernateSession.getSessionFactory().openSession();
	    session.beginTransaction();
	 
	    session.save(curriculum); 
	 
	    session.getTransaction().commit();
	    session.close();
	}
	
	public Curriculum readById(int id) {
		Session session = null;
		try {
			session = HibernateSession.getSessionFactory().openSession();
			return session.get(Curriculum.class, id);
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
