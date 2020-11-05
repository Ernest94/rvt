package nu.educom.rvt.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import nu.educom.rvt.models.Concept;
import nu.educom.rvt.models.ConceptRating;
import nu.educom.rvt.models.Review;
import nu.educom.rvt.models.User;
import nu.educom.rvt.models.Review.Status;
import nu.educom.rvt.models.view.ConceptPlusRating;
import nu.educom.rvt.repositories.ConceptRatingRepository;
import nu.educom.rvt.repositories.ConceptRepository;
import nu.educom.rvt.repositories.ReviewRepository;
import one.util.streamex.StreamEx;

public class ReviewService {
	
	public List<Concept> getActiveConcepts() {
		List<Concept> activeConcepts = new ArrayList<Concept>();
		
		ConceptRepository conceptRepo = new ConceptRepository();
		
		//Now assume all concepts are active
		activeConcepts = conceptRepo.readAll();
		
		
		//TO DO: look at the concepts that are active for a user
//		UserConceptRepository userConceptRepo = new UserConceptRepository();
//		
//		List<Concept> allConcepts = conceptRepo.readAll();
//		List<UserConcept> userConcepts = userConceptRepo.readByUserId(user.getId());
//		
//		for (UserConcept userConcept : userConcepts) {
//			
//			activeConcepts.add(allConcepts.get(userConcept.getId()));
//		}
		
		return activeConcepts;
	}
	
	public List<ConceptRating> getLatestConceptRatings() {
		List<ConceptRating> conceptsRatings = null;

		ConceptRatingRepository conceptRatingRepo = new ConceptRatingRepository();
		
		//now the mock is for 1 trainee and all concept ratings are the last ratings
		conceptsRatings = conceptRatingRepo.readAll();

		//TO DO: only read the concept ratings of the last given review for the current user
		
		
		return conceptsRatings;
	}
	
	
//	public List<ConceptPlusRating> createActiveConceptsPlusRatingsList(List<Concept>activeConcepts, List<ConceptRating> conceptRatings) {
//		List<ConceptPlusRating> conceptsPlusRatings = new ArrayList<ConceptPlusRating>();
//		int rating = 0;
//		for (Concept activeConcept : activeConcepts) {
//			for (ConceptRating conceptRating : conceptRatings) {
//				if (activeConcept.getId()==conceptRating.getConcept().getId()) {
//					rating = conceptRating.getRating();
//				}
//			}
//			conceptsPlusRatings.add(new ConceptPlusRating(activeConcept,rating));
//			rating = 0;
//		}
//		return conceptsPlusRatings;
//	}
	
	public List<Review> getAllCompletedReviewForUser(User user){
		
		ReviewRepository reviewRepo = new ReviewRepository();
		Status status = Review.Status.COMPLETED;
		List<Review> reviews = reviewRepo.readAll();
		return	reviews.stream().filter(review -> review.getUser().getId() == user.getId())
								.filter(r -> r.getReviewStatus() == Review.Status.COMPLETED)
								.collect(Collectors.toList());
		
	}
	
	public List<ConceptPlusRating> createActiveConceptsPlusRatingsList (List<Concept> concepts, List<Review> reviews){
		
		List<ConceptPlusRating> conceptPlusRating = new ArrayList<>();
		if(reviews.size() == 0) {
			for(Concept concept: concepts) {
				conceptPlusRating.add(new ConceptPlusRating(concept, 0, ""));
			}
			return conceptPlusRating;
		}
		
		LocalDate mostRecentDate = reviews.stream().map(r -> r.getDate()).max(LocalDate::compareTo).get();
		Review mostRecentReview = reviews.stream().filter(r -> r.getDate() == mostRecentDate).findFirst().orElse(null);
		List<Review> otherReviews = reviews.stream().filter(r -> r.getDate().getDayOfYear() < mostRecentDate.getDayOfYear()).collect(Collectors.toList());
		
		
		
		List<ConceptPlusRating> CPRMostRecent = getCPRFromReview(mostRecentReview);
		
		List<ConceptPlusRating> CPRother = new ArrayList<>();
		
		for(Review review : otherReviews) {
			CPRother.addAll(this.getCPRFromReview(review));
		}
		List<Concept> removedDuplicates = removeAllDuplicates(concepts, CPRother);
		
		for(Concept concept: removedDuplicates) {
			CPRother.add(new ConceptPlusRating(concept, 0, ""));
		}
		CPRother = CPRother.stream().sorted((o1,o2) -> o1.getConcept().getWeek().compareTo(o2.getConcept().getWeek())).collect(Collectors.toList());		
		
		conceptPlusRating.addAll(CPRMostRecent);
		conceptPlusRating.addAll(CPRother);
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
	
	private List<Concept> removeAllDuplicates(List<Concept> concepts,List<ConceptPlusRating> CPRs)
	{
		List<Concept> removedDuplicates = new ArrayList<>();
		List<Concept> conceptsWithRatings = CPRs.stream().map(c ->c.getConcept()).collect(Collectors.toList());
		removedDuplicates.addAll(concepts);
		removedDuplicates.removeAll(conceptsWithRatings);
		return removedDuplicates;
	}
	
}
