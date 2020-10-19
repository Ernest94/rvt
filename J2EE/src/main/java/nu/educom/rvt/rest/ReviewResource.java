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
import nu.educom.rvt.models.view.ConceptsPlusRatings;
import nu.educom.rvt.services.ReviewService;

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
		
//		UserService userServ = new UserService();//load injectables
//	    User userOutput = userServ.getUserById(user.getId());
//		
//		int reviewId = this.reviewServ.getLatestReviewForUser(user);
//		int reviewId = 1;
		List<ConceptRating> conceptRatings = this.reviewServ.getLatestConceptRatings();
		List<Concept> activeConcepts = this.reviewServ.getActiveConcepts();
		List<ConceptsPlusRatings> conceptsPlusRatings = this.reviewServ.createActiveConceptsPlusRatingsList(activeConcepts,conceptRatings);
		
		ConceptRatingJSON conceptsRatingsJSON = new ConceptRatingJSON();
		String traineeName = "Trainee1";
		String traineeLocation = "Utrecht";
		conceptsRatingsJSON.setTraineeName(traineeName);
		conceptsRatingsJSON.setTraineeLocation(traineeLocation);
		conceptsRatingsJSON.setConceptsPlusRatings(conceptsPlusRatings);

		return Response.status(200).entity(conceptsRatingsJSON).build();
  	}
	
}
