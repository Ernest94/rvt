package nu.educom.rvt.rest;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import nu.educom.rvt.models.Concept;
import nu.educom.rvt.models.Review;
import nu.educom.rvt.models.User;
import nu.educom.rvt.models.view.ConceptPlusRating;
import nu.educom.rvt.models.view.ConceptRatingJSON;
import nu.educom.rvt.services.ReviewService;
import nu.educom.rvt.services.ThemeConceptService;
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
		ThemeConceptService conceptServ = new ThemeConceptService();
	    User userOutput = userServ.getUserById(user.getId());
		
		List<Review> allReviews = this.reviewServ.getAllCompletedReviewsForUser(userOutput);
		List<Concept> allActiveConcepts = conceptServ.getAllÁctiveConceptsfromUser(userOutput);
		List<ConceptPlusRating> conceptsPlusRatings = this.reviewServ.createActiveConceptsPlusRatingsList(allActiveConcepts,allReviews);
		
		ConceptRatingJSON conceptsRatingsJSON = new ConceptRatingJSON();
		String traineeName = userOutput.getName();
		String traineeLocation = userOutput.getLocation().getName();
		String reviewDate = reviewServ.convertDateTimeToString(reviewServ.getMostRecentReview(allReviews).getDate());
		conceptsRatingsJSON.setTraineeName(traineeName);
		conceptsRatingsJSON.setTraineeLocation(traineeLocation);
		conceptsRatingsJSON.setReviewDate(reviewDate);
		conceptsRatingsJSON.setConceptPlusRating(conceptsPlusRatings);

		return Response.status(200).entity(conceptsRatingsJSON).build();
  	}
	
	@POST
	@Path("/makeReview")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getMakeReviewData(User user) {
		
		UserService userServ = new UserService(); //load injectables
		ThemeConceptService conceptServ = new ThemeConceptService();
	    User userOutput = userServ.getUserById(user.getId());
		
	    reviewServ.makeNewReviewIfNoPending(userOutput);
		List<Review> allReviews = this.reviewServ.getAllReviewsForUser(userOutput);
		List<Concept> allActiveConcepts = conceptServ.getAllÁctiveConceptsfromUser(userOutput);
		List<ConceptPlusRating> conceptsPlusRatings = this.reviewServ.createActiveConceptsPlusRatingsList(allActiveConcepts,allReviews);
		
		ConceptRatingJSON conceptsRatingsJSON = new ConceptRatingJSON();
		String traineeName = userOutput.getName();
		String traineeLocation = userOutput.getLocation().getName();
		
		Review mostRecentReview = reviewServ.getMostRecentReview(allReviews);		
		String reviewDate = reviewServ.convertDateTimeToString(mostRecentReview.getDate());
		int reviewId = mostRecentReview.getId();
		
		conceptsRatingsJSON.setTraineeName(traineeName);
		conceptsRatingsJSON.setTraineeLocation(traineeLocation);
		conceptsRatingsJSON.setReviewDate(reviewDate);
		conceptsRatingsJSON.setConceptPlusRating(conceptsPlusRatings);
		conceptsRatingsJSON.setReviewId(reviewId);

		return Response.status(200).entity(conceptsRatingsJSON).build();
      }
    
	@POST
    @Path("/confirmReview")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response setActiveReviewComplete(Review review){
        Review reviewOutput = reviewServ.getReviewById(review.getId());
        Review completedReview = reviewServ.completedReview(reviewOutput);
        reviewServ.updateReview(completedReview);

		return Response.status(202).build();
    }
	
	@POST
    @Path("/addConceptRatings")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addconceptratings(ConceptRatingJSON crJSON){
        Review completedReview = reviewServ.addConceptRatings(crJSON.getConceptsPlusRatings(), crJSON.getReviewId());
        reviewServ.updateReview(completedReview);

		return Response.status(201).build();
    }
	
}
