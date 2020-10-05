package nu.educom.rvt.rest;

import java.util.List;
import java.util.logging.*;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import nu.educom.rvt.models.User;
import nu.educom.rvt.models.view.RoleLocationJson;
import nu.educom.rvt.models.view.UserSearchJson;
import nu.educom.rvt.models.PasswordChange;
import nu.educom.rvt.models.Role;
import nu.educom.rvt.models.Search;
import nu.educom.rvt.models.Location;
import nu.educom.rvt.services.UserService;

@Path("webapi/user")
public class UserResource {

//  Logger log = LoggerFactory.getLogger(UserResource.class);
  

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
	
	@GET
	@Path("/test")
	@Produces(MediaType.APPLICATION_JSON)
	public Response test() {
		return Response.status(200).entity(new Role("test")).build();
	}
	
	@GET
	@Path("/roles")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getRoles() {
		if (Filler.isDatabaseEmpty()) {
			Filler.fillDatabase();
		}
		UserService userServ = new UserService();
		List<Role> roles = userServ.getRoles();	
		List<Location> locations = userServ.getLocations();
		RoleLocationJson rlJson = new RoleLocationJson() ;
		rlJson.setRoles(roles);
		rlJson.setLocations(locations);
					
		return Response.status(200)
					   .entity(rlJson).build();
	}
	
	@POST
	@Path("/create")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createUser (User user) {
		UserService userServ = new UserService();
		
		boolean valid = userServ.validateUser(user);
		
		if(valid) userServ.addUser(user);
		else return Response.status(412).build();
		
		return Response.status(201).build();

	}

	@GET
	@Path("/linking")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllRelations(@HeaderParam("userId") int userId){
	  UserService userServ = new UserService();//load injectables
	  User user = userServ.getUserById(userId);
	  
	  boolean valid = true;
	  
	  if(valid) {
        List<User> connectedUsers = userServ.getConnectedUsers(user);
        return Response.status(200).entity(connectedUsers).build();
	  } else {
        return Response.status(404).build();	    
	  }
	}
	
	@POST
	@Path("/search")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUsers(Search search ) {
		System.out.println(search);
		UserService userServ = new UserService();
		List<User> searchResult = userServ.getFilteredUsers(search.getCriteria(), search.getRole(), search.getLocation());
		UserSearchJson USJ = userServ.convertToUSJ(searchResult);			
		
		return Response.status(200).entity(USJ).build();
	}
	
	@GET
    @Path("/UserRelations")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllRelations(){
      UserService userServ = new UserService();//load injectables
      User user = userServ.getUserById(1);
      
      boolean valid = true;
      
      if(valid) {
        return Response.status(200).entity(user).build();
      } else {
        return Response.status(400).build();        
      }
    }
		
	@GET
    @Path("/dossier")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserDossier(@HeaderParam("UserId") int userId ){
      UserService userServ = new UserService();//load injectables
      User user = userServ.getUserById(userId);
      
      boolean valid = true;
      
      if(valid) {
        return Response.status(200).entity(user).build();
      } else {
        return Response.status(400).build();        
      }
    }	
	
}

