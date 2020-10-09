package nu.educom.rvt.rest;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import nu.educom.rvt.models.Theme;
import nu.educom.rvt.models.Concept;

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
		Theme createdTheme = this.themeConceptServ.addTheme(theme);
		return (createdTheme == null ? Response.status(409).build() : Response.status(201).build());  
  	}
  
	@POST
	@Path("/saveConcept")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response saveConcept(Concept concept) {
		Concept createdConcept = this.themeConceptServ.addConcept(concept);
		return (createdConcept == null ? Response.status(409).build() : Response.status(201).build());  
	}
}
