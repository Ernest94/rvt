package nu.educom.rvt.repositories;

import java.util.List;

import org.hibernate.Session;

import nu.educom.rvt.models.Theme;

public class ThemeRepository {
	protected Session session;
	
	public ThemeRepository(Session session) {
		super();
		this.session = session;
	}

	public Theme create(Theme theme) throws DatabaseException {
		if (!session.isOpen() || !session.getTransaction().isActive()) {
			throw new DatabaseException("Create called on an DB transaction that is not open");
		}
	    int themeId = (int)session.save(theme);
	    theme.setId(themeId);
		return theme;
	}
	
	public List<Theme> readAll() throws DatabaseException {
		return HibernateSession.loadAllData(Theme.class, session);
	}
	
	
	public Theme readById(int id) throws DatabaseException {
		return session.get(Theme.class, id);
	}
	
	public Theme readByName(String name) throws DatabaseException {
		return (Theme) session
					.createQuery("from Theme where name =:name", Theme.class)
					.setParameter("name", name)
					.uniqueResultOptional().orElse(null);
	}
	
	protected void update() throws DatabaseException {
	}
	
	protected void delete() throws DatabaseException {
	}
}
