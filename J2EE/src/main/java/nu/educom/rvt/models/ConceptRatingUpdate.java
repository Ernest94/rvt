package nu.educom.rvt.models;

import nu.educom.rvt.models.view.CPRActive;

public class ConceptRatingUpdate {
  private int reviewId;
  private CPRActive conceptPlusRating;

    public int getReviewId() {
	  return reviewId;
    }
    
    public void setReviewId(int reviewId) {
	  this.reviewId = reviewId;
    }
    
    public CPRActive getConceptPlusRating() {
	  return conceptPlusRating;
    }
    
    public void setConceptPlusRating(CPRActive conceptPlusRating) {
	  this.conceptPlusRating = conceptPlusRating;
    }
  
  
}
