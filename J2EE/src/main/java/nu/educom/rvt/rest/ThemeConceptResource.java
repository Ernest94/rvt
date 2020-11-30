package nu.educom.rvt.rest;

import java.util.List;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import nu.educom.rvt.models.Theme;
import nu.educom.rvt.models.User;
import nu.educom.rvt.models.view.BundleCheck;
import nu.educom.rvt.models.view.BundleCheckJson;
import nu.educom.rvt.models.Bundle;
import nu.educom.rvt.models.Concept;

import nu.educom.rvt.services.ThemeConceptService;
import nu.educom.rvt.services.UserService;

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
        /* JH TIP: Mis hier of in de service de controle op alle velden van het thema */ 
		Theme createdTheme = this.themeConceptServ.addTheme(theme);
		return (createdTheme == null ? Response.status(409/* JH: Had hier 400 verwacht */).build() : Response.status(201).entity(createdTheme).build());  
  	}
	
	@GET
	@Path("/themes")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllThemes() {
		List<Theme> themes = this.themeConceptServ.getAllThemes();
		return (themes == null ? Response.status(409/* JH: Had hier 404 verwacht */).build() : Response.status(200).entity(themes).build());
	}
	
	
	@POST
	@Path("/saveConcept")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response saveConcept(Concept concept) {
        /* JH TIP: Mis hier of in de service de controle op alle velden van het concept */ 
		
		Concept createdConcept = this.themeConceptServ.addConcept(concept);
		return (createdConcept == null ? Response.status(409/* JH: Had hier 400 verwacht */).build() : Response.status(201).build());  
	}
	
	@GET
	@Path("/concepts")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllConcepts() {
		List<Concept> concepts = this.themeConceptServ.getAllConcepts();
		return (concepts == null ? Response.status(409 /* JH: Had hier 404 verwacht */).build() : Response.status(200).entity(concepts).build());
	}
}
