package nu.educom.rvt.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.hibernate.Session;

import nu.educom.rvt.models.Concept;
import nu.educom.rvt.models.ConceptRating;
import nu.educom.rvt.models.Review;
import nu.educom.rvt.models.User;
import nu.educom.rvt.models.view.ConceptPlusRating;
import nu.educom.rvt.repositories.ConceptRatingRepository;
import nu.educom.rvt.repositories.ConceptRepository;
import nu.educom.rvt.repositories.DatabaseException;
import nu.educom.rvt.repositories.EntryNotFoundException;
import nu.educom.rvt.repositories.ReviewRepository;
import one.util.streamex.StreamEx;

public class ReviewService {
	private final ReviewRepository reviewRepo;
	private final ConceptRepository conceptRepo;
	private final ConceptRatingRepository conceptRatingRepo;
	private final BundleService bundleServ;
	
	public ReviewService(Session session) {
		super();
		reviewRepo = new ReviewRepository(session);
		conceptRepo = new ConceptRepository(session);
		conceptRatingRepo = new ConceptRatingRepository(session);
		bundleServ = new BundleService(session);
	}

	public List<Concept> getallConcepts() throws DatabaseException {
		List<Concept> activeConcepts = conceptRepo.readAll();		
		return activeConcepts;
	}
	
	public List<User> getAllUsersWithPendingReviews(int locationId) throws DatabaseException {
		//		List<Review> reviews = reviewRepo.readAll().stream().filter(r -> r.getReviewStatus() == Review.Status.PENDING).collect(Collectors.toList());
		List<User> users = reviewRepo.readAll().stream().filter(r -> r.getReviewStatus() == Review.Status.PENDING).map(r ->r.getUser()).filter(u->u.getCurrentLocations().get(0).getId() == locationId).collect(Collectors.toList());
		
		return users;
	}
	
	public void makeNewReviewIfNoPending(User user) throws DatabaseException
	{
		List<Review> pendingReviews = reviewRepo.readAll().stream()
														  .filter(r -> r.getUser().getId() == user.getId())
														  .filter(r -> r.getReviewStatus() == Review.Status.PENDING)
														  .collect(Collectors.toList());
		if(pendingReviews.size() == 0) {
			reviewRepo.create(new Review(LocalDateTime.now(), "", "", Review.Status.PENDING, user));
		}
	}
	
	public List<Review> getAllReviewsForUser(User user) throws DatabaseException{
		
		List<Review> reviews = reviewRepo.readAll();
		return	reviews.stream().filter(review -> review.getUser().getId() == user.getId())
								.collect(Collectors.toList());
		
	}
	
	public List<Review> getAllCompletedReviewsForUser(User user) throws DatabaseException{
		
		List<Review> reviews = reviewRepo.readAll();
		return	reviews.stream().filter(r -> r.getUser().getId() == user.getId())
								.filter(r -> r.getReviewStatus() == Review.Status.COMPLETED)
								.collect(Collectors.toList());
		
	}
	
	/*
	 * JH: Use English comment!
	 * 
	 * Functie ontvangt: een lijst van reviews waar de conceptRatings van gebruikt moeten worden, Een lijst van alle concepten.
	 * Functie geeft terug: een lijst van alle concepten met de meest recente rating bij elk concept, gesorteerd op de concepten van de meest recente review en daarna de rest op week.
	 * 
	 * Hier zit expliciet nog geen functionaliteit om ook de mutations mee te nemen in of niet de recentste 
	 */
	public List<ConceptPlusRating> createActiveConceptsPlusRatingsList (List<Concept> concepts, List<Review> reviews, User user) throws DatabaseException{
		
		List<ConceptRating> allConceptRatings = conceptRatingRepo.readAll();

		List<ConceptPlusRating> conceptPlusRating = new ArrayList<>();
		if(reviews.size() == 0) {
			for(Concept concept: concepts) {
				conceptPlusRating.add(new ConceptPlusRating(concept, 0, "", 0));
			}
			return conceptPlusRating;
		}
		
		LocalDateTime mostRecentDate = reviews.stream().map(r -> r.getDate()).max(LocalDateTime::compareTo).get();
		Review mostRecentReview = reviews.stream().filter(r -> r.getDate() == mostRecentDate).findFirst().orElse(null);
		List<Review> otherReviews = reviews.stream().filter(r -> r.getDate().isBefore(mostRecentDate)).sorted(Comparator.comparing(Review::getDate).reversed()).collect(Collectors.toList());

		
		
		List<ConceptPlusRating> CPRMostRecent = getCPRFromReview(mostRecentReview, allConceptRatings);
		
		List<ConceptPlusRating> CPRother = new ArrayList<>();
		
		for(Review review : otherReviews) {
			CPRother.addAll(this.getCPRFromReview(review, allConceptRatings));
		}
		List<Concept> removedDuplicates = removeAllDuplicates(concepts, CPRother);
		
		for(Concept concept: removedDuplicates) {
			CPRother.add(new ConceptPlusRating(concept, 0, "", 0));
		}
		CPRother = bundleServ.getWeekForCPR(CPRother, user);
		CPRMostRecent = bundleServ.getWeekForCPR(CPRMostRecent, user);
		
		CPRMostRecent = CPRMostRecent.stream().sorted((o1,o2) -> o1.getWeek().compareTo(o2.getWeek())).collect(Collectors.toList());		
		CPRother = CPRother.stream().sorted((o1,o2) -> o1.getWeek().compareTo(o2.getWeek())).collect(Collectors.toList());		
		
		conceptPlusRating.addAll(CPRMostRecent);
		conceptPlusRating.addAll(CPRother.stream().filter(c -> c.getRating() != 0).collect(Collectors.toList()));
		conceptPlusRating.addAll(CPRother.stream().filter(c -> c.getRating() == 0).collect(Collectors.toList()));
		conceptPlusRating = StreamEx.of(conceptPlusRating).distinct(foo -> foo.getConcept().getId()).toList();
		
		return conceptPlusRating;
	}
	
