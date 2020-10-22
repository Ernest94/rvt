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
import nu.educom.rvt.models.Review;
import nu.educom.rvt.models.User;
import nu.educom.rvt.models.view.ConceptRatingJSON;
import nu.educom.rvt.models.view.ConceptsPlusRatings;
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
		
		UserService userServ = new UserService(); //load injectables
	    User userOutput = userServ.getUserById(user.getId());
		
		List<Review> allReviews = this.reviewServ.getAllCompletedReviewForUser(userOutput); //deze moet nog geschreven worden
		List<Concept> allActiveConcepts = this.reviewServ.getAllÁctiveConceptsfromUser(userOutput); // moet nog geschreven worden
		List<ConceptsPlusRatings> conceptsPlusRatings = this.reviewServ.createActiveConceptsPlusRatingsList(allActiveConcepts,allReviews); //moet herschreven worden
		
		
		
//		List<ConceptRating> conceptRatings = this.reviewServ.getLatestConceptRatings();
//		List<Concept> activeConcepts = this.reviewServ.getActiveConcepts();
//		List<ConceptsPlusRatings> conceptsPlusRatings = this.reviewServ.createActiveConceptsPlusRatingsList(activeConcepts,conceptRatings);
		
		ConceptRatingJSON conceptsRatingsJSON = new ConceptRatingJSON();
		String traineeName = userOutput.getName();
		String traineeLocation = userOutput.getLocation().getName();
		conceptsRatingsJSON.setTraineeName(traineeName);
		conceptsRatingsJSON.setTraineeLocation(traineeLocation);
		conceptsRatingsJSON.setConceptsPlusRatings(conceptsPlusRatings);

		return Response.status(200).entity(conceptsRatingsJSON).build();
  	}
	
}
