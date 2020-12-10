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
import nu.educom.rvt.models.Concept;
import nu.educom.rvt.models.User;
import nu.educom.rvt.models.view.BundleConceptWeekOffset;
import nu.educom.rvt.models.view.BundleJson;
import nu.educom.rvt.services.BundleService;
import nu.educom.rvt.services.UserService;

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
		try {
		List<Bundle> bundles = bundleServ.getAllBundles();
//		BundleJson bundleJson = new BundleJson(bundles);
		return Response.status(200).entity(bundles).build();
		} catch (Exception e) {
			return Response.status(400).build();
		}
	}
	
	@GET
	@Path("/bundleCreator/{userId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCreatorBundles(@PathParam("userId") int userId) {
		
		UserService userServ = new UserService();
		User user = userServ.getUserById(userId);
		List<Bundle> bundles = bundleServ.getAllCreatorBundles(user);
		BundleJson bundleJson = new BundleJson(bundles);
		
		return Response.status(200).entity(bundleJson).build();
	}
	
	@GET
	@Path("/bundleTrainee/{userId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTraineeBundles(User user) {
		List<Bundle> bundles = bundleServ.getAllBundles();
//		List<Bundle> bundlesTrainee = bundleServ.getAllBundlesFromUser(user);
		
		
		BundleJson bundleJson = new BundleJson(bundles);
		
		return Response.status(200).entity(bundleJson).build();
	}
	
	@GET
	@Path("/conceptsBundle/{bundleId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllConceptsFromBundle(@PathParam("bundleId") int bundleId) {
				
		Bundle bundle = bundleServ.getBundleById(bundleId);
		List<Concept> concepts = bundleServ.getAllConceptsFromBundle(bundle);		
		
		return Response.status(200).entity(concepts).build();
	}
}
