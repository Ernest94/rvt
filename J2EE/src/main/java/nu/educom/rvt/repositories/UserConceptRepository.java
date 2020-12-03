package nu.educom.rvt.repositories;

import java.util.List;

import org.hibernate.Session;

import nu.educom.rvt.models.UserConcept;

/* JH: Voor link tabellen is doorgaans geen aparte repository, maar dit wordt in de andere repositories opgelost */
public class UserConceptRepository {
	
	public UserConcept create(UserConcept userConcept) {
		Session session = null;
		try {
			session = HibernateSession.getSessionFactory().openSession();
		    session.beginTransaction();
		    int conceptId = (int)session.save(userConcept);
		    session.getTransaction().commit();
		    userConcept.setId(conceptId);
			return userConcept;
		} catch (Exception e) { //TO DO: catch all the different exceptions: {f.e. HibernateException,RollbackException} 
			return null;
		} finally {		   
			if (session != null) {
				session.close();
			}
		}
	}
	
	public List<UserConcept> readAll() {
		Session session = null;
		try {
			session = HibernateSession.getSessionFactory().openSession();
			return HibernateSession.loadAllData(UserConcept.class, session);
		} catch (Exception e) {//TO DO: catch all the different exceptions: {f.e. HibernateException} 
			return null;
		}
		finally {
			if (session != null) {
				session.close();
			}
		}
	}
	
	public List<UserConcept> readByUserId(int user_id) {
		Session session = null;
		try {
			session = HibernateSession.getSessionFactory().openSession();
			return (List<UserConcept>) session
					.createQuery("from user_concept where user_id =:user_id", UserConcept.class)
					.setParameter("user_id", user_id)
					.getResultList();
		} catch (Exception e) {//TO DO: catch all the different exceptions: {f.e. HibernateException} 
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
