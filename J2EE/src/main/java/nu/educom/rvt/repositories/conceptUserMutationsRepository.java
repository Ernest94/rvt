//package nu.educom.rvt.repositories;
//
//import java.util.List;
//
//import org.hibernate.Session;
//
//import nu.educom.rvt.models.ConceptRating;
///* JH: Voor link tabellen is doorgaans geen aparte repository, maar dit wordt in de andere repositories opgelost */
//public class conceptUserMutationsRepository {
//	
//	public ConceptRating create(ConceptRating conceptRating) {
//		Session session = null;
//		try {
//			session = HibernateSession.getSessionFactory().openSession();
//		    session.beginTransaction();
//		    int conceptId = (int)session.save(conceptRating);
//		    session.getTransaction().commit();
//		    conceptRating.setId(conceptId);
//			return conceptRating;
//		} catch (Exception e) { //TO DO: catch all the different exceptions: {f.e. HibernateException,RollbackException} 
//			return null;
//		} finally {		   
//			if (session != null) {
//				session.close();
//			}
//		}
//	}
//	
//	public List<ConceptRating> readAll() {
//		Session session = null;
//		try {
//			session = HibernateSession.getSessionFactory().openSession();
//			return HibernateSession.loadAllData(ConceptRating.class, session);
//		} catch (Exception e) {//TO DO: catch all the different exceptions: {f.e. HibernateException} 
//			return null;
//		}
//		finally {
//			if (session != null) {
//				session.close();
//			}
//		}
//	}
//	
//	public List<ConceptRating> readByReviewId(int review_id) {
//		Session session = null;
//		try {
//			session = HibernateSession.getSessionFactory().openSession();
//			return (List<ConceptRating>) session
//					.createQuery("from concept_rating where review_id =:review_id", ConceptRating.class)
//					.setParameter("review_id", review_id)
//					.getResultList();
//		} catch (Exception e) {//TO DO: catch all the different exceptions: {f.e. HibernateException} 
//			return null;
//		} finally {
//			if (session != null) {
//				session.close();
//			}
//		}
//	}
//	
//	protected void update() {
//	}
//	
//	protected void delete() {
//	}
//
//
//}
//
//
