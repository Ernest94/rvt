//package nu.educom.rvt.repositories;
//
//import org.hibernate.Session;
//import org.hibernate.SessionFactory;
//
//import nu.educom.rvt.models.CurriculumConcept;
///* JH: Voor link tabellen is doorgaans geen aparte repository, maar dit wordt in de andere repositories opgelost */
//public class CurriculumConceptRepository {
//
//protected SessionFactory sessionFactory;
//	
//	public void create(CurriculumConcept concept) {
//		Session session = HibernateSession.getSessionFactory().openSession();
//	    session.beginTransaction();
//	 
//	    session.save(concept); 
//	 
//	    session.getTransaction().commit();
//	    session.close();
//	}
//	
//	public CurriculumConcept readById(int id) {
//		Session session = null;
//		try {
//			session = HibernateSession.getSessionFactory().openSession();
//			return session.get(CurriculumConcept.class, id);
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
