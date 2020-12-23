package nu.educom.rvt.repositories;

import java.util.List;

import org.hibernate.Session;
import nu.educom.rvt.models.BundleTrainee;

public class BundleTraineeRepository {

	private final Session session;
	
	public BundleTraineeRepository(Session session) {
		super();
		this.session = session;
	}

	public void create(BundleTrainee bundleTrainee) throws DatabaseException {
		session.save(bundleTrainee); 
	}
	
	public List<BundleTrainee> createMulti(List<BundleTrainee> crs) throws DatabaseException {
		for ( int i=0; i<crs.size(); i++ ) {
			  int bundleId = (int)session.save(crs.get(i));
			  session.save(crs.get(i));
	          crs.get(i).setId(bundleId);
		      if ( i % 20 == 0 ) { 
		        //flush a batch of inserts and release memory:
		        session.flush();
		        session.clear();
		    }
		}
		return crs;
	}
	
	public List<BundleTrainee> readAll() throws DatabaseException {
		return HibernateSession.loadAllData(BundleTrainee.class, session);
	}
	
	public BundleTrainee readByKnownId(int id) throws EntryNotFoundException, DatabaseException {
		return HibernateSession.loadByKnownId(BundleTrainee.class, id,session);
	}
	
//	public List<BundleConcept> readByConceptId(int concept_id) {
//	try {
//		return (List<BundleConcept>) session
//				.createQuery("from bundle_concept where concept_id =:concept_id", BundleConcept.class)
//				.setParameter("concept_id", concept_id)
//				.getResultList();
//	} 
//} This function isn't necessary yet, but I believe it will be in the future so it's already built. 
//=======
//				.createQuery("from bundle_trainee where user_id =:userId", BundleTrainee.class)
//				.setParameter("userId", userId)
//				.getResultList();
//} 
//>>>>>>> dbe9b30caaf93afe00897c6326269369b0f8ed6f
	
	public List<BundleTrainee> readByUserId(int userId) {
		return (List<BundleTrainee>) session
			.createQuery("from BundleTrainee BT where BT.user.id =:user_id", BundleTrainee.class)
			.setParameter("user_id", userId)
			.getResultList();
	}
	
	public void update(BundleTrainee bundleTrainee) throws DatabaseException {
		// TODO fix this met record based
		session.saveOrUpdate(bundleTrainee);
	}
	
	protected void delete() throws DatabaseException {
		
	}
	
}
