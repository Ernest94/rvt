package nu.educom.rvt.models.view;

import java.io.Serializable;
import java.util.List;

public class CPRActiveJSON implements Serializable{

	public CPRActiveJSON() { 
	}
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private List<CPRActive> CPRActive;
		private String traineeName;
		private String traineeLocation;
		private String reviewDate;
		private int reviewId;
		
		public List<CPRActive> getCPRActive() {
			return CPRActive;
		}
		public void setCPRActive(List<CPRActive> CPRActive) {
			this.CPRActive = CPRActive;
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
