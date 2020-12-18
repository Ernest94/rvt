package nu.educom.rvt.repositories;

import java.util.List;

import org.hibernate.Session;

import nu.educom.rvt.models.Role;

public class RoleRepository {
	protected Session session;
	
	public RoleRepository(Session session) {
		super();
		this.session = session;
	}

	public void create(Role role) throws DatabaseException {
		if (!session.isOpen() || !session.getTransaction().isActive()) {
			throw new DatabaseException("Create called on an DB transaction that is not open");
		}
		session.save(role); 
	}
	
	public Role readById(int id) throws DatabaseException {
		if (!session.isOpen() || !session.getTransaction().isActive()) {
			throw new DatabaseException("ReadById called on an DB transaction that is not open");
		}
		return session.get(Role.class, id);
	}
	
	public List<Role> readAll() throws DatabaseException {
		return HibernateSession.loadAllData(Role.class, session);
	}
	
	protected void update() throws DatabaseException {
		
	}
	
	protected void delete() throws DatabaseException {
		
	}
	
}
