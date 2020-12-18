package nu.educom.rvt.rest;

import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

import nu.educom.rvt.repositories.EntryNotFoundException;
import nu.educom.rvt.repositories.HibernateSession;

public class BaseResource {
	private static final Logger LOG = LogManager.getLogger();
	
	/**
	 * Generate a Read-Only session and execute inner Lambda with the this session<br/>
	 * If an exception occurs, the exception is logged and Response code "500" is returned 
	 * 
	 * @param func the lambda function
	 * @return the response
	 */
	public Response wrapInSession(FunctionWithSession func) {
		try (Session session = HibernateSession.openSession()) {
			session.setDefaultReadOnly(true);
			return func.Execute(session);
		} catch (EntryNotFoundException e) {
			LOG.warn(e.getMessage());
			return Response.status(404).build();
		} catch (Exception e) {
			LOG.error("failed to execute", e);
			return Response.status(500).build();
		}
	}

	/**
	 * Generate a Read-Write session and execute inner Lambda with the this session<br/>
	 * - If no exceptions occur, all changes are committed to the database<br/>
	 * - If an exception occurs, all changes made are reversed (rolled-back), the exception is logged and Response code "500" is returned  
	 * 
	 * @param func the lambda function
	 * @return the response
	 */
	public Response wrapInSessionWithTransaction(FunctionWithSession func) {
		Session session = null;
		try {
			session = HibernateSession.openSessionAndTransaction();
			Response Result = func.Execute(session);
			session.getTransaction().commit();
			return Result;
		} catch (EntryNotFoundException e) {
			LOG.warn(e.getMessage());
			if (session != null && session.getTransaction().isActive()) {
				session.getTransaction().rollback();
			}
			return Response.status(404).build();
		} catch (Exception e) {
			LOG.error("failed to execute", e);
			if (session != null && session.getTransaction().isActive()) {
				session.getTransaction().rollback();
			}
			return Response.status(500).build();
		} finally {
			if (session != null) { 
				session.close();
			}
		}
	}
}
