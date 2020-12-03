package nu.educom.rvt.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import nu.educom.rvt.models.Concept;
import nu.educom.rvt.models.ConceptRating;
import nu.educom.rvt.models.Review;
import nu.educom.rvt.models.User;
import nu.educom.rvt.models.view.ConceptPlusRating;
import nu.educom.rvt.repositories.ConceptRatingRepository;
import nu.educom.rvt.repositories.ConceptRepository;
import nu.educom.rvt.repositories.ReviewRepository;
import one.util.streamex.StreamEx;

public class ReviewService {
	
	public List<Concept> getallConcepts() {
		List<Concept> activeConcepts = new ArrayList<Concept>();
		
        ConceptRepository conceptRepo = new ConceptRepository();	
		activeConcepts = conceptRepo.readAll();		
		return activeConcepts;
	}
	
	public List<User> getAllUsersWithPendingReviews(){
		ReviewRepository reviewRepo = new ReviewRepository();
//		List<Review> reviews = reviewRepo.readAll().stream().filter(r -> r.getReviewStatus() == Review.Status.PENDING).collect(Collectors.toList());
		List<User> users = reviewRepo.readAll().stream().filter(r -> r.getReviewStatus() == Review.Status.PENDING).map(r ->r.getUser()).collect(Collectors.toList());
		
		return users;
	}
	
	public void makeNewReviewIfNoPending(User user)
	{
		ReviewRepository reviewRepo = new ReviewRepository();
		List<Review> pendingReviews = reviewRepo.readAll().stream()
														  .filter(r -> r.getUser().getId() == user.getId())
														  .filter(r -> r.getReviewStatus() == Review.Status.PENDING)
														  .collect(Collectors.toList());
		if(pendingReviews.size() == 0) {
			reviewRepo.create(new Review(LocalDateTime.now(), "", "", Review.Status.PENDING, user));
		}
	}
	
	public List<Review> getAllReviewsForUser(User user){
		
		ReviewRepository reviewRepo = new ReviewRepository();
		List<Review> reviews = reviewRepo.readAll();
		return	reviews.stream().filter(review -> review.getUser().getId() == user.getId())
								.collect(Collectors.toList());
		
	}
	
