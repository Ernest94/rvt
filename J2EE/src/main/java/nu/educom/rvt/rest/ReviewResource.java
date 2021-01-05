package nu.educom.rvt.rest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
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
import nu.educom.rvt.rest.filter.Secured;
import nu.educom.rvt.services.ReviewService;
import nu.educom.rvt.services.ThemeConceptService;
import nu.educom.rvt.services.UserService;

@Path("/webapi")
@Secured
public class ReviewResource extends BaseResource {

	private static final Logger LOG = LogManager.getLogger();
	
	@GET
	@Path("/users/{userId}/reviews")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getActiveConceptsAndRating(@PathParam("userId") int userId) {
		LOG.debug("getActiveConceptsAndRating {} called", userId);
		return wrapInSession(session -> {
			UserService userServ = new UserService(session); //load injectables
			ReviewService reviewServ = new ReviewService(session);
			ThemeConceptService conceptServ = new ThemeConceptService(session);
	
		    User userOutput = userServ.getUserById(userId);
		    if (userOutput.getRole().getId() == 3 /* JH: Make this an enum */) { /* JH TIP: invert the if */
				
				List<Review> allReviews = reviewServ.getAllCompletedReviewsForUser(userOutput);
				List<Concept> allActiveConcepts = conceptServ.getAllActiveConceptsFromUser(userOutput);
				List<ConceptPlusRating> conceptsPlusRatings = reviewServ.createActiveConceptsPlusRatingsList(allActiveConcepts,allReviews, userOutput);
				
				/* JH: Move this to a logic function or to conceptRatingJSON's constructor */
				ConceptRatingJSON conceptsRatingsJSON = new ConceptRatingJSON();
				String traineeName = userOutput.getName();
				String traineeLocation = userOutput.getCurrentLocations().get(0).getName();
				conceptsRatingsJSON.setTraineeName(traineeName);
				conceptsRatingsJSON.setTraineeLocation(traineeLocation);
				if (!allReviews.isEmpty()) {
					Review mostRecentReview = reviewServ.getMostRecentReview(allReviews);
					LocalDateTime reviewDate = mostRecentReview.getDate();
					conceptsRatingsJSON.setReviewDate(reviewDate);
					conceptsRatingsJSON.setCommentStudent(mostRecentReview.getCommentStudent());
					conceptsRatingsJSON.setCommentOffice(mostRecentReview.getCommentOffice());
				}
				else {
					conceptsRatingsJSON.setReviewDate(LocalDateTime.now());
				}
				conceptsRatingsJSON.setConceptPlusRating(conceptsPlusRatings);

	
				return Response.status(200).entity(conceptsRatingsJSON).build();
		    }
		    return Response.status(412).build();
		});
  	}
	
	@POST
	@Path("/users/{userId}/reviews")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getMakeReviewData(@PathParam("userId") int userId) {
		LOG.debug("getActiveConceptsAndRating called for user {}", userId);
		return wrapInSessionWithTransaction(session -> {
			UserService userServ = new UserService(session); //load injectables
			ReviewService reviewServ = new ReviewService(session);
			ThemeConceptService conceptServ = new ThemeConceptService(session);
		    User userOutput = userServ.getUserById(userId);
				    
		    reviewServ.makeNewReviewIfNoPending(userOutput);
	
			List<Review> allReviews = reviewServ.getAllReviewsForUser(userOutput).stream().filter(rev -> rev.getReviewStatus()!=Review.Status.CANCELLED).collect(Collectors.toList());
			List<Concept> allActiveConcepts = conceptServ.getAllActiveConceptsFromUser(userOutput);
			//hier kan de week functie ook. waarschijnlijk het meest logisch om het hier te doen
			List<ConceptPlusRating> conceptsPlusRatings = reviewServ.createActiveConceptsPlusRatingsList(allActiveConcepts,allReviews, userOutput);
		    //extra functie om de week te bepalen nadat de ratings eraan zijn gegeven
			List<ConceptPlusRating> CPRActive = conceptServ.converToCPRActive(conceptsPlusRatings);
			
			ConceptRatingJSON conceptsRatingsJSON = new ConceptRatingJSON();
			String traineeName = userOutput.getName();
			String traineeLocation = userOutput.getCurrentLocations().get(0).getName();
			
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
	
	@PUT
	@Path("/users/{userId}/reviews")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateReview(Review review) {
		LOG.debug("updateReview {} called", review);
		return wrapInSessionWithTransaction(session -> {
			ReviewService reviewServ = new ReviewService(session);
			Review reviewOutput = reviewServ.getReviewById(review.getId());
			review.setReviewStatus(Review.Status.PENDING);
			LOG.info("Review for trainee {} is marked 'PENDING' by {}.", 
					 reviewOutput.getUser(), /*reviewOutput.getDocent()*/"<someone>");
			reviewServ.updateReview(review, reviewOutput);
			return Response.status(202).build();
		});
	}
	
	@POST
    @Path("/users/{userId}/reviews/confirm")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response setActiveReviewComplete(Review review){
		LOG.debug("setActiveReviewComplete {} called", review);
		return wrapInSessionWithTransaction(session -> {
			ReviewService reviewServ = new ReviewService(session);
	        Review reviewOutput = reviewServ.completedReview(review.getId());
	        reviewOutput.setDocent(review.getDocent());
			LOG.info("Review for trainee {} is marked 'COMPLETED' by {}.", 
					 reviewOutput.getUser(), /*reviewOutput.getDocent()*/"<someone>");
			return Response.status(202).build();
		});
    }
	
	@POST
    @Path("/users/{userId}/reviews/cancel")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response setActiveReviewCancelled(Review review){
		LOG.debug("setActiveReviewComplete {} called", review);
		return wrapInSessionWithTransaction(session -> {
			ReviewService reviewServ = new ReviewService(session);
			Review reviewOutput = reviewServ.cancelledReview(review.getId());
			reviewOutput.setDocent(review.getDocent());
			LOG.info("Review for trainee {} is marked 'CANCELLED' by {}.", 
					 reviewOutput.getUser(), /*reviewOutput.getDocent()*/"<someone>");
			return Response.status(202).build();
		});
    }
	

	
	@POST
    @Path("/users/{userId}/reviews/ratings/")
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
				return Response.status(200).build();
			}
			else {
				reviewServ.addConceptRating(cru.getConceptPlusRating(), cru.getReviewId());
		  	    return Response.status(201).build();
			}
		});
    }
	

}
