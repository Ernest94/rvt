package nu.educom.rvt.repositories;

import java.util.List;

import org.hibernate.Session;

import nu.educom.rvt.models.TraineeMutation;

public class TraineeMutationRepository {
	private Session session;
	
	public TraineeMutationRepository(Session session) {
		this.session = session;
	}

	public void create(TraineeMutation traineeMutation) throws DatabaseException {
	    session.save(traineeMutation); 
	}
	
	public TraineeMutation readById(int id) throws DatabaseException {
		return session.get(TraineeMutation.class, id);
	}
	
	protected void update() throws DatabaseException {
	}
	
	protected void delete() throws DatabaseException {	
	}
	
	public List<TraineeMutation> readAll() throws DatabaseException {
		return HibernateSession.loadAllData(TraineeMutation.class, session);
	}
}
