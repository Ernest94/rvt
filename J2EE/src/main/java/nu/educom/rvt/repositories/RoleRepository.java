package nu.educom.rvt.repositories;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import nu.educom.rvt.models.Role;

public class RoleRepository {
	protected SessionFactory sessionFactory;
	
	public void create(Role role) {
		Session session = HibernateSession.getSessionFactory().openSession();
	    session.beginTransaction();
	 
	    session.save(role); 
	 
	    session.getTransaction().commit();
	    session.close();
	}
	
	public Role readById(int id) {
		Session session = null;
		try {
			session = HibernateSession.getSessionFactory().openSession();
			return session.get(Role.class, id);
		}
		finally {
			if (session != null) {
				session.close();
			}
		}
	}
	
	public List<Role> readAll() {
		Session session = null;
		try {
			session = HibernateSession.getSessionFactory().openSession();
			return HibernateSession.loadAllData(Role.class, session);
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
