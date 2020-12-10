package nu.educom.rvt.rest;

import java.time.LocalDateTime;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import nu.educom.rvt.models.Concept;
import nu.educom.rvt.models.ConceptRating;
import nu.educom.rvt.models.ConceptRatingUpdate;
import nu.educom.rvt.models.Review;
import nu.educom.rvt.models.User;
import nu.educom.rvt.models.view.ConceptPlusRating;
import nu.educom.rvt.models.view.ConceptRatingJSON;
import nu.educom.rvt.models.view.UserSearchJson;
import nu.educom.rvt.services.ReviewService;
import nu.educom.rvt.services.ThemeConceptService;
import nu.educom.rvt.services.UserService;

@Path("/webapi/review")
public class ReviewResource extends BaseResource {

	private static final Logger LOG = LogManager.getLogger();
	
	// Maak functies als:
	// - getActiveConceptsAndRating
	// - getReviews
	// - 
  
	@GET
	@Path("/curriculum/{userId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getActiveConceptsAndRating(@PathParam("userId") int userId) {
		LOG.debug("getActiveConceptsAndRating {} called", userId);
		return wrapInSession(session -> {
			UserService userServ = new UserService(session); //load injectables
			ReviewService reviewServ = new ReviewService(session);
			ThemeConceptService conceptServ = new ThemeConceptService(session);
	
		    User userOutput = userServ.getUserById(userId);
		    if (userOutput == null) {
		    	return Response.status(404).build();
		    }
		    if (userOutput.getRole().getId() == 3 /* JH: Make this an enum */) { /* JH TIP: invert the if */
				
				List<Review> allReviews = reviewServ.getAllCompletedReviewsForUser(userOutput);
				List<Concept> allActiveConcepts = conceptServ.getAllActiveConceptsFromUser(userOutput);
				List<ConceptPlusRating> conceptsPlusRatings = reviewServ.createActiveConceptsPlusRatingsList(allActiveConcepts,allReviews, userOutput);
				
				/* JH: Move this to a logic function or to conceptRatingJSON's constructor */
				ConceptRatingJSON conceptsRatingsJSON = new ConceptRatingJSON();
				String traineeName = userOutput.getName();
				String traineeLocation = userOutput.getLocation().getName();
				conceptsRatingsJSON.setTraineeName(traineeName);
				conceptsRatingsJSON.setTraineeLocation(traineeLocation);
				if (!allReviews.isEmpty()) {
					LocalDateTime reviewDate = reviewServ.getMostRecentReview(allReviews).getDate();
					conceptsRatingsJSON.setReviewDate(reviewDate);
				}
				conceptsRatingsJSON.setConceptPlusRating(conceptsPlusRatings);
	
				return Response.status(200).entity(conceptsRatingsJSON).build();
		    }
		    return Response.status(412).build();
		});
  	}
	
	@POST
	@Path("/makeReview")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getMakeReviewData(User user) {
		LOG.debug("getActiveConceptsAndRating {} called", user);
		return wrapInSessionWithTransaction(session -> {
			UserService userServ = new UserService(session); //load injectables
			ReviewService reviewServ = new ReviewService(session);
			ThemeConceptService conceptServ = new ThemeConceptService(session);
		    User userOutput = userServ.getUserById(user.getId());
				    
		    reviewServ.makeNewReviewIfNoPending(userOutput);
	
			List<Review> allReviews = reviewServ.getAllReviewsForUser(userOutput); // hier moet de check of iets active is in.
			List<Concept> allActiveConcepts = conceptServ.getAllActiveConceptsFromUser(userOutput);
			//hier kan de week functie ook. waarschijnlijk het meest logisch om het hier te doen
			List<ConceptPlusRating> conceptsPlusRatings = reviewServ.createActiveConceptsPlusRatingsList(allActiveConcepts,allReviews, userOutput);
		    //extra functie om de week te bepalen nadat de ratings eraan zijn gegeven
			List<ConceptPlusRating> CPRActive = conceptServ.converToCPRActive(conceptsPlusRatings);
			
			ConceptRatingJSON conceptsRatingsJSON = new ConceptRatingJSON();
			String traineeName = userOutput.getName();
			String traineeLocation = userOutput.getLocation().getName();
			
			Review mostRecentReview = reviewServ.getMostRecentReview(allReviews);		
			LocalDateTime reviewDate = mostRecentReview.getDate();
			int reviewId = mostRecentReview.getId();
			
			conceptsRatingsJSON.setTraineeName(traineeName);
			conceptsRatingsJSON.setTraineeLocation(traineeLocation);
			conceptsRatingsJSON.setReviewDate(reviewDate);
			conceptsRatingsJSON.setConceptPlusRating(CPRActive);
			conceptsRatingsJSON.setReviewId(reviewId);
			conceptsRatingsJSON.setCommentOffice(mostRecentReview.getCommentOffice());
			conceptsRatingsJSON.setCommentStudent(mostRecentReview.getCommentStudent());

			return Response.status(200).entity(conceptsRatingsJSON).build();
		});
      }
    