	public List<Review> getAllCompletedReviewsForUser(User user){
		
		ReviewRepository reviewRepo = new ReviewRepository();
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
	public List<ConceptPlusRating> createActiveConceptsPlusRatingsList (List<Concept> concepts, List<Review> reviews){
		
		List<ConceptPlusRating> conceptPlusRating = new ArrayList<>();
		if(reviews.size() == 0) {
			for(Concept concept: concepts) {
				conceptPlusRating.add(new ConceptPlusRating(concept, 0, ""));
			}
			return conceptPlusRating;
		}
		
		LocalDateTime mostRecentDate = reviews.stream().map(r -> r.getDate()).max(LocalDateTime::compareTo).get();
		Review mostRecentReview = reviews.stream().filter(r -> r.getDate() == mostRecentDate).findFirst().orElse(null);
		List<Review> otherReviews = reviews.stream().filter(r -> r.getDate().isBefore(mostRecentDate)).collect(Collectors.toList());
		
		
		
		List<ConceptPlusRating> CPRMostRecent = getCPRFromReview(mostRecentReview);
		
		List<ConceptPlusRating> CPRother = new ArrayList<>();
		
		for(Review review : otherReviews) {
			CPRother.addAll(this.getCPRFromReview(review));
		}
		List<Concept> removedDuplicates = removeAllDuplicates(concepts, CPRother);
		
		for(Concept concept: removedDuplicates) {
			CPRother.add(new ConceptPlusRating(concept, 0, ""));
		}
//		Comparator<ConceptPlusRating> weekCompare = (o1, o2) -> o1.getConcept().getWeek().compareTo(o2.getConcept().getWeek());
//		Comparator<ConceptPlusRating> ratingCompare = Comparator.comparing(ConceptPlusRating::getRating);
		
//		Comparator<ConceptPlusRating> ratingThenWeek = weekCompare.thenComparing(ratingCompare)
	
		CPRother = CPRother.stream().sorted((o1,o2) -> o1.getConcept().getWeek().compareTo(o2.getConcept().getWeek())).collect(Collectors.toList());		
		
		conceptPlusRating.addAll(CPRMostRecent);
		conceptPlusRating.addAll(CPRother.stream().filter(c -> c.getRating() != 0).collect(Collectors.toList()));
		conceptPlusRating.addAll(CPRother.stream().filter(c -> c.getRating() == 0).collect(Collectors.toList()));
		conceptPlusRating = StreamEx.of(conceptPlusRating).distinct(foo -> foo.getConcept().getId()).toList();
		
		return conceptPlusRating;
	}
	
	private List<ConceptPlusRating> getCPRFromReview(Review review)
	{
		List<ConceptPlusRating> conceptPlusRatings = new ArrayList<>();
		ConceptRatingRepository conceptRatingRepo = new ConceptRatingRepository();
		List<ConceptRating> conceptRatings =  conceptRatingRepo.readAll().stream()
															   .filter(c -> c.getReview().getId() == review.getId())
															   .sorted((o1,o2) -> o1.getConcept().getWeek().compareTo(o2.getConcept().getWeek()))
															   .collect(Collectors.toList());
		
		for(ConceptRating conceptRating : conceptRatings)
		{
			conceptPlusRatings.add(
					new ConceptPlusRating(
					conceptRating.getConcept(), 
					conceptRating.getRating(),
					conceptRating.getComment())
					);
		}
		
		return conceptPlusRatings;
	}
	
	public Review addConceptRatings(List <ConceptPlusRating> conceptRatings, int reviewId) {
		ConceptRatingRepository crRepo = new ConceptRatingRepository();
		List<ConceptRating> ratingList = convertConceptPlusRatings(conceptRatings, reviewId);
		crRepo.createMulti(ratingList);
		Review review = getReviewById(reviewId);

		return review;
	}
	
	private List<Concept> removeAllDuplicates(List<Concept> concepts,List<ConceptPlusRating> CPRs)
	{
		List<Concept> removedDuplicates = new ArrayList<>();
		List<Concept> conceptsWithRatings = CPRs.stream().map(c ->c.getConcept()).collect(Collectors.toList());
		removedDuplicates.addAll(concepts);
		removedDuplicates.removeAll(conceptsWithRatings);
		return removedDuplicates;
    }
    
    public Review getReviewById(int reviewId) {
        ReviewRepository reviewRepo = new ReviewRepository();
        return reviewRepo.readById(reviewId);
    }
    
    public ConceptRating getConceptRatingById(int id) {
    	ConceptRatingRepository conceptRatingRepo = new ConceptRatingRepository();
    	return conceptRatingRepo.readById(id);
    }
    
    public ConceptRating checkIfConceptRatingExists(int reviewId, int conceptId) {
    	ConceptRatingRepository conceptRatingRepo = new ConceptRatingRepository();
    	return conceptRatingRepo.readAll().stream()
    							.filter(c -> c.getReview().getId() == reviewId)
    							.filter(c -> c.getConcept().getId() == conceptId)
    							.findFirst().orElse(null);
    }

    public Review completedReview(Review review) {
        Review completedReview = review;
        completedReview.setReviewStatus(Review.Status.COMPLETED);
        return completedReview;
    }
    
    public Review cancelledReview(Review review) {
    	Review cancelledReview = review;
    	cancelledReview.setReviewStatus(Review.Status.CANCELLED);
		return cancelledReview;
    }
    
    public int replaceReview(Review review) {
    	Review toUpdate = this.getReviewById(review.getId());
    	toUpdate = this.updateReview(review, toUpdate);
        ReviewRepository reviewRepo = new ReviewRepository();
        reviewRepo.update(toUpdate);
        return review.getId();
    }
    
    public Review updateReview(Review review, Review toUpdate) {
    	toUpdate.setDate(review.getDate());
    	toUpdate.setCommentOffice(review.getCommentOffice());
    	toUpdate.setCommentStudent(review.getCommentStudent());   
    	toUpdate.setReviewStatus(review.getReviewStatus());
    	return toUpdate;
    }
	
	public Review getMostRecentReview(List<Review> allReviews) {
		LocalDateTime mostRecentDate = allReviews.stream().map(r -> r.getDate()).max(LocalDateTime::compareTo).get();;
		return allReviews.stream().filter(r -> r.getDate() == mostRecentDate).findFirst().orElse(null);
	}
	
	public String convertDateTimeToString(LocalDate date)
	{
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		return date.format(formatter);
	}
	
	private List<ConceptRating> convertConceptPlusRatings(List<ConceptPlusRating> cpr, int reviewId){
		List<ConceptRating> ratings = new ArrayList<ConceptRating>();
		for(int i = 0; i<cpr.size(); i++) {
			ratings.add(this.convertConceptPlusRating(cpr.get(i), reviewId));
		}
		return ratings;
	}
	
	private ConceptRating convertConceptPlusRating(ConceptPlusRating cpr, int reviewId) {
		return new ConceptRating(this.getReviewById(reviewId), cpr.getConcept(), cpr.getRating(), cpr.getComment());
	}

	public void addReview(Review review) {
		ReviewRepository reviewRepo = new ReviewRepository();
		reviewRepo.update(review);		
	}

	public Review addConceptRating(ConceptPlusRating conceptPlusRating, int reviewId) {
		ConceptRatingRepository crRepo = new ConceptRatingRepository();
		crRepo.create(this.convertConceptPlusRating(conceptPlusRating, reviewId));
		
		return this.getReviewById(reviewId);
	}
	
	public Review updateConceptRating(ConceptRating old, ConceptPlusRating conceptPlusRating) {
		ConceptRatingRepository crRepo = new ConceptRatingRepository();
		ConceptRating updated = old;
		updated.setComment(conceptPlusRating.getComment());
		updated.setRating(conceptPlusRating.getRating());
		crRepo.update(updated);
		
		return this.getReviewById(updated.getReview().getId());
	}
}
