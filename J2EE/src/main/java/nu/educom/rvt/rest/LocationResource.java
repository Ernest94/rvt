package nu.educom.rvt.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

import nu.educom.rvt.models.Location;
import nu.educom.rvt.repositories.DatabaseException;
import nu.educom.rvt.repositories.HibernateSession;
import nu.educom.rvt.services.UserService;

@Path("webapi")
public class LocationResource{
	private static final Logger LOG = LogManager.getLogger();
	
	/* JH: Mis hier de GET op /locations om een lijst van alle lokaties te krijgen (nodig voor de dropdowns) */
	
	@POST
	@Path("/locations")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response saveLocation(Location location) {
		LOG.debug("saveLocation {} called", location);
		Session session = null;
		try {
			session = HibernateSession.openSessionAndTransaction();
			UserService userServ = new UserService(session);
		
			boolean valid = userServ.validateLocation(location);
			if(valid) {
				userServ.addLocation(location);
				session.getTransaction().commit();
				LOG.info("Location {} added", location);
				return Response.status(201).build();
			}
			else {
				return Response.status(400).build();
			}
		} catch (DatabaseException e) {
			LOG.error("safe location failed", e);
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
