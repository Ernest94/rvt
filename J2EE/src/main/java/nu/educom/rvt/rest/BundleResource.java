package nu.educom.rvt.rest;

import java.time.LocalDate;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import nu.educom.rvt.models.Bundle;
import nu.educom.rvt.models.User;
import nu.educom.rvt.models.view.BundleJson;
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
		bundle.setStartDate(LocalDate.now().toString());
		if(bundle.getName() != "" && bundle.getCreator() != null && bundle.getStartDate() != null && bundleServ.findBundleByName(bundle.getName()) == null)
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
	@Path("/bundleTrainee")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTraineeBundles(User user) {
		List<Bundle> bundles = bundleServ.getAllBundles();
		List<Bundle> bundlesTrainee = bundleServ.getAllBundlesFromUser(user);
		
		
		BundleJson bundleJson = new BundleJson(bundles);
		
		return Response.status(200).entity(bundleJson).build();
	}
}
