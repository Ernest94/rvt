package nu.educom.rvt.repositories;

import java.util.List;

import org.hibernate.Session;

import nu.educom.rvt.models.TraineeMutation;

public class TraineeMutationRepository {
	public TraineeMutationRepository() {
	}

	public void create(TraineeMutation traineeMutation) {
		Session session = HibernateSession.getSessionFactory().openSession();
	    session.beginTransaction();
	 
	    session.save(traineeMutation); 
	 
	    session.getTransaction().commit();
	    session.close();
	}
	
	public TraineeMutation readById(int id) {
		Session session = null;
		try {
			session = HibernateSession.getSessionFactory().openSession();
			return session.get(TraineeMutation.class, id);
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
	
	public List<TraineeMutation> readAll() {
		Session session = null;
		try {
			session = HibernateSession.getSessionFactory().openSession();
			return HibernateSession.loadAllData(TraineeMutation.class, session);
		}
		finally {
			if (session != null) {
				session.close();
			}
		}
	}
}
