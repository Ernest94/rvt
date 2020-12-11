package nu.educom.rvt.models.view;
	
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

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
		private String commentOffice;
		private String commentStudent;
		private LocalDateTime reviewDate;
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
		
		@XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
		public LocalDateTime getReviewDate() {
			return reviewDate;
		}
		@XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
		public void setReviewDate(LocalDateTime reviewDate) {
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
		public String getCommentOffice() {
			return commentOffice;
		}
		public void setCommentOffice(String commentOffice) {
			this.commentOffice = commentOffice;
		}
		public String getCommentStudent() {
			return commentStudent;
		}
		public void setCommentStudent(String commentStudent) {
			this.commentStudent = commentStudent;
		}

}
