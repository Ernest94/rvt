package nu.educom.rvt.repositories;

import java.util.List;

import org.hibernate.Session;

import nu.educom.rvt.models.UserRelation;

/* JH: Voor link tabellen is doorgaans geen aparte repository, maar dit wordt in de andere repositories opgelost */
public class UserRelationRepository {
	protected Session session;
	
	public UserRelationRepository(Session session) {
		super();
		this.session = session;
	}

	public void create(UserRelation relation) throws DatabaseException {
	    session.save(relation); 
	}
	
	public UserRelation readById(int id) throws DatabaseException {
		return session.get(UserRelation.class, id);
	}
	
	public List<UserRelation> readAll() throws DatabaseException {
		return HibernateSession.loadAllData(UserRelation.class, session);
	}
	
	protected void update() throws DatabaseException {
		
	}
	
	protected void delete() throws DatabaseException {
		
	}
}
