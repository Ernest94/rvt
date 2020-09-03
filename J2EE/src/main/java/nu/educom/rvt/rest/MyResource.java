package nu.educom.rvt.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("test")
public class MyResource {
	
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public Response testMethod() {
		if (Filler.isDatabaseEmpty()) {
			Filler.fillDatabase();
		}
		
		return Response.status(200).build();
	}
}
