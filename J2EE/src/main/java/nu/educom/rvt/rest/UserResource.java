package nu.educom.rvt.rest;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import nu.educom.rvt.models.Location;
import nu.educom.rvt.models.PasswordChange;
import nu.educom.rvt.models.Role;
import nu.educom.rvt.models.Search;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import nu.educom.rvt.models.User;
import nu.educom.rvt.models.view.RoleLocationJson;
import nu.educom.rvt.models.view.UserLocationView;
import nu.educom.rvt.models.view.UserSearchJson;
import nu.educom.rvt.rest.filter.Secured;
import nu.educom.rvt.services.UserService;

@Path("webapi/user")

public class UserResource extends BaseResource {

	private static final Logger LOG = LogManager.getLogger();
  
	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response login(User user) {
		LOG.debug("login {} called", user);
		System.out.print("\n" + "login is called" + "\n");

		return wrapInSession(session -> {
			try {
				UserService userServ = new UserService(session);
				User foundUser = userServ.checkUser(user);
				String token = userServ.issueToken(foundUser);
				//TODO: token is not entity, how to send string, not object?
				return Response.status(200).entity(token).build();
			}
			catch (Exception e){
				LOG.error("Login failed: " + e.getMessage());
				return Response.status(401).build();
			}
		});
	}
	
	@POST
	@Secured
	@Path("/password")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response changePassword(PasswordChange change) {
		LOG.debug("changePassword called for user id {}", change.getUserId());
		return wrapInSessionWithTransaction(session -> {
			UserService userServ = new UserService(session);
			User foundUser = userServ.checkUserPasswordById(change.getUserId(), change.getCurrentPassword());
			if (foundUser != null) { /* JH TIP: Invert the if */
				User changedUser = userServ.changePassword(foundUser, change.getNewPassword());
				LOG.info("Password changed for user {}.", changedUser);
				return Response.status(200).entity(changedUser).build();
			}
			return Response.status(401).build();
		});
	}
	
	@GET
	@Secured
	@Path("/roles")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getRoles() {
		LOG.trace("getRoles called");
//		if (Filler.isDatabaseEmpty()) {
//			Filler.fillDatabase();
//		}
		return wrapInSession(session -> {
			UserService userServ = new UserService(session);
			List<Role> roles = userServ.getRoles();	
			List<Location> locations = userServ.getLocations();
			RoleLocationJson rlJson = new RoleLocationJson() ;
			rlJson.setRoles(roles);
			rlJson.setLocations(locations);
						
			return Response.status(200).entity(rlJson).build();
		});
	}
		
	@POST
	@Secured
	@Path("/create")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createUser (UserLocationView userLocationsView) {
		LOG.debug("createUser called for user {}", userLocationsView.getUser().getName());
		User user = userLocationsView.getUser();
		List<Location> locations = userLocationsView.getLocations();
		return wrapInSessionWithTransaction(session -> {
			UserService userServ = new UserService(session);
			boolean valid = userServ.validateUser(user);
			if (!valid) {
				return Response.status(412).build();
			}
			userServ.addUserAndLocations(user,locations);
			LOG.info("User {} has been created.", user);
			return Response.status(201).build();
		});
	}
	
	
	@POST
	@Secured
	@Path("/search")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUsers(Search search) {
		LOG.trace("getUsers {} from {} and criteria '{}'", 
				   search.getRole(), search.getLocations(), search.getCriteria());
		return wrapInSession(session -> {
			UserService userServ = new UserService(session);
			List<User> searchResult = userServ.getFilteredUsers(search.getCriteria(), search.getRole(), search.getLocations());
			UserSearchJson USJ = userServ.convertToUSJ(searchResult);			
			
			return Response.status(200).entity(USJ).build();
		});
	}
	
	@GET
	@Secured
	@Path("/users")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllUsers() {
		LOG.trace("getAllUsers called");
		return wrapInSession(session -> {
			UserService userServ = new UserService(session);
			List<User> users = userServ.getAllUsers();
		  return Response.status(200).entity(users).build();
		});
	}
		
	@GET
	@Secured
    @Path("/dossier")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserDossier(@HeaderParam("UserId") int userId ){ /* JH: Had hier @PathParam verwacht */
		LOG.trace("getUserDossier for user with id {} called", userId);
		return wrapInSession(session -> {
			UserService userServ = new UserService(session);//load injectables
		    User user = userServ.getUserById(userId);

	    	LOG.debug("Dossier of {}", user);
	        return Response.status(200).entity(user).build();
		});
	}	
	
	@PUT
	@Secured
	@Path("/dossier")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateUser(UserLocationView userLocationsView) {
		User user = userLocationsView.getUser();
		List<Location> locations = userLocationsView.getLocations();
		LOG.debug("updateUser called for {} on [{}]", user, locations);
			
		return wrapInSessionWithTransaction(session -> {
			UserService userServ = new UserService(session);
			User updatedUser = userServ.updateUser(user,locations);
			LOG.info("{} has been updated.", updatedUser);
			return Response.status(200).build();
		});
	}	
}
