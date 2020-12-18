//package nu.educom.rvt.repositories;
//
//import java.util.List;
//
//import org.hibernate.Session;
//
//import nu.educom.rvt.models.UserConcept;
//
///* JH: Voor link tabellen is doorgaans geen aparte repository, maar dit wordt in de andere repositories opgelost */
//public class UserConceptRepository {
//	
//	private Session session;
//	
//	public UserConceptRepository(Session session) {
//		super();
//		this.session = session;
//	}
//	
//	public UserConcept create(UserConcept userConcept) throws DatabaseException {
//		if (!session.isOpen() || !session.getTransaction().isActive()) {
//			throw new DatabaseException("Create called on an DB transaction that is not open");
//		}
//	    int conceptId = (int)session.save(userConcept);
//		userConcept.setId(conceptId);
//		return userConcept;
//	}
//	
//	public List<UserConcept> readAll() throws DatabaseException {
//		return HibernateSession.loadAllData(UserConcept.class, session);
//	}
//	
//	public List<UserConcept> readByUserId(int user_id) throws DatabaseException {
//		return (List<UserConcept>) session
//				.createQuery("from user_concept where user_id =:user_id", UserConcept.class)
//				.setParameter("user_id", user_id)
//				.getResultList();
//	}
//	
//	protected void update() throws DatabaseException {
//	}
//	
//	protected void delete() throws DatabaseException {
//	}
//}
