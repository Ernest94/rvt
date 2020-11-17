package nu.educom.rvt.rest;

import java.util.List;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import nu.educom.rvt.models.Location;

import nu.educom.rvt.services.UserService;

@Path("webapi")
public class LocationResource{
	
	@POST
	@Path("/locations")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response saveLocation(Location location) {
		UserService userServ = new UserService();
		boolean valid = userServ.validateLocation(location);
		if(valid) {
			userServ.addLocation(location);
			return Response.status(200).build();
		}
		else {
			return Response.status(400).build();
		}
	}
	

}
