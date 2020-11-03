package nu.educom.rvt.repositories;


import java.util.List;

import javax.persistence.NoResultException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import nu.educom.rvt.models.User;

public class UserRepository {
	protected SessionFactory sessionFactory;
	
	public UserRepository() {
	}

	public void create(User user) {
		Session session = HibernateSession.getSessionFactory().openSession();
	    session.beginTransaction();
	 
	    session.save(user); 
	 
	    session.getTransaction().commit();
	    session.close();
	}
	
	public User readById(int id) {
		Session session = null;
		try {
			session = HibernateSession.getSessionFactory().openSession();
			return session.get(User.class, id);
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
	
	public List<User> readAll() {
		Session session = null;
		try {
			session = HibernateSession.getSessionFactory().openSession();
			return HibernateSession.loadAllData(User.class, session);
		}
		finally {
			if (session != null) {
				session.close();
			}
		}
	}

	public User readByEmail(String email) {
		Session session = null;
		try {
			session = HibernateSession.getSessionFactory().openSession();
			return (User) session
					.createQuery("from User where email =:email", User.class)
					.setParameter("email", email)
					.getSingleResult();
		} catch (NoResultException ex) {
			return null;
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}
	
	public User updatePassword(User user, String password) {
		Session session = null;
		try {
			session = HibernateSession.getSessionFactory().openSession();
			Transaction tx = session.beginTransaction();
			session.update(user);
			user.setPassword(password);
			tx.commit();
			return user;
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}
}
