package nu.educom.rvt.repositories;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.function.BiPredicate;
import java.util.function.Function;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;

import com.mysql.cj.exceptions.UnableToConnectException;

import nu.educom.rvt.models.BaseEntity;
import nu.educom.rvt.models.Bundle;
import nu.educom.rvt.models.BundleConcept;
import nu.educom.rvt.models.BundleTrainee;
import nu.educom.rvt.models.Concept;
import nu.educom.rvt.models.ConceptRating;
import nu.educom.rvt.models.Location;
import nu.educom.rvt.models.ReadOnlyEntity;
import nu.educom.rvt.models.Review;
import nu.educom.rvt.models.Role;
import nu.educom.rvt.models.Theme;
import nu.educom.rvt.models.TraineeActive;
import nu.educom.rvt.models.TraineeMutation;
import nu.educom.rvt.models.User;
import nu.educom.rvt.models.UserLocation;

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
		    configuration.addAnnotatedClass(UserLocation.class);
		    configuration.addAnnotatedClass(Location.class);
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
        return getSessionFactory().openSession();
    }
    
    public static Session openSessionAndTransaction() {
        Session session = openSession();
        session.beginTransaction();
        return session;
    }
    
	public static void shutDown() {
		getSessionFactory().close();
	}

	/**
	 * Get an known entity by id
	 * 
	 * @param <T> the type of the entity
	 * @param type the type of the entity
	 * @param id the id
	 * @param session the session
	 * @return the initialized and attached entry
	 * @throws EntryNotFoundException when entry is missing
	 * @throws DatabaseException when session is open
	 */
	public static <T extends BaseEntity> T loadByKnownId(Class<T> type, int id, Session session) throws EntryNotFoundException, DatabaseException {
	    if (session == null || !session.isOpen()) {
            throw new DatabaseException("Load by known id " + id + " of type " + type.getSimpleName() + " called on an session that is not open");
    	}
	    T entry = session.get(type, id);
	    if (entry == null) {
	    	throw new EntryNotFoundException(type, id);
	    }
	    return entry;
	}
	
    public static <T> List<T> loadAllData(Class<T> type, Session session) throws DatabaseException {
	    if (session == null || !session.isOpen()) {
            throw new DatabaseException("Load all data of type " + type.getSimpleName() + " called on an session that is not open");
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
//    /**
//	 * Return a list of all currently active entries in the database
//	 * @param <T> the type of the entries
//	 * @param type the type of the entry
//	 * @param session the session
//	 * @return a List of all entries that have no end date 
//     * @throws DatabaseException 
//	 */	
//	public static <T extends ReadOnlyEntity> List<T> loadAllCurrentData(Class<T> type, Session session) throws DatabaseException {
//		 if (session == null || !session.isOpen()) {
//	         throw new DatabaseException("Load all current data of type " + type.getName() + " called on an session that is not open");
//		 }
//		 try {
//			CriteriaBuilder builder = session.getCriteriaBuilder();
//		    CriteriaQuery<T> criteria = builder.createQuery(type);
//		    Root<T> root = criteria.from(type);
//			criteria.where(builder.isNull(root.get("endDate")));
//		    List<T> data = session.createQuery(criteria).getResultList();
//		    return data;
//		} catch (Exception e) {
//		    throw new DatabaseException(e);
//		}
//	}
//	
//	/**
//	 * Return the currently active entry in the database
//	 * @param <T> the type of the entry
//	 * @param type the type of the entry
//	 * @param session the session
//	 * @return a List of all entries that have no end date 
//	 * @throws DatabaseException on errors
//	 */	
//	public static <T extends ReadOnlyEntity> T loadCurrentData(Class<T> type, Session session) throws DatabaseException {
//		 if (session == null || !session.isOpen()) {
//	         throw new DatabaseException("Load all data of type " + type.getName() + " called on an session that is not open");
//	     }
//		 try {
//			CriteriaBuilder builder = session.getCriteriaBuilder();
//		    CriteriaQuery<T> criteria = builder.createQuery(type);
//		    Root<T> root = criteria.from(type);
//			criteria.where(builder.isNull(root.get("endDate")));
//		    return session.createQuery(criteria).uniqueResultOptional().orElse(null);
//		} catch (Exception e) {
//		    throw new DatabaseException(e);
//		}
//	}
//		
//	/**
//	 * Return a list of all active entries in the database at a specific moment in time
//	 * @param <T> the type of the entries
//	 * @param type the type of the entry
//	 * @param session the session
//	 * @param date the date to get the list for
//	 * @return a List of all entries that have a start date in the past and have no end date 
//	 * @throws DatabaseException 
//	 */	
//	public static <T extends ReadOnlyEntity> List<T> loadAllDataAtSpecificDate(Class<T> type, Session session, LocalDate date) throws DatabaseException {
//		 if (session == null || !session.isOpen()) {
//	         throw new DatabaseException("Load all data of type " + type.getName() + " called on an session that is not open");
//	     }
//		 try{
//	    	CriteriaBuilder builder = session.getCriteriaBuilder();
//		    CriteriaQuery<T> criteria = builder.createQuery(type);
//		    Root<T> root = criteria.from(type);
//			criteria.where(builder.and(builder.lessThanOrEqualTo(root.get("startDate"), date)),  // StartDate <= date
//						   			   builder.or(
//						   					   builder.isNull(root.get("endDate")),              // endDate is null -or-
//						   					   builder.greaterThan(root.get("endDate"), date))); // endDate > date
//		    List<T> data = session.createQuery(criteria).getResultList();
//		    return data;
//	    } catch (Exception e) {
//		    throw new DatabaseException(e);
//		}
//	}
//	
//	/**
//	 * Return an entry in the database at a specific moment in time
//	 * @param <T> the type of the entries
//	 * @param type the type of the entry
//	 * @param session the session
//	 * @param date the date to get the list for
//	 * @return a List of all entries that have a start date in the past and have no end date 
//	 * @throws DatabaseException 
//	 */	
//	public static <T extends ReadOnlyEntity> T loadDataAtSpecificDate(Class<T> type, Session session, LocalDate date) throws DatabaseException {
//		 if (session == null || !session.isOpen()) {
//	         throw new DatabaseException("Load data at date " + date + " of type " + type.getName() + " called on an session that is not open");
//	     }
//		 try {
//	    	CriteriaBuilder builder = session.getCriteriaBuilder();
//		    CriteriaQuery<T> criteria = builder.createQuery(type);
//		    Root<T> root = criteria.from(type);
//			criteria.where(builder.and(builder.lessThanOrEqualTo(root.get("startDate"), date)),  // StartDate <= date
//						   			   builder.or(
//						   					   builder.isNull(root.get("endDate")),              // endDate is null -or-
//						   					   builder.greaterThan(root.get("endDate"), date))); // endDate > date
//		    return session.createQuery(criteria).uniqueResultOptional().orElse(null);
//	    } catch (Exception e) {
//		    throw new DatabaseException(e);
//		}
//	}
//	
//	/**
//	 * update an entry if the start date is the same, 
//	 * or close the previous entry and create a new entry with the given record on the new start date
//	 * 
//	 * @param <T> the type of the entry
//	 * @param oldId the id of the old entry
//	 * @param type the type of the entry
//	 * @param newEntry the new created entry with the new start date (NOTE: NOT LOADED FROM THE REPOSITORY!)
//	 * @param session the session
//	 * 
//	 * @return the new or updated entry
//	 * 
//	 * @throws DatabaseException when the new entry is before the current one
//	 */
//	public static <T extends ReadOnlyEntity> T updateReadOnlyEntry(Class<T> type, int oldId, T newEntry, Session session) throws DatabaseException {
//		if (!session.isOpen() || !session.getTransaction().isActive()) {
//			throw new DatabaseException("Create called on an DB transaction that is not open");
//		}
//		T oldEntry = session.load(type, oldId);
//	    int dateDiff = oldEntry.getStartDate().compareTo(newEntry.getStartDate());
//	    
//		if (dateDiff < 0) { 
//			throw new DatabaseException("Cannot update, record of " + type.getName() + " with id " + oldId + " has a startdate after newEntry");
//		} else if (dateDiff == 0) { // Same start date, replace the current record
//	    	newEntry.setId(oldId);
//	    } else { // new start date, close the old record, and save the new record
//	    	oldEntry.setEndDate(newEntry.getStartDate()); // set the end date, this will automatically update
//	    	newEntry.setId(0); // make sure it has no id already
//	    }
//    	session.saveOrUpdate(newEntry);
//		return newEntry;
//	}

	public static <T extends ReadOnlyEntity, V> void updateReadOnlyEntries(List<T> allLinkTableEntries, 
																		   List<V> newViewEntries,
																		   BiPredicate<T, V> comparer, 
																		   Function<V,T> creator,
																		   Session session) {
		// create copy of newEntries list
		List<V> newEntries = new ArrayList<V>();
		newEntries.addAll(newViewEntries);
		
		for (T entry : allLinkTableEntries) {		
			if (entry.getEndDate() != null) {
				// Skip older entries
				continue;
			}
			V found = newEntries.stream().filter(newEntry -> comparer.test(entry, newEntry)).findFirst().orElse(null);
			if (found == null) {
				// No longer part of our list, close current record
				entry.setEndDate(LocalDate.now());
			} else {
				// Keep current record and remove from the list
				newEntries.remove(found);
			}
		}
		for (V newEntry : newEntries) {
			T newRecord = creator.apply(newEntry);
			session.save(newRecord);
			allLinkTableEntries.add(newRecord);
		}
	}
}
