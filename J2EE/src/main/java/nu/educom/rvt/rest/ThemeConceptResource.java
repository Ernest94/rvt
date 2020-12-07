package nu.educom.rvt.rest;

import java.time.LocalDate;
import java.util.List;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

import nu.educom.rvt.models.Theme;
import nu.educom.rvt.models.view.ConceptBundleJSON;
import nu.educom.rvt.repositories.DatabaseException;
import nu.educom.rvt.repositories.HibernateSession;
import nu.educom.rvt.models.Concept;

import nu.educom.rvt.services.ThemeConceptService;

@Path("/webapi/theme_concept")
public class ThemeConceptResource {
	private static final Logger LOG = LogManager.getLogger();
	
	@POST
	@Path("/saveTheme")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response saveTheme(Theme theme) {
		LOG.debug("saveTheme {} called", theme);
		Session session = null;
		try {
			session = HibernateSession.openSessionAndTransaction();
			ThemeConceptService themeConceptServ = new ThemeConceptService(session);
			boolean valid = themeConceptServ.validateTheme(theme);
			if(valid) {
				Theme createdTheme = themeConceptServ.addTheme(theme);
				session.getTransaction().commit();
				LOG.info("Theme added with name {} ({}) and description {}.", 
						 createdTheme.getName(), createdTheme.getAbbreviation(), createdTheme.getDescription());
				return Response.status(201).entity(createdTheme).build();
			}
			else {
				return Response.status(400).build();
			}
		} catch (DatabaseException e) {
			LOG.error("saveTheme failed", e);
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
	@Path("/themes")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllThemes() {
		LOG.trace("getAllBundles called");
		try (Session session = HibernateSession.openSession()) {
			ThemeConceptService themeConceptServ = new ThemeConceptService(session);
		
			List<Theme> themes = themeConceptServ.getAllThemes();
			return (themes == null ? Response.status(409/* JH: Had hier 404 verwacht */).build() : 
				                     Response.status(200).entity(themes).build());
		
		} catch (DatabaseException e) {
			LOG.error("getAllThemes failed", e);
			return Response.status(500).build();
		}
	}
	
	
	@POST
	@Path("/saveConcept")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response saveConcept(Concept concept) {
		LOG.debug("saveConcept {} called", concept);
		Session session = null;
		try {
			session = HibernateSession.openSessionAndTransaction();
			ThemeConceptService themeConceptServ = new ThemeConceptService(session);
			
			concept.setStartDate(LocalDate.now());
			boolean valid = themeConceptServ.validateConcept(concept);
			if(valid) {
				Concept createdConcept = themeConceptServ.addConcept(concept);
				session.getTransaction().commit();
				LOG.info("Concept added with name {} and description {}.", 
					     createdConcept.getName(), createdConcept.getDescription());
				return Response.status(201).entity(createdConcept).build();
			}
			else {
				return Response.status(400).build();
			}
		} catch (DatabaseException e) {
			LOG.error("save Concept failed", e);
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
	@Path("/concepts")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllConcepts() {
		LOG.trace("getAllConcepts called");
		try (Session session = HibernateSession.openSession()) {
			ThemeConceptService themeConceptServ = new ThemeConceptService(session);
			List<Concept> concepts = themeConceptServ.getAllConcepts();
			return (concepts == null ? Response.status(409 /* JH: Had hier 404 verwacht */).build() : 
				Response.status(200).entity(concepts).build());
		} catch (DatabaseException e) {
			LOG.error("getAllConcepts failed", e);
			return Response.status(500).build();
		}
	}
	
	@GET
	@Path("/concepts/bundles")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllConceptsAndBundles() {
		LOG.trace("getAllConceptsAndBundles called");
		try (Session session = HibernateSession.openSession()) {
			ThemeConceptService themeConceptServ = new ThemeConceptService(session);
			ConceptBundleJSON allConceptsAndAllBundles = themeConceptServ.getAllConceptsAndAllBundles();
		return (allConceptsAndAllBundles == null ? Response.status(404).build() : 
			Response.status(200).entity(allConceptsAndAllBundles).build());
		} catch (DatabaseException e) {
			LOG.error("getAllConceptsAndBundles failed", e);
			return Response.status(500).build();
		}
	}
}

