package nu.educom.rvt.rest;

import java.time.LocalDate;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

import nu.educom.rvt.models.Bundle;
import nu.educom.rvt.models.User;
import nu.educom.rvt.models.view.BundleConceptWeekOffset;
import nu.educom.rvt.models.view.BundleJson;
import nu.educom.rvt.repositories.DatabaseException;
import nu.educom.rvt.repositories.HibernateSession;
import nu.educom.rvt.services.BundleService;

@Path("/webapi/bundle")
public class BundleResource {
	private static final Logger LOG = LogManager.getLogger();
	
	@POST
	@Path("/create")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createNewBundle(Bundle bundle) {
		LOG.debug("createNewBundle {} called", bundle);
		Session session = null;
		try {
			session = HibernateSession.openSessionAndTransaction();
			BundleService bundleService = new BundleService(session);
			
			bundle.setStartDate(LocalDate.now());
			// TODO move the validation to a BundleLogic class so this reads if (BundelLogic.isValidBundel(bundel)) { .... including logging
			if(bundle.getName() != "" && bundle.getCreator() != null && bundle.getStartDate() != null && bundleService.findBundleByName(bundle.getName()) == null)
			{
				bundleService.createNewBundle(bundle);
				return Response.status(201).build();
			}
			else {
				return Response.status(412).build();
			}
		} catch (DatabaseException e) {
			LOG.error("CreateNewBundle failed", e);
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
		Session session = null;
		try {
			session = HibernateSession.openSessionAndTransaction();
			BundleService bundleService = new BundleService(session);
			int bundleId = bundleService.isBundleIdConsistent(frontendBundleConcepts);
			if (bundleId==-1) {
				return Response.status(412).build();
			} 
			else {
				/*Bundle bundle =*/ bundleService.updateBundle(bundleId,frontendBundleConcepts);
				session.getTransaction().commit();
				return Response.status(201).build(/* TODO stuur de nieuwe bundel terug, new JSONBundel(bundle)*/);
			}
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
	@Path("/bundles")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllBundles() {
		LOG.trace("getAllBundles called");
		try (Session session = HibernateSession.openSession()) {
			BundleService bundleService = new BundleService(session);
			List<Bundle> bundles = bundleService.getAllBundles();
//			BundleJson bundleJson = new BundleJson(bundles);
			return Response.status(200).entity(bundles).build();
		} catch (DatabaseException e) {
			LOG.error("get all bundels failed", e);
			return Response.status(500).build();
		}
	}

	@GET
	@Path("/bundleTrainee")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTraineeBundles(User user) {
		LOG.debug("getTraineeBundles for user {} called", user);
		try (Session session = HibernateSession.openSession()) {
			BundleService bundleService = new BundleService(session);
			List<Bundle> bundles = bundleService.getAllBundles();
//			List<Bundle> bundlesTrainee = bundleServ.getAllBundlesFromUser(user);
			
			BundleJson bundleJson = new BundleJson(bundles);
			
			return Response.status(200).entity(bundleJson).build();
		} catch (DatabaseException e) {
			LOG.error("get all bundels failed", e);
			return Response.status(500).build();
		}
	}
	
}
