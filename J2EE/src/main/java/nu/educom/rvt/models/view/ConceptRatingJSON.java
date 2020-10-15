package nu.educom.rvt.models.view;

import java.io.Serializable;
import java.util.List;

import nu.educom.rvt.models.Concept;
import nu.educom.rvt.models.ConceptRating;

public class ConceptRatingJSON implements Serializable{

	public ConceptRatingJSON() {
	}
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private List<Concept> activeConcepts;
		private List<ConceptRating> conceptRatings;
		
		public List<Concept> getActiveConcepts() {
			return activeConcepts;
		}
		public void setActiveConcepts(List<Concept> activeConcepts) {
			this.activeConcepts = activeConcepts;
		}
		public List<ConceptRating> getConceptRatings() {
			return conceptRatings;
		}
		public void setConceptRatings(List<ConceptRating> ratings) {
			this.conceptRatings = ratings;
		}
		public static long getSerialversionuid() {
			return serialVersionUID;
		}
	
}
