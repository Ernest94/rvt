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

import nu.educom.rvt.models.Concept;
import nu.educom.rvt.models.TraineeActive;
import nu.educom.rvt.models.TraineeMutation;
import nu.educom.rvt.models.User;
import nu.educom.rvt.models.view.ConceptPlusBundles;
import nu.educom.rvt.rest.filter.Secured;
import nu.educom.rvt.services.BundleService;
import nu.educom.rvt.services.ThemeConceptService;


@Path("/webapi")
@Secured
public class ConceptResource extends BaseResource {
	private static final Logger LOG = LogManager.getLogger();
	
	@Path("/concepts")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllConcepts() {
		LOG.trace("getAllConcepts called");
		return wrapInSession(session -> {
			ThemeConceptService themeConceptServ = new ThemeConceptService(session);
			List<Concept> concepts = themeConceptServ.getAllConcepts();
			return (concepts == null ? Response.status(404).build() : 
				Response.status(200).entity(concepts).build());
		});
	}
	
	@Path("/concepts")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response saveConcept(ConceptPlusBundles concept) {
		
		LOG.debug("saveConcept {} called", concept);
		return wrapInSessionWithTransaction(session -> {
			ThemeConceptService themeConceptServ = new ThemeConceptService(session);
			BundleService bundleServ = new BundleService(session);
			concept.getConcept().setStartDate(LocalDate.now());
			boolean valid = themeConceptServ.validateConcept(concept.getConcept());
			if(valid) {
				Concept createdConcept = themeConceptServ.addConcept(concept.getConcept());
				LOG.info("Concept added with name {} and description {}.", createdConcept.getName(), createdConcept.getDescription()); 
				bundleServ.addBundlesToConcept(concept.getConcept(), concept.getBundles());
				LOG.trace("Concept {} added to bundles {} ", createdConcept.getName(), concept.getBundles());
				return Response.status(201).build();
			}
			else {
				return Response.status(400).build();
			}
		});
	}
	
	
	@PUT
	@Path("/trainees/{userId}/concepts/{conceptId}")
	public Response changeActive(@PathParam("userId") int userId, @PathParam("conceptId") int conceptId){
		LOG.debug("changeActive {} called");
		return wrapInSessionWithTransaction(session -> {
			ThemeConceptService themeConceptServ = new ThemeConceptService(session);
			User user = new User();
			user.setId(userId);
			Concept concept = new Concept();
			concept.setId(conceptId);
			TraineeActive currentMutation = themeConceptServ.getCurrentMutationForUserAndConcept(user, concept);

			if(currentMutation==null) {
				boolean inBundel = themeConceptServ.isConceptInBundleUser(user, concept);
				themeConceptServ.createNewMutation(user,concept,!inBundel);
				return Response.status(201).build();
			}
			else {
				themeConceptServ.endMutation(currentMutation);
				return Response.status(200).build();
			}
		});
	}
	
	@PUT
	@Path("/trainees/{userId}/concepts/{conceptId}/week/{newWeek}")
	public Response changeMutation(@PathParam("userId") int userId, @PathParam("conceptId") int conceptId, @PathParam("newWeek") int newWeek){
		LOG.debug("changeMutation {} called");
		return wrapInSessionWithTransaction(session -> {
			ThemeConceptService themeConceptServ = new ThemeConceptService(session);
			User user = new User();
			user.setId(userId);
			Concept concept = new Concept();
			concept.setId(conceptId);
			
			TraineeMutation currentMutation = themeConceptServ.getCurrentWeekMutationForUserAndConcept(user, concept);
			
			if(currentMutation==null) {
				LOG.trace("currentMutation is null");
				themeConceptServ.createNewMutation(user,concept,newWeek);
				return Response.status(201).build();
			}
			else {
				LOG.trace("currentMutation is {}", currentMutation);
				themeConceptServ.endMutation(currentMutation);
				themeConceptServ.createNewMutation(user,concept,newWeek);
				return Response.status(200).build();
			}
		});
	}
}
