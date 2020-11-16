package nu.educom.rvt.models;

import nu.educom.rvt.models.view.ConceptPlusRating;

public class ConceptRatingUpdate {
  private int reviewId;
  private ConceptPlusRating conceptPlusRating;

    public int getReviewId() {
	  return reviewId;
    }
    
    public void setReviewId(int reviewId) {
	  this.reviewId = reviewId;
    }
    
    public ConceptPlusRating getConceptPlusRating() {
	  return conceptPlusRating;
    }
    
    public void setConceptPlusRating(ConceptPlusRating conceptPlusRating) {
	  this.conceptPlusRating = conceptPlusRating;
    }
  
  
}
