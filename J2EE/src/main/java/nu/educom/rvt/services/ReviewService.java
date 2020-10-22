package nu.educom.rvt.services;

import java.util.ArrayList;
import java.util.List;

import nu.educom.rvt.models.Concept;
import nu.educom.rvt.models.ConceptRating;
import nu.educom.rvt.models.Review;
import nu.educom.rvt.models.view.ConceptsPlusRatings;
import nu.educom.rvt.repositories.ConceptRatingRepository;
import nu.educom.rvt.repositories.ConceptRepository;

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
	
	
	public List<ConceptsPlusRatings> createActiveConceptsPlusRatingsList(List<Concept>activeConcepts, List<ConceptRating> conceptRatings) {
		List<ConceptsPlusRatings> conceptsPlusRatings = new ArrayList<ConceptsPlusRatings>();
		int rating = 0;
		for (Concept activeConcept : activeConcepts) {
			for (ConceptRating conceptRating : conceptRatings) {
				if (activeConcept.getId()==conceptRating.getConcept().getId()) {
					rating = conceptRating.getRating();
				}
			}
			conceptsPlusRatings.add(new ConceptsPlusRatings(activeConcept,rating));
			rating = 0;
		}
		return conceptsPlusRatings;
	}
	
	public List<Review> getAllCompletedReviewForUser(User user){
		
		ReviewRepository 
		
	}
}
