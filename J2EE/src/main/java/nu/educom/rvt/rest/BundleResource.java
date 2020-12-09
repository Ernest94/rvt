package nu.educom.rvt.rest;

import java.time.LocalDate;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import nu.educom.rvt.models.Bundle;
import nu.educom.rvt.models.User;
import nu.educom.rvt.models.view.BaseBundleView;
import nu.educom.rvt.models.view.BundleConceptWeekOffset;
import nu.educom.rvt.models.view.BundleTraineeView;
import nu.educom.rvt.models.view.BundleView;
import nu.educom.rvt.services.BundleService;

@Path("/webapi/bundle")
public class BundleResource {

	private BundleService bundleServ;
	
	public BundleResource() {
		this.bundleServ = new BundleService();
	}
	
	@POST
	@Path("/create")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createNewBundle(Bundle bundle) {
		bundle.setStartDate(LocalDate.now());
		if(bundle.getName() != "" && bundle.getCreator() != null && bundle.getStartDate() != null && bundleServ.findBundleByName(bundle.getName()) == null)
		{
			bundleServ.createNewBundle(bundle);
			return Response.status(201).build();
		}
		else return Response.status(412).build();
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
		int bundleId = bundleServ.isBundleIdConsistent(frontendBundleConcepts);
		if (bundleId==-1) {
			return Response.status(412).build();
		} else {
			bundleServ.updateBundle(bundleId,frontendBundleConcepts);
//			System.out.print(bundleId);
			return Response.status(201).build();
		}
	}
	
	@GET
	@Path("/bundles")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllBundles() {
		List<BaseBundleView> bundles = bundleServ.getAllBundleViews();
		
		return Response.status(200).entity(bundles).build();
	}
	
	@GET
	@Path("/user/{userId}")
	@Produces(MediaType.APPLICATION_JSON)
    public Response getTraineeBundles(@PathParam("userId") int userId) {
		User user = new User();
		user.setId(userId);

        List<BundleTraineeView> bundlesTrainee = bundleServ.getAllBundlesFromUser(user);
        
        return Response.status(200).entity(bundlesTrainee).build();
	}
	
}
