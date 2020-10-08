package nu.educom.rvt.rest;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import nu.educom.rvt.models.Theme;
import nu.educom.rvt.models.Concept;

import nu.educom.rvt.services.ConceptService;

@Path("/webapi/Concept")
public class ConceptResource {
  private ConceptService conceptServ;
  
  public ConceptResource() {
    this.conceptServ = new ConceptService();
  }
  
  @POST
  @Path("/saveTheme")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response saveTheme(Theme theme) {
    
	boolean validCreate = this.conceptServ.addTheme(theme);
	
	return Response.status(418).build();
  }
  
  @POST
  @Path("/saveConcept")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response saveConcept(Concept concept) {
    
	boolean validCreate = this.conceptServ.addConcept(concept);

	
	return Response.status(418).build();
  }
  
  
  
  
}
