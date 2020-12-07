package nu.educom.rvt.models;

import nu.educom.rvt.models.view.ConceptPlusRating;

public class ConceptRatingUpdate {
  private int reviewId;
  private Boolean active;
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

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}
  
  
}
