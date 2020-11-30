package nu.educom.rvt.rest;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import nu.educom.rvt.models.Concept;
import nu.educom.rvt.models.ConceptRating;
import nu.educom.rvt.models.ConceptRatingUpdate;
import nu.educom.rvt.models.Review;
import nu.educom.rvt.models.User;
import nu.educom.rvt.models.view.CPRActive;
import nu.educom.rvt.models.view.CPRActiveJSON;
import nu.educom.rvt.models.view.ConceptPlusRating;
import nu.educom.rvt.models.view.ConceptRatingJSON;
import nu.educom.rvt.models.view.UserSearchJson;
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
		List<Concept> allActiveConcepts = conceptServ.getAllActiveConceptsFromUser(userOutput); // hier moet de check of iets active is in.
		//hier kan de week functie ook. waarschijnlijk het meest logisch om het hier te doen (WeekOffsetFunctie).
		List<ConceptPlusRating> conceptsPlusRatings = this.reviewServ.createActiveConceptsPlusRatingsList(allActiveConcepts,allReviews, userOutput);
		//extra functie om de week te bepalen nadat de ratings eraan zijn gegeven.
		
		ConceptRatingJSON conceptsRatingsJSON = new ConceptRatingJSON();
		String traineeName = userOutput.getName();
		String traineeLocation = userOutput.getLocation().getName();
		String reviewDate = reviewServ.getMostRecentReview(allReviews).getDate();
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

		List<Review> allReviews = reviewServ.getAllReviewsForUser(userOutput); // hier moet de check of iets active is in.
		List<Concept> allActiveConcepts = conceptServ.getAllActiveConceptsFromUser(userOutput);
		//hier kan de week functie ook. waarschijnlijk het meest logisch om het hier te doen
		List<ConceptPlusRating> conceptsPlusRatings = reviewServ.createActiveConceptsPlusRatingsList(allActiveConcepts,allReviews, userOutput);
	    //extra functie om de week te bepalen nadat de ratings eraan zijn gegeven
		
		List<CPRActive> CPRActive = conceptServ.converToCPRActive(conceptsPlusRatings);
		
		
		CPRActiveJSON conceptsRatingsJSON = new CPRActiveJSON();
		String traineeName = userOutput.getName();
		String traineeLocation = userOutput.getLocation().getName();
		
		Review mostRecentReview = reviewServ.getMostRecentReview(allReviews);
		String reviewDate = mostRecentReview.getDate();
		int reviewId = mostRecentReview.getId();
		
		conceptsRatingsJSON.setTraineeName(traineeName);
		conceptsRatingsJSON.setTraineeLocation(traineeLocation);
		conceptsRatingsJSON.setReviewDate(reviewDate);
		conceptsRatingsJSON.setCPRActive(CPRActive);
		conceptsRatingsJSON.setReviewId(reviewId);

		return Response.status(200).entity(conceptsRatingsJSON).build();
      }
    
	@POST
    @Path("/confirmReview")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response setActiveReviewComplete(Review review){
        Review reviewOutput = reviewServ.getReviewById(review.getId());
        Review completedReview = reviewServ.completedReview(reviewOutput);
        reviewServ.replaceReview(completedReview);

		return Response.status(202).build();
    }
	
	@POST
    @Path("/cancelReview")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response setActiveReviewCancelled(Review review){
        Review reviewOutput = reviewServ.getReviewById(review.getId());
        Review cancelledReview = reviewServ.cancelledReview(reviewOutput);
        reviewServ.replaceReview(cancelledReview);

		return Response.status(202).build();
    }
	
	@GET
	@Path("/pendingUsers")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllUsersWithPendingReviews() {
		UserService userServ = new UserService();
		List<User> foundUsers = reviewServ.getAllUsersWithPendingReviews();
		UserSearchJson USJ = userServ.convertToUSJ(foundUsers);
		
		return Response.status(200).entity(USJ).build();
	}
	
//	@POST
//    @Path("/addConceptRatings")
//    @Consumes(MediaType.APPLICATION_JSON)
//    public Response addconceptratings(ConceptRatingJSON crJSON){
//        Review completedReview = reviewServ.addConceptRatings(crJSON.getConceptsPlusRatings(), crJSON.getReviewId());
//        reviewServ.updateReview(completedReview);
//		return Response.status(201).build();
//    }

	@POST
    @Path("/addConceptRating")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addconceptrating(ConceptRatingUpdate cru){
		int reviewId = cru.getReviewId();
		int conceptId = cru.getConceptPlusRating().getConcept().getId();
		ConceptRating conceptRating = reviewServ.checkIfConceptRatingExists(reviewId, conceptId);
		if(conceptRating != null) {
			reviewServ.updateConceptRating(conceptRating, cru.getConceptPlusRating());
			return Response.status(201).build();
		}
		else {
			reviewServ.addConceptRating(cru.getConceptPlusRating(), cru.getReviewId());
	  	    return Response.status(201).build();
		}

    }
	
	@POST
	@Path("/updateReview")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateReview(Review review) {
		boolean exists = reviewServ.getReviewById(review.getId())!=null;
		if(exists) {
		  reviewServ.replaceReview(review);
		  return Response.status(202).build();
		} 
		return Response.status(404).build();
	}
	
}
