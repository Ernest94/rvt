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
import org.hibernate.Session;

import nu.educom.rvt.models.Concept;
import nu.educom.rvt.models.ConceptRating;
import nu.educom.rvt.models.ConceptRatingUpdate;
import nu.educom.rvt.models.Review;
import nu.educom.rvt.models.User;
import nu.educom.rvt.models.view.ConceptPlusRating;
import nu.educom.rvt.models.view.ConceptRatingJSON;
import nu.educom.rvt.models.view.UserSearchJson;
import nu.educom.rvt.repositories.DatabaseException;
import nu.educom.rvt.repositories.HibernateSession;
import nu.educom.rvt.services.ReviewService;
import nu.educom.rvt.services.ThemeConceptService;
import nu.educom.rvt.services.UserService;

@Path("/webapi/review")
public class ReviewResource {

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
		try (Session session = HibernateSession.openSession()) {
			UserService userServ = new UserService(session); //load injectables
			ReviewService reviewServ = new ReviewService(session);
			ThemeConceptService conceptServ = new ThemeConceptService(session);
	
		    User userOutput = userServ.getUserById(userId);
		    if (userOutput == null) {
		    	return Response.status(404).build();
		    }
		    if (userOutput.getRole().getId() == 3 /* JH: Make this an enum */) { /* JH TIP: invert the if */
				
				List<Review> allReviews = reviewServ.getAllCompletedReviewsForUser(userOutput);
				LocalDateTime reviewDate = reviewServ.getMostRecentReview(allReviews).getDate();
				List<Concept> allActiveConcepts = conceptServ.getAllActiveConceptsFromUser(userOutput);
				List<ConceptPlusRating> conceptsPlusRatings = reviewServ.createActiveConceptsPlusRatingsList(allActiveConcepts,allReviews);
				
				/* JH: Move this to a logic function or to conceptRatingJSON's constructor */
				ConceptRatingJSON conceptsRatingsJSON = new ConceptRatingJSON();
				String traineeName = userOutput.getName();
				String traineeLocation = userOutput.getLocation().getName();
				conceptsRatingsJSON.setTraineeName(traineeName);
				conceptsRatingsJSON.setTraineeLocation(traineeLocation);
				conceptsRatingsJSON.setReviewDate(reviewDate);
				conceptsRatingsJSON.setConceptPlusRating(conceptsPlusRatings);
	
				return Response.status(200).entity(conceptsRatingsJSON).build();
		    }
		    return Response.status(412).build();
		} catch (DatabaseException e) {
			LOG.error("Get active concepts and ratings failed", e);
			return Response.status(500).build();
		} 
  	}
	
	@POST
	@Path("/makeReview")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getMakeReviewData(User user) {
		LOG.debug("getActiveConceptsAndRating {} called", user);
		try (Session session = HibernateSession.openSession()) {
			UserService userServ = new UserService(session); //load injectables
			ReviewService reviewServ = new ReviewService(session);
			ThemeConceptService conceptServ = new ThemeConceptService(session);
		    User userOutput = userServ.getUserById(user.getId());
				    
		    reviewServ.makeNewReviewIfNoPending(userOutput);
	
			List<Review> allReviews = reviewServ.getAllReviewsForUser(userOutput); // hier moet de check of iets active is in.
			List<Concept> allActiveConcepts = conceptServ.getAllActiveConceptsFromUser(userOutput);
			//hier kan de week functie ook. waarschijnlijk het meest logisch om het hier te doen
			List<ConceptPlusRating> conceptsPlusRatings = reviewServ.createActiveConceptsPlusRatingsList(allActiveConcepts,allReviews);
		    //extra functie om de week te bepalen nadat de ratings eraan zijn gegeven
	
			ConceptRatingJSON conceptsRatingsJSON = new ConceptRatingJSON();
			String traineeName = userOutput.getName();
			String traineeLocation = userOutput.getLocation().getName();
			
			Review mostRecentReview = reviewServ.getMostRecentReview(allReviews);		
			LocalDateTime reviewDate = mostRecentReview.getDate();
			int reviewId = mostRecentReview.getId();
			
			conceptsRatingsJSON.setTraineeName(traineeName);
			conceptsRatingsJSON.setTraineeLocation(traineeLocation);
			conceptsRatingsJSON.setReviewDate(reviewDate);
			conceptsRatingsJSON.setConceptPlusRating(conceptsPlusRatings);
			conceptsRatingsJSON.setReviewId(reviewId);
	
			return Response.status(200).entity(conceptsRatingsJSON).build();
		} catch (DatabaseException e) {
			LOG.error("Get active concepts and ratings failed", e);
			return Response.status(500).build();
		} 
      }
    
	@POST
    @Path("/confirmReview")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response setActiveReviewComplete(Review review){
		LOG.debug("setActiveReviewComplete {} called", review);
		Session session = null;
		try {
			session = HibernateSession.openSessionAndTransaction();
			ReviewService reviewServ = new ReviewService(session);
			if (reviewServ.getReviewById(review.getId()) == null) {
				return Response.status(404).build();
			}
	        Review reviewOutput = reviewServ.completedReview(review.getId());
	        session.getTransaction().commit();
			LOG.info("Review for trainee {} is marked 'COMPLETED' by {}.", 
					 reviewOutput.getUser(), /*reviewOutput.getDocent()*/"<someone>");
			return Response.status(202).build();
		} catch (DatabaseException e) {
			LOG.error("setActiveReviewComplete failed", e);
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
	
	@POST
    @Path("/cancelReview")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response setActiveReviewCancelled(Review review){
		LOG.debug("setActiveReviewComplete {} called", review);
		Session session = null;
		try {
			session = HibernateSession.openSessionAndTransaction();
			ReviewService reviewServ = new ReviewService(session);
			if (reviewServ.getReviewById(review.getId()) == null) {
				return Response.status(404).build();
			}
			Review reviewOutput = reviewServ.cancelledReview(review.getId());
	        session.getTransaction().commit();
			LOG.info("Review for trainee {} is marked 'CANCELLED' by {}.", 
					 reviewOutput.getUser(), /*reviewOutput.getDocent()*/"<someone>");
			return Response.status(202).build();
		} catch (DatabaseException e) {
			LOG.error("setActiveReviewComplete failed", e);
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
	@Path("/pendingUsers")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllUsersWithPendingReviews() {
		LOG.debug("getAllUsersWithPendingReviews called");
		try (Session session = HibernateSession.openSession()) {
			UserService userServ = new UserService(session); /* JH: Should be a UserLogic class */
			ReviewService reviewServ = new ReviewService(session);
			List<User> foundUsers = reviewServ.getAllUsersWithPendingReviews();
			UserSearchJson USJ = userServ.convertToUSJ(foundUsers); 
		
			return Response.status(200).entity(USJ).build();
		} catch (DatabaseException e) {
			LOG.error("Get all users with pending reviews failed", e);
			return Response.status(500).build();
		} 
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
		Session session = null;
		try {
			session = HibernateSession.openSessionAndTransaction();
			ReviewService reviewServ = new ReviewService(session);
			
			int reviewId = cru.getReviewId();
			int conceptId = cru.getConceptPlusRating().getConcept().getId();
			ConceptRating conceptRating = reviewServ.checkIfConceptRatingExists(reviewId, conceptId);
			if(conceptRating != null) {
				Review reviewOutput = reviewServ.updateConceptRating(conceptRating, cru.getConceptPlusRating());
				session.getTransaction().commit();
				LOG.debug("Review for trainee {} rating for concept {} changed to {} '{}'.", 
						  reviewOutput.getUser(), conceptRating.getConcept().getName(), conceptRating.getRating(), conceptRating.getComment());
				return Response.status(201).build();
			}
			else {
				reviewServ.addConceptRating(cru.getConceptPlusRating(), cru.getReviewId());
		  	    return Response.status(404).build();
			}
		} catch (DatabaseException e) {
			LOG.error("setActiveReviewComplete failed", e);
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
	
	@POST
	@Path("/updateReview")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateReview(Review review) {
		LOG.debug("updateReview {} called", review);
		Session session = null;
		try {
			session = HibernateSession.openSessionAndTransaction();
			ReviewService reviewServ = new ReviewService(session);
			Review reviewOutput = reviewServ.getReviewById(review.getId());
			if (reviewOutput != null) {
				review.setReviewStatus(Review.Status.PENDING);
				session.getTransaction().commit();
				LOG.info("Review for trainee {} is marked 'PENDING' by {}.", 
						 reviewOutput.getUser(), /*reviewOutput.getDocent()*/"<someone>");
			  return Response.status(202).build();
			} 
			return Response.status(404).build();
		} catch (DatabaseException e) {
			LOG.error("updateReview failed", e);
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
	
}
