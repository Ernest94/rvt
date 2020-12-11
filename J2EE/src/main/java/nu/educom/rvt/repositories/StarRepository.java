//package nu.educom.rvt.repositories;
//
//import org.hibernate.Session;
//import org.hibernate.SessionFactory;
//
//import nu.educom.rvt.models.Star;
//
//public class StarRepository {
//
//protected SessionFactory sessionFactory;
//	
//	public void create(Star star) {
//		Session session = HibernateSession.getSessionFactory().openSession();
//	    session.beginTransaction();
//	 
//	    session.save(star); 
//	 
//	    session.getTransaction().commit();
//	    session.close();
//	}
//	
//	public Star readById(int id) {
//		Session session = null;
//		try {
//			session = HibernateSession.getSessionFactory().openSession();
//			return session.get(Star.class, id);
//		}
//		finally {
//			if (session != null) {
//				session.close();
//			}
//		}
//	}
//	
//	protected void update() {
//		
//	}
//	
//	protected void delete() {
//		
//	}
//}
