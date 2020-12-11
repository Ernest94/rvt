package nu.educom.rvt.rest;

import java.time.LocalDate;
import java.util.List;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import nu.educom.rvt.models.Theme;
import nu.educom.rvt.models.TraineeActive;
import nu.educom.rvt.models.TraineeMutation;
import nu.educom.rvt.models.User;
import nu.educom.rvt.models.view.ActiveChangeForUser;
import nu.educom.rvt.models.view.ConceptBundleJSON;
import nu.educom.rvt.models.view.ConceptPlusBundles;
import nu.educom.rvt.models.view.WeekChangeForUser;
import nu.educom.rvt.models.Concept;
import nu.educom.rvt.services.BundleService;
import nu.educom.rvt.services.ThemeConceptService;

@Path("/webapi/theme_concept")
public class ThemeConceptResource {
	private ThemeConceptService themeConceptServ;
	 
	public ThemeConceptResource() {
		this.themeConceptServ = new ThemeConceptService();
	}
  
	@POST
	@Path("/saveTheme")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response saveTheme(Theme theme) {	
		boolean valid = themeConceptServ.validateTheme(theme);
		if(valid) {
			Theme createdTheme = this.themeConceptServ.addTheme(theme);
			return Response.status(201).entity(createdTheme).build();
		}
		else {
			return Response.status(400).build();
		}
  	}
	
	@GET
	@Path("/themes")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllThemes() {
		List<Theme> themes = this.themeConceptServ.getAllThemes();
		return (themes == null ? Response.status(409/* JH: Had hier 404 verwacht */).build() : 
			Response.status(200).entity(themes).build());
	}
	
	@POST
	@Path("/saveConcept")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response saveConcept(ConceptPlusBundles concept) {
		BundleService bundleServ = new BundleService();
		concept.getConcept().setStartDate(LocalDate.now());
		boolean valid = themeConceptServ.validateConcept(concept.getConcept());
		if(valid) {
			Concept createdConcept = this.themeConceptServ.addConcept(concept.getConcept());
			bundleServ.addBundlesToConcept(concept.getConcept(), concept.getBundles());
			return Response.status(201).build();
		}
		else {
			return Response.status(402).build();
		}
	}
	
	@GET
	@Path("/concepts")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllConcepts() {
		List<Concept> concepts = this.themeConceptServ.getAllConcepts();
		return (concepts == null ? Response.status(409 /* JH: Had hier 404 verwacht */).build() : 
			Response.status(200).entity(concepts).build());
	}
	
	@GET
	@Path("/concepts/bundles")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllConceptsAndBundles() {
		ConceptBundleJSON allConceptsAndAllBundles = this.themeConceptServ.getAllConceptsAndAllBundles();
		return (allConceptsAndAllBundles == null ? Response.status(404).build() : 
			Response.status(200).entity(allConceptsAndAllBundles).build());
	}
	
	@POST
	@Path("/active")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response setActive(ActiveChangeForUser activeChange){
		User user = activeChange.getUser();
		Concept concept = activeChange.getConcept();
		
		TraineeActive currentMutation = themeConceptServ.getCurrentMutationForUserAndConcept(user, concept);
		
		if(currentMutation==null) {
			boolean inBundel = themeConceptServ.isConceptInBundleUser(user, concept);
			activeChange.setActive(!inBundel);
			themeConceptServ.createNewMutation(activeChange);
			return Response.status(201).build();
		}
		else {
			themeConceptServ.endMutation(currentMutation);
			return Response.status(200).build();
		}
	}
	@POST
	@Path("/week")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response setWeek(WeekChangeForUser weekChange){
		
		System.out.print("\n" + weekChange.getUser().getId() + "  " 
				+  weekChange.getConcept().getId() + "  " 
				+  weekChange.getWeek() + "\n");
		
		User user = weekChange.getUser();
		Concept concept = weekChange.getConcept();
		
		TraineeMutation currentMutation = themeConceptServ.getCurrentWeekMutationForUserAndConcept(user, concept);
		
		if(currentMutation==null) {
			System.out.print("\n" +  "currentMutation is null" + "\n" );
			themeConceptServ.createNewWeekMutation(weekChange);
			return Response.status(201).build();
		}
		else {
			System.out.print("\n" +  "currentMutation is not null" + "\n" );
			themeConceptServ.endWeekMutation(currentMutation);
			themeConceptServ.createNewWeekMutation(weekChange);
			return Response.status(200).build();
		}
	}
}
