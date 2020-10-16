package nu.educom.rvt.rest;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import nu.educom.rvt.models.Concept;
import nu.educom.rvt.models.ConceptRating;
import nu.educom.rvt.models.User;
import nu.educom.rvt.models.view.ConceptRatingJSON;
import nu.educom.rvt.services.ReviewService;
import nu.educom.rvt.services.UserService;

@Path("/webapi/review")
public class ReviewResource {

	// Maak functies als:
	// - getActiveConceptsAndRating
	// - getReviews
	// - 
	
	private ReviewService reviewServ;
	
	public ReviewResource() {
		this.reviewServ = new ReviewService();
	}
  
	@POST
	@Path("/curriculum")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getActiveConceptsAndRating(User user) {
		
		ConceptRatingJSON conceptsRatingsJSON = new ConceptRatingJSON();

		UserService userServ = new UserService();//load injectables
	    User userOutput = userServ.getUserById(user.getId());
		
//		int reviewId = this.reviewServ.getLatestReviewForUser(user);
		int reviewId = 1;
		List<ConceptRating> conceptRatings = this.reviewServ.getLatestConceptRatings(user,reviewId);
		List<Concept> activeConcepts = this.reviewServ.getActiveConcepts(user);

		conceptsRatingsJSON.setActiveConcepts(activeConcepts);
		conceptsRatingsJSON.setConceptRatings(conceptRatings);
		conceptsRatingsJSON.setUserName(userOutput.getName());
		conceptsRatingsJSON.setUserLocation(userOutput.getLocation().getName());

		return Response.status(200).entity(conceptsRatingsJSON).build();
  	}
}
