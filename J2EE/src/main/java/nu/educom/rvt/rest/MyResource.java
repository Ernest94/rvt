package nu.educom.rvt.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import nu.educom.rvt.models.User;
import nu.educom.rvt.repositories.UserRepository;

@Path("test")
public class MyResource {
	
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String testMethod() {
		if (Filler.isDatabaseEmpty()) {
			Filler.fillDatabase();
		}
		UserRepository userRepo = new UserRepository();
		User user = userRepo.readById(1);
		return "It works " + user.getName();
	}
}