	private List<ConceptPlusRating> getCPRFromReview(Review review, List<ConceptRating> allConceptRatings) throws DatabaseException
	{
		List<ConceptPlusRating> conceptPlusRatings = new ArrayList<>();
		List<ConceptRating> conceptRatings =  allConceptRatings.stream()
															   .filter(c -> c.getReview().getId() == review.getId())
															   .collect(Collectors.toList());
		
		for(ConceptRating conceptRating : conceptRatings)
		{
			conceptPlusRatings.add(
					new ConceptPlusRating(
					conceptRating.getConcept(), 
					conceptRating.getRating(),
					conceptRating.getComment(),
					0)
					);
		}
		
		return conceptPlusRatings;
	}
	
	public Review addConceptRatings(List <ConceptPlusRating> conceptRatings, int reviewId) throws DatabaseException {
		List<ConceptRating> ratingList = convertConceptPlusRatings(conceptRatings, reviewId);
		conceptRatingRepo.createMulti(ratingList);
		return getReviewById(reviewId);
	}
	
	private List<Concept> removeAllDuplicates(List<Concept> concepts,List<ConceptPlusRating> CPRs)
	{
		List<Concept> removedDuplicates = new ArrayList<>();
		List<Concept> conceptsWithRatings = CPRs.stream().map(c ->c.getConcept()).collect(Collectors.toList());
		removedDuplicates.addAll(concepts);
		removedDuplicates.removeAll(conceptsWithRatings);
		return removedDuplicates;
    }
    
    public Review getReviewById(int reviewId) throws EntryNotFoundException, DatabaseException {
        return reviewRepo.readByKnownId(reviewId);
    }
    
    public ConceptRating getConceptRatingById(int id) throws DatabaseException {
    	return conceptRatingRepo.readByKnownId(id);
    }
    
    public ConceptRating checkIfConceptRatingExists(int reviewId, int conceptId) throws DatabaseException {
    	return conceptRatingRepo.readAll().stream()
    							.filter(c -> c.getReview().getId() == reviewId)
    							.filter(c -> c.getConcept().getId() == conceptId)
    							.findFirst().orElse(null);
    }

    public Review completedReview(int reviewId) throws DatabaseException {
    	return reviewRepo.updateStatus(reviewId, Review.Status.COMPLETED);
    }
    
    public Review cancelledReview(int reviewId) throws DatabaseException {
    	return reviewRepo.updateStatus(reviewId, Review.Status.CANCELLED);
    }
    
    public int replaceReview(Review review) throws DatabaseException {
        reviewRepo.update(review);
        return review.getId();
    }
    
    public Review updateReview(Review review, Review toUpdate) {
    	toUpdate.setDate(review.getDate());
    	toUpdate.setCommentOffice(review.getCommentOffice());
    	toUpdate.setCommentStudent(review.getCommentStudent());   
    	toUpdate.setReviewStatus(review.getReviewStatus());
    	return toUpdate;
    }
	
    /* TODO Move to reviewLogic class */
	public Review getMostRecentReview(List<Review> allReviews) {
		Optional<LocalDateTime> mostRecentDate = allReviews.stream().map(r -> r.getDate()).max(LocalDateTime::compareTo);
		if (mostRecentDate.isEmpty()) {
			return null;
		}
		return allReviews.stream().filter(r -> r.getDate() == mostRecentDate.get()).findFirst().orElse(null);
	}
	
	public String convertDateTimeToString(LocalDate date)
	{
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		return date.format(formatter);
	}
	
	private List<ConceptRating> convertConceptPlusRatings(List<ConceptPlusRating> cpr, int reviewId) throws DatabaseException{
		List<ConceptRating> ratings = new ArrayList<ConceptRating>();
		for(int i = 0; i<cpr.size(); i++) {
			ratings.add(this.convertConceptPlusRating(cpr.get(i), reviewId));
		}
		return ratings;
	}
	
	private ConceptRating convertConceptPlusRating(ConceptPlusRating cpr, int reviewId) throws DatabaseException {
		return new ConceptRating(getReviewById(reviewId), cpr.getConcept(), cpr.getRating(), cpr.getComment());
	}

	public void addReview(Review review) throws DatabaseException {
		reviewRepo.update(review);		
	}

	public Review addConceptRating(ConceptPlusRating conceptPlusRating, int reviewId) throws DatabaseException {
		conceptRatingRepo.create(this.convertConceptPlusRating(conceptPlusRating, reviewId));
		return getReviewById(reviewId);
	}
	
	public Review updateConceptRating(ConceptRating old, ConceptPlusRating conceptPlusRating) throws DatabaseException {
		ConceptRating updated = conceptRatingRepo.readByKnownId(old.getId());
		updated.setComment(conceptPlusRating.getComment());
		updated.setRating(conceptPlusRating.getRating());		
		return updated.getReview();
	}
	
	public List<ConceptRating> getAllConceptRatings() throws DatabaseException {
		return conceptRatingRepo.readAll();
	}

}
