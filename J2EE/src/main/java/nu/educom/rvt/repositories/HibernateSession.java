package nu.educom.rvt.repositories;


import java.util.List;
import java.util.Properties;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;

import com.mysql.cj.exceptions.UnableToConnectException;

import nu.educom.rvt.models.*;

public class HibernateSession {

    protected static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {

        if (sessionFactory == null) {

            // Hibernate settings equivalent to hibernate.cfg.xml's properties

            Properties settings = new Properties();

            settings.put(Environment.DRIVER, "com.mysql.cj.jdbc.Driver");

            settings.put(Environment.URL, "jdbc:mysql://localhost:3306/db_voortgang?createDatabaseIfNotExist=true&serverTimezone=Europe/Amsterdam");
//            settings.put(Environment.URL, "jdbc:mysql://localhost:3306/educom_rvt_2?createDatabaseIfNotExist=true&serverTimezone=UTC");

            settings.put(Environment.USER, "usr_voortgang");
            
            settings.put(Environment.PASS, "?120qhZl");

            settings.put(Environment.SHOW_SQL, "true");

            settings.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");

            settings.put(Environment.HBM2DDL_AUTO, "update");

            connectToHibernate(settings);
        }
        return sessionFactory;
    }

	protected static void connectToHibernate(Properties settings) {
		try {

		    Configuration configuration = new Configuration();
		    configuration.setProperties(settings);
		    
		    configuration.addAnnotatedClass(Role.class);
		    configuration.addAnnotatedClass(User.class);
		    configuration.addAnnotatedClass(Location.class);
		    configuration.addAnnotatedClass(UserRelation.class);
		    configuration.addAnnotatedClass(Theme.class);
		    configuration.addAnnotatedClass(Concept.class);                
		    configuration.addAnnotatedClass(Review.class);
		    configuration.addAnnotatedClass(Bundle.class);
		    configuration.addAnnotatedClass(BundleTrainee.class);
		    configuration.addAnnotatedClass(BundleConcept.class);
		    configuration.addAnnotatedClass(TraineeActive.class);
		    configuration.addAnnotatedClass(TraineeMutation.class);
		    configuration.addAnnotatedClass(ConceptRating.class);
		    
		    ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
		    		.applySettings(configuration.getProperties()).build();

		    sessionFactory = configuration.buildSessionFactory(serviceRegistry);
		} catch (Exception e) {
		    e.printStackTrace();
		    throw new UnableToConnectException();
		}
	}
	    
    public static Session openSession() {
        return sessionFactory.openSession();
    }
    
    public static Session openSessionAndTransaction() {
        Session session = openSession();
        session.beginTransaction();
        return session;
    }
    
	public static void shutDown() {
		getSessionFactory().close();
	}

    public static <T> List<T> loadAllData(Class<T> type, Session session) throws DatabaseException {
	    if (session == null || !session.isOpen()) {
            throw new DatabaseException("Load all data of type " + type.getName() + " called on an session that is not open");
    	}

	    try {
		    CriteriaBuilder builder = session.getCriteriaBuilder();
		    CriteriaQuery<T> criteria = builder.createQuery(type);
		    criteria.from(type);
		    List<T> data = session.createQuery(criteria).getResultList();
		    return data;
		} catch (Exception e) {
		    throw new DatabaseException(e);
		}
    }
}
