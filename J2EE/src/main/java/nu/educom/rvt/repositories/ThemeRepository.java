package nu.educom.rvt.repositories;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import nu.educom.rvt.models.Theme;

public class ThemeRepository {
protected SessionFactory sessionFactory;
	
	public Theme create(Theme theme) {
		Session session = null;
		try {
			session = HibernateSession.getSessionFactory().openSession();
		    session.beginTransaction();
		    int themeId = (int)session.save(theme);
		    session.getTransaction().commit();
		    theme.setId(themeId);
			return theme;
		} catch (Exception e) { //TO DO: catch all the different exceptions: {f.e. HibernateException,RollbackException} 
			return null;
		} finally {		   
			if (session != null) {
				session.close();
			}
		}
	}
	
	public List<Theme> readAll() {
		Session session = null;
		try {
			session = HibernateSession.getSessionFactory().openSession();
			return HibernateSession.loadAllData(Theme.class, session);
		} catch (Exception e) {//TO DO: catch all the different exceptions: {f.e. HibernateException} 
			return null;
		}
		finally {
			if (session != null) {
				session.close();
			}
		}
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
