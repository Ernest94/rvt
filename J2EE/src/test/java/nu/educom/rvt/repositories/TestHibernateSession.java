package nu.educom.rvt.repositories;

import java.util.Properties;

import org.hibernate.Session;
import org.hibernate.cfg.Environment;

import org.junit.jupiter.api.Assertions;

public class TestHibernateSession extends HibernateSession {

	public static void switchToTestDatabase() {
        sessionFactory = null; // Remove factory of last test.
        
		Properties settings = new Properties();

		settings.put(Environment.DIALECT, "org.hibernate.dialect.HSQLDialect");
		
        settings.put(Environment.DRIVER, "org.hsqldb.jdbcDriver");

        settings.put(Environment.URL, "jdbc:hsqldb:mem:db_voortgang");//?createDatabaseIfNotExist=true&serverTimezone=UTC");

        settings.put(Environment.USER, "sa");
        
        settings.put(Environment.PASS, "");

        settings.put(Environment.SHOW_SQL, "true");

        settings.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");

        settings.put(Environment.HBM2DDL_AUTO, "create");

        connectToHibernate(settings);
    }

	public static void closeSession(Session session) {
		if (session.isOpen()) {
			if (session.getTransaction().isActive()) {
				session.getTransaction().rollback();
			}
			session.close();
		}
	}
}
