package nu.educom.rvt.repositories;

import java.io.Serializable;
import javax.persistence.RollbackException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import nu.educom.rvt.models.Theme;

public class ThemeRepository {
protected SessionFactory sessionFactory;
	
	public int create(Theme theme) throws RollbackException {
		Session session = HibernateSession.getSessionFactory().openSession();
	    session.beginTransaction();
	    int themeId = (int)session.save(theme); 
	    session.getTransaction().commit();
	    session.close();
	    
		return themeId;
	}
	
	public Theme readById(int id) {
		Session session = null;
		try {
			session = HibernateSession.getSessionFactory().openSession();
			return session.get(Theme.class, id);
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
