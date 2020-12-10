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

import nu.educom.rvt.models.Concept;
import nu.educom.rvt.models.Theme;
import nu.educom.rvt.models.view.ConceptBundleJSON;
import nu.educom.rvt.services.ThemeConceptService;

@Path("/webapi/theme_concept")
public class ThemeConceptResource extends BaseResource {
	private static final Logger LOG = LogManager.getLogger();
	
	@POST
	@Path("/saveTheme")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response saveTheme(Theme theme) {
		LOG.debug("saveTheme {} called", theme);
		return wrapInSessionWithTransaction(session -> {
			ThemeConceptService themeConceptServ = new ThemeConceptService(session);
			boolean valid = themeConceptServ.validateTheme(theme);
			if(valid) {
				Theme createdTheme = themeConceptServ.addTheme(theme);
				LOG.info("Theme added with name {} ({}) and description {}.", 
						 createdTheme.getName(), createdTheme.getAbbreviation(), createdTheme.getDescription());
				return Response.status(201).entity(createdTheme).build();
			}
			else {
				return Response.status(400).build();
			}
		});
  	}
	
	@GET
	@Path("/themes")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllThemes() {
		LOG.trace("getAllBundles called");
		return wrapInSession(session -> {
			ThemeConceptService themeConceptServ = new ThemeConceptService(session);
		
			List<Theme> themes = themeConceptServ.getAllThemes();
			return (themes == null ? Response.status(409/* JH: Had hier 404 verwacht */).build() : 
				                     Response.status(200).entity(themes).build());
		
		});
	}
	
	
	@POST
	@Path("/saveConcept")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response saveConcept(Concept concept) {
		LOG.debug("saveConcept {} called", concept);
		return wrapInSessionWithTransaction(session -> {
			ThemeConceptService themeConceptServ = new ThemeConceptService(session);
			
			concept.setStartDate(LocalDate.now());
			boolean valid = themeConceptServ.validateConcept(concept);
			if(valid) {
				Concept createdConcept = themeConceptServ.addConcept(concept);
				LOG.info("Concept added with name {} and description {}.", 
					     createdConcept.getName(), createdConcept.getDescription());
				return Response.status(201).entity(createdConcept).build();
			}
			else {
				return Response.status(400).build();
			}
		});
	}
	
	@GET
	@Path("/concepts")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllConcepts() {
		LOG.trace("getAllConcepts called");
		return wrapInSession(session -> {
			ThemeConceptService themeConceptServ = new ThemeConceptService(session);
			List<Concept> concepts = themeConceptServ.getAllConcepts();
			return (concepts == null ? Response.status(409 /* JH: Had hier 404 verwacht */).build() : 
				Response.status(200).entity(concepts).build());
		});
	}
	
	@GET
	@Path("/concepts/bundles")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllConceptsAndBundles() {
		LOG.trace("getAllConceptsAndBundles called");
		return wrapInSessionWithTransaction(session -> {
			ThemeConceptService themeConceptServ = new ThemeConceptService(session);
			ConceptBundleJSON allConceptsAndAllBundles = themeConceptServ.getAllConceptsAndAllBundles();
			return (allConceptsAndAllBundles == null ? Response.status(404).build() : 
			Response.status(200).entity(allConceptsAndAllBundles).build());
		});
	}
}

