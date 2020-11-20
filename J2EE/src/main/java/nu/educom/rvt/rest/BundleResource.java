package nu.educom.rvt.rest;

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
import nu.educom.rvt.models.view.BundleCheck;
import nu.educom.rvt.models.view.BundleCheckJson;
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
		if(bundle.getName() != null && bundle.getCreator() != null && bundle.getStartDate() != null && bundleServ.findBundleByName(bundle.getName()) == null)
		{
			bundleServ.createNewBundle(bundle);
			return Response.status(201).build();
		}
		else return Response.status(412).build();
	}
	
	@GET
	@Path("/bundles")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllBundles() {
		List<Bundle> bundles = bundleServ.getAllBundles();
		BundleJson bundleJson = new BundleJson(bundles);
		
		return Response.status(200).entity(bundleJson).build();
	}
	
	@GET
	@Path("/bundleTrainee/{userId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTraineeBundles(@PathParam("userId") int userId) {
		
		UserService userServ = new UserService();
		User user = userServ.getUserById(userId);
		List<Bundle> bundles = bundleServ.getAllBundles();
		List<Bundle> bundlesTrainee = bundleServ.getAllBundlesFromUser(user);
		
		List<BundleCheck> bundleCheck = bundleServ.convertToBundleCheck(bundles, bundlesTrainee);
		BundleCheckJson bundleCheckJson = new BundleCheckJson(bundleCheck);
		
		return Response.status(200).entity(bundleCheckJson).build();
	}
}
