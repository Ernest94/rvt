package nu.educom.rvt.rest;

import javax.ws.rs.core.Response;

import org.hibernate.Session;

import nu.educom.rvt.repositories.DatabaseException;

public interface FunctionWithSession {

	/**
	 * Within this function a active session is present and commit the session afterwards
	 * @param session the session
	 * @return the response code
	 * @throws an DatabaseException when the session should not commit
	 */
	Response Execute(Session session) throws DatabaseException;
}
