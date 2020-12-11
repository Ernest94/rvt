package nu.educom.rvt.repositories;


import java.util.List;

import org.hibernate.Session;
import nu.educom.rvt.models.Bundle;

public class BundleRepository {

	protected Session session;
	
	public BundleRepository(Session session) {
		super();
		this.session = session;
	}

	public Bundle create(Bundle bundle) throws DatabaseException {
		if (!session.isOpen() || !session.getTransaction().isActive()) {
			throw new DatabaseException("Create called on an DB transaction that is not open");
		}
	    int bundleId = (int)session.save(bundle);
	    bundle.setId(bundleId);
		return bundle;
	}
	
	public List<Bundle> readAll() throws DatabaseException {
		return HibernateSession.loadAllData(Bundle.class, session);
	}
	
	public Bundle readById(int id) throws DatabaseException {
		if (!session.isOpen()) {
			throw new DatabaseException("Create called on an session that is not open");
		}
		return session.get(Bundle.class, id);
	}
	
	protected void update(Bundle bundle) throws DatabaseException {
		if (!session.isOpen() || !session.getTransaction().isActive()) {
			throw new DatabaseException("Create called on an DB transaction that is not open");
		}
		try {
			// TODO fix records structure here
		    session.saveOrUpdate(bundle);
		} catch (Exception e) { //TO DO: catch all the different exceptions: {f.e. HibernateException,RollbackException} 
			throw new DatabaseException(e);
		}
	}
	
	protected void delete() {
	}
}
