package nu.educom.rvt.models.view;

import java.io.Serializable;
import java.util.List;

public class ConceptRatingJSON implements Serializable{

	public ConceptRatingJSON() {
	}
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private List<ConceptPlusRating> conceptsPlusRatings;
		private String traineeName;
		private String traineeLocation;
		private String reviewDate;
		private int reviewId;
		
		public List<ConceptPlusRating> getConceptsPlusRatings() {
			return conceptsPlusRatings;
		}
		public void setConceptPlusRating(List<ConceptPlusRating> conceptsPlusRatings) {
			this.conceptsPlusRatings = conceptsPlusRatings;
		}
		public String getTraineeName() {
			return traineeName;
		}
		public void setTraineeName(String traineeName) {
			this.traineeName = traineeName;
		}
		public String getTraineeLocation() {
			return traineeLocation;
		}
		public void setTraineeLocation(String traineeLocation) {
			this.traineeLocation = traineeLocation;
		}
		public String getReviewDate() {
			return reviewDate;
		}
		public void setReviewDate(String reviewDate) {
			this.reviewDate = reviewDate;
		}
		
		public static long getSerialversionuid() {
			return serialVersionUID;
		}
		public int getReviewId() {
			return reviewId;
		}
		public void setReviewId(int reviewId) {
			this.reviewId = reviewId;
		}

}
