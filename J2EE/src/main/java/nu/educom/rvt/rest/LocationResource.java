package nu.educom.rvt.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import nu.educom.rvt.models.Location;
import nu.educom.rvt.rest.filter.Secured;
import nu.educom.rvt.services.UserService;

@Path("webapi")
@Secured
public class LocationResource extends BaseResource {
	private static final Logger LOG = LogManager.getLogger();
	
	/* JH: Mis hier de GET op /locations om een lijst van alle lokaties te krijgen (nodig voor de dropdowns) */
	
	@POST
	@Path("/locations")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response saveLocation(Location location) {
		LOG.debug("saveLocation {} called", location);
		return wrapInSessionWithTransaction(session -> {
			UserService userServ = new UserService(session);
		
			boolean valid = userServ.validateLocation(location);
			if(valid) {
				userServ.addLocation(location);
				LOG.info("Location {} added", location);
				return Response.status(201).build();
			}
			else {
				return Response.status(400).build();
			}
		}); 
	}
	

}
