package nu.educom.rvt.rest;

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

import nu.educom.rvt.models.Theme;
import nu.educom.rvt.rest.filter.Secured;
import nu.educom.rvt.services.ThemeConceptService;


@Path("/webapi/themes")
@Secured
public class ThemeResource extends BaseResource {
	private static final Logger LOG = LogManager.getLogger();
	
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllThemes() {
		LOG.trace("getAllThemes called");
		return wrapInSession(session -> {
			ThemeConceptService themeConceptServ = new ThemeConceptService(session);
		
			List<Theme> themes = themeConceptServ.getAllThemes();
			return (themes == null ? Response.status(404).build() : 
				                     Response.status(200).entity(themes).build());
		
		});
	}
	
	@POST
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
	
}
