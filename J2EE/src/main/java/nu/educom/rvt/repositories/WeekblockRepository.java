//package nu.educom.rvt.repositories;
//
//import org.hibernate.Session;
//import org.hibernate.SessionFactory;
//
//import nu.educom.rvt.models.Weekblock;
//
//public class WeekblockRepository {
//
//	protected SessionFactory sessionFactory;
//	
//	public void create(Weekblock weekblock) {
//		Session session = HibernateSession.getSessionFactory().openSession();
//	    session.beginTransaction();
//	 
//	    session.save(weekblock); 
//	 
//	    session.getTransaction().commit();
//	    session.close();
//	}
//	
//	public Weekblock readById(int id) {
//		Session session = null;
//		try {
//			session = HibernateSession.getSessionFactory().openSession();
//			return session.get(Weekblock.class, id);
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