	@POST
    @Path("/confirmReview")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response setActiveReviewComplete(Review review){
		LOG.debug("setActiveReviewComplete {} called", review);
		return wrapInSessionWithTransaction(session -> {
			ReviewService reviewServ = new ReviewService(session);
			if (reviewServ.getReviewById(review.getId()) == null) {
				return Response.status(404).build();
			}
	        Review reviewOutput = reviewServ.completedReview(review.getId());
			LOG.info("Review for trainee {} is marked 'COMPLETED' by {}.", 
					 reviewOutput.getUser(), /*reviewOutput.getDocent()*/"<someone>");
			return Response.status(202).build();
		});
    }
	
	@POST
    @Path("/cancelReview")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response setActiveReviewCancelled(Review review){
		LOG.debug("setActiveReviewComplete {} called", review);
		return wrapInSessionWithTransaction(session -> {
			ReviewService reviewServ = new ReviewService(session);
			if (reviewServ.getReviewById(review.getId()) == null) {
				return Response.status(404).build();
			}
			Review reviewOutput = reviewServ.cancelledReview(review.getId());
			LOG.info("Review for trainee {} is marked 'CANCELLED' by {}.", 
					 reviewOutput.getUser(), /*reviewOutput.getDocent()*/"<someone>");
			return Response.status(202).build();
		});
    }
	
	@GET
	@Path("/pending/location/{locationId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllUsersWithPendingReviews(@PathParam("locationId") int locationId) {
		LOG.debug("getAllUsersWithPendingReviews called");
		return wrapInSession(session -> {
			UserService userServ = new UserService(session); /* JH: Should be a UserLogic class */
			ReviewService reviewServ = new ReviewService(session);
			List<User> foundUsers = reviewServ.getAllUsersWithPendingReviews(locationId);
			UserSearchJson USJ = userServ.convertToUSJ(foundUsers); 
		
			return Response.status(200).entity(USJ).build();
		});
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
		LOG.debug("addConceptRating {} called", cru);
		return wrapInSessionWithTransaction(session -> {
			ReviewService reviewServ = new ReviewService(session);
			
			int reviewId = cru.getReviewId();
			int conceptId = cru.getConceptPlusRating().getConcept().getId();
			ConceptRating conceptRating = reviewServ.checkIfConceptRatingExists(reviewId, conceptId);
			if(conceptRating != null) {
				Review reviewOutput = reviewServ.updateConceptRating(conceptRating, cru.getConceptPlusRating());
				LOG.debug("Review for trainee {} rating for concept {} changed to {} '{}'.", 
						  reviewOutput.getUser(), conceptRating.getConcept().getName(), conceptRating.getRating(), conceptRating.getComment());
				return Response.status(201).build();
			}
			else {
				reviewServ.addConceptRating(cru.getConceptPlusRating(), cru.getReviewId());
		  	    return Response.status(404).build();
			}
		});
    }
	
	@POST
	@Path("/updateReview")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateReview(Review review) {
		LOG.debug("updateReview {} called", review);
		return wrapInSessionWithTransaction(session -> {
			ReviewService reviewServ = new ReviewService(session);
			Review reviewOutput = reviewServ.getReviewById(review.getId());
			if (reviewOutput != null) {
				review.setReviewStatus(Review.Status.PENDING);
				LOG.info("Review for trainee {} is marked 'PENDING' by {}.", 
						 reviewOutput.getUser(), /*reviewOutput.getDocent()*/"<someone>");
			  return Response.status(202).build();
			} 
			return Response.status(404).build();
		});
	}
	
}
