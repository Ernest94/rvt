package nu.educom.rvt.repositories;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import nu.educom.rvt.models.UserRelation;

/* JH: Voor link tabellen is doorgaans geen aparte repository, maar dit wordt in de andere repositories opgelost */
public class UserRelationRepository {
	protected SessionFactory sessionFactory;
	
	public void create(UserRelation relation) {
		Session session = HibernateSession.getSessionFactory().openSession();
	    session.beginTransaction();
	 
	    session.save(relation); 
	 
	    session.getTransaction().commit();
	    session.close();
	}
	
	public UserRelation readById(int id) {
		Session session = null;
		try {
			session = HibernateSession.getSessionFactory().openSession();
			return session.get(UserRelation.class, id);
		}
		finally {
			if (session != null) {
				session.close();
			}
		}
	}
	
	public List<UserRelation> readAll() {
		Session session = null;
		try {
			session = HibernateSession.getSessionFactory().openSession();
			return HibernateSession.loadAllData(UserRelation.class, session);
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
