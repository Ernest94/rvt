package nu.educom.rvt.models.view;

import nu.educom.rvt.models.Concept;

public class ConceptPlusRating {

	private Concept concept;
	private int rating;
	private String comment;	
	
	public ConceptPlusRating() {
		
	}

	public ConceptPlusRating(Concept concept,int rating) {
		this.concept = concept;
		this.rating = rating;
	}
	
	public ConceptPlusRating(Concept concept,int rating, String comment) {
		this.concept = concept;
		this.rating = rating;
		this.comment = comment;
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
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	
}
