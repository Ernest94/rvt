package nu.educom.rvt.rest;

import java.time.LocalDate;
import java.util.List;

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

import nu.educom.rvt.models.Bundle;
import nu.educom.rvt.models.Concept;
import nu.educom.rvt.models.User;
import nu.educom.rvt.models.view.BaseBundleView;
import nu.educom.rvt.models.view.BundleConceptWeekOffset;
import nu.educom.rvt.models.view.BundleTraineeView;
import nu.educom.rvt.services.BundleService;
import nu.educom.rvt.services.UserService;

@Path("/webapi/bundle")
public class BundleResource extends BaseResource {
	private static final Logger LOG = LogManager.getLogger();
	
	@POST
	@Path("/create")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createNewBundle(Bundle bundle) {
		LOG.debug("createNewBundle {} called", bundle);
		return wrapInSessionWithTransaction(session -> {
			BundleService bundleService = new BundleService(session);
			bundle.setStartDate(LocalDate.now());
			if (bundleService.validateBundle(bundle))
			{
				bundleService.createNewBundle(bundle);
				return Response.status(201).build();
			}
			else {
				return Response.status(412).build();
			}
		});
	}	
	
	// 1. check if bundle_id consistent
	// 2. collect bundle from database (Hibernate) (or collect the bundle_concept with specific bundle_id from database (Hibernate))
	// 2. 
	// 3. loop over all current concepts in bundle
	// 4a. if not exists in frontend bundle_concepts -> close record
	// 4b. exist in frontend bundle_concepts -> weekoffset is the same -> remove from frontend bundle_concepts
	// 4c. exist in frontend bundle_concepts -> weekoffset is not the same -> close current record open new record and remove from frontend bundle_concepts
	// 5. if frontend bundle_concepts empty -> done
	// 6. if frontend bundle_concepts not empty -> get all concepts based on ids left in frontend bundle_concept -> 
	// 	loop over frontend bundle_concepts -> create new bundle_conept object ->  add to bundle_table database				
	@POST
	@Path("/change")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response changeBundle(List<BundleConceptWeekOffset> frontendBundleConcepts) {
		LOG.debug("changeBundle called for bundle id {}", 
				  frontendBundleConcepts.isEmpty() ? "<none>" : frontendBundleConcepts.get(0).getBundleId());
		return wrapInSessionWithTransaction(session -> {
			BundleService bundleService = new BundleService(session);
			int bundleId = bundleService.isBundleIdConsistent(frontendBundleConcepts);
			if (bundleId==-1) {
				return Response.status(412).build();
			} 
			else {
				if (!bundleService.doesBundleExists(bundleId)) {
					return Response.status(404).build();
				}
				/*Bundle bundle =*/ bundleService.updateBundle(bundleId,frontendBundleConcepts);
				return Response.status(201).build(/* TODO stuur de nieuwe bundel terug, new JSONBundel(bundle)*/);
			}
		});
	}
	
	@GET
	@Path("/bundles")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllBundles() {
		LOG.trace("getAllBundles called");
		return wrapInSession(session -> {
			BundleService bundleService = new BundleService(session);
			List<BaseBundleView> bundles = bundleService.getAllBundleViews();
			return Response.status(200).entity(bundles).build();
		});
	}
	
	@GET
	@Path("/bundleCreator/{userId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCreatorBundles(@PathParam("userId") int userId) {
		return wrapInSession(session -> {
			UserService userService = new UserService(session);
			BundleService bundleService = new BundleService(session);
			User user = userService.getUserById(userId);
			List<Bundle> bundles = bundleService.getAllCreatorBundles(user);
			
			return Response.status(200).entity(bundles).build();
		});
	}
	
	@GET 
	@Path("/user/{userId}") 
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTraineeBundles(@PathParam("userId") int userId) {
		LOG.debug("getTraineeBundles for user {} called", userId);
		return wrapInSessionWithTransaction(session -> {
			BundleService bundleService = new BundleService(session);
			User user = new User();
			user.setId(userId);

	        List<BundleTraineeView> bundlesTrainee = bundleService.getAllBundlesFromUser(user);
	        
	        return Response.status(200).entity(bundlesTrainee).build();
		});
	}
	
	@PUT
	@Path("/user/{userId}")
	@Consumes(MediaType.APPLICATION_JSON)
    public Response updateTraineeBundle(@PathParam("userId") int userId, List<BundleTraineeView> bundlesTrainee) {
		LOG.debug("updateTraineeBundle for user {} called with {}", userId, bundlesTrainee);
		return wrapInSessionWithTransaction(session -> {
			BundleService bundleService = new BundleService(session);
			User user = new User();
			user.setId(userId);
		
			bundleService.setBundlesForUser(user, bundlesTrainee);
        
			return Response.status(200).build();
		});
	}	
	
	@GET
	@Path("/conceptsBundle/{bundleId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllConceptsFromBundle(@PathParam("bundleId") int bundleId) {
		return wrapInSession(session -> {
			BundleService bundleService = new BundleService(session);
				
			Bundle bundle = bundleService.getBundleById(bundleId);
			List<Concept> concepts = bundleService.getAllConceptsFromBundle(bundle);		
		
			return Response.status(200).entity(concepts).build();
		});
	}

	
}
