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
import org.hibernate.Session;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import nu.educom.rvt.models.User;
import nu.educom.rvt.models.view.LinkJson;
import nu.educom.rvt.models.view.RoleLocationJson;
import nu.educom.rvt.models.view.UserSearchJson;
import nu.educom.rvt.repositories.DatabaseException;
import nu.educom.rvt.repositories.HibernateSession;
import nu.educom.rvt.models.PasswordChange;
import nu.educom.rvt.models.Role;
import nu.educom.rvt.models.Search;
import nu.educom.rvt.models.LinkedUsers;
import nu.educom.rvt.models.Location;
import nu.educom.rvt.services.UserService;

@Path("webapi/user")
public class UserResource {

	private static final Logger LOG = LogManager.getLogger();
  
	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response login(User user) {
		LOG.debug("login {} called", user);
		try (Session session = HibernateSession.openSession()) {
			UserService userServ = new UserService(session);
			User foundUser = userServ.checkUser(user);
			if (foundUser != null) {
				return Response.status(200).entity(foundUser).build();
			}
			else {
				return Response.status(401).build();
			}
		} catch (DatabaseException e) {
			LOG.error("login check failed", e);
			return Response.status(500).build();
		}
	}
	
	@POST
	@Path("/password")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response changePassword(PasswordChange change) {
		LOG.debug("changePassword called for user id {}", change.getUserId());
		Session session = null;
		try {
			session = HibernateSession.openSessionAndTransaction();
			UserService userServ = new UserService(session);
			User foundUser = userServ.checkUserPasswordById(change.getUserId(), change.getCurrentPassword());
			if (foundUser != null) { /* JH TIP: Invert the if */
				userServ.changePassword(foundUser, change.getNewPassword());
				session.getTransaction().commit();
				LOG.info("Password changed for user {}.", foundUser);
				return Response.status(200).entity(foundUser).build();
			}
			return Response.status(401).build();
		} catch (DatabaseException e) {
			LOG.error("change bundel failed", e);
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
	
	@GET
	@Path("/roles")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getRoles() {
		LOG.trace("getRoles called");
//		if (Filler.isDatabaseEmpty()) {
//			Filler.fillDatabase();
//		}
		try (Session session = HibernateSession.openSession()) {
			UserService userServ = new UserService(session);
			List<Role> roles = userServ.getRoles();	
			List<Location> locations = userServ.getLocations();
			RoleLocationJson rlJson = new RoleLocationJson() ;
			rlJson.setRoles(roles);
			rlJson.setLocations(locations);
						
			return Response.status(200).entity(rlJson).build();
		} catch (DatabaseException e) {
			LOG.error("login check failed", e);
			return Response.status(500).build();
		}
	}
		
	@POST
	@Path("/create")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createUser (User user) {
		LOG.debug("createUser called for user {}", user.getName());
		Session session = null;
		try {
			session = HibernateSession.openSessionAndTransaction();
			UserService userServ = new UserService(session);
			boolean valid = userServ.validateUser(user);
		
			if (!valid) {
				return Response.status(412).build();
			}
			userServ.addUser(user);
			session.getTransaction().commit();
			LOG.info("User {} has been created.", user);
			return Response.status(201).build();
		} catch (DatabaseException e) {
			LOG.error("change bundel failed", e);
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

	@GET
	@Path("/linking")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllRelations(@HeaderParam("userId") int userId){
		LOG.trace("getAllReleations of {} called", userId);
		try (Session session = HibernateSession.openSession()) {
			UserService userServ = new UserService(session);//load injectables
	        User user = userServ.getUserById(userId);
	        List<User> connectedUsers = userServ.getConnectedUsers(user);
	        List<User> possibleRelatedUsers = userServ.getPossibleRelations(user);
	        List<LinkedUsers> linkedUsers = userServ.combineUsers(user, connectedUsers, possibleRelatedUsers);
	        LinkJson linkJson = new LinkJson(user, linkedUsers);
	        
	        boolean valid = true;
	        
	        if(valid) {
	            return Response.status(200).entity(linkJson).build();
	        } else {
	            return Response.status(404).build();	    
	        }
		} catch (DatabaseException e) {
			LOG.error("get All releations failed", e);
			return Response.status(500).build();
		}
	}
	
	@POST
	@Path("/search")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUsers(Search search) {
		LOG.trace("getUsers located {}, of role {} and criteria {}", 
				   search.getRole(), search.getLocations(), search.getCriteria());
		try (Session session = HibernateSession.openSession()) {
			UserService userServ = new UserService(session);
			List<User> searchResult = userServ.getFilteredUsers(search.getCriteria(), search.getRole(), search.getLocations());
			UserSearchJson USJ = userServ.convertToUSJ(searchResult);			
			
			return Response.status(200).entity(USJ).build();
		} catch (DatabaseException e) {
			LOG.error("get users failed", e);
			return Response.status(500).build();
		}
	}
	
	@GET
	@Path("/users")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllUsers() {
		LOG.trace("getAllUsers called");
		try (Session session = HibernateSession.openSession()) {
			UserService userServ = new UserService(session);
			List<User> users = userServ.getAllUsers();
	  
		  return Response.status(200).entity(users).build();
		} catch (DatabaseException e) {
			LOG.error("login check failed", e);
			return Response.status(500).build();
		}
	}
	
	@GET
    @Path("/UserRelations")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllRelations(){
//		LOG.trace("getAllRelations called");
//		try (Session session = HibernateSession.openSession()) {
//			UserService userServ = new UserService(session);//load injectables
//			User user = userServ.getUserById(1);
//      
//		    boolean valid = true;
//		      
//		    if(valid) {
//		       return Response.status(200).entity(user).build();
//		    } else {
//		       return Response.status(400).build();        
//		    }
//		} catch (DatabaseException e) {
//			LOG.error("getAllRelations failed", e);
//			return Response.status(500).build();
//		}
		return Response.status(410).build();
    }
		
	@GET
    @Path("/dossier")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserDossier(@HeaderParam("UserId") int userId ){
		LOG.trace("getUserDossier for user with id {} called");
		try (Session session = HibernateSession.openSession()) {
			UserService userServ = new UserService(session);//load injectables
		    User user = userServ.getUserById(userId);
		      
		    if(user != null) {
		    	LOG.debug("Dossier of user {}", user);
		        return Response.status(200).entity(user).build();
		    } else {
		        return Response.status(400).build();        
		    }
		} catch (DatabaseException e) {
			LOG.error("get User Dosssier failed", e);
			return Response.status(500).build();
		}
	}	
	
	@PUT
	@Path("/dossier")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateUser(User user) {
		LOG.debug("updateUser called for user id {}", user.getId());
		Session session = null;
		try {
			session = HibernateSession.openSessionAndTransaction();
			UserService userServ = new UserService(session);
			User foundUser = userServ.getUserById(user.getId());
			if(foundUser==null) {
				return Response.status(404).build();  
			}
			else {
				userServ.updateUser(user);
				session.getTransaction().commit();
				LOG.info("User {} has been updated.", foundUser);
				return Response.status(200).build();
			}
		} catch (DatabaseException e) {
			LOG.error("change user failed", e);
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
