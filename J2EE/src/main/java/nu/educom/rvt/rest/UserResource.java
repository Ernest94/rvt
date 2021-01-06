package nu.educom.rvt.rest;

import java.util.List;
import java.util.Locale;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import nu.educom.rvt.models.Location;
import nu.educom.rvt.models.Role;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import nu.educom.rvt.models.User;
import nu.educom.rvt.models.view.PasswordChange;
import nu.educom.rvt.models.view.Search;
import nu.educom.rvt.models.view.UserLocationView;
import nu.educom.rvt.models.view.UserSearchJson;
import nu.educom.rvt.rest.filter.Secured;
import nu.educom.rvt.services.ReviewService;
import nu.educom.rvt.services.UserService;

@Path("/webapi")

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
	
	@POST
	@Secured
	@Path("/users")
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
			User createdUser = userServ.addUserAndLocations(user,locations);
			LOG.info("User {} has been created.", user);
			return Response.status(201).entity(createdUser).build();
		});
	}
	
	@GET
	@Secured
    @Path("/users/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserDossier(@PathParam("userId") int userId){
		LOG.trace("getUserDossier for user with id {} called", userId);
		return wrapInSession(session -> {
			UserService userServ = new UserService(session);
		    User user = userServ.getUserById(userId);

	    	LOG.debug("Dossier of {}", user);
	        return Response.status(200).entity(user).build();
		});
	}	
	
	@PUT
	@Secured
	@Path("/users/{userId}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateUser(@PathParam("userId") int userId, UserLocationView userLocationsView) {
		User user = userLocationsView.getUser();
		List<Location> locations = userLocationsView.getLocations();
		LOG.debug("updateUser called for {} on [{}]", user, locations);
			
		return wrapInSessionWithTransaction(session -> {
			UserService userServ = new UserService(session);
			boolean valid = userServ.validateUser(user);
			if (!valid) {
				return Response.status(412).build();	
			}
			User updatedUser = userServ.updateUser(user,locations);
			LOG.info("{} has been updated.", updatedUser);
			return Response.status(200).build();
		});
	}	
	
	@PUT
	@Secured
	@Path("/users/{userId}/password")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response changePassword(@PathParam("userId") int userId, PasswordChange change) {
		LOG.debug("changePassword called for user id {}", userId);
		return wrapInSessionWithTransaction(session -> {
			UserService userServ = new UserService(session);
			User changedUser;
			//if someone else than the user in the URI is requesting the password change, check if this user is an admin
			if(change.getRequester()!=null && change.getRequester().getId()!=userId) {
				User requester = userServ.getUserById(change.getRequester().getId());
				if(requester.getRole().getId()!=1) { //Role 1 is Admin
					return Response.status(401).build();
				}
				User foundUser = userServ.getUserById(userId);
				changedUser = userServ.changePassword(foundUser, change.getNewPassword());

			}
			//if the requester is the same check if the current password is correct
			else {
				User foundUser = userServ.checkUserPasswordById(userId, change.getCurrentPassword());
				if (foundUser == null) { 
					return Response.status(401).build();
				}
				changedUser = userServ.changePassword(foundUser, change.getNewPassword());
			}
			LOG.info("Password changed for user {}.", changedUser);
			return Response.status(200).entity(changedUser).build();
		});
	}
	
	@GET
	@Secured
	@Path("/roles")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getRoles() {
		LOG.trace("getRoles called");
		return wrapInSession(session -> {
			UserService userServ = new UserService(session);
			List<Role> roles = userServ.getRoles();	
						
			return Response.status(200).entity(roles).build();
		});
	}
		
	@POST
	@Secured
	@Path("/users/search")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUsers(Search search) {
		LOG.trace("getUsers {} from {} and criteria '{}'", 
				   search.getRole(), search.getLocations(), search.getCriteria());
		return wrapInSession(session -> {
			UserService userServ = new UserService(session);
			List<User> searchResult = userServ.getFilteredUsers(search.getCriteria().toLowerCase(Locale.ROOT), search.getRole(), search.getLocations());
			UserSearchJson USJ = userServ.convertToUSJ(searchResult);			
			
			return Response.status(200).entity(USJ).build();
		});
	}
	
	@GET
	@Path("/users/pending/teachers/{teacherId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllUsersWithPendingReviews(@PathParam("teacherId") int teacherId) {
		LOG.debug("getAllUsersWithPendingReviews called");
		return wrapInSession(session -> {
			UserService userServ = new UserService(session); /* JH: Should be a UserLogic class */
			ReviewService reviewServ = new ReviewService(session);
			User docent = userServ.getUserById(teacherId);
			List<User> foundUsers = reviewServ.getAllUsersWithPendingReviews(docent);
			UserSearchJson USJ = userServ.convertToUSJ(foundUsers); 
		
			return Response.status(200).entity(USJ).build();
		});
	}
		

}
