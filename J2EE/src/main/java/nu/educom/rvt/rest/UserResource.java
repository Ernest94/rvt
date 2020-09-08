package nu.educom.rvt.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import nu.educom.rvt.models.User;
import nu.educom.rvt.models.PasswordChange;
import nu.educom.rvt.services.UserService;

@Path("user")
public class UserResource {

	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response login(User user) {
		if (Filler.isDatabaseEmpty()) {
			Filler.fillDatabase();
		}
		UserService userServ = new UserService();
		User foundUser = userServ.checkUser(user);
		if (foundUser != null) {
			return Response.status(200)
					.entity(foundUser).build();
		}
		else {
			return Response.status(401)
					.build();
		}
		
	}
	
	@POST
	@Path("/password")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response changePassword(PasswordChange change) {
		UserService userServ = new UserService();
		User foundUser = userServ.checkUserPasswordById(change.getUserId(), change.getCurrentPassword());
		if (foundUser != null) {
			User changedUser = userServ.changePassword(foundUser, change.getNewPassword());
			if (changedUser != null) {
				return Response.status(200)
						.entity(changedUser).build();
			}
		}
		return Response.status(401).build();
	}
}
