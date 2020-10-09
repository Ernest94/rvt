package nu.educom.rvt.rest;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import nu.educom.rvt.models.Theme;
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
    this.conceptServ.addTheme(theme);

    return Response.status(200).entity(theme).build();
  }


}
