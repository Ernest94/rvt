package nu.educom.rvt.models.view;

import nu.educom.rvt.models.Concept;

public class ConceptPlusRating {

	private Concept concept;
	private int rating;

	public ConceptPlusRating(Concept concept,int rating) {
		this.concept = concept;
		this.rating = rating;
	}
	
	public Concept getConcept() {
		return concept;
	}
	public void setConcept(Concept concept) {
		this.concept = concept;
	}
	public int getRating() {
		return rating;
	}
	public void setRating(int rating) {
		this.rating = rating;
	}
	
}
